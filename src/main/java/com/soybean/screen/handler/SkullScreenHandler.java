package com.soybean.screen.handler;

import com.soybean.screen.ScreenRegister;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

import java.util.List;

/**
 * @author soybean
 * @date 2025/4/23 17:29
 * @description
 */
public class SkullScreenHandler extends ScreenHandler {
    private static final int NUM_COLUMNS = 9;
    private final Inventory inventory;
    private final int rows;

    private SkullScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, int rows) {
        this(type, syncId, playerInventory, new SimpleInventory(9 * rows), rows);
    }

    public static SkullScreenHandler createGeneric9x3(int syncId, PlayerInventory playerInventory) {
        return new SkullScreenHandler(ScreenRegister.SKULL_SCREEN_HANDLER, syncId, playerInventory, 3);
    }

    public static SkullScreenHandler createGeneric9x3(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        return new SkullScreenHandler(ScreenRegister.SKULL_SCREEN_HANDLER, syncId, playerInventory, inventory, 3);
    }

    public SkullScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
        super(type, syncId);
        checkSize(inventory, rows * 9);
        this.inventory = inventory;
        this.rows = rows;
        inventory.onOpen(playerInventory.player);
        int i = (this.rows - 4) * 18;

        int j;
        int k;

        // 放置T形骨头和骷髅头的位置
        setupSkullPattern();

        for(j = 0; j < this.rows; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(j = 0; j < 3; ++j) {
            for(k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for(j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
        }

    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.rows * 9) {
                if (!this.insertItem(itemStack2, this.rows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, this.rows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
        }

        return itemStack;
    }

    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);

        //杀死骷髅逻辑
        killSkeleton(player);
    }

    private static void killSkeleton(PlayerEntity player) {
        double searchRadius = 16.0;

        List<SkeletonEntity> skeletonEntities = player.getWorld().getEntitiesByClass(
                SkeletonEntity.class,
                player.getBoundingBox().expand(searchRadius),
                entity -> true
        );

        SkeletonEntity skeletonEntity = null;
        double closestDistSq = Double.MAX_VALUE;

        for (SkeletonEntity skeleton : skeletonEntities) {
            double distSq = player.squaredDistanceTo(skeleton);
            if (distSq < closestDistSq) {
                closestDistSq = distSq;
                skeletonEntity = skeleton;
            }
        }

        if (skeletonEntity != null) {
            skeletonEntity.kill();
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getRows() {
        return this.rows;
    }

    // 初始化T形骨头和骷髅头的位置
    private void setupSkullPattern() {
        // 清空物品栏
        for (int i = 0; i < inventory.size(); i++) {
            inventory.setStack(i, ItemStack.EMPTY);
        }

        // 放置骷髅头在T形的顶部
        inventory.setStack(4, new ItemStack(Items.SKELETON_SKULL));

        // 放置骨头形成T形
        // 横向的三个骨头
        inventory.setStack(3 + 9, new ItemStack(Items.BONE));
        inventory.setStack(4 + 9, new ItemStack(Items.BONE)); // 中间的骨头（第二行）
        inventory.setStack(5 + 9, new ItemStack(Items.BONE));

        // 竖向的一个骨头（第三行）
        inventory.setStack(4 + 18, new ItemStack(Items.BONE));
    }
}
