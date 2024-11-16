package com.soybean.entity.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

/**
 * @author soybean
 * @date 2024/11/2 10:31
 * @description
 */

@Environment(EnvType.CLIENT)
public class InvertMinecartRenderer extends MinecartEntityRenderer {
    public InvertMinecartRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, EntityModelLayers.MINECART);  // 使用默认的矿车模型层
    }
    @Override
    public void render(AbstractMinecartEntity abstractMinecartEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        long l = (long)abstractMinecartEntity.getId() * 493286711L;
        l = l * l * 4392167121L + l * 98761L;
        float h = (((float)(l >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float j = (((float)(l >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float k = (((float)(l >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        matrixStack.translate(h, j, k);
        double d = MathHelper.lerp((double)g, abstractMinecartEntity.lastRenderX, abstractMinecartEntity.getX());
        double e = MathHelper.lerp((double)g, abstractMinecartEntity.lastRenderY, abstractMinecartEntity.getY());
        double m = MathHelper.lerp((double)g, abstractMinecartEntity.lastRenderZ, abstractMinecartEntity.getZ());
        double n = 0.30000001192092896;
        Vec3d vec3d = abstractMinecartEntity.snapPositionToRail(d, e, m);
        float o = MathHelper.lerp(g, abstractMinecartEntity.prevPitch, abstractMinecartEntity.getPitch());
        if (vec3d != null) {
            Vec3d vec3d2 = abstractMinecartEntity.snapPositionToRailWithOffset(d, e, m, 0.30000001192092896);
            Vec3d vec3d3 = abstractMinecartEntity.snapPositionToRailWithOffset(d, e, m, -0.30000001192092896);
            if (vec3d2 == null) {
                vec3d2 = vec3d;
            }

            if (vec3d3 == null) {
                vec3d3 = vec3d;
            }

            matrixStack.translate(vec3d.x - d, (vec3d2.y + vec3d3.y) / 2.0 - e, vec3d.z - m);
            Vec3d vec3d4 = vec3d3.add(-vec3d2.x, -vec3d2.y, -vec3d2.z);
            if (vec3d4.length() != 0.0) {
                vec3d4 = vec3d4.normalize();
                f = (float)(Math.atan2(vec3d4.z, vec3d4.x) * 180.0 / Math.PI);
                o = (float)(Math.atan(vec3d4.y) * 73.0);
            }
        }

        matrixStack.translate(0.0F, 0.375F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-o));
        float p = (float)abstractMinecartEntity.getDamageWobbleTicks() - g;
        float q = abstractMinecartEntity.getDamageWobbleStrength() - g;
        if (q < 0.0F) {
            q = 0.0F;
        }

        if (p > 0.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(p) * p * q / 10.0F * (float)abstractMinecartEntity.getDamageWobbleSide()));
        }

        int r = abstractMinecartEntity.getBlockOffset();
        BlockState blockState = abstractMinecartEntity.getContainedBlock();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            matrixStack.push();
            float s = 0.75F;
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            matrixStack.translate(-0.5F, (float)(r - 8) / 16.0F, 0.5F);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
            this.renderBlock(abstractMinecartEntity, g, blockState, matrixStack, vertexConsumerProvider, i);
            matrixStack.pop();
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F)); // 添加这一行来翻转矿车
        this.model.setAngles(abstractMinecartEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(abstractMinecartEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);

        matrixStack.pop();
    }
}
