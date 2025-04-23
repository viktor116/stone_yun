package com.soybean.block.custom.entity;

import com.soybean.block.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/**
 * @author soybean
 * @date 2025/4/23 12:05
 * @description
 */
public class BeefFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    public BeefFurnaceBlockEntity(BlockPos pos, BlockState state)  {
        super(ModBlock.BEEF_FURNACE_TYPE, pos, state, RecipeType.SMELTING);
    }

    protected Text getContainerName() {
        return Text.of("牛肉熔炉");
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
//        return new BeefFurnaceScreenHandler(syncId, playerInventory);
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);

    }
}
