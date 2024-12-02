package com.soybean.block.custom;

import com.soybean.block.custom.entity.CowPlantBlockEntity;
import com.soybean.items.ItemsRegister;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

/**
 * @author soybean
 * @date 2024/12/2 17:53
 * @description
 */
public class CowPlantBlock extends Block implements BlockEntityProvider, Fertilizable {
    public static final IntProperty AGE = Properties.AGE_7;

    public CowPlantBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(AGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CowPlantBlockEntity(pos, state);
    }

    public float getScale(BlockState state) {
        return (state.get(AGE) + 1) / 8.0f;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) < 7;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (age < 7 && random.nextInt(10) == 0) {
            world.setBlockState(pos, state.with(AGE, age + 1), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isOf(Blocks.FARMLAND);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(4.0, 0.0, 4.0, 12.0, 8.0 * getScale(state), 12.0);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return state.get(AGE) < 7;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return state.get(AGE) < 7 ;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int newAge = Math.min(7, state.get(AGE) + 1);
        world.setBlockState(pos, state.with(AGE, newAge), Block.NOTIFY_LISTENERS);
    }
}

