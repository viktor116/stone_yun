package com.soybean.mixin;

import com.soybean.config.InitValue;
import com.soybean.items.ItemsRegister;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author soybean
 * @date 2025/4/23 15:59
 * @description
 */
@Mixin(AbstractHorseEntity.class)
public abstract class MixinAbstractHorseEntity extends AnimalEntity {

    @Shadow
    private void playEatingAnimation(){};

    @Shadow @Nullable public abstract EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData);

    protected MixinAbstractHorseEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "receiveFood", at = @At("HEAD"), cancellable = true)
    private void injectTail(PlayerEntity player, ItemStack item, CallbackInfoReturnable<Boolean> cir) {
        if (item.isOf(ItemsRegister.DOUBLE_ENCHANTED_GOLDEN_APPLE)) {
            AbstractHorseEntity horse = (AbstractHorseEntity)(Object)this;

            if (!this.getWorld().isClient) {
                horse.heal(50.0F);
                horse.setTame(true);
                this.playEatingAnimation();
                this.emitGameEvent(GameEvent.EAT);

                this.getWorld().addParticle(ParticleTypes.HEART,
                        this.getParticleX(1.0),
                        this.getRandomBodyY() + 0.5,
                        this.getParticleZ(1.0),
                        0.0, 0.0, 0.0);
            }

            cir.setReturnValue(true);
        }
    }
}