package com.soybean.entity.vehicle;

import com.soybean.entity.EntityRegister;
import com.soybean.items.ItemsRegister;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class PurpleBoatEntity extends ChestBoatEntity {

    public PurpleBoatEntity(EntityType<? extends ChestBoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public PurpleBoatEntity(World world, double x, double y, double z) {
        this(EntityRegister.PURPLE_BOAT_TYPE, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    public Item asItem() {
        return ItemsRegister.PURPLE_BOAT;
    }
}

