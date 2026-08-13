package com.soybean.entity.custom;

import com.soybean.entity.EntityRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BowProjectileEntity extends ProjectileEntity {

    private ItemStack projectileStack = ItemStack.EMPTY;
    private boolean dropped = false;
    private int breakCount = 0;
    private static final int MAX_BREAKS = 5;

    public BowProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public BowProjectileEntity(World world, LivingEntity owner, ItemStack projectileStack) {
        this(EntityRegister.BOW_PROJECTILE_ENTITY, world);
        this.setOwner(owner);
        this.projectileStack = projectileStack.copyWithCount(1);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
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
            this.setVelocity(velocity.x, velocity.y - 0.05, velocity.z);
        }

        ProjectileUtil.setRotationFromVelocity(this, 0.2F);
        this.checkBlockCollision();
    }

    private void dropProjectile() {
        if (dropped) return;
        dropped = true;
        World world = this.getWorld();
        if (world.isClient) return;

        ItemStack drop = projectileStack.isEmpty() ? new ItemStack(net.minecraft.item.Items.BOW) : projectileStack.copy();
        ItemEntity itemEntity = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), drop);
        itemEntity.setVelocity(0, 0, 0);
        world.spawnEntity(itemEntity);
    }

    private void tryBreakBlock(BlockHitResult hitResult) {
        World world = this.getWorld();
        if (world.isClient) return;

        BlockState state = world.getBlockState(hitResult.getBlockPos());
        float hardness = state.getBlock().getHardness();
        if (hardness >= 0 && hardness <= 1.5f && state.getBlock() != Blocks.BEDROCK) {
            world.breakBlock(hitResult.getBlockPos(), true);
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity hit = entityHitResult.getEntity();
        Entity owner = this.getOwner();
        World world = this.getWorld();
        if (world.isClient) return;

        float impactSpeed = (float) this.getVelocity().length();
        float damage = 4.0f + impactSpeed * 2.0f;

        DamageSource damageSource;
        if (owner instanceof LivingEntity living) {
            damageSource = world.getDamageSources().mobProjectile(this, living);
        } else {
            damageSource = world.getDamageSources().generic();
        }
        hit.damage(damageSource, damage);

        dropProjectile();
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        World world = this.getWorld();
        if (world.isClient) return;

        tryBreakBlock(blockHitResult);
        dropProjectile();
        this.discard();
    }

    @Override
    public ItemStack getWeaponStack() {
        return projectileStack.isEmpty() ? new ItemStack(net.minecraft.item.Items.BOW) : projectileStack.copy();
    }
}
