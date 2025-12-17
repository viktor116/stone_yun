package com.soybean.world.portals;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 *
 * @Author: gjh
 * @Date: 2025/12/17 15:38
 **/
public class CustomPortalActivator {

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;

            if (player.getStackInHand(hand).isOf(Items.FLINT_AND_STEEL)) {
                BlockPos pos = hitResult.getBlockPos();
                if (world.getBlockState(pos).isOf(Blocks.OBSIDIAN)) {
                    // 尝试在 X 轴方向激活 (传送门平面在 X 轴)
                    if (tryCreate(world, pos, Direction.Axis.X)) return ActionResult.SUCCESS;
                    // 如果失败，尝试在 Z 轴方向激活
                    if (tryCreate(world, pos, Direction.Axis.Z)) return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }

    private static boolean tryCreate(World world, BlockPos clickedPos, Direction.Axis axis) {
        // 同样尝试 4 个可能的起始点
        for (int offset = 0; offset <= 3; offset++) {
            BlockPos startPos = getRelativePos(clickedPos, axis, -offset, 0);

            // 1. 首先检查共有部分（底层和第二层框架）
            if (checkBaseFrame(world, startPos, axis)) {

                // 2. 检查正向排列 (y=2 的黑曜石在左侧索引 0)
                if (isObsidian(world, getRelativePos(startPos, axis, 0, 2))) {
                    deployPortal(world, startPos, axis, false); // false 代表非镜像
                    return true;
                }

                // 3. 检查镜像排列 (y=2 的黑曜石在右侧索引 3)
                if (isObsidian(world, getRelativePos(startPos, axis, 3, 2))) {
                    deployPortal(world, startPos, axis, true); // true 代表镜像
                    return true;
                }
            }
        }
        return false;
    }

    // 检查共有的基础框架部分
    private static boolean checkBaseFrame(World world, BlockPos start, Direction.Axis axis) {
        // y=0: [#][#][#][#]
        for (int i = 0; i < 4; i++) {
            if (!isObsidian(world, getRelativePos(start, axis, i, 0))) return false;
        }
        // y=1: [#][ ][ ][#]
        if (!isObsidian(world, getRelativePos(start, axis, 0, 1))) return false;
        if (!isObsidian(world, getRelativePos(start, axis, 3, 1))) return false;

        return true;
    }

    // 放置传送门，增加镜像判断
    private static void deployPortal(World world, BlockPos start, Direction.Axis axis, boolean isMirrored) {
        if (!isMirrored) {
            // 原始形状生成位置
            setPortal(world, getRelativePos(start, axis, 1, 1), axis);
            setPortal(world, getRelativePos(start, axis, 2, 1), axis);
            setPortal(world, getRelativePos(start, axis, 1, 2), axis);
        } else {
            // 镜像形状生成位置
            setPortal(world, getRelativePos(start, axis, 1, 1), axis);
            setPortal(world, getRelativePos(start, axis, 2, 1), axis);
            setPortal(world, getRelativePos(start, axis, 2, 2), axis); // 顶层 X 移到了右边
        }
    }

    private static void setPortal(World world, BlockPos pos, Direction.Axis axis) {
        if (world.isAir(pos)) {
            world.setBlockState(pos, Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, axis));
        }
    }

    private static boolean checkPattern(World world, BlockPos start, Direction.Axis axis) {
        // y=0 层: [#] [#] [#] [#] (全部4个位置)
        for (int i = 0; i < 4; i++) {
            if (!isObsidian(world, getRelativePos(start, axis, i, 0))) return false;
        }

        // y=1 层: [#] [X] [X] [#] (索引 0 和 3 是黑曜石)
        if (!isObsidian(world, getRelativePos(start, axis, 0, 1))) return false;
        if (!isObsidian(world, getRelativePos(start, axis, 3, 1))) return false;
        // 确保 X 的位置是空的
        if (!world.isAir(getRelativePos(start, axis, 1, 1))) return false;
        if (!world.isAir(getRelativePos(start, axis, 2, 1))) return false;

        // y=2 层: [#] [X] [ ] [ ] (索引 0 是黑曜石)
        if (!isObsidian(world, getRelativePos(start, axis, 0, 2))) return false;
        // 确保 X 的位置是空的
        if (!world.isAir(getRelativePos(start, axis, 1, 2))) return false;

        return true;
    }

    private static void deployPortal(World world, BlockPos start, Direction.Axis axis) {
        // 严格按照你的 X 坐标定义放置
        BlockPos[] portalPositions = {
                getRelativePos(start, axis, 1, 1), // 层1中间左
                getRelativePos(start, axis, 2, 1), // 层1中间右
                getRelativePos(start, axis, 1, 2)  // 层2左侧
        };
        for (BlockPos p : portalPositions) {
            world.setBlockState(p, Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, axis));
        }
    }

    /**
     * 核心工具方法：统一相对坐标计算
     * @param start 起始基准点 (左下角的 #)
     * @param axis 传送门的平面轴向
     * @param hOffset 水平偏移（沿轴向）
     * @param vOffset 垂直偏移（Y轴）
     */
    private static BlockPos getRelativePos(BlockPos start, Direction.Axis axis, int hOffset, int vOffset) {
        if (axis == Direction.Axis.X) {
            return start.east(hOffset).up(vOffset);
        } else {
            return start.south(hOffset).up(vOffset);
        }
    }

    private static boolean isObsidian(World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(Blocks.OBSIDIAN);
    }
}
