package com.soybean.items.item;

import com.soybean.network.BlockPosPayload;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

/**
 * @author soybean
 * @date 2025/1/13 17:22
 * @description
 */
public class EnderSword extends SwordItem {
    private static final int FIRE_DURATION = 100;

    private static final SimpleParticleType PARTICLE_TYPE = ParticleTypes.DRAGON_BREATH;
    private Map<UUID, List<EnderSword.DamageParticle>> activeParticles = new HashMap<>();

    private class DamageParticle {
        Vec3d position;
        Vec3d velocity;
        int lifetime;
        UUID id;

        DamageParticle(Vec3d pos, Vec3d vel) {
            this.position = pos;
            this.velocity = vel;
            this.lifetime = 0;
            this.id = UUID.randomUUID();
        }
    }

    public EnderSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity) {
            updateParticles((PlayerEntity)entity, world);
        }
    }

    private void updateParticles(PlayerEntity player, World world) {
        UUID playerId = player.getUuid();
        List<EnderSword.DamageParticle> particles = activeParticles.getOrDefault(playerId, new ArrayList<>());

        if (!particles.isEmpty()) {
            Iterator<EnderSword.DamageParticle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                EnderSword.DamageParticle particle = iterator.next();
                // 更新粒子位置
                particle.position = particle.position.add(particle.velocity);
                particle.lifetime++;

                // 生成视觉效果
                ((ServerWorld)world).spawnParticles(
                        PARTICLE_TYPE,
                        particle.position.x,
                        particle.position.y,
                        particle.position.z,
                        1, 0.1, 0.1, 0.1, 0
                );

                // 检测碰撞和伤害
                Box hitbox = Box.from(particle.position).expand(1.0);
                List<LivingEntity> entities = world.getEntitiesByClass(
                        LivingEntity.class,
                        hitbox,
                        entity -> entity != player
                );
                if(!world.isClient()) {
                    for (LivingEntity entity : entities) {
                        Vec3d velocity = particle.velocity;
                        entity.setPos(entity.getX() + velocity.x * 10000, entity.getY(), entity.getZ() + velocity.z * 10000);
                    }
                }

                // 移除超出距离的粒子
                if (particle.lifetime > 100) { // 30格距离约等于60tick
                    iterator.remove();
                }
            }

            if (particles.isEmpty()) {
                activeParticles.remove(playerId);
            } else {
                activeParticles.put(playerId, particles);
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        launchFireballs(user, world);
        ItemStack stackInHand = user.getStackInHand(hand);
        stackInHand.damage(1,user, EquipmentSlot.MAINHAND);
        return TypedActionResult.success(stackInHand);
    }
    private void launchFireballs(PlayerEntity playerEntity, World world) {
        if (!world.isClient()) {
            // 获取玩家位置和朝向
            Vec3d lookDir = playerEntity.getRotationVector();
            Vec3d playerPos = playerEntity.getEyePos();

            // 水平方向计算
            Vec3d horizontalLook = new Vec3d(lookDir.x, lookDir.y, lookDir.z).normalize();
            Vec3d rightVec;
            if (Math.abs(lookDir.y) > 0.99) {
                // 当玩家垂直向上或向下看时，使用固定的右向量
                rightVec = new Vec3d(1, 0, 1);
            }else{
                rightVec = horizontalLook.crossProduct(new Vec3d(0,1, 0)).normalize();
            }

            // 半圆参数
            double radius = 5.0;
            int particleCount = 80;
            int layerCount = 3;
            double spreadAngle = Math.PI;
            double forwardOffset = 1.0;
            double particleSpeed = 1; // 粒子飞行速度

            List<EnderSword.DamageParticle> newParticles = new ArrayList<>();

            // 为每一层生成粒子
            for (int layer = 0; layer < layerCount; layer++) {
                double layerOffset = (layer - (layerCount - 1) / 2.0) * 0.3;

                for (int i = 0; i < particleCount; i++) {
                    double angle = -spreadAngle/2 + (spreadAngle * i / (particleCount - 1));

                    Vec3d basePos = playerPos.add(lookDir.multiply(forwardOffset));

                    // 修改这里：根据角度调整半径，使末端也向外扩展
                    double adjustedRadius = radius * (1.0 + Math.abs(angle / (spreadAngle/2)) * 0.5);

                    Vec3d horizontalOffset = lookDir.multiply(Math.cos(angle) * adjustedRadius)
                            .add(rightVec.multiply(Math.sin(angle) * adjustedRadius));

                    Vec3d finalOffset = new Vec3d(
                            horizontalOffset.x,
                            layerOffset+horizontalOffset.y,
                            horizontalOffset.z
                    );

                    Vec3d particlePos = basePos.add(finalOffset);

                    // 计算粒子的速度向量（主要是向前）
                    Vec3d velocity = horizontalLook.multiply(particleSpeed);

                    // 创建新的伤害粒子
                    newParticles.add(new EnderSword.DamageParticle(particlePos, velocity));

                    // 初始视觉效果
                    ((ServerWorld)world).spawnParticles(
                            PARTICLE_TYPE,
                            particlePos.x, particlePos.y, particlePos.z,
                            1,
                            velocity.x * 0.1, velocity.y * 0.1, velocity.z * 0.1,
                            0.02
                    );
                }
            }


            // 添加新的粒子到跟踪列表
            activeParticles.put(playerEntity.getUuid(), newParticles);

            // 播放音效
            world.playSound(
                    null,
                    playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(),
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS,
                    1.0F,
                    1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F
            );
        }
    }


}
