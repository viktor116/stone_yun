package com.soybean.mixin;

import com.soybean.manager.HeadlessPlayerManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author soybean
 * @date 2024/12/23 17:37
 * @description
 */
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"),cancellable = true)
    private void onRender(AbstractClientPlayerEntity player, float f, float g, MatrixStack matrixStack,
                          VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (HeadlessPlayerManager.isPlayerHeadless(player.getUuid())) {
            // 使头部模型不可见
            PlayerEntityModel<?> model = ((PlayerEntityRenderer)(Object)this).getModel();
            model.head.visible = false;
            model.hat.visible = false;
        }
    }

    @Inject(method = "render", at = @At("RETURN"),cancellable = true)
    private void afterRender(AbstractClientPlayerEntity player, float f, float g, MatrixStack matrixStack,
                             VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (HeadlessPlayerManager.isPlayerHeadless(player.getUuid())) {
            // 恢复头部模型可见性，以免影响其他玩家的渲染
            PlayerEntityModel<?> model = ((PlayerEntityRenderer)(Object)this).getModel();
            model.head.visible = true;
            model.hat.visible = true;
        }
    }
}
