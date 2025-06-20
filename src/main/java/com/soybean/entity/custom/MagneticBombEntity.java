package com.soybean.entity.custom;

import com.soybean.entity.EntityRegister;
import com.soybean.items.ItemsRegister;
import com.soybean.sound.SoundRegister;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.tick.Tick;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

import java.util.UUID;

/**
 * @author soybean
 * @date 2025/4/24 11:28
 * @description
 */
public class MagneticBombEntity extends PersistentProjectileEntity implements GeoEntity {
    private ItemStack BombStack = new ItemStack(ItemsRegister.MAGNETIC_BOMB);
    private boolean isTorchBlock = false;
    public boolean isReturning = false;
    public int BOMB_TIME_MAX = 90;
    public int BOMB_TIME = 0;
    public int flashTimer = 0; // 每次响声时设置成5，每tick减1

    private Direction hitDirection = Direction.UP; // 默认朝下（地面）


    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);


    public MagneticBombEntity(EntityType<? extends MagneticBombEntity> entityType, World world) {
        super(entityType, world);
    }

    public MagneticBombEntity(World world, double x, double y, double z, ItemStack stack) {
        super(EntityRegister.MAGNETIC_BOMB_ENTITY_TYPE, x, y, z, world, stack, stack);
    }

    public MagneticBombEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegister.MAGNETIC_BOMB_ENTITY_TYPE, owner, world,stack,stack);
        this.BombStack = stack.copy();
        this.setOwner(owner);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    protected ItemStack asItemStack() {
        return this.BombStack.copy();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ItemsRegister.MAGNETIC_BOMB.getDefaultStack();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.hitDirection = blockHitResult.getSide(); // 保存撞击面的方向
        isTorchBlock = true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 1.0F;
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.getDamageSources().trident(this, (Entity)(entity2 == null ? this : entity2));
        World var7 = this.getWorld();
        if (var7 instanceof ServerWorld serverWorld) {
            f = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, f);
        }

        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            var7 = this.getWorld();
            if (var7 instanceof ServerWorld serverWorld) {
                serverWorld = (ServerWorld)var7;
                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack());
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                this.knockback(livingEntity, damageSource);
                this.onHit(livingEntity);
            }
        }

        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.ENTITY_SNIFFER_DROP_SEED, 1.0F, 1.0F);
    }

    @Override
    public void tick() {
        if(this.isTorchBlock){

            BOMB_TIME++;
            if(BOMB_TIME >= BOMB_TIME_MAX){
                World world = this.getWorld();

                if (world instanceof ServerWorld serverWorld) {
                    // 主爆炸火球粒子
                    for (int i = 0; i < 80; i++) {
                        serverWorld.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(),
                                1, (this.random.nextDouble() - 0.5) * 4, (this.random.nextDouble() - 0.5) * 4, (this.random.nextDouble() - 0.5) * 4, 0);
                    }

                    // 火焰范围扩大
                    serverWorld.spawnParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(),
                            150, 1.0, 1.0, 1.0, 0.1);

                    // 烟雾范围扩大
                    serverWorld.spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(),
                            100, 1.0, 1.0, 1.0, 0.05);

                    // 炫光核心闪光（仍然聚焦中心）
                    serverWorld.spawnParticles(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);

                    // 更大范围的能量扩散
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, this.getX(), this.getY(), this.getZ(),
                            60, 1.0, 1.0, 1.0, 0.2);
                }

                // 加强音效组合
                world.playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 2.0F, 0.8F);
                world.playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 1.5F, 1.2F);

                // 爆炸本体
                world.createExplosion(this, this.getX(), this.getY(), this.getZ(),
                        4.0F, World.ExplosionSourceType.NONE);

                discard();
            }

            // 60 tick 前每 20 tick 播放一次声音
            if (BOMB_TIME % 20 == 0 && BOMB_TIME <= BOMB_TIME_MAX - 30) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundRegister.BOMB_TIME, this.getSoundCategory(), 1.0F, 1.0F);
                this.flashTimer = 5; // 短暂变红
            }

            // 最后30 tick 每 5 tick 播放一次声音
            if (BOMB_TIME >= BOMB_TIME_MAX - 30 && BOMB_TIME % 5 == 0) {
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundRegister.BOMB_TIME, this.getSoundCategory(), 1.0F, 1.0F);
                this.flashTimer = 5; // 短暂变红
            }

            if (flashTimer > 0) {
                flashTimer--;
            }

        }
        super.tick();
    }

    public int getBombColor() {
        if (flashTimer > 0) {
            return 0xFFFF5555; // 更亮的红色
        }

        if (!isTorchBlock) return 0xFFFFFFFF; // 默认颜色：白

        if (BOMB_TIME >= BOMB_TIME_MAX - 30) {
            float progress = (BOMB_TIME - (BOMB_TIME_MAX - 30)) / 30.0f;
            int red = 255;
            int green = (int)(255 * (1 - progress));
            int blue = (int)(255 * (1 - progress));
            int alpha = 255;

            return (alpha << 24) | (red << 16) | (green << 8) | blue;
        }

        return 0xFFFFFFFF;
    }
    public Direction getHitDirection() {
        return this.hitDirection;
    }


    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_SNIFFER_DROP_SEED;
    }



    @Override
    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
