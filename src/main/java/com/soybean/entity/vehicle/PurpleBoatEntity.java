package com.soybean.entity.vehicle;

import com.soybean.entity.EntityRegister;
import com.soybean.items.ItemsRegister;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import java.util.Iterator;

/**
 * @author soybean
 * @date 2024/10/23 12:15
 * @description
 */
public class PurpleBoatEntity extends BoatEntity {

    public PurpleBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
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

