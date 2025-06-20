package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.items.item.MagneticBombItem;
import com.soybean.items.item.RocketLauncherItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2025/2/15 17:16
 * @description
 */
public class RocketLauncherModel extends GeoModel<RocketLauncherItem> {
    @Override
    public Identifier getModelResource(RocketLauncherItem animatable) {
        return InitValue.id("geo/rocket_launcher.geo.json");
    }

    @Override
    public Identifier getTextureResource(RocketLauncherItem animatable) {
        return InitValue.id("textures/item/geo/rocket_launcher.png");
    }

    @Override
    public Identifier getAnimationResource(RocketLauncherItem animatable) {
        return InitValue.id("animations/rocket_launcher.animation.json");
    }
}
