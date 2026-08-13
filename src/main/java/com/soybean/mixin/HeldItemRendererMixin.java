package com.soybean.mixin;

import com.soybean.items.ItemsRegister;
import com.soybean.items.item.UnbreakablePickaxeItem;
import com.soybean.utils.SpyglassRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Shadow @Final
    private MinecraftClient client;
    @Shadow
    protected abstract void renderArmHoldingItem(MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light, float equipProgress, float swingProgress, Arm arm);

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"), cancellable = true)
    private void onRenderFirstPersonItemHead(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (stack.getItem() instanceof UnbreakablePickaxeItem) {
            boolean bl = hand == Hand.MAIN_HAND;
            Arm arm = bl ? player.getMainArm() : player.getMainArm().getOpposite();
            this.renderArmHoldingItem(matrices, vertexConsumers, light, equipProgress, swingProgress, arm);
            ci.cancel();
        }
        if (stack.isOf(ItemsRegister.FALL_SPYGLASS)) {
            SpyglassRenderState.setHandheld(true);
        }
    }

    @Inject(method = "renderFirstPersonItem", at = @At("RETURN"))
    private void onRenderFirstPersonItemReturn(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        SpyglassRenderState.clear();
    }

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void onRenderItemHead(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (stack.getItem() instanceof UnbreakablePickaxeItem) {
            ci.cancel();
        }
        if (stack.isOf(ItemsRegister.FALL_SPYGLASS)) {
            SpyglassRenderState.setHandheld(true);
        }
    }

    @Inject(method = "renderItem", at = @At("RETURN"))
    private void onRenderItemReturn(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        SpyglassRenderState.clear();
    }
}
