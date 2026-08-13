package com.soybean.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "isUsingSpyglass", at = @At("HEAD"), cancellable = true)
    private void onIsUsingSpyglass(CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self.isUsingItem()) {
            ItemStack active = self.getActiveItem();
            if (active != null && active.getUseAction() == UseAction.SPYGLASS) {
                cir.setReturnValue(true);
            }
        }
    }
}
