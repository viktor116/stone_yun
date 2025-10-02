package com.soybean.block.custom.entity;

import com.soybean.block.ModBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author soybean
 * @date 2025/1/18 12:14
 * @description
 */
public class TntWhiteBedEntity extends BlockEntity {
    public TntWhiteBedEntity(BlockPos pos, BlockState state) {
        super(ModBlock.TNT_WHITE_BED_ENTITY, pos, state);  // 假设 AIR_BED 是你的自定义实体类型
    }
    public String getSpecialName() {
        return "tnt_white";
    }
}
