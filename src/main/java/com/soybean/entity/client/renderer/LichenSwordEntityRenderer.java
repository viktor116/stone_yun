package com.soybean.entity.client.renderer;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.LichenSwordEntity;
import com.soybean.items.ItemsRegister;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

/**
 * @author soybean
 * @date 2025/4/24 11:36
 * @description
 */
public class LichenSwordEntityRenderer extends EntityRenderer<LichenSwordEntity> {
    private static final Identifier TEXTURE = Identifier.of(InitValue.MOD_ID, "textures/item/lichen_sword.png");
    private final ItemRenderer itemRenderer;

    public LichenSwordEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowOpacity = 0.75F;
    }

    @Override
    public void render(LichenSwordEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // 获取实体的速度向量
        Vec3d velocity = entity.getVelocity();

        if (velocity.lengthSquared() < 0.01) {
            // 使用实体的原始旋转角度
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw(tickDelta)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getPitch(tickDelta)));
        } else {
            Vec3d direction = velocity.normalize();

            // 计算yaw（水平角度）- 使用正确的反正切函数
            float yawFromVelocity = (float) Math.toDegrees(Math.atan2(direction.x, direction.z));

            // 计算pitch（垂直角度）
            double horizontalLength = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
            float pitch = (float) Math.toDegrees(Math.atan2(-direction.y, horizontalLength));

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yawFromVelocity));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
        }

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
        matrices.push();

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-45)); // 使剑倾斜45度
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180)); // 剑翻转180度

        matrices.scale(1f, 1f, 1f);

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                new ItemStack(ItemsRegister.LICHEN_SWORD),
                ModelTransformationMode.FIXED,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                0
        );

        matrices.pop();
        matrices.pop();
        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(LichenSwordEntity entity) {
        return TEXTURE;
    }
}
