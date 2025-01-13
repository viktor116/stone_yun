package com.soybean.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/13 15:03
 * @description
 */
public class BigWallTorchBlock extends WallTorchBlock {
    public BigWallTorchBlock(SimpleParticleType simpleParticleType, Settings settings) {
        super(simpleParticleType, settings);
    }
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        Direction direction = (Direction)state.get(FACING);
        double d = (double)pos.getX() + 0.5;
        double e = (double)pos.getY() + 1;
        double f = (double)pos.getZ() + 0.5;
        double g = 0.22;
        double h = 0.27;
        Direction direction2 = direction.getOpposite();
        world.addParticle(ParticleTypes.SMOKE, d + 0.27 * (double)direction2.getOffsetX(), e + 0.22, f + 0.27 * (double)direction2.getOffsetZ(), 0.0, 0.0, 0.0);
        world.addParticle(this.particle, d + 0.27 * (double)direction2.getOffsetX(), e + 0.22, f + 0.27 * (double)direction2.getOffsetZ(), 0.0, 0.0, 0.0);
    }
}
