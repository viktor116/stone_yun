package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.MagneticBombEntity;
import com.soybean.items.item.MagneticBombItem;
import com.soybean.utils.CommonUtils;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2025/2/15 17:16
 * @description
 */
public class MagneticBombEntityModel extends GeoModel<MagneticBombEntity> {
    @Override
    public Identifier getModelResource(MagneticBombEntity animatable) {
        return InitValue.id("geo/magnetic_bomb.geo.json");
    }

    @Override
    public Identifier getTextureResource(MagneticBombEntity animatable) {
        return InitValue.id("textures/entity/magnetic_bomb.png");
    }

    @Override
    public Identifier getAnimationResource(MagneticBombEntity animatable) {
        return InitValue.id(CommonUtils.DEFAULT_ANIMATION);
    }
}
