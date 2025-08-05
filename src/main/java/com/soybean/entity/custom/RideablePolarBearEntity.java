package com.soybean.entity.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RideablePolarBearEntity extends PolarBearEntity implements Saddleable, RideableInventory {
    private static final TrackedData<Boolean> SADDLED = DataTracker.registerData(RideablePolarBearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public RideablePolarBearEntity(EntityType<? extends PolarBearEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SADDLED, false);
    }

    @Override
    public boolean canBeSaddled() {
        return true;
    }

    @Override
    public void saddle(ItemStack stack, @Nullable SoundCategory soundCategory) {

    }

    @Override
    public boolean isSaddled() {
        return this.dataTracker.get(SADDLED);
    }

    public void saddle(SoundCategory soundCategory) {
        this.dataTracker.set(SADDLED, true);
        if (soundCategory != null) {
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.ENTITY_HORSE_SADDLE, soundCategory, 0.5F, 1.0F);
        }
    }

    // 右键交互 - 骑乘逻辑
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // 如果手持鞍，给熊装鞍
        if (itemStack.getItem() == Items.SADDLE && !this.isSaddled()) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            this.saddle(SoundCategory.NEUTRAL);
            return ActionResult.success(this.getWorld().isClient);
        }

        // 如果已经装鞍且没有乘客，玩家可以骑上去
        if (!this.hasPassengers() && !player.shouldCancelInteraction()) {
            if (!this.getWorld().isClient) {
                player.startRiding(this);
            }
            return ActionResult.success(this.getWorld().isClient);
        }

        return super.interactMob(player, hand);
    }

    // 控制移动
    @Override
    public void travel(Vec3d movementInput) {
        super.travel(movementInput);
    }

    // 跳跃方法
    public void jump() {
        this.setVelocity(this.getVelocity().add(0.0, 0.5, 0.0));
        this.velocityDirty = true;
    }

    // 乘客位置
    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw());
        Vec3d vec3d2 = this.getPassengerRidingPos(passenger);
        if (vec3d2 != null) {
            return vec3d2;
        } else {
            return super.updatePassengerForDismount(passenger);
        }
    }

    @Override
    protected Vec3d getPassengerAttachmentPos(Entity passenger, EntityDimensions dimensions, float scaleFactor) {
        return new Vec3d(0.0, dimensions.height(), 0);
    }

    // NBT数据保存
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.getBoolean("Saddled")) {
            this.saddle(null);
        }
    }

    // 防止AI在被骑乘时干扰
    @Override
    protected void mobTick() {
        if (!this.hasPassengers()) {
            super.mobTick();
        }
    }

    // 物品掉落（掉落鞍）
    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (this.isSaddled()) {
            this.dropItem(Items.SADDLE);
        }
    }

    @Override
    public void openInventory(PlayerEntity player) {
        // 不需要打开GUI
    }
}
