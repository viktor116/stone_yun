package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.items.custom.MinecartHatItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

/**
 * @author soybean
 * @date 2024/11/2 13:11
 * @description
 */
public class MinecartHatModel extends GeoModel<MinecartHatItem> {
    @Override
    public Identifier getModelResource(MinecartHatItem animatable) {
        return Identifier.of(InitValue.MOD_ID, "geo/minecart_hat.geo.json");
    }

    @Override
    public Identifier getTextureResource(MinecartHatItem animatable) {
        return Identifier.of(InitValue.MOD_ID, "textures/armor/minecart_hat.png");
    }

    @Override
    public Identifier getAnimationResource(MinecartHatItem animatable) {
        return Identifier.of(InitValue.MOD_ID, "animations/minecart_hat.animation.json");
    }
}
