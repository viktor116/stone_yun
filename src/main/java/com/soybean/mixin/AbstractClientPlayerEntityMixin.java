package com.soybean.mixin;

import com.mojang.authlib.GameProfile;
import com.soybean.items.ItemsRegister;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }
    @Inject(method = "getFovMultiplier",at = @At(value = "TAIL"),locals = LocalCapture.CAPTURE_FAILSOFT,cancellable = true)
    public void getFovMultiplierMixin(CallbackInfoReturnable<Float> info,float f) {
        ItemStack itemStack = this.getActiveItem();
        if (this.isUsingItem()) {
            if (itemStack.isOf(ItemsRegister.WITHER_BOW) || itemStack.isOf(ItemsRegister.THE_END_BOW)) {
                int i = this.getItemUseTime();
                float g = (float)i / 20.0F;
                if (g > 1.0F) {
                    g = 1.0F;
                } else {
                    g *= g;
                }
                f *= 1.0F - g * 0.15F;
                info.setReturnValue(MathHelper.lerp(((Double)MinecraftClient.getInstance().options.getFovEffectScale().getValue()).floatValue(), 1.0F, f));
            }
        }
    }
}
