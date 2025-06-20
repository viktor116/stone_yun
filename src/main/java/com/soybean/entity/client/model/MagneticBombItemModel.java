package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.items.item.MagneticBombItem;
import com.soybean.utils.CommonUtils;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2025/2/15 17:16
 * @description
 */
public class MagneticBombItemModel extends GeoModel<MagneticBombItem> {
    @Override
    public Identifier getModelResource(MagneticBombItem animatable) {
        return InitValue.id("geo/magnetic_bomb.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagneticBombItem animatable) {
        return InitValue.id("textures/entity/magnetic_bomb.png");
    }

    @Override
    public Identifier getAnimationResource(MagneticBombItem animatable) {
        return InitValue.id(CommonUtils.DEFAULT_ANIMATION);
    }
}
