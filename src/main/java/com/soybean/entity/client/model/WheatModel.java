package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.WheatEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class WheatModel extends GeoModel<WheatEntity> {
    @Override
    public Identifier getModelResource(WheatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"geo/wheat_entity.geo.json");
    }

    @Override
    public Identifier getTextureResource(WheatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"textures/entity/wheat_entity.png");
    }

    @Override
    public Identifier getAnimationResource(WheatEntity animatable) {
        return Identifier.of(InitValue.MOD_ID,"animations/wheat_entity.animation.json");
    }
    @Override
    public void setCustomAnimations(WheatEntity animatable, long instanceId, AnimationState<WheatEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
