package com.soybean.mixin;

import com.soybean.items.ItemsRegister;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntityRenderer.class)
public abstract class FishingBobberEntityRendererMixin {
    
    @ModifyVariable(
        method = "getHandPos",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0,
            shift = At.Shift.AFTER
        ),
        ordinal = 0
    )
    private int adjustHandSide(int i, PlayerEntity player) {
        ItemStack itemStack = player.getMainHandStack();
        if (itemStack.isOf(ItemsRegister.COD_FISHING_ROD)) {
            return -i;
        }
        return i;
    }
} 