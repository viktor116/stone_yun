package com.soybean.particles.custom;

import com.soybean.utils.CommonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

import java.util.Random;

/**
 * @author soybean
 * @date 2025/1/10 15:19
 * @description
 */
public class BlueThunderParticle extends SpriteBillboardParticle {

    private final SpriteProvider spriteProvider;

    public BlueThunderParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
        super(world, x, y, z);
        // 设置粒子的初始大小
        this.scale = -0.1F; // 让粒子变大
        // 设置粒子的移动速度
        this.velocityX = 0.0;
        this.velocityY = 0.0;
        this.velocityZ = 0.0;
        // 设置粒子的生命周期（法阵粒子通常会停留更久）
        this.maxAge = 20;  // 20 tick（1秒）
        this.spriteProvider = spriteProvider;
        this.setSpriteForAge(this.spriteProvider);
        this.setAlpha(1.0F); // 设置粒子透明度为不透明
    }


    @Override
    public void tick() {
        super.tick();

        Random random = CommonUtils.getRandom();

        // 完全随机的位置偏移
        double randomX = (random.nextDouble() - 0.5) * 0.3; // -0.15 到 0.15
        double randomY = (random.nextDouble() - 0.5) * 0.3;
        double randomZ = (random.nextDouble() - 0.5) * 0.3;

        // 随机速度
        double velX = (random.nextDouble() - 0.5) * 0.2;
        double velY = (random.nextDouble() - 0.5) * 0.2;
        double velZ = (random.nextDouble() - 0.5) * 0.2;

        this.setVelocity(velX, velY, velZ);

        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        // 随机位置更新
        this.x += randomX;
        this.y += randomY;
        this.z += randomZ;

        // 完全随机角度
        this.prevAngle = this.angle;
        this.angle = random.nextFloat() * 360F;

        this.setSpriteForAge(this.spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {

        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new BlueThunderParticle(world, x, y, z,this.spriteProvider);
        }
    }
}
