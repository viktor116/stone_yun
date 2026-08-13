package com.soybean.mixin;

import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RespawnAnchorBlock.class)
public class RespawnAnchorBlockMixin {

    @Inject(method = "isNether", at = @At("HEAD"), cancellable = true)
    private static void onIsNether(World world, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
