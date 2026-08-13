package com.soybean.screen.handler;

import com.soybean.block.ModBlock;
import com.soybean.block.custom.entity.FallFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class FallFurnaceScreenHandler extends ScreenHandler {

    private final FallFurnaceBlockEntity blockEntity;
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    private static final int DOMAIN_SLOTS = 3;
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_OUTPUT_1 = 1;
    private static final int SLOT_OUTPUT_2 = 2;
    private static final int TOTAL_SLOTS = DOMAIN_SLOTS + 36;

    public FallFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, FallFurnaceBlockEntity blockEntity) {
        super(ModBlock.FALL_FURNACE_SCREEN_HANDLER, syncId);
        this.blockEntity = blockEntity;
        this.inventory = blockEntity.getInventory();
        this.propertyDelegate = blockEntity.getPropertyDelegate();
        initSlots(playerInventory);
        this.addProperties(this.propertyDelegate);
    }

    public FallFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModBlock.FALL_FURNACE_SCREEN_HANDLER, syncId);
        this.blockEntity = null;
        this.inventory = new SimpleInventory(3);
        this.propertyDelegate = new PropertyDelegate() {
            private final int[] v = new int[4];
            @Override public int size() { return 4; }
            @Override public int get(int i) { return v[i]; }
            @Override public void set(int i, int val) { v[i] = val; }
        };
        initSlots(playerInventory);
        this.addProperties(this.propertyDelegate);
    }

    private void initSlots(PlayerInventory playerInventory) {
        this.addSlot(new Slot(inventory, SLOT_INPUT, 53, 35));
        this.addSlot(new OutputSlot(inventory, SLOT_OUTPUT_1, 114, 17));
        this.addSlot(new OutputSlot(inventory, SLOT_OUTPUT_2, 114, 53));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public FallFurnaceBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack stackInSlot = slot.getStack();
            originalStack = stackInSlot.copy();
            if (index < DOMAIN_SLOTS) {
                if (!this.insertItem(stackInSlot, DOMAIN_SLOTS, TOTAL_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= DOMAIN_SLOTS) {
                if (!this.insertItem(stackInSlot, SLOT_INPUT, SLOT_INPUT + 1, false)) {
                    return ItemStack.EMPTY;
                }
                if (index < DOMAIN_SLOTS + 27) {
                    if (!this.insertItem(stackInSlot, DOMAIN_SLOTS + 27, TOTAL_SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(stackInSlot, DOMAIN_SLOTS, DOMAIN_SLOTS + 27, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return originalStack;
    }

    static class OutputSlot extends Slot {
        OutputSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }
    }
}
