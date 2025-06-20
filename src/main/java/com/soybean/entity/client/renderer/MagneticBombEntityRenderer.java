package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.MagneticBombEntityModel;
import com.soybean.entity.client.model.MagneticBombItemModel;
import com.soybean.entity.custom.MagneticBombEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

/**
 * @author soybean
 * @date 2025/1/16 10:38
 * @description
 */
public class MagneticBombEntityRenderer extends GeoEntityRenderer<MagneticBombEntity> {

    public MagneticBombEntityRenderer(EntityRendererFactory.Context context) {
        super(context,new MagneticBombEntityModel());
    }

    @Override
    public Color getRenderColor(MagneticBombEntity animatable, float partialTick, int packedLight) {
        int rgba = animatable.getBombColor();
        return new Color(rgba); // true 表示包含 alpha 通道
    }

    @Override
    public void applyRotations(MagneticBombEntity entity, MatrixStack matrices, float ageInTicks, float rotationYaw, float partialTick, float scale) {
        Direction dir = entity.getHitDirection();

        super.applyRotations(entity, matrices, ageInTicks, rotationYaw, partialTick, scale);

        // 预偏移到中心，方便旋转
        matrices.translate(0.0, 0.5, 0.0);

        // 调整旋转
        switch (dir) {
            case DOWN -> {
                matrices.translate(0, -1, 0);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            }
            case UP -> {
                // 默认朝上，无需旋转
            }
            case NORTH -> { // 背向北，朝南
                matrices.translate(0, -0.5, 0.45);
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-90)); // 往上翻90°
            }
            case SOUTH -> { // 背向南，朝北
                matrices.translate(0, -0.5, -0.45);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90)); // 往下翻90°
            }
            case WEST -> { // 背向西，朝东
                matrices.translate(0.45, -0.5, 0);
                matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90)); // 往右翻90°
            }
            case EAST -> { // 背向东，朝西
                matrices.translate(-0.45, -0.5, 0);
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90)); // 往左翻90°
            }
        }

        // 平移回原来位置
        matrices.translate(0.0, -0.5, 0.0);
    }

}
