package com.soybean.mixin;

import com.soybean.items.ItemsRegister;
import com.soybean.items.item.EntityAsItem;
import com.soybean.items.item.UnbreakablePickaxeItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.EmptyMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow
    private @Final ItemModels models;

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void onRenderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        // 检查是否是我们的空气镐
        if (stack.getItem() instanceof UnbreakablePickaxeItem) {
            ci.cancel(); // 取消渲染
        }
        //生物作为物品渲染
        if(stack.getItem() instanceof EntityAsItem entityAsItem){
            Entity entity = null;
            if (stack.isOf(ItemsRegister.CREEPER_ITEM)){
                entity = new CreeperEntity(EntityType.CREEPER, MinecraftClient.getInstance().world);
            }else if(stack.isOf(ItemsRegister.SKELETON_ITEM)){
                entity = new SkeletonEntity(EntityType.SKELETON, MinecraftClient.getInstance().world);
            }else if (stack.isOf(ItemsRegister.ENDER_MAN_ITEM)){
                entity = new EndermanEntity(EntityType.ENDERMAN, MinecraftClient.getInstance().world);
            }else if (stack.isOf(ItemsRegister.FLAME_MAN_ITEM)){
                entity = new BlazeEntity(EntityType.BLAZE, MinecraftClient.getInstance().world);
            }else {
                entity = new CowEntity(EntityType.COW, MinecraftClient.getInstance().world);
            }
            matrices.push();
            matrices.scale(0.8f,0.8f,0.8f);
            if(renderMode == ModelTransformationMode.GUI){
                if(entity instanceof EndermanEntity){
                    matrices.scale(0.5f,0.5f,0.5f);
                    matrices.translate(0,-0.6f,0);
                }else if(entity instanceof SkeletonEntity){
                    matrices.scale( 0.6f, 0.6f,0.6f);
                    matrices.translate(0,-0.4f,0);
                }else if(entity instanceof BlazeEntity){
                    matrices.scale(0.8f,0.8f,0.8f);
                    matrices.translate(0,-0.4f,0);
                }else { //苦力怕
                    matrices.scale(0.8f,0.8f,0.8f);
                    matrices.translate(0,-0.2f,0);
                }
            }
            matrices.translate(0,-0.6f,0);
            MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0f, matrices, vertexConsumers, light);
            matrices.pop();
        }

    }
}