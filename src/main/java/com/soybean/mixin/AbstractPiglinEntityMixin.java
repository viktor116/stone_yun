package com.soybean.mixin;

import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractPiglinEntity.class)
public class AbstractPiglinEntityMixin {

    @Inject(method = "shouldZombify", at = @At("HEAD"), cancellable = true)
    private void preventZombify(CallbackInfoReturnable<Boolean> cir) {
        AbstractPiglinEntity self = (AbstractPiglinEntity) (Object) this;

        // 非地狱维度直接返回 false，禁止颤抖计时器累加
        if (!self.getWorld().getDimension().piglinSafe()) {
            cir.setReturnValue(false);
        }
    }
}
