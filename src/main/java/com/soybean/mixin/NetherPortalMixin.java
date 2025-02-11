package com.soybean.mixin;

import com.soybean.block.ModBlock;
import net.minecraft.block.*;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.NetherPortal;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;


@Mixin(NetherPortal.class)
public abstract class NetherPortalMixin {
    @Shadow @Final private WorldAccess world;
    @Shadow @Final private static AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK;
    @Shadow private Direction.Axis axis;
    @Shadow private Direction negativeDir;
    @Shadow @Nullable
    private BlockPos lowerCorner;
    @Shadow private int height;
    @Shadow private int width;

    @Shadow
    public abstract int getPotentialHeight(BlockPos.Mutable pos);

    // 修改 getWidth 方法，允许 1x1 的传送门
    @Inject(method = "getWidth", at = @At("HEAD"), cancellable = true)
    private void getWidth(CallbackInfoReturnable<Integer> cir) {
        int width = getWidth(this.lowerCorner, this.negativeDir);
        if (width >= 1 && width <= 21) { // 允许最小宽度为 1
            cir.setReturnValue(width);
        }
    }

    // 修改 getHeight 方法，允许 1x1 的传送门
    @Inject(method = "getHeight", at = @At("HEAD"), cancellable = true)
    private void getHeight(CallbackInfoReturnable<Integer> cir) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int height = this.getPotentialHeight(mutable);
        if (height >= 1 && height <= 21) { // 允许最小高度为 1
            cir.setReturnValue(height);
        }
    }

    // 修改 isValid 方法，支持 1x1 的传送门
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void isValid(CallbackInfoReturnable<Boolean> cir) {
        if (this.lowerCorner != null && this.width >= 1 && this.width <= 21 && this.height >= 1 && this.height <= 21) {
            cir.setReturnValue(true);
        }
    }

    // 修改 createPortal 方法，支持创建 1x1 的传送门
    @Inject(method = "createPortal", at = @At("HEAD"), cancellable = true)
    private void createPortal(CallbackInfo ci) {
        BlockState blockState = Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, this.axis);
        BlockPos.iterate(this.lowerCorner, this.lowerCorner.offset(Direction.UP, this.height - 1).offset(this.negativeDir, this.width - 1)).forEach((pos) -> {
            this.world.setBlockState(pos, blockState, 18);
        });
        ci.cancel();  // 取消原始的传送门创建逻辑
    }

    // 修改 getLowerCorner 方法，支持 1x1 的传送门
    @Inject(method = "getLowerCorner", at = @At("HEAD"), cancellable = true)
    private void getLowerCorner(BlockPos pos, CallbackInfoReturnable<BlockPos> cir) {
        for (int i = Math.max(this.world.getBottomY(), pos.getY() - 21); pos.getY() > i && validStateInsidePortal(this.world.getBlockState(pos.down())); pos = pos.down()) {
        }

        Direction direction = this.negativeDir.getOpposite();
        int j = getWidth(pos, direction) - 1;
        if (j <= 0) { // 允许 1x1 的传送门
            cir.setReturnValue(pos);
        } else {
            cir.setReturnValue(pos.offset(direction, j));
        }
    }

    // 修改 getWidth 方法，支持宽度为 1
    private int getWidth(BlockPos pos, Direction direction) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int i = 0; i <= 21; ++i) {
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

        return 1;  // 修改：确保最低宽度为 1
    }

    // 你可以添加一些辅助方法，如 validStateInsidePortal 等，来确保其工作正常
    private static boolean validStateInsidePortal(BlockState state) {
        return state.isAir() || state.isIn(BlockTags.FIRE) || state.isOf(Blocks.NETHER_PORTAL);
    }
}