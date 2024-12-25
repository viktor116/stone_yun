package com.soybean.mixin;

import com.soybean.block.ModBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.Heightmap;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {
//    @Shadow
//    private ServerWorld world;
//
//    @Inject(
//            method = "createPortal",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void createHorizontalPortal(BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Optional<BlockLocating.Rectangle>> cir) {
//        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
//        WorldBorder worldBorder = this.world.getWorldBorder();
//
//        // 查找合适的位置创建水平传送门
//        int maxY = Math.min(this.world.getTopY(), this.world.getBottomY() + this.world.getLogicalHeight()) - 1;
//        BlockPos.Mutable mutable = pos.mutableCopy();
//        BlockPos portalPos = null;
//        double minDistance = -1.0;
//
//        // 在16格范围内寻找合适的位置
//        for (BlockPos.Mutable searchPos : BlockPos.iterateInSquare(pos, 16, Direction.EAST, Direction.SOUTH)) {
//            if (!worldBorder.contains(searchPos)) {
//                continue;
//            }
//
//            int topY = Math.min(maxY, this.world.getTopY(Heightmap.Type.MOTION_BLOCKING, searchPos.getX(), searchPos.getZ()));
//
//            // 从上往下搜索合适的位置
//            for (int y = topY; y >= this.world.getBottomY(); --y) {
//                searchPos.setY(y);
//                if (isValidHorizontalPortalLocation(searchPos, mutable)) {
//                    double distance = pos.getSquaredDistance(searchPos);
//                    if (minDistance == -1.0 || distance < minDistance) {
//                        minDistance = distance;
//                        portalPos = searchPos.toImmutable();
//                    }
//                }
//            }
//        }
//
//        // 如果找不到合适的位置，让原版逻辑处理
//        if (portalPos == null) {
//            return;
//        }
//
//        // 创建水平传送门结构
//        BlockState obsidian = Blocks.OBSIDIAN.getDefaultState();
//        BlockState portalBlock = ModBlock.HORIZONTAL_NETHER_PORTAL.getDefaultState().with(Properties.HORIZONTAL_AXIS, axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
//
//        // 放置黑曜石框架
//        for (int x = -1; x <= 1; x++) {
//            for (int z = -1; z <= 1; z++) {
//                if (Math.abs(x) == 1 || Math.abs(z) == 1) {
//                    mutable.set(portalPos.getX() + x, portalPos.getY(), portalPos.getZ() + z);
//                    this.world.setBlockState(mutable, obsidian, Block.NOTIFY_ALL);
//                }
//            }
//        }
//
//        // 放置传送门方块
//        mutable.set(portalPos);
//        this.world.setBlockState(mutable, portalBlock, Block.NOTIFY_ALL);
//        cir.cancel();
//        cir.setReturnValue(Optional.of(new BlockLocating.Rectangle(portalPos, 1, 1)));
//    }
//
//    private boolean isValidHorizontalPortalLocation(BlockPos pos, BlockPos.Mutable mutable) {
//        // 检查下方是否有实心方块
//        if (!this.world.getBlockState(pos.down()).isSolid()) {
//            return false;
//        }
//
//        // 检查传送门位置和周围是否有足够空间
//        for (int x = -1; x <= 1; x++) {
//            for (int z = -1; z <= 1; z++) {
//                mutable.set(pos.getX() + x, pos.getY(), pos.getZ() + z);
//                BlockState state = this.world.getBlockState(mutable);
//
//                if (Math.abs(x) == 1 || Math.abs(z) == 1) {
//                    // 框架位置必须可以放置方块
//                    if (!state.isReplaceable()) {
//                        return false;
//                    }
//                } else {
//                    // 传送门内部必须是空的
//                    if (!state.isAir() && !state.isReplaceable()) {
//                        return false;
//                    }
//                }
//
//                // 检查上方是否有空间
//                if (!this.world.getBlockState(mutable.up()).isReplaceable()) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
}
