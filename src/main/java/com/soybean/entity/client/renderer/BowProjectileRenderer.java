package com.soybean.entity.client.renderer;

import com.soybean.entity.custom.BowProjectileEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BowProjectileRenderer extends EntityRenderer<BowProjectileEntity> {
    private final ItemRenderer itemRenderer;

    public BowProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.25F;
        this.shadowOpacity = 0.75F;
    }

    @Override
    public void render(BowProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
                MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(
                MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));

        // 让弓像飞镖一样旋转飞行
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.age * 30.0f));
        matrices.scale(1.5F, 1.5F, 1.5F);

        this.itemRenderer.renderItem(
                new net.minecraft.item.ItemStack(Items.BOW),
                ModelTransformationMode.GROUND,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                entity.getId()
        );

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(BowProjectileEntity entity) {
        return Identifier.ofVanilla("textures/item/bow.png");
    }
}
