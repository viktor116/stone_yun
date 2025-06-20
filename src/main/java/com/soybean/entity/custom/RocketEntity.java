package com.soybean.entity.custom;

import com.soybean.entity.EntityRegister;
import com.soybean.items.ItemsRegister;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

/**
 * @author soybean
 * @date 2025/4/24 11:28
 * @description
 */
public class RocketEntity extends PersistentProjectileEntity implements GeoEntity {
    private ItemStack BombStack = new ItemStack(ItemsRegister.ROCKET);
    private boolean isTorch = false;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public RocketEntity(EntityType<? extends RocketEntity> entityType, World world) {
        super(entityType, world);
    }

    public RocketEntity(World world, double x, double y, double z, ItemStack stack) {
        super(EntityRegister.ROCKET_ENTITY_TYPE, x, y, z, world, stack, stack);
    }

    public RocketEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegister.ROCKET_ENTITY_TYPE, owner, world,stack,stack);
        this.BombStack = stack.copy();
        this.setOwner(owner);
        this.pickupType = PickupPermission.DISALLOWED;
        this.setNoGravity(true);
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
        isTorch = true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        isTorch = true;
    }

    @Override
    public void tick() {
        if(this.age > 600) this.discard();

        // 飞行时的尾迹效果
        if (!this.isTorch && this.getWorld() instanceof ServerWorld serverWorld) {
            createFlightTrail(serverWorld);
        }

        if (this.isTorch) {
            createMassiveExplosion();
            discard();
        }
        super.tick();
    }

    /**
     * 创建飞行尾迹效果
     */
    private void createFlightTrail(ServerWorld serverWorld) {
        // 获取火箭后方位置（相对于飞行方向）
        Vec3d velocity = this.getVelocity();
        Vec3d backwardOffset = velocity.normalize().multiply(-0.5); // 在火箭后方0.5格

        double trailX = this.getX() + backwardOffset.x;
        double trailY = this.getY() + backwardOffset.y;
        double trailZ = this.getZ() + backwardOffset.z;

        // 主要火焰尾迹 - 橙色火焰
        serverWorld.spawnParticles(ParticleTypes.FLAME,
                trailX, trailY, trailZ,
                8, 0.1, 0.1, 0.1, 0.02);

        // 黑烟尾迹
        serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                trailX, trailY, trailZ,
                6, 0.15, 0.15, 0.15, 0.03);

        // 灰色烟雾
        serverWorld.spawnParticles(ParticleTypes.SMOKE,
                trailX, trailY, trailZ,
                10, 0.2, 0.2, 0.2, 0.05);

        // 火花效果
        if (this.age % 3 == 0) { // 每3tick生成一次火花
            serverWorld.spawnParticles(ParticleTypes.LAVA,
                    trailX, trailY, trailZ,
                    2, 0.1, 0.1, 0.1, 0.1);
        }

        // 能量粒子（紫色传送门粒子）
        if (this.age % 2 == 0) {
            serverWorld.spawnParticles(ParticleTypes.PORTAL,
                    trailX, trailY, trailZ,
                    4, 0.12, 0.12, 0.12, 0.08);
        }
    }

    /**
     * 创建宏伟的爆炸效果 - 混乱乱序版本
     */
    private void createMassiveExplosion() {
        World world = this.getWorld();

        if (world instanceof ServerWorld serverWorld) {
            double explosionX = this.getX();
            double explosionY = this.getY();
            double explosionZ = this.getZ();

            // === 混乱爆炸：所有粒子随机混合生成 ===

            // 创建多个随机爆炸中心点
            int explosionCenters = 3 + this.random.nextInt(4); // 3-6个爆炸中心

            for (int center = 0; center < explosionCenters; center++) {
                // 每个爆炸中心的随机偏移
                double centerX = explosionX + (this.random.nextDouble() - 0.5) * 4;
                double centerY = explosionY + (this.random.nextDouble() - 0.5) * 3;
                double centerZ = explosionZ + (this.random.nextDouble() - 0.5) * 4;

                // 这个中心的强度（随机）
                double intensity = 0.5 + this.random.nextDouble() * 0.8;

                // 混乱生成各种粒子
                int totalParticles = (int)(100 + this.random.nextInt(150) * intensity);

                for (int p = 0; p < totalParticles; p++) {
                    // 完全随机的位置偏移，使用不同的分布
                    double distance = this.random.nextGaussian() * 3.0; // 高斯分布，更自然
                    double angle = this.random.nextDouble() * 2 * Math.PI;
                    double verticalAngle = (this.random.nextDouble() - 0.5) * Math.PI;

                    double offsetX = Math.cos(angle) * Math.cos(verticalAngle) * distance;
                    double offsetY = Math.sin(verticalAngle) * distance + this.random.nextGaussian() * 1.5;
                    double offsetZ = Math.sin(angle) * Math.cos(verticalAngle) * distance;

                    double particleX = centerX + offsetX;
                    double particleY = centerY + offsetY;
                    double particleZ = centerZ + offsetZ;

                    // 随机速度
                    double velocityX = (this.random.nextDouble() - 0.5) * 2.0;
                    double velocityY = (this.random.nextDouble() - 0.5) * 1.5;
                    double velocityZ = (this.random.nextDouble() - 0.5) * 2.0;

                    // 随机选择粒子类型 - 完全混乱
                    double particleType = this.random.nextDouble();

                    if (particleType < 0.25) {
                        // 爆炸粒子 - 25%
                        if (this.random.nextDouble() < 0.7) {
                            serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                                    particleX, particleY, particleZ, 1, velocityX, velocityY, velocityZ, 0);
                        } else {
                            // 偶尔来个大爆炸粒子
                            serverWorld.spawnParticles(ParticleTypes.EXPLOSION_EMITTER,
                                    particleX, particleY, particleZ, 1, velocityX * 0.5, velocityY * 0.5, velocityZ * 0.5, 0);
                        }
                    } else if (particleType < 0.5) {
                        // 火焰类 - 25%
                        if (this.random.nextDouble() < 0.6) {
                            serverWorld.spawnParticles(ParticleTypes.FLAME,
                                    particleX, particleY, particleZ, 1, velocityX * 0.8, velocityY * 0.8, velocityZ * 0.8, 0.1);
                        } else if (this.random.nextDouble() < 0.8) {
                            serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                                    particleX, particleY, particleZ, 1, velocityX * 0.6, velocityY * 0.6, velocityZ * 0.6, 0.08);
                        } else {
                            serverWorld.spawnParticles(ParticleTypes.LAVA,
                                    particleX, particleY, particleZ, 1, velocityX * 1.2, velocityY * 0.8, velocityZ * 1.2, 0.3);
                        }
                    } else if (particleType < 0.75) {
                        // 烟雾类 - 25%
                        if (this.random.nextDouble() < 0.6) {
                            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                                    particleX, particleY, particleZ, 1, velocityX * 0.4, velocityY * 0.6, velocityZ * 0.4, 0.05);
                        } else {
                            serverWorld.spawnParticles(ParticleTypes.SMOKE,
                                    particleX, particleY, particleZ, 1, velocityX * 0.3, velocityY * 0.5, velocityZ * 0.3, 0.03);
                        }
                    } else {
                        // 特殊效果类 - 25%
                        double specialType = this.random.nextDouble();
                        if (specialType < 0.4) {
                            serverWorld.spawnParticles(ParticleTypes.PORTAL,
                                    particleX, particleY, particleZ, 1, velocityX * 0.7, velocityY * 0.7, velocityZ * 0.7, 0.2);
                        } else if (specialType < 0.7) {
                            serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                                    particleX, particleY, particleZ, 1, velocityX * 0.5, velocityY * 0.5, velocityZ * 0.5, 0.15);
                        } else if (specialType < 0.9) {
                            serverWorld.spawnParticles(ParticleTypes.FLASH,
                                    particleX, particleY, particleZ, 1, 0, 0, 0, 0);
                        } else {
                            // 稀有：末影粒子
                            serverWorld.spawnParticles(ParticleTypes.DRAGON_BREATH,
                                    particleX, particleY, particleZ, 1, velocityX * 0.3, velocityY * 0.3, velocityZ * 0.3, 0.1);
                        }
                    }
                }
            }

            // === 额外的随机爆发效果 ===

            // 随机的延迟爆炸波
            for (int wave = 0; wave < 2 + this.random.nextInt(3); wave++) {
                double waveRadius = 2.0 + this.random.nextDouble() * 6.0;
                int waveParticles = 20 + this.random.nextInt(40);

                for (int i = 0; i < waveParticles; i++) {
                    // 不规则的波形
                    double angle = this.random.nextDouble() * 2 * Math.PI;
                    double actualRadius = waveRadius * (0.7 + this.random.nextDouble() * 0.6); // 不完美的圆形

                    double waveX = explosionX + Math.cos(angle) * actualRadius;
                    double waveY = explosionY + (this.random.nextDouble() - 0.5) * 2.0;
                    double waveZ = explosionZ + Math.sin(angle) * actualRadius;

                    // 随机粒子类型
                    if (this.random.nextDouble() < 0.5) {
                        serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                                waveX, waveY, waveZ, 1,
                                (this.random.nextDouble() - 0.5) * 0.5,
                                (this.random.nextDouble() - 0.5) * 0.5,
                                (this.random.nextDouble() - 0.5) * 0.5, 0);
                    } else {
                        serverWorld.spawnParticles(ParticleTypes.FLAME,
                                waveX, waveY, waveZ, 1,
                                (this.random.nextDouble() - 0.5) * 0.8,
                                (this.random.nextDouble() - 0.5) * 0.8,
                                (this.random.nextDouble() - 0.5) * 0.8, 0.1);
                    }
                }
            }

            // === 完全随机的散乱粒子 ===

            // 大范围散乱效果
            for (int i = 0; i < 50 + this.random.nextInt(100); i++) {
                double scatterX = explosionX + (this.random.nextGaussian() * 8.0);
                double scatterY = explosionY + (this.random.nextGaussian() * 6.0);
                double scatterZ = explosionZ + (this.random.nextGaussian() * 8.0);

                // 完全随机的粒子
                ParticleEffect[] randomParticles = {
                        ParticleTypes.EXPLOSION, ParticleTypes.FLAME, ParticleTypes.LARGE_SMOKE,
                        ParticleTypes.SMOKE, ParticleTypes.LAVA, ParticleTypes.PORTAL,
                        ParticleTypes.SOUL_FIRE_FLAME, ParticleTypes.REVERSE_PORTAL
                };

                ParticleEffect randomParticle = randomParticles[this.random.nextInt(randomParticles.length)];

                serverWorld.spawnParticles(randomParticle,
                        scatterX, scatterY, scatterZ, 1,
                        (this.random.nextDouble() - 0.5) * 1.0,
                        (this.random.nextDouble() - 0.5) * 1.0,
                        (this.random.nextDouble() - 0.5) * 1.0,
                        0.05 + this.random.nextDouble() * 0.2);
            }
        }

        // === 音效层次 ===
        // 主爆炸音效 - 更响更低沉
        world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, 0.5F);

        // 龙息爆炸音效
        world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 3.0F, 0.8F);

        // 雷击音效增加震撼感
        world.playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 2.0F, 1.5F);

        // === 物理爆炸 ===
        // 增大爆炸威力和范围
        world.createExplosion(this, this.getX(), this.getY(), this.getZ(),
                8.0F, World.ExplosionSourceType.NONE);

        // 创建二次爆炸增加层次感
        world.createExplosion(this, this.getX(), this.getY() + 1, this.getZ(),
                6.0F, World.ExplosionSourceType.NONE);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENTITY_GENERIC_EXPLODE.value();
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