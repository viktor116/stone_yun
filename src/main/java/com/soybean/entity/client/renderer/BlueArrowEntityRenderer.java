package com.soybean.entity.client.renderer;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.BlueArrowEntity;
import com.soybean.utils.CommonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BlueArrowEntityRenderer extends ProjectileEntityRenderer<BlueArrowEntity> {

    public static final Identifier TEXTURE = InitValue.id("textures/entity/blue_arrow.png");

    public BlueArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(BlueArrowEntity entity) {
        return TEXTURE;
    }
}