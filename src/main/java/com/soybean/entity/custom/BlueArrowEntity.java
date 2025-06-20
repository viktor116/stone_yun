package com.soybean.entity.custom;

import com.soybean.enchant.EnchantmentRegister;
import com.soybean.entity.EntityRegister;
import com.soybean.particles.ParticlesRegister;
import com.soybean.sound.SoundRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueArrowEntity extends PersistentProjectileEntity {

    private int existTime = 0;

    @Override
    public void tick() {
        super.tick();
        if(this.inGroundTime> 1){
            if(existTime % 20 == 0 && existTime <= 70){
                this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundRegister.BLUE_ARROW, this.getSoundCategory(), 0.2F, 1.0F);
            }

            if(!this.getWorld().isClient()){
                createExpandingParticleRing((ServerWorld) this.getWorld(), this, (PlayerEntity) this.getOwner());
            }

            existTime++;
            if (existTime > 100) {
                this.discard();
            }
        }
    }

    public BlueArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public BlueArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityRegister.BLUE_ARROW_ENTITY, x, y, z, world, stack, shotFrom);
    }

    public BlueArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(EntityRegister.BLUE_ARROW_ENTITY, owner, world, stack, shotFrom);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.ARROW.getDefaultStack();
    }

    public static void createExpandingParticleRing(ServerWorld world, Entity arrow, PlayerEntity player) {
        Random random = world.getRandom();

        for (int radius = 1; radius <= 5; radius++) {
            final int currentRadius = radius;
            world.getServer().execute(() -> {
                // 每个半径生成36个粒子，但位置完全随机化
                int particleCount = 36;
                for (int i = 0; i < particleCount; i++) {
                    // 在环形区域内随机分布
                    double minRadius = Math.max(0, currentRadius - 0.5);
                    double maxRadius = currentRadius + 0.5;

                    // 随机角度
                    double angle = random.nextDouble() * 360;
                    // 随机半径（在范围内）
                    double randomRadius = minRadius + random.nextDouble() * (maxRadius - minRadius);

                    double x = arrow.getX() + Math.cos(Math.toRadians(angle)) * randomRadius;
                    double z = arrow.getZ() + Math.sin(Math.toRadians(angle)) * randomRadius;
                    double y = arrow.getY() + 0.1 + (random.nextDouble() - 0.5) * 0.4;

                    world.spawnParticles(
                            ParticlesRegister.BLUE_THUNDER,
                            x, y, z,
                            1, 0.1, 0.1, 0.1, 0.05
                    );
                }

                // 伤害检测保持使用原始半径
                Box damageBox = new Box(
                        arrow.getX() - currentRadius,
                        arrow.getY() - 1,
                        arrow.getZ() - currentRadius,
                        arrow.getX() + currentRadius,
                        arrow.getY() + 1,
                        arrow.getZ() + currentRadius
                );

                List<Entity> entities = world.getOtherEntities(null, damageBox);
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity) {
                        entity.damage(world.getDamageSources().magic(), 6F);
                    }
                }
            });
        }
    }
}
