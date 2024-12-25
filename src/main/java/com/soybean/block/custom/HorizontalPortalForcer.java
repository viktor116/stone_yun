package com.soybean.block.custom;

import com.soybean.block.ModBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.Heightmap;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestTypes;

import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author soybean
 * @date 2024/12/25 16:50
 * @description
 */
public class HorizontalPortalForcer {
    private final ServerWorld world;
    private final Random random;

    public HorizontalPortalForcer(ServerWorld world) {
        this.world = world;
        this.random = new Random();
    }

    public Optional<BlockPos> getPortalPos(BlockPos destination, boolean inNether, WorldBorder worldBorder) {
        // Search for existing portal in a 128-block radius
        BlockPos portalPos = findPortalAround(destination, inNether, worldBorder);
        return Optional.ofNullable(portalPos);
    }

    private BlockPos findPortalAround(BlockPos center, boolean inNether, WorldBorder worldBorder) {
        int radius = 16;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos pos = center.add(x, 0, z);
                if (worldBorder.contains(pos) && world.getBlockState(pos).getBlock() instanceof HorizontalNetherPortalBlock) {
                    return pos;
                }
            }
        }
        return null;
    }

    public Optional<BlockLocating.Rectangle> createPortal(BlockPos pos, Direction.Axis axis) {
        Direction mainDir = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        Direction crossDir = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;

        // Find suitable ground
        BlockPos.Mutable mutable = pos.mutableCopy();
        while (mutable.getY() > world.getBottomY() && world.isAir(mutable)) {
            mutable.move(Direction.DOWN);
        }

        if (!world.getBlockState(mutable).isSolid()) {
            return Optional.empty();
        }

        mutable.move(Direction.UP);

        // Create obsidian frame
        for (int w = 0; w <= 2; w++) {
            for (int h = 0; h <= 3; h++) {
                if (w == 0 || w == 2 || h == 0 || h == 3) {
                    world.setBlockState(mutable.move(mainDir, w).move(crossDir, h),
                            Blocks.OBSIDIAN.getDefaultState(), Block.NOTIFY_ALL);
                }
                mutable.move(crossDir, -h);
            }
            mutable.move(mainDir, -w);
        }

        // Create portal blocks
        Optional<HorizontalNetherPortal> portal = HorizontalNetherPortal.createPortal(world, mutable);
        if (portal.isPresent()) {
            return Optional.of(new BlockLocating.Rectangle(mutable, 2, 3));
        }

        return Optional.empty();
    }
}
