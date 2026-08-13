package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.RoachModel;
import com.soybean.entity.custom.RoachEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RoachRenderer extends GeoEntityRenderer<RoachEntity> {
    public RoachRenderer(EntityRendererFactory.Context context) {
        super(context, new RoachModel());
        this.shadowRadius = 0.3f;
    }
}