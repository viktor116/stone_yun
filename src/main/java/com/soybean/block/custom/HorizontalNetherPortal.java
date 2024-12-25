package com.soybean.block.custom;

import com.soybean.block.ModBlock;
import net.minecraft.block.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author soybean
 * @date 2024/12/25 12:52
 * @description
 */
public class HorizontalNetherPortal {
    private static final int MIN_WIDTH = 2;
    private static final int MAX_WIDTH = 21;
    private static final int MIN_HEIGHT = 3;
    private static final int MAX_HEIGHT = 21;

    private final WorldAccess world;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private final Direction leftDir;
    private final Direction topDir;
    private final Direction bottomDir;
    private final BlockPos bottomLeft;
    private int width;
    private int height;

    public HorizontalNetherPortal(WorldAccess world, BlockPos pos, Direction.Axis axis) {
        this.world = world;
        this.axis = axis;

        this.rightDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        this.leftDir = rightDir.getOpposite();
        this.topDir = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        this.bottomDir = topDir.getOpposite();

        BlockPos.Mutable mutable = pos.mutableCopy();

        // Find bottom-left corner
        while (world.getBlockState(mutable.move(leftDir)).isOf(Blocks.OBSIDIAN)) {
            if (width >= MAX_WIDTH) break;
            width++;
        }
        mutable.move(rightDir);

        while (world.getBlockState(mutable.move(bottomDir)).isOf(Blocks.OBSIDIAN)) {
            if (height >= MAX_HEIGHT) break;
            height++;
        }
        mutable.move(topDir);

        this.bottomLeft = mutable.toImmutable();
    }

    public boolean wasAlreadyValid() {
        if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT) {
            return false;
        }

        return validateFrame();
    }

    private boolean validateFrame() {
        BlockPos.Mutable mutable = bottomLeft.mutableCopy();

        // Validate horizontal edges
        for (int i = 0; i <= width; i++) {
            if (!isObsidian(mutable) || !isObsidian(mutable.move(topDir, height))) {
                return false;
            }
            mutable.move(topDir, -height).move(rightDir);
        }
        mutable.move(rightDir, -width);

        // Validate vertical edges
        for (int i = 0; i <= height; i++) {
            if (!isObsidian(mutable) || !isObsidian(mutable.move(rightDir, width))) {
                return false;
            }
            mutable.move(rightDir, -width).move(topDir);
        }

        return true;
    }

    private boolean isObsidian(BlockPos pos) {
        return world.getBlockState(pos).isOf(Blocks.OBSIDIAN);
    }

    public static Optional<HorizontalNetherPortal> createPortal(WorldAccess world, BlockPos pos) {
        Optional<HorizontalNetherPortal> xPortal = createPortalInAxis(world, pos, Direction.Axis.X);
        if (xPortal.isPresent()) return xPortal;

        return createPortalInAxis(world, pos, Direction.Axis.Z);
    }

    private static Optional<HorizontalNetherPortal> createPortalInAxis(WorldAccess world, BlockPos pos, Direction.Axis axis) {
        HorizontalNetherPortal portal = new HorizontalNetherPortal(world, pos, axis);
        if (portal.wasAlreadyValid()) {
            portal.createPortalBlocks();
            return Optional.of(portal);
        }
        return Optional.empty();
    }

    private void createPortalBlocks() {
        BlockPos.Mutable mutable = bottomLeft.mutableCopy().move(Direction.UP);
        BlockState portalState = ModBlock.HORIZONTAL_NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, axis);

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                world.setBlockState(mutable.move(rightDir, w).move(topDir, h), portalState, Block.NOTIFY_ALL);
            }
            mutable.move(rightDir, -width).move(topDir, -height);
        }
    }

}
