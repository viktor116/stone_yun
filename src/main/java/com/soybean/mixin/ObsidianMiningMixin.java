package com.soybean.mixin;

import com.soybean.config.InitValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author soybean
 * @date 2024/11/12 17:29
 * @description
 */
@Mixin(PlayerEntity.class)
public class ObsidianMiningMixin {
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    private void onGetBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        if (block.getBlock() == Blocks.OBSIDIAN) {
            // 设置挖掘速度为普通木头的速度
            cir.setReturnValue(2.0f);
        }
    }

    @Inject(method = "canHarvest", at = @At("HEAD"), cancellable = true)
    private void onCanHarvest(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == Blocks.OBSIDIAN) {
            cir.setReturnValue(true);
        }
    }
}
