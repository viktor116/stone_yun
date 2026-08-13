package com.soybean.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import java.util.List;

public abstract class AbstractPowderWireBlock extends RedstoneWireBlock {

    public AbstractPowderWireBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return false;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    protected abstract ItemStack getDropStack();

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        return List.of(getDropStack());
    }

    @Override
    protected void neighborUpdate(BlockState state, net.minecraft.world.World world, BlockPos pos, Block block, BlockPos fromPos, boolean movedByPiston) {
    }

    public static boolean canConnectTo(BlockState state) {
        Block b = state.getBlock();
        return b == Blocks.REDSTONE_WIRE || b instanceof AbstractPowderWireBlock;
    }

    @Override
    public BlockState getPlacementState(net.minecraft.item.ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        WorldAccess world = ctx.getWorld();
        return recomputeConnections(getDefaultState(), world, pos);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                   WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockPos below = pos.down();
        if (!world.getBlockState(below).isSolidBlock(world, below)) {
            return Blocks.AIR.getDefaultState();
        }
        return recomputeConnections(state, world, pos);
    }

    private BlockState recomputeConnections(BlockState state, WorldAccess world, BlockPos pos) {
        BlockState result = state;
        for (Direction dir : Direction.Type.HORIZONTAL) {
            WireConnection wc = computeConnection(world, pos, dir);
            result = result.with(DIRECTION_TO_WIRE_CONNECTION_PROPERTY.get(dir), wc);
        }
        return result;
    }

    private WireConnection computeConnection(WorldAccess world, BlockPos pos, Direction dir) {
        BlockPos neighborPos = pos.offset(dir);
        BlockState neighborState = world.getBlockState(neighborPos);

        if (canConnectTo(neighborState)) {
            return WireConnection.SIDE;
        }

        if (!neighborState.isSolidBlock(world, neighborPos)) {
            BlockPos belowNeighbor = neighborPos.down();
            BlockState belowState = world.getBlockState(belowNeighbor);
            if (canConnectTo(belowState)) {
                return WireConnection.SIDE;
            }
        }

        BlockPos aboveNeighbor = neighborPos.up();
        BlockState aboveState = world.getBlockState(aboveNeighbor);
        if (canConnectTo(aboveState) && neighborState.isSideSolidFullSquare(world, neighborPos, dir.getOpposite())) {
            return WireConnection.UP;
        }

        return WireConnection.NONE;
    }
}