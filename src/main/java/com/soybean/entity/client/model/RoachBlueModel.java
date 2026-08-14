package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.RoachBlueEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class RoachBlueModel extends GeoModel<RoachBlueEntity> {
    @Override
    public Identifier getModelResource(RoachBlueEntity animatable) {
        return InitValue.id("geo/roach_blue.geo.json");
    }

    @Override
    public Identifier getTextureResource(RoachBlueEntity animatable) {
        return InitValue.id("textures/entity/roach_blue.png");
    }

    @Override
    public Identifier getAnimationResource(RoachBlueEntity animatable) {
        return InitValue.id("animations/roach.animation.json");
    }

    @Override
    public void setCustomAnimations(RoachBlueEntity animatable, long instanceId, AnimationState<RoachBlueEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
