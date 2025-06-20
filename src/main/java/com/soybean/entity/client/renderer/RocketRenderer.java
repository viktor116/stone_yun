package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.RocketItemModel;
import com.soybean.items.item.RocketItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author soybean
 * @date 2025/1/16 10:38
 * @description
 */
public class RocketRenderer extends GeoItemRenderer<RocketItem> {
    public RocketRenderer() {
        super(new RocketItemModel());
    }

}
