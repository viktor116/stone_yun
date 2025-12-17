package com.soybean.block.custom.entity;

import com.soybean.block.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;

/**
 * @author soybean
 * @date 2025/1/18 12:14
 * @description
 */
public class HalfWhiteBedEntity extends BlockEntity {

    private DyeColor color = DyeColor.RED;
    public HalfWhiteBedEntity(BlockPos pos, BlockState state) {
        super(ModBlock.HALF_WHITE_BED_ENTITY, pos, state);
    }
    public DyeColor getColor() {
        return color;
    }

    public String getSpecialName() {
        return "half_white";
    }
}
