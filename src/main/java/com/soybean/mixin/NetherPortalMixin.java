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
    @Shadow @Final private WorldAccess world;
    @Shadow @Final private BlockPos lowerCorner;
    @Shadow @Final private Direction.Axis axis;

    // 修改高度检查
    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    private void getHeight(CallbackInfoReturnable<Integer> cir) {
        if (isSimplePortalValid()) {
            cir.setReturnValue(1);
        }
    }

    // 修改宽度检查
    @Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    private void getWidth(CallbackInfoReturnable<Integer> cir) {
        if (isSimplePortalValid()) {
            cir.setReturnValue(1);
        }
    }

    // 修改有效性检查
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void isValid(CallbackInfoReturnable<Boolean> cir) {
        if (isSimplePortalValid()) {
            cir.setReturnValue(true);
        }
    }

    // 修改框架有效性检查
    @Inject(method = "isHorizontalFrameValid", at = @At("HEAD"), cancellable = true)
    private void isHorizontalFrameValid(BlockPos.Mutable pos, int height, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    // 修改传送门创建逻辑
    @Inject(method = "createPortal", at = @At("HEAD"), cancellable = true)
    private void createPortal(CallbackInfo ci) {
        if (isSimplePortalValid()) {
            // 根据初始轴向设置传送门方向
            Direction.Axis portalAxis = (this.axis == Direction.Axis.X) ? Direction.Axis.X : Direction.Axis.Z;

            // 设置传送门方块
            BlockState portalState = Blocks.NETHER_PORTAL.getDefaultState()
                    .with(NetherPortalBlock.AXIS, portalAxis);

            this.world.setBlockState(this.lowerCorner, portalState, 18);
            ci.cancel();
        }
    }

    // 辅助方法：检查简单传送门是否有效
    private boolean isSimplePortalValid() {
        return this.world.getBlockState(this.lowerCorner.up()).isOf(Blocks.OBSIDIAN) &&
                this.world.getBlockState(this.lowerCorner.down()).isOf(Blocks.OBSIDIAN);
    }
}