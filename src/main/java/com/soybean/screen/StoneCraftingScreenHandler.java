package com.soybean.screen;

import com.soybean.block.ModBlock;
import com.sun.jna.platform.win32.WinDef;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/11/1 10:09
 * @description
 */
public class StoneCraftingScreenHandler extends CraftingScreenHandler {
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    // 添加一个符合工厂方法签名的构造函数
    public StoneCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public StoneCraftingScreenHandler(int syncId, PlayerInventory playerInventory,
                                      ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
        this.context = context;
        this.player = playerInventory.player;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        // 确保只有在方块存在的时候才能使用
        return canUse(context, player, ModBlock.STONE_CRAFT_TABLE) ||
                canUse(context, player, ModBlock.AIR_CRAFT_TABLE) ||
                canUse(context,player, ModBlock.DIRT_CRAFT_TABLE);
    }
}