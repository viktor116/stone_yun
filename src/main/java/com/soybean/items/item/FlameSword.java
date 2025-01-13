package com.soybean.items.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author soybean
 * @date 2025/1/13 17:22
 * @description
 */
public class FlameSword extends SwordItem {
    private static final int FIRE_DURATION = 100;
    public FlameSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        launchFireballs(user, world);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
    private void launchFireballs(PlayerEntity playerEntity, World world) {
        // 获取玩家视角方向
        Vec3d lookDirection = playerEntity.getRotationVector();
        Vec3d playerPos = playerEntity.getPos();


        world.playSound(
                null, // null 表示所有玩家都能听到
                playerEntity.getX(),
                playerEntity.getY(),
                playerEntity.getZ(),
                SoundEvents.ITEM_FIRECHARGE_USE, // 火焰发射音效
                SoundCategory.PLAYERS,
                1.0F, // 音量
                1.0F  // 音调
        );

        // 存储所有火焰粒子的路径点
        List<Vec3d> pathPoints = new ArrayList<>();
        double pathStepLength = 1.0; // 每隔1格检查一次
        int maxDistance = 30; // 最大射程

        double radius = 5.0;  // 半圆的半径
        int particleCount = 30;  // 半圆粒子数量
        int arcDegrees = 180;  // 角度范围，半圆为180度
        double angleStep = Math.toRadians(arcDegrees) / (particleCount - 1);
        // 发射大量火焰粒子
        for (int i = 0; i < particleCount; i++) {
            double angle = -arcDegrees / 2.0 + i * (arcDegrees / (double)(particleCount - 1));
            double offsetX = Math.cos(Math.toRadians(angle)) * radius;
            double offsetZ = Math.sin(Math.toRadians(angle)) * radius;

            // 旋转粒子方向，使其与玩家视角对齐
            Vec3d rotatedOffset = rotateVector(offsetX, 0, offsetZ, lookDirection);
            Vec3d startPos = playerPos.add(rotatedOffset.x, playerEntity.getEyeY() - 0.1, rotatedOffset.z);
            world.addParticle(
                    ParticleTypes.FLAME,
                    startPos.x,
                    startPos.y,
                    startPos.z,
                    lookDirection.x * 0.5,
                    lookDirection.y * 0.5,
                    lookDirection.z * 0.5
            );

            // 为每个粒子生成路径点
            for (double step = 0; step <= maxDistance; step += pathStepLength) {
                Vec3d pathPoint = new Vec3d(
                        startPos.x + lookDirection.x * step,
                        startPos.y + lookDirection.y * step,
                        startPos.z + lookDirection.z * step
                );
                pathPoints.add(pathPoint);
            }
        }

        // 检测范围内的实体
        double checkRadius = 2.0; // 每个检查点的检测半径

        // 获取可能受影响的实体
        Box totalArea = Box.from(playerPos).expand(15, 15, 15);
        List<LivingEntity> nearbyEntities = world.getEntitiesByClass(
                LivingEntity.class,
                totalArea,
                e -> e != playerEntity
        );

        // 用于记录已经受到伤害的实体，防止重复伤害
        Set<LivingEntity> damagedEntities = new HashSet<>();

        // 检查每个实体是否在任何路径点的范围内
        for (LivingEntity entity : nearbyEntities) {
            if (damagedEntities.contains(entity)) {
                continue;
            }

            for (Vec3d pathPoint : pathPoints) {
                // 检查实体是否在检查点的范围内
                if (entity.getBoundingBox().intersects(
                        Box.from(pathPoint).expand(checkRadius)
                )) {
                    entity.setOnFireFor(FIRE_DURATION);
                    entity.damage(playerEntity.getDamageSources().inFire(), 5.0f);
                    damagedEntities.add(entity);
                    break;
                }
            }
        }
    }

    private Vec3d rotateVector(double x, double y, double z, Vec3d direction) {
        double yaw = Math.atan2(direction.z, direction.x) - Math.PI / 2; // 修正角度
        double cos = Math.cos(yaw);
        double sin = Math.sin(yaw);
        return new Vec3d(x * cos - z * sin, y, x * sin + z * cos);
    }
}
