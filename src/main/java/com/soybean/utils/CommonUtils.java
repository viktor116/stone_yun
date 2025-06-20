package com.soybean.utils;

import net.minecraft.entity.Entity;
import net.minecraft.particle.AbstractDustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author soybean
 * @date 2024/10/9 16:46
 * @description
 */
public class CommonUtils {

    private static final Random RANDOM = new Random();
    //无动画默认占位
    public static final String DEFAULT_ANIMATION = "animations/default.animation.json";


    public static void spawnParticlesAndPlaySound(World world, Entity entity, ParticleEffect particleType, SoundEvent soundEvent, SoundCategory soundCategory, int particleCount, double particleSpeed, double particleHeight, float volume, float pitch) {
        if (!world.isClient) {
            ((ServerWorld)world).spawnParticles(particleType,
                    entity.getX(), entity.getY(), entity.getZ(),
                    particleCount,
                    particleHeight, particleHeight, particleHeight,
                    particleSpeed
            );
            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    soundEvent,
                    soundCategory,
                    volume, pitch);
        }
    }

    public static void spawnSelfParticle(ServerWorld serverWorld, Entity entity, SimpleParticleType particleType){
        for (int i = 0; i < 20; i++) { // 生成20个粒子
            double d0 = entity.getWorld().random.nextGaussian() * 0.02D;
            double d1 = entity.getWorld().random.nextGaussian() * 0.02D;
            double d2 = entity.getWorld().random.nextGaussian() * 0.02D;

            serverWorld.spawnParticles(
                    particleType,
                    entity.getX() + (entity.getWorld().random.nextDouble() - 0.5) * 2.0,
                    entity.getY() + entity.getWorld().random.nextDouble() * 2.0,
                    entity.getZ() + (entity.getWorld().random.nextDouble() - 0.5) * 2.0,
                    1, // 每个位置生成的粒子数量
                    d0, // X方向速度
                    d1 + 0.05, // Y方向速度，稍微向上漂浮
                    d2, // Z方向速度
                    0.0D // 粒子速度
            );
        }
    }

    public static void spawnSelfParticle(ServerWorld serverWorld, BlockPos pos, SimpleParticleType particleType){
        for (int i = 0; i < 20; i++) { // 生成20个粒子
            double d0 = getRandom().nextGaussian() * 0.02D;
            double d1 = getRandom().nextGaussian() * 0.02D;
            double d2 = getRandom().nextGaussian() * 0.02D;

            serverWorld.spawnParticles(
                    particleType,
                    pos.getX() + (getRandom().nextDouble() - 0.5) * 2.0,
                    pos.getY() + getRandom().nextDouble() * 2.0,
                    pos.getZ() + (getRandom().nextDouble() - 0.5) * 2.0,
                    1, // 每个位置生成的粒子数量
                    d0, // X方向速度
                    d1 + 0.05, // Y方向速度，稍微向上漂浮
                    d2, // Z方向速度
                    0.0D // 粒子速度
            );
        }
    }

    public static void spawnSelfParticle(ServerWorld serverWorld, Entity entity, AbstractDustParticleEffect particleType){
        for (int i = 0; i < 20; i++) { // 生成20个粒子
            double d0 = entity.getWorld().random.nextGaussian() * 0.02D;
            double d1 = entity.getWorld().random.nextGaussian() * 0.02D;
            double d2 = entity.getWorld().random.nextGaussian() * 0.02D;

            serverWorld.spawnParticles(
                    particleType,
                    entity.getX() + (entity.getWorld().random.nextDouble() - 0.5) * 2.0,
                    entity.getY() + entity.getWorld().random.nextDouble() * 2.0,
                    entity.getZ() + (entity.getWorld().random.nextDouble() - 0.5) * 2.0,
                    1, // 每个位置生成的粒子数量
                    d0, // X方向速度
                    d1 + 0.05, // Y方向速度，稍微向上漂浮
                    d2, // Z方向速度
                    0.0D // 粒子速度
            );
        }
    }

    public static Random getRandom() {
        return RANDOM;
    }
}
