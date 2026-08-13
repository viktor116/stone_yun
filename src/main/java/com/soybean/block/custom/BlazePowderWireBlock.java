package com.soybean.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlazePowderWireBlock extends AbstractPowderWireBlock {

    public BlazePowderWireBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected ItemStack getDropStack() {
        return new ItemStack(Items.BLAZE_POWDER);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient && entity instanceof LivingEntity living) {
            if (living.getFireTicks() <= 0) {
                living.setOnFireForTicks(60);
            }
        }
    }
}