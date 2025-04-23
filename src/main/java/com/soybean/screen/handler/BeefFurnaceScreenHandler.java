package com.soybean.screen.handler;

import com.soybean.screen.ScreenRegister;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

/**
 * @author soybean
 * @date 2025/4/23 13:21
 * @description
 */
public class BeefFurnaceScreenHandler extends AbstractFurnaceScreenHandler {

    public BeefFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenRegister.BEEF_FURNACE_SCREEN_HANDLER_TYPE,RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public BeefFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ScreenRegister.BEEF_FURNACE_SCREEN_HANDLER_TYPE,RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }
}
