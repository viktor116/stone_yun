package com.soybean.entity.custom;

import com.soybean.entity.EntityRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class MaceProjectileEntity extends ProjectileEntity {

    private ItemStack maceStack = new ItemStack(Items.MACE);
    private boolean dropped = false;

    public MaceProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public MaceProjectileEntity(World world, LivingEntity owner) {
        this(world, owner, new ItemStack(Items.MACE));
    }

    public MaceProjectileEntity(World world, LivingEntity owner, ItemStack maceStack) {
        this(EntityRegister.MACE_PROJECTILE_ENTITY, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        if (!maceStack.isEmpty()) {
            this.maceStack = maceStack.copy();
        }
    }

    @Override
    protected void initDataTracker(net.minecraft.entity.data.DataTracker.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();

        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }

        Vec3d velocity = this.getVelocity();
        this.setPosition(this.getX() + velocity.x, this.getY() + velocity.y, this.getZ() + velocity.z);

        if (!this.hasNoGravity()) {
            this.setVelocity(velocity.x, velocity.y - 0.08, velocity.z);
        }

        ProjectileUtil.setRotationFromVelocity(this, 0.2F);
        this.checkBlockCollision();
    }

    private void dropMace() {
        if (dropped) return;
        dropped = true;
        World world = this.getWorld();
        if (world.isClient) return;

        ItemStack droppedStack = maceStack.copy();
        droppedStack.setCount(1);
        int newDmg = Math.min(droppedStack.getDamage() + 1, droppedStack.getMaxDamage() + 1);
        if (newDmg > droppedStack.getMaxDamage()) {
            droppedStack.decrement(1);
        } else {
            droppedStack.setDamage(newDmg);
        }

        ItemEntity itemEntity = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), droppedStack);
        itemEntity.setVelocity(0, 0, 0);
        world.spawnEntity(itemEntity);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity hit = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        World world = this.getWorld();
        if (world.isClient) return;

        float impactSpeed = (float) this.getVelocity().length();
        float fallDistance = Math.max(3.0f, impactSpeed * 5.0f);

        float bonus;
        if (fallDistance <= 3.0f) {
            bonus = 4.0f * fallDistance;
        } else if (fallDistance <= 8.0f) {
            bonus = 12.0f + 2.0f * (fallDistance - 3.0f);
        } else {
            bonus = 22.0f;
        }
        float totalDamage = 5.0f + bonus;

        DamageSource damageSource;
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

        world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.BLOCKS, 1.0f, 1.0f);

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 20, 1.5, 0.3, 1.5, 0.1);
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 5, 0.5, 0.2, 0.5, 0.05);
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 3, 0.3, 0.1, 0.3, 0.02);
        }

        dropMace();
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        World world = this.getWorld();
        if (world.isClient) return;

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 15, 1.2, 0.2, 1.2, 0.1);
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 5, 0.4, 0.1, 0.4, 0.05);
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 3, 0.2, 0.1, 0.2, 0.02);
        }

        world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ITEM_MACE_SMASH_GROUND, SoundCategory.BLOCKS, 1.0f, 1.0f);

        dropMace();
        this.discard();
    }

    @Override
    public ItemStack getWeaponStack() {
        return maceStack;
    }
}
