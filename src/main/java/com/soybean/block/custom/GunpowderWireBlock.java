package com.soybean.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class GunpowderWireBlock extends AbstractPowderWireBlock {

    public GunpowderWireBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected ItemStack getDropStack() {
        return new ItemStack(Items.GUNPOWDER);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient && sourceBlock instanceof FireBlock) {
            ignite((ServerWorld) world, pos);
        }
    }

    public void ignite(ServerWorld world, BlockPos pos) {
        if (!(world.getBlockState(pos).getBlock() instanceof GunpowderWireBlock)) return;

        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.3f, 0.8f);

        // 先处理 TNT —— 在自己变 FIRE 之前！防止 FIRE.randomTick 重复点燃
        for (Direction d : Direction.values()) {
            BlockPos np = pos.offset(d);
            if (world.getBlockState(np).getBlock() instanceof TntBlock) {
                world.setBlockState(np, Blocks.AIR.getDefaultState());
                TntBlock.primeTnt(world, np);
            }
        }

        // 自己变成火焰
        world.setBlockState(pos, Blocks.FIRE.getDefaultState());

        // 递归点燃所有相连的火药粉
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos neighborPos = pos.offset(dir);
            BlockState neighborState = world.getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof GunpowderWireBlock gpw) {
                gpw.ignite(world, neighborPos);
            }

            BlockPos upPos = neighborPos.up();
            if (world.getBlockState(upPos).getBlock() instanceof GunpowderWireBlock gpw) {
                gpw.ignite(world, upPos);
            }

            BlockPos downPos = neighborPos.down();
            if (world.getBlockState(downPos).getBlock() instanceof GunpowderWireBlock gpw) {
                gpw.ignite(world, downPos);
            }
        }
    }
}