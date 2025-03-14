package com.soybean.block.custom;

import com.soybean.block.ModBlock;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.dimension.NetherPortal;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author soybean
 * @date 2024/12/25 12:52
 * @description
 */
public class HorizontalNetherPortal extends NetherPortal {
    private static final int MIN_WIDTH = 2;
    public static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    public static final int MAX_HEIGHT = 21;
    private static final AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK = (state, world, pos) -> {
        return state.isOf(Blocks.OBSIDIAN);
    };
    private static final float FALLBACK_THRESHOLD = 4.0F;
    private static final double HEIGHT_STRETCH = 1.0;
    private final WorldAccess world;
    private final Direction.Axis axis;
    private final Direction negativeDir;
    private int foundPortalBlocks;
    @Nullable
    private BlockPos lowerCorner;
    private int height;
    private final int width;

    public HorizontalNetherPortal(WorldAccess world, BlockPos pos, Direction.Axis axis) {
        super( world,pos, axis);
        this.world = world;
        this.axis = axis;
        this.negativeDir = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        this.lowerCorner = this.getLowerCorner(pos);
        if (this.lowerCorner == null) {
            this.lowerCorner = pos;
            this.width = 1;
            this.height = 1;
        } else {
            this.width = this.getWidth();
            if (this.width > 0) {
                this.height = this.getHeight();
            }
        }

    }
    @Nullable
    private BlockPos getLowerCorner(BlockPos pos) {
        for(int i = Math.max(this.world.getBottomY(), pos.getY() - 21); pos.getY() > i && validStateInsidePortal(this.world.getBlockState(pos.down())); pos = pos.down()) {
        }

        Direction direction = this.negativeDir.getOpposite();
        int j = this.getWidth(pos, direction) - 1;
        return j < 0 ? null : pos.offset(direction, j);
    }

    private int getWidth() {
        int i = this.getWidth(this.lowerCorner, this.negativeDir);
        return i >= 2 && i <= 21 ? i : 0;
    }

    private int getWidth(BlockPos pos, Direction direction) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int i = 0; i <= 21; ++i) {
            mutable.set(pos).move(direction, i);
            BlockState blockState = this.world.getBlockState(mutable);
            if (!validStateInsidePortal(blockState)) {
                if (IS_VALID_FRAME_BLOCK.test(blockState, this.world, mutable)) {
                    return i;
                }
                break;
            }

            BlockState blockState2 = this.world.getBlockState(mutable.move(Direction.DOWN));
            if (!IS_VALID_FRAME_BLOCK.test(blockState2, this.world, mutable)) {
                break;
            }
        }

        return 0;
    }

    private int getHeight() {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = this.getPotentialHeight(mutable);
        return i >= 3 && i <= 21 && this.isHorizontalFrameValid(mutable, i) ? i : 0;
    }

    private boolean isHorizontalFrameValid(BlockPos.Mutable pos, int height) {
        for(int i = 0; i < this.width; ++i) {
            BlockPos.Mutable mutable = pos.set(this.lowerCorner).move(Direction.UP, height).move(this.negativeDir, i);
            if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(mutable), this.world, mutable)) {
                return false;
            }
        }

        return true;
    }

    private int getPotentialHeight(BlockPos.Mutable pos) {
        for(int i = 0; i < 21; ++i) {
            pos.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, -1);
            if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(pos), this.world, pos)) {
                return i;
            }

            pos.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, this.width);
            if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(pos), this.world, pos)) {
                return i;
            }

            for(int j = 0; j < this.width; ++j) {
                pos.set(this.lowerCorner).move(Direction.UP, i).move(this.negativeDir, j);
                BlockState blockState = this.world.getBlockState(pos);
                if (!validStateInsidePortal(blockState)) {
                    return i;
                }

                if (blockState.isOf(Blocks.NETHER_PORTAL)) {
                    ++this.foundPortalBlocks;
                }
            }
        }

        return 21;
    }

    private static boolean validStateInsidePortal(BlockState state) {
        return state.isAir() || state.isIn(BlockTags.FIRE) || state.isOf(Blocks.NETHER_PORTAL);
    }

    public boolean isValid() {
        return this.lowerCorner != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }

    public void createPortal() {
        BlockState blockState = (BlockState)Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, this.axis);
        BlockPos.iterate(this.lowerCorner, this.lowerCorner.offset(Direction.UP, this.height - 1).offset(this.negativeDir, this.width - 1)).forEach((pos) -> {
            this.world.setBlockState(pos, blockState, 18);
        });
    }

    public boolean wasAlreadyValid() {
        return this.isValid() && this.foundPortalBlocks == this.width * this.height;
    }

    public static Vec3d entityPosInPortal(BlockLocating.Rectangle portalRect, Direction.Axis portalAxis, Vec3d entityPos, EntityDimensions entityDimensions) {
        double d = (double)portalRect.width - (double)entityDimensions.width();
        double e = (double)portalRect.height - (double)entityDimensions.height();
        BlockPos blockPos = portalRect.lowerLeft;
        double g;
        double f;
        if (d > 0.0) {
            f = (double)blockPos.getComponentAlongAxis(portalAxis) + (double)entityDimensions.width() / 2.0;
            g = MathHelper.clamp(MathHelper.getLerpProgress(entityPos.getComponentAlongAxis(portalAxis) - f, 0.0, d), 0.0, 1.0);
        } else {
            g = 0.5;
        }

        Direction.Axis axis;
        if (e > 0.0) {
            axis = Direction.Axis.Y;
            f = MathHelper.clamp(MathHelper.getLerpProgress(entityPos.getComponentAlongAxis(axis) - (double)blockPos.getComponentAlongAxis(axis), 0.0, e), 0.0, 1.0);
        } else {
            f = 0.0;
        }

        axis = portalAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        double h = entityPos.getComponentAlongAxis(axis) - ((double)blockPos.getComponentAlongAxis(axis) + 0.5);
        return new Vec3d(g, f, h);
    }

    public static Vec3d findOpenPosition(Vec3d fallback, ServerWorld world, Entity entity, EntityDimensions dimensions) {
        if (!(dimensions.width() > 4.0F) && !(dimensions.height() > 4.0F)) {
            double d = (double)dimensions.height() / 2.0;
            Vec3d vec3d = fallback.add(0.0, d, 0.0);
            VoxelShape voxelShape = VoxelShapes.cuboid(Box.of(vec3d, (double)dimensions.width(), 0.0, (double)dimensions.width()).stretch(0.0, 1.0, 0.0).expand(1.0E-6));
            Optional<Vec3d> optional = world.findClosestCollision(entity, voxelShape, vec3d, (double)dimensions.width(), (double)dimensions.height(), (double)dimensions.width());
            Optional<Vec3d> optional2 = optional.map((pos) -> {
                return pos.subtract(0.0, d, 0.0);
            });
            return (Vec3d)optional2.orElse(fallback);
        } else {
            return fallback;
        }
    }
}
