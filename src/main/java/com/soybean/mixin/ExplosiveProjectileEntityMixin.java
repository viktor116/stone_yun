package com.soybean.mixin;

import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 飞行尾迹粒子单独注入父类 ExplosiveProjectileEntity
@Mixin(ExplosiveProjectileEntity.class)
public abstract class ExplosiveProjectileEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        ExplosiveProjectileEntity self = (ExplosiveProjectileEntity)(Object)this;
        // 只处理凋零之首
        if (!(self instanceof WitherSkullEntity skull)) return;
        if (self.getWorld().isClient) return;

        ServerWorld serverWorld = (ServerWorld) self.getWorld();
        boolean charged = skull.isCharged();

        if (charged) {
            serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                    skull.getX(), skull.getY(), skull.getZ(),
                    3, 0.15, 0.15, 0.15, 0.01);
            serverWorld.spawnParticles(ParticleTypes.DRAGON_BREATH,
                    skull.getX(), skull.getY(), skull.getZ(),
                    2, 0.1, 0.1, 0.1, 0.02);
        } else {
            serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                    skull.getX(), skull.getY(), skull.getZ(),
                    3, 0.1, 0.1, 0.1, 0.01);
            serverWorld.spawnParticles(ParticleTypes.SOUL,
                    skull.getX(), skull.getY(), skull.getZ(),
                    2, 0.1, 0.1, 0.1, 0.01);
        }
    }
}