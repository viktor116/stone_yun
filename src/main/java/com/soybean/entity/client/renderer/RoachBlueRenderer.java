package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.RoachBlueModel;
import com.soybean.entity.custom.RoachBlueEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RoachBlueRenderer extends GeoEntityRenderer<RoachBlueEntity> {
    public RoachBlueRenderer(EntityRendererFactory.Context context) {
        super(context, new RoachBlueModel());
        this.shadowRadius = 0.3f;
    }
}
