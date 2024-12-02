package com.soybean.entity.vehicle;

import com.soybean.entity.EntityRegister;
import com.soybean.items.ItemsRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/2 15:56
 * @description
 */
public class InvertBoatEntity extends BoatEntity {
    public InvertBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }
    public InvertBoatEntity(World world, double x, double y, double z) {
        this(EntityRegister.INVERT_BOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }
    @Override
    public Item asItem() {
        return ItemsRegister.INVERT_BOAT;
    }
    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        Vec3d originalPos  = super.getPassengerAttachmentPos(passenger,dimensions, scaleFactor);
        return new Vec3d(
                originalPos.x,
                originalPos.y + 0.68,
                originalPos.z
        );
    }

}
