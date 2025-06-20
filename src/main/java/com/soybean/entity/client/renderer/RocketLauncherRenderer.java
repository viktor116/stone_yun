package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.RocketLauncherModel;
import com.soybean.items.item.RocketLauncherItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author soybean
 * @date 2025/1/16 10:38
 * @description
 */
public class RocketLauncherRenderer extends GeoItemRenderer<RocketLauncherItem> {
    public RocketLauncherRenderer() {
        super(new RocketLauncherModel());
    }

}
