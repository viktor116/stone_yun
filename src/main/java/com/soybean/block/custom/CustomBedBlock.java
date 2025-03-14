package com.soybean.block.custom;

import com.soybean.block.custom.entity.CustomBedBlockEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

/**
 * @author soybean
 * @date 2025/1/18 12:00
 * @description
 */
public class CustomBedBlock extends BedBlock {
    public CustomBedBlock(Settings settings) {
        super(DyeColor.WHITE, settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomBedBlockEntity(pos, state);  // 返回自定义的 BlockEntity
    }

}
