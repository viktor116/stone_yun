package com.soybean.entity.custom;

import com.soybean.entity.EntityRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class MaceBobberEntity extends ProjectileEntity {

    private boolean stopped = false;
    private boolean maceDamageApplied = false;
    private boolean pulling = false;
    private UUID itemUuid;

    public MaceBobberEntity(EntityType<? extends ProjectileEntity> type, World world) {
        super(type, world);
    }

    public MaceBobberEntity(World world, LivingEntity owner) {
        this(EntityRegister.MACE_BOBBER_ENTITY, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    public void setItemUuid(UUID uuid) {
        this.itemUuid = uuid;
    }

    public UUID getItemUuid() {
        return itemUuid;
    }

    @Override
    protected void initDataTracker(net.minecraft.entity.data.DataTracker.Builder builder) {
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (itemUuid != null) nbt.putUuid("itemUuid", itemUuid);
        nbt.putBoolean("stopped", stopped);
        nbt.putBoolean("maceDamageApplied", maceDamageApplied);
        nbt.putBoolean("pulling", pulling);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("itemUuid")) itemUuid = nbt.getUuid("itemUuid");
        stopped = nbt.getBoolean("stopped");
        maceDamageApplied = nbt.getBoolean("maceDamageApplied");
        pulling = nbt.getBoolean("pulling");
    }

    @Override
    public void checkDespawn() {
    }

    @Override
    public void tick() {
        if (stopped) {
            this.setVelocity(Vec3d.ZERO);
        } else {
            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }
            Vec3d velocity = this.getVelocity();
            Vec3d gravity = new Vec3d(velocity.x, velocity.y - 0.05, velocity.z);
            this.setVelocity(gravity);
            this.prevX = this.getX();
            this.prevY = this.getY();
            this.prevZ = this.getZ();
            this.setPosition(this.getX() + gravity.x, this.getY() + gravity.y, this.getZ() + gravity.z);
            ProjectileUtil.setRotationFromVelocity(this, 0.2F);
            this.checkBlockCollision();
        }
        super.tick();
    }

    public void pullBack() {
        pulling = true;
        stopped = false;
    }

    public boolean isPulling() {
        return pulling;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (!stopped) {
            stopped = true;
            this.setVelocity(Vec3d.ZERO);
            playSmashEffect();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity hit = entityHitResult.getEntity();
        if (hit == null || hit == this.getOwner() || maceDamageApplied) return;

        maceDamageApplied = true;
        stopped = true;
        this.setVelocity(Vec3d.ZERO);
        applyMaceDamage(hit);
    }

    private void playSmashEffect() {
        World world = this.getWorld();
        world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.BLOCKS, 1.0f, 1.0f);
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 20, 1.5, 0.3, 1.5, 0.1);
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 5, 0.5, 0.2, 0.5, 0.05);
        }
    }

    private void applyMaceDamage(Entity hit) {
        World world = this.getWorld();
        if (world.isClient) return;

        float totalDamage = 27.0f;

        DamageSource damageSource;
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity living) {
            damageSource = world.getDamageSources().mobProjectile(this, living);
        } else {
            damageSource = world.getDamageSources().generic();
        }
        hit.damage(damageSource, totalDamage);

        Box area = this.getBoundingBox().expand(3.5);
        for (LivingEntity nearby : world.getEntitiesByClass(LivingEntity.class, area, e -> e != hit && e != owner)) {
            double resistance = nearby.getAttributeValue(net.minecraft.entity.attribute.EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            double knockback = 1.5 * (1.0 - resistance);
            Vec3d dir = nearby.getPos().subtract(this.getX(), this.getY(), this.getZ()).normalize().multiply(knockback);
            nearby.addVelocity(dir.x, 0.5, dir.z);
        }

        playSmashEffect();
    }

    @Override
    public ItemStack getWeaponStack() {
        return new ItemStack(Items.MACE);
    }
}
