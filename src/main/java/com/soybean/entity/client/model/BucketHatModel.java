package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.items.custom.BucketHatItem;
import com.soybean.items.custom.MinecartHatItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2024/11/2 13:11
 * @description
 */
public class BucketHatModel extends GeoModel<BucketHatItem> {
    @Override
    public Identifier getModelResource(BucketHatItem animatable) {
        return Identifier.of(InitValue.MOD_ID, "geo/bucket.geo.json");
    }

    @Override
    public Identifier getTextureResource(BucketHatItem animatable) {
        return Identifier.of(InitValue.MOD_ID, "textures/armor/bucket.png");
    }

    @Override
    public Identifier getAnimationResource(BucketHatItem animatable) {
        return Identifier.of(InitValue.MOD_ID, "animations/default.animation.json");
    }
}
