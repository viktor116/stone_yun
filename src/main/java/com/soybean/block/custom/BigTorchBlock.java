package com.soybean.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/13 15:00
 * @description
 */
public class BigTorchBlock extends TorchBlock {
    public BigTorchBlock(SimpleParticleType particle, Settings settings) {
        super(particle, settings);
    }
    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double d = (double)pos.getX() + 0.5;
        double e = (double)pos.getY() + 1;
        double f = (double)pos.getZ() + 0.5;
        world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
        world.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);
    }
}
