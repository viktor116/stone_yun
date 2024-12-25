package com.soybean.mixin;

import com.soybean.block.ModBlock;
import net.minecraft.block.*;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(NetherPortal.class)
public class NetherPortalMixin {
    @Shadow @Final
    private WorldAccess world;
    @Shadow @Final
    private BlockPos lowerCorner;
    @Shadow @Final
    private Direction negativeDir;
    @Shadow @Final
    private int height;
    @Shadow @Final
    private int width;
    @Shadow @Final
    private static AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK;

    // 检查高度：用于水平传送门
    // 检查水平传送门的高度
    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    private void getHorizontalHeight(CallbackInfoReturnable<Integer> cir) {
        // 检查是否是水平传送门
        if (this.world.getBlockState(this.lowerCorner).isOf(ModBlock.HORIZONTAL_NETHER_PORTAL)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            // 检查框架是否完整
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (Math.abs(x) == 1 || Math.abs(z) == 1) {
                        mutable.set(this.lowerCorner).move(x, 0, z);
                        if (!IS_VALID_FRAME_BLOCK.test(this.world.getBlockState(mutable), this.world, mutable)) {
                            cir.setReturnValue(0);
                            return;
                        }
                    }
                }
            }
            cir.cancel();
            cir.setReturnValue(1); // 返回水平传送门的高度
        }
    }

    // 检查水平传送门的宽度
    @Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    private void getHorizontalWidth(CallbackInfoReturnable<Integer> cir) {
//        if (this.world.getBlockState(this.lowerCorner).isOf(ModBlock.HORIZONTAL_NETHER_PORTAL)) {
            cir.cancel();
            cir.setReturnValue(1); // 设置水平传送门的宽度
//        }
    }

    // 检查是否有效的水平传送门
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void checkHorizontalPortal(CallbackInfoReturnable<Boolean> cir) {
        // 只要框架符合水平传送门的要求，返回 true
        if (this.world.getBlockState(this.lowerCorner).isOf(ModBlock.HORIZONTAL_NETHER_PORTAL)) {
            cir.cancel();
            cir.setReturnValue(true);
        }
    }

    // 创建水平传送门
    @Inject(method = "createPortal", at = @At("HEAD"), cancellable = true)
    private void createHorizontalPortal(CallbackInfo ci) {
        if (this.world.getBlockState(this.lowerCorner).isOf(ModBlock.HORIZONTAL_NETHER_PORTAL)) {
            // 创建水平传送门
            BlockState portalState = ModBlock.HORIZONTAL_NETHER_PORTAL.getDefaultState()
                    .with(NetherPortalBlock.AXIS, Direction.Axis.X);  // 或根据需要设置为 Z

            // 重新计算 lowerCorner
            BlockPos startPos = this.lowerCorner;
            BlockPos endPos = startPos.offset(Direction.UP, this.height - 1).offset(this.negativeDir, this.width - 1);

            // 填充传送门
            BlockPos.iterate(startPos, endPos).forEach(pos -> {
                this.world.setBlockState(pos, portalState, 18);
            });

            // 取消原有的传送门创建逻辑，因为我们已经自定义了创建过程
            ci.cancel();
        }
    }

    @Inject(method = "isHorizontalFrameValid",at = @At("HEAD"),cancellable = true)
    private void isHorizontalFrameValid(BlockPos.Mutable pos, int height,CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        cir.setReturnValue(true);
    }
}
