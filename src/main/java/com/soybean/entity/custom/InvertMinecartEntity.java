package com.soybean.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.world.World;

public class InvertMinecartEntity extends MinecartEntity {
    public InvertMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
}
