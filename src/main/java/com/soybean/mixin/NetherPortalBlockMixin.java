package com.soybean.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author soybean
 * @date 2025/2/11 17:46
 * @description
 */
@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {
    @Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"), cancellable = true)
    private void onGetStateForNeighborUpdate(BlockState state, Direction direction,
                                             BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos,
                                             CallbackInfoReturnable<BlockState> cir) {
        // 检查上下是否有黑曜石
        if (world.getBlockState(pos.up()).isOf(Blocks.OBSIDIAN) &&
                world.getBlockState(pos.down()).isOf(Blocks.OBSIDIAN)) {
            cir.setReturnValue(state);
            return;
        }
        // 如果上下没有黑曜石，让原方法处理（返回 air）
    }
}