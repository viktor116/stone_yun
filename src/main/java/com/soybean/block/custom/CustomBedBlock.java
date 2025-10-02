package com.soybean.block.custom;

import com.soybean.block.custom.entity.FlipWhiteBedEntity;
import com.soybean.block.custom.entity.InvertRedBedEntity;
import com.soybean.block.custom.entity.TntWhiteBedEntity;
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

    public DyeColor color;
    public CustomBedBlock(Settings settings, DyeColor color) {
        super(color, settings);
        this.color = color;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if(this.color == DyeColor.WHITE) {
            return new FlipWhiteBedEntity(pos, state);  // 返回自定义的 BlockEntity
        }else if(this.color == DyeColor.BLACK){
            return new TntWhiteBedEntity(pos,state); //暂时使用黑色来替代TNT床
        }else {
            return new InvertRedBedEntity(pos, state);
        }
    }

}
