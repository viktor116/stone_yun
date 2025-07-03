package com.soybean.entity.client.renderer;

import com.soybean.entity.custom.BedProjectileEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

/**
 * 床弹射物的渲染器，使用物品渲染
 */
public class BedProjectileRenderer extends EntityRenderer<BedProjectileEntity> {
    private final ItemRenderer itemRenderer;

    public BedProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15F;
        this.shadowOpacity = 0.75F;
    }

    @Override
    public void render(BedProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        
        // 调整床实体的大小和位置
        matrices.scale(1.5F, 1.5F, 1.5F);
        
        // 根据速度旋转
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));
        
        // 旋转床以便它水平飞行
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
        
        // 渲染床物品
        this.itemRenderer.renderItem(
                entity.getBedItem(),
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
    public Identifier getTexture(BedProjectileEntity entity) {
        // 这个纹理不会被使用，因为我们使用物品渲染器
        return Identifier.ofVanilla("textures/item/red_bed.png");
    }
} 