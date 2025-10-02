package com.soybean.entity.client.renderer;

import com.soybean.entity.custom.CommonCreeperEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.CreeperChargeFeatureRenderer;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * @author soybean
 * @date 2024/12/9 15:56
 * @description
 */
@Environment(EnvType.CLIENT)
public class CommonCreeperEntityRenderer extends MobEntityRenderer<CommonCreeperEntity, CreeperEntityModel<CommonCreeperEntity>> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/creeper/creeper.png");

    public CommonCreeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CreeperEntityModel(context.getPart(EntityModelLayers.CREEPER)), 0.5F);
    }

    @Override
    public Identifier getTexture(CommonCreeperEntity entity) {
        return TEXTURE;
    }

    protected void scale(CreeperEntity creeperEntity, MatrixStack matrixStack, float f) {
        float g = creeperEntity.getClientFuseTime(f);
        float h = 1.0F + MathHelper.sin(g * 100.0F) * g * 0.01F;
        g = MathHelper.clamp(g, 0.0F, 1.0F);
        g *= g;
        g *= g;
        float i = (1.0F + g * 0.4F) * h;
        float j = (1.0F + g * 0.1F) / h;
        matrixStack.scale(i, j, i);
    }

    protected float getAnimationCounter(CreeperEntity creeperEntity, float f) {
        float g = creeperEntity.getClientFuseTime(f);
        return (int)(g * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(g, 0.5F, 1.0F);
    }
}
