package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.RoachEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class RoachModel extends GeoModel<RoachEntity> {
    @Override
    public Identifier getModelResource(RoachEntity animatable) {
        return InitValue.id("geo/roach.geo.json");
    }

    @Override
    public Identifier getTextureResource(RoachEntity animatable) {
        return InitValue.id("textures/entity/roach.png");
    }

    @Override
    public Identifier getAnimationResource(RoachEntity animatable) {
        return InitValue.id("animations/roach.animation.json");
    }

    @Override
    public void setCustomAnimations(RoachEntity animatable, long instanceId, AnimationState<RoachEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}