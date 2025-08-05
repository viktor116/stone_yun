package com.soybean.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public class AxisBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

    public AxisBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AXIS, Direction.Axis.X));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction.Axis axis = ctx.getHorizontalPlayerFacing().getAxis();
        return this.getDefaultState().with(AXIS, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch (rotation) {
            case CLOCKWISE_90:
            case COUNTERCLOCKWISE_90:
                switch (state.get(AXIS)) {
                    case Z:
                        return state.with(AXIS, Direction.Axis.X);
                    case X:
                        return state.with(AXIS, Direction.Axis.Z);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }
}
