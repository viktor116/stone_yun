package com.soybean.entity.client.renderer;

import com.soybean.config.InitValue;
import com.soybean.entity.client.model.HimModel;
import com.soybean.entity.custom.HimEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/12/3 10:57
 * @description
 */
public class HimRenderer extends MobEntityRenderer<HimEntity, HimModel> {
    private static final Identifier TEXTURE = Identifier.of(InitValue.MOD_ID, "textures/entity/him.png");

    public HimRenderer(EntityRendererFactory.Context context, EntityModelLayer modelLayer) {
        super(context, new HimModel(context.getPart(modelLayer)), 0.5f);
    }

    @Override
    public Identifier getTexture(HimEntity entity) {
        return TEXTURE;
    }
}