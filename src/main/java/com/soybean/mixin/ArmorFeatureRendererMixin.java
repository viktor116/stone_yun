package com.soybean.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.soybean.entity.client.renderer.InvertBoatRenderer;
import com.soybean.entity.vehicle.InvertBoatEntity;
import com.soybean.items.custom.InvertBoatItem;
import com.soybean.items.custom.InvertBucketItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author soybean
 * @date 2024/12/2 14:51
 * @description
 */
@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private void onRenderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                               T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
        ItemStack itemStack = entity.getEquippedStack(armorSlot);
        if (itemStack.getItem() instanceof InvertBucketItem) { //处理反转桶
            handleInvertBucketItemRenderer(itemStack, matrices, vertexConsumers, entity, armorSlot, light, model, ci);
        }
        if(itemStack.getItem() instanceof InvertBoatItem){ //处理反转船
            handleInvertBoatItemRenderer(itemStack,matrices,vertexConsumers,entity,armorSlot,light,model,ci);
        }
    }


    public void handleInvertBucketItemRenderer(ItemStack itemStack,MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                               T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci){
        ci.cancel(); // 取消原版头盔的3D渲染
        if (armorSlot == EquipmentSlot.HEAD) {
            matrices.push();
            matrices.translate(0, -0.35, 0.2); // X, Y, Z adjustments
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-180f)); // Optional rotation
            matrices.scale(0.5f, 0.5f, 0.5f); // Adjust scale if needed

            // Render the item model
            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    itemStack,
                    ModelTransformationMode.HEAD,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    matrices,
                    vertexConsumers,
                    entity.getWorld(),
                    entity.getId()
            );
            matrices.pop();
        }
    }

    public void handleInvertBoatItemRenderer(ItemStack itemStack,MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
                                               T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci){
        ci.cancel(); // 取消原版头盔的3D渲染
        if (armorSlot == EquipmentSlot.HEAD) {
            if (armorSlot == EquipmentSlot.HEAD) {
                matrixStack.push();

                // 获取玩家朝向
                PlayerEntity player = (PlayerEntity) entity;

                // 平移和旋转调整，使船体倒转并朝向玩家
                matrixStack.translate(0.0F, -0.23F, 0.0F);

                // 获取现有的船模型，假设你已经在项目中定义了 `BoatEntityModel`
                BoatEntityModel boatModel = new BoatEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.createBoat(BoatEntity.Type.OAK)));

                // 获取船的纹理
                Identifier boatTexture = InvertBoatRenderer.CUSTOM_BOAT_TEXTURE;

                // 反转船的坐标，使其上下颠倒
                matrixStack.scale(-1.0F, -1.0F, 1.0F);  // 反转坐标系
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
                // 渲染船模型
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(boatModel.getLayer(boatTexture));
                boatModel.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

                matrixStack.pop();
            }
        }
    }
}
