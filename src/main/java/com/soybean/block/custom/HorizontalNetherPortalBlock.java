package com.soybean.block.custom;

import com.mojang.serialization.MapCodec;
import com.soybean.config.InitValue;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author soybean
 * @date 2024/12/25 12:51
 * @description
 */

public class HorizontalNetherPortalBlock extends NetherPortalBlock {
    protected static final VoxelShape X_SHAPE = Block.createCuboidShape(0.0, 6.0, 0.0, 16.0, 10.0, 16.0);
    protected static final VoxelShape Z_SHAPE = Block.createCuboidShape(0.0, 6.0, 0.0, 16.0, 10.0, 16.0);

    public HorizontalNetherPortalBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X));
    }

    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getDimension().natural() && world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && random.nextInt(2000) < world.getDifficulty().getId()) {
            while(world.getBlockState(pos).isOf(this)) {
                pos = pos.down();
            }

            if (world.getBlockState(pos).allowsSpawning(world, pos, EntityType.ZOMBIFIED_PIGLIN)) {
                Entity entity = EntityType.ZOMBIFIED_PIGLIN.spawn(world, pos.up(), SpawnReason.STRUCTURE);
                if (entity != null) {
                    entity.resetPortalCooldown();
                }
            }
        }
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                   WorldAccess world, BlockPos pos, BlockPos neighborPos) {

        Direction.Axis axis = direction.getAxis();
        Direction.Axis portalAxis = state.get(AXIS);
        boolean bl = portalAxis != axis && axis.isHorizontal();
        return !bl && !neighborState.isOf(this) &&
                !(new HorizontalNetherPortal(world, pos, portalAxis)).wasAlreadyValid() ?
                Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(AXIS) == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
    }
}
