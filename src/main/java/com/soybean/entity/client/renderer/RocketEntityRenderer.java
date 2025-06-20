package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.MagneticBombEntityModel;
import com.soybean.entity.client.model.RocketEntityModel;
import com.soybean.entity.custom.MagneticBombEntity;
import com.soybean.entity.custom.RocketEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

/**
 * @author soybean
 * @date 2025/1/16 10:38
 * @description
 */
public class RocketEntityRenderer extends GeoEntityRenderer<RocketEntity> {

    public RocketEntityRenderer(EntityRendererFactory.Context context) {
        super(context,new RocketEntityModel());
    }


    //精准的方向
    @Override
    public void applyRotations(RocketEntity entity, MatrixStack matrices, float ageInTicks, float rotationYaw, float partialTick, float scale) {
        // 获取火箭的速度向量
        Vec3d velocity = entity.getVelocity();

        // 如果速度太小，使用默认朝向（避免除零或不稳定的旋转）
        if (velocity.lengthSquared() < 0.01) {
            // 使用实体的朝向作为后备
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-entity.getYaw() + 90.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getPitch()));
            return;
        }

        // 将速度向量标准化
        velocity = velocity.normalize();

        // 计算水平朝向角度 (yaw)
        // atan2(x, z) 给出从北向（+Z）到向量的角度
        double yaw = Math.atan2(velocity.x, velocity.z) * 180.0 / Math.PI;

        // 计算俯仰角度 (pitch)
        // asin(y) 给出向上/向下的角度
        double pitch = -Math.asin(velocity.y) * 180.0 / Math.PI;

        // 应用旋转变换
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)yaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)pitch));

        // 调试输出（可选）
        if (entity.age % 20 == 0) {
            System.out.println("Velocity-based rotation - Yaw: " + yaw + ", Pitch: " + pitch);
            System.out.println("Velocity: " + velocity);
        }

        super.applyRotations(entity, matrices, ageInTicks, rotationYaw, partialTick, scale);
    }

}
