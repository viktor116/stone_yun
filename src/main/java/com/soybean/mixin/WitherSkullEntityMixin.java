package com.soybean.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherSkullEntity.class)
public abstract class WitherSkullEntityMixin {

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onCollision(HitResult hitResult, CallbackInfo ci) {
        WitherSkullEntity self = (WitherSkullEntity) (Object) this;
        if (!self.getWorld().isClient) {
            ServerWorld serverWorld = (ServerWorld) self.getWorld();
            boolean charged = self.isCharged();

            double x = self.getX(), y = self.getY(), z = self.getZ();

            if (charged) {
                // 充能：超大爆炸 + 黑蓝粒子
                serverWorld.createExplosion(self, x, y, z, 15.0F, true, World.ExplosionSourceType.MOB);

                serverWorld.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        x, y, z, 80, 1.5, 1.5, 1.5, 0.3);
                serverWorld.spawnParticles(ParticleTypes.DRAGON_BREATH,
                        x, y, z, 60, 1.2, 1.2, 1.2, 0.2);
                serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                        x, y, z, 40, 1.0, 1.0, 1.0, 0.05);
            } else {
                // 普通：大爆炸 + 黑色粒子
                serverWorld.createExplosion(self, x, y, z, 10.0F, true, World.ExplosionSourceType.MOB);

                serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                        x, y, z, 80, 1.5, 1.5, 1.5, 0.1);
                serverWorld.spawnParticles(ParticleTypes.SOUL,
                        x, y, z, 50, 1.2, 1.2, 1.2, 0.15);
                serverWorld.spawnParticles(ParticleTypes.PORTAL,
                        x, y, z, 60, 1.0, 1.0, 1.0, 0.8);
            }

            self.discard();
            ci.cancel(); // 取消原版爆炸逻辑
        }
    }
}