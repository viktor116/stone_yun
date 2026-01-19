package com.soybean.block.custom.entity;

import com.soybean.block.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class SheepPlantBlockEntity extends BlockEntity {

    public SheepPlantBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.SHEEP_PLANT_TYPE, pos, state);
    }
}
