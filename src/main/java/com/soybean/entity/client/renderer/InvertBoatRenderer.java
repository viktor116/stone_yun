package com.soybean.entity.client.renderer;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.soybean.config.InitValue;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author soybean
 * @date 2024/12/2 15:59
 * @description
 */
public class InvertBoatRenderer extends BoatEntityRenderer {
    public static final Identifier CUSTOM_BOAT_TEXTURE = Identifier.of(InitValue.MOD_ID +":textures/entity/boat/invert_boat.png");
    public final Map<BoatEntity.Type, Pair<Identifier, CompositeEntityModel<BoatEntity>>> texturesAndModels;

    public InvertBoatRenderer(EntityRendererFactory.Context ctx) {
        super(ctx,false);
        this.shadowRadius = 0.8F;

        // 为所有船只类型创建使用自定义材质的映射
        this.texturesAndModels = Stream.of(BoatEntity.Type.values())
                .collect(ImmutableMap.toImmutableMap(
                        type -> type,
                        type -> Pair.of(CUSTOM_BOAT_TEXTURE, this.createModel(ctx, type, false))
                ));
    }

    private CompositeEntityModel<BoatEntity> createModel(EntityRendererFactory.Context ctx, BoatEntity.Type type, boolean chest) {
        EntityModelLayer entityModelLayer = EntityModelLayers.createBoat(type);
        ModelPart modelPart = ctx.getPart(entityModelLayer);
        return type == BoatEntity.Type.BAMBOO
                ? new RaftEntityModel(modelPart)
                : new BoatEntityModel(modelPart);
    }

    @Override
    public void render(BoatEntity boatEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0F, 0.5f, 0.0F);
        // 原有的渲染逻辑
        matrixStack.translate(0.0F, 0, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F-f));

        float h = (float)boatEntity.getDamageWobbleTicks() - g;
        float j = boatEntity.getDamageWobbleStrength() - g;
        if (j < 0.0F) {
            j = 0.0F;
        }

        if (h > 0.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(
                    MathHelper.sin(h) * h * j / 10.0F * (float)boatEntity.getDamageWobbleSide()
            ));
        }

        float k = boatEntity.interpolateBubbleWobble(g);
        if (!MathHelper.approximatelyEquals(k, 0.0F)) {
            matrixStack.multiply((new Quaternionf()).setAngleAxis(
                    boatEntity.interpolateBubbleWobble(g) * 0.017453292F, 1.0F, 0.0F, 1.0F
            ));
        }

        Pair<Identifier, CompositeEntityModel<BoatEntity>> pair = this.texturesAndModels.get(boatEntity.getVariant());
        Identifier identifier = pair.getFirst();
        CompositeEntityModel<BoatEntity> compositeEntityModel = pair.getSecond();

        matrixStack.scale(-1.0F, 1.0F, 1.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        compositeEntityModel.setAngles(boatEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(compositeEntityModel.getLayer(identifier));
        compositeEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);

        if (!boatEntity.isSubmergedInWater()) {
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
            if (compositeEntityModel instanceof ModelWithWaterPatch) {
                ModelWithWaterPatch modelWithWaterPatch = (ModelWithWaterPatch)compositeEntityModel;
                modelWithWaterPatch.getWaterPatch().render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
            }
        }

        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
        matrixStack.pop();
    }


    @Override
    public Identifier getTexture(BoatEntity boatEntity) {
        return CUSTOM_BOAT_TEXTURE;
    }

}
