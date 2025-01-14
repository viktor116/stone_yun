package com.soybean.items.item;

import com.soybean.enchant.EnchantmentRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

/**
 * @author soybean
 * @date 2025/1/13 17:22
 * @description
 */
public class TheEndBowItem extends BowItem {

    private Map<UUID, List<RingParticle>> activeRings = new HashMap<>();

    private class RingParticle {
        Vec3d position;
        Vec3d velocity;
        int lifetime;
        float strength;
        UUID id;

        RingParticle(Vec3d pos, Vec3d vel, float strength) {
            this.position = pos;
            this.velocity = vel;
            this.lifetime = 0;
            this.strength = strength;
            this.id = UUID.randomUUID();
        }
    }
    public TheEndBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity) {
            updateRings((PlayerEntity)entity, world);
        }
    }
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient) {
            if(user instanceof PlayerEntity player) {
                // 获取玩家的位置和视线方向
                Vec3d lookDir = player.getRotationVector();
                Vec3d playerPos = player.getEyePos();

                // 计算拉弓进度
                float progress = (getMaxUseTime(stack,player) - remainingUseTicks) / 20.0F;
                progress = (progress * progress + progress * 2.0F) / 3.0F;
                progress = Math.min(progress, 1.0F);

                // 在玩家前方生成圆环的基准点
                double forwardOffset = 2.0; // 调整圆环与玩家的距离
                Vec3d basePos = playerPos.add(lookDir.multiply(forwardOffset));

                // 获取垂直于视线方向的向量用于构建圆环
                Vec3d rightVec;
                if (Math.abs(lookDir.y) > 0.99) {
                    // 当玩家垂直向上或向下看时，使用固定的右向量
                    rightVec = new Vec3d(1, 0, 0);
                } else {
                    rightVec = lookDir.crossProduct(new Vec3d(0, 1, 0)).normalize();
                }
                Vec3d upVec = rightVec.crossProduct(lookDir).normalize();

                // 创建环绕粒子效果
                double radius = 0.8 + progress * 0.5; // 圆环半径
                int particleCount = 20 + (int)(progress * 10); // 粒子数量

                for (int i = 0; i < particleCount; i++) {
                    double angle = i * (Math.PI * 2 / particleCount);

                    // 计算圆环上的点
                    Vec3d circlePos = basePos.add(
                            rightVec.multiply(Math.cos(angle) * radius).add(
                                    upVec.multiply(Math.sin(angle) * radius))
                    );

                    // 添加粒子效果
                    world.addParticle(
                            ParticleTypes.REVERSE_PORTAL,
                            circlePos.x, circlePos.y, circlePos.z,
                            0, // 粒子速度设为0使其保持在原位
                            0,
                            0
                    );

                    if (world.random.nextFloat() < 0.3) {
                        // 计算从圆心指向粒子位置的方向向量
                        Vec3d spreadDir = new Vec3d(
                                circlePos.x - basePos.x,
                                circlePos.y - basePos.y,
                                circlePos.z - basePos.z
                        ).normalize();

                        // 添加一些随机性到扩散方向
                        double spreadSpeed = 0.15 + world.random.nextDouble() * 0.1;

                        world.addParticle(
                                ParticleTypes.REVERSE_PORTAL,
                                circlePos.x, circlePos.y, circlePos.z,
                                spreadDir.x * spreadSpeed,
                                spreadDir.y * spreadSpeed,
                                spreadDir.z * spreadSpeed
                        );
                    }
                }
            }
        }
    }

    private void updateRings(PlayerEntity player, World world) {
        UUID playerId = player.getUuid();
        List<RingParticle> rings = activeRings.getOrDefault(playerId, new ArrayList<>());

        if (!rings.isEmpty()) {
            Iterator<RingParticle> iterator = rings.iterator();
            while (iterator.hasNext()) {
                RingParticle ring = iterator.next();
                // 更新环位置
                ring.position = ring.position.add(ring.velocity);
                ring.lifetime++;

                // 生成环形粒子效果
                createRingParticles(world, ring);

                // 检测碰撞和伤害
                Box hitbox = Box.from(ring.position).expand(1.0);
                List<LivingEntity> entities = world.getEntitiesByClass(
                        LivingEntity.class,
                        hitbox,
                        entity -> entity != player
                );

                for (LivingEntity entity : entities) {
                    float damage = 50.0F * ring.strength;
                    entity.damage(world.getDamageSources().magic(), damage);
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60));
                }

                // 移除超出距离的环
                if (ring.lifetime > 100) {
                    iterator.remove();
                }
            }

            if (rings.isEmpty()) {
                activeRings.remove(playerId);
            } else {
                activeRings.put(playerId, rings);
            }
        }
    }

    private void createRingParticles(World world, RingParticle ring) {
        // 获取垂直于移动方向的向量
        Vec3d direction = ring.velocity.normalize();
        Vec3d rightVec;
        if (Math.abs(direction.y) > 0.99) {
            rightVec = new Vec3d(1, 0, 0);
        } else {
            rightVec = direction.crossProduct(new Vec3d(0, 1, 0)).normalize();
        }
        Vec3d upVec = rightVec.crossProduct(direction).normalize();

        // 创建环形粒子
        double radius = 0.8 + ring.strength * 0.3;
        int particleCount = 40;

        for (int i = 0; i < particleCount; i++) {
            double angle = i * (Math.PI * 2 / particleCount);
            Vec3d circlePos = ring.position.add(
                    rightVec.multiply(Math.cos(angle) * radius).add(
                            upVec.multiply(Math.sin(angle) * radius))
            );

            ((ServerWorld)world).spawnParticles(
                    ParticleTypes.REVERSE_PORTAL,
                    circlePos.x, circlePos.y, circlePos.z,
                    1, 0, 0, 0, 0
            );
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }

        float progress = BowItem.getPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks);

        if (!world.isClient && progress >= 0.1F) {
            boolean hasInfinite = playerEntity.getAbilities().creativeMode ||
                    EnchantmentRegister.getEnchantmentLevel(user.getWorld(), Enchantments.INFINITY, stack) > 0;
            ItemStack arrowStack = playerEntity.getProjectileType(stack);

            if (!arrowStack.isEmpty() || hasInfinite) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                if (!hasInfinite) {
                    arrowStack.decrement(1);
                    if (arrowStack.isEmpty()) {
                        playerEntity.getInventory().removeOne(arrowStack);
                    }
                }

                // 发射能量环
                float strength = progress * 2.0F;
                Vec3d rotation = playerEntity.getRotationVector();
                Vec3d startPos = playerEntity.getEyePos();
                Vec3d velocity = rotation.multiply(1); // 调整速度

                // 创建新的能量环
                RingParticle ring = new RingParticle(startPos, velocity, strength);

                // 添加到追踪列表
                UUID playerId = playerEntity.getUuid();
                List<RingParticle> rings = activeRings.getOrDefault(playerId, new ArrayList<>());
                rings.add(ring);
                activeRings.put(playerId, rings);

                // 播放音效
                world.playSound(
                        null,
                        playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                        SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT,
                        SoundCategory.PLAYERS,
                        1.0F,
                        1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F)
                );

                // 损耗耐久
                stack.damage(1, playerEntity, EquipmentSlot.MAINHAND);
            }
        }
    }
}
