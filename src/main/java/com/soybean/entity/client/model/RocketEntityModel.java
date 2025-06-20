package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.RocketEntity;
import com.soybean.items.item.RocketItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2025/2/15 17:16
 * @description
 */
public class RocketEntityModel extends GeoModel<RocketEntity> {
    @Override
    public Identifier getModelResource(RocketEntity animatable) {
        return InitValue.id("geo/rocket.geo.json");
    }

    @Override
    public Identifier getTextureResource(RocketEntity animatable) {
        return InitValue.id("textures/item/geo/rocket.png");
    }

    @Override
    public Identifier getAnimationResource(RocketEntity animatable) {
        return InitValue.id("animations/rocket.animation.json");
    }
}
