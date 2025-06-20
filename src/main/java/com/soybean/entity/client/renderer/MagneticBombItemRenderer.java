package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.MagneticBombItemModel;
import com.soybean.items.item.MagneticBombItem;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author soybean
 * @date 2025/1/16 10:38
 * @description
 */
public class MagneticBombItemRenderer extends GeoItemRenderer<MagneticBombItem> {
    public MagneticBombItemRenderer() {
        super(new MagneticBombItemModel());
    }

    public MagneticBombItemRenderer(EntityRendererFactory.Context context) {
        this();
    }
}
