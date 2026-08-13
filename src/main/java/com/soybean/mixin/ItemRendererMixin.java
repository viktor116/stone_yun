package com.soybean.mixin;

import com.soybean.items.item.EntityAsItem;
import com.soybean.items.item.UnbreakablePickaxeItem;
import com.soybean.items.ItemsRegister;
import com.soybean.utils.SpyglassRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    private void onRenderItemCustom(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (stack.getItem() instanceof UnbreakablePickaxeItem) {
            ci.cancel();
        }
        if (stack.getItem() instanceof EntityAsItem) {
            net.minecraft.entity.Entity entity = null;
            if (stack.isOf(ItemsRegister.CREEPER_ITEM)) {
                entity = new CreeperEntity(EntityType.CREEPER, MinecraftClient.getInstance().world);
            } else if (stack.isOf(ItemsRegister.SKELETON_ITEM)) {
                entity = new SkeletonEntity(EntityType.SKELETON, MinecraftClient.getInstance().world);
            } else if (stack.isOf(ItemsRegister.ENDER_MAN_ITEM)) {
                entity = new EndermanEntity(EntityType.ENDERMAN, MinecraftClient.getInstance().world);
            } else if (stack.isOf(ItemsRegister.FLAME_MAN_ITEM)) {
                entity = new BlazeEntity(EntityType.BLAZE, MinecraftClient.getInstance().world);
            } else {
                entity = new CowEntity(EntityType.COW, MinecraftClient.getInstance().world);
            }
            matrices.push();
            matrices.scale(0.8f, 0.8f, 0.8f);
            if (renderMode == ModelTransformationMode.GUI) {
                if (entity instanceof EndermanEntity) {
                    matrices.scale(0.5f, 0.5f, 0.5f);
                    matrices.translate(0, -0.6f, 0);
                } else if (entity instanceof SkeletonEntity) {
                    matrices.scale(0.6f, 0.6f, 0.6f);
                    matrices.translate(0, -0.4f, 0);
                } else if (entity instanceof BlazeEntity) {
                    matrices.scale(0.8f, 0.8f, 0.8f);
                    matrices.translate(0, -0.4f, 0);
                } else {
                    matrices.scale(0.8f, 0.8f, 0.8f);
                    matrices.translate(0, -0.2f, 0);
                }
            }
            matrices.translate(0, -0.6f, 0);
            MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0f, matrices, vertexConsumers, light);
            matrices.pop();
        }
    }

    @Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
    private void onGetModelForSpyglass(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
        if (stack.isOf(ItemsRegister.FALL_SPYGLASS) && SpyglassRenderState.isHandheld()) {
            cir.setReturnValue(MinecraftClient.getInstance().getBakedModelManager().getModel(
                    ModelIdentifier.ofInventoryVariant(Identifier.ofVanilla("spyglass_in_hand"))));
        }
    }

}
