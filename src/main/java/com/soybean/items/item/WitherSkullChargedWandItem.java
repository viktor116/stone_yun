package com.soybean.items.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// WitherSkullChargedWandItem.java —— 充能凋零之首（黑蓝粒子特效）
public class WitherSkullChargedWandItem extends Item {

    public WitherSkullChargedWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            Vec3d look = user.getRotationVec(1.0F);

            // 发射时粒子特效（黑蓝爆发）
            spawnChargedLaunchParticles((ServerWorld) world, user, look);

            WitherSkullEntity skull = new WitherSkullEntity(
                    EntityType.WITHER_SKULL, world);
            skull.setOwner(user);
            skull.setCharged(true);
            skull.setPosition(
                    user.getX() + look.x * 1.5,
                    user.getEyeY() - 0.1,
                    user.getZ() + look.z * 1.5
            );
            skull.addVelocity(look.x * 2.0, look.y * 2.0, look.z * 2.0);
            world.spawnEntity(skull);

            world.playSound(null, user.getBlockPos(),
                    SoundEvents.ENTITY_WITHER_SHOOT,
                    SoundCategory.PLAYERS, 1.0F, 0.5F);
        }
        return TypedActionResult.success(stack);
    }

    private void spawnChargedLaunchParticles(ServerWorld world, PlayerEntity user, Vec3d look) {
        double x = user.getX() + look.x * 1.5;
        double y = user.getEyeY() - 0.1;
        double z = user.getZ() + look.z * 1.5;

        // 黑蓝灵魂火焰
        world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                x, y, z, 25, 0.4, 0.4, 0.4, 0.08);
        // 深色传送门粒子（黑蓝色调）
        world.spawnParticles(ParticleTypes.REVERSE_PORTAL,
                x, y, z, 20, 0.3, 0.3, 0.3, 0.3);
        // 黑烟
        world.spawnParticles(ParticleTypes.LARGE_SMOKE,
                x, y, z, 15, 0.2, 0.2, 0.2, 0.03);
        // 龙息粒子（蓝紫）
        world.spawnParticles(ParticleTypes.DRAGON_BREATH,
                x, y, z, 20, 0.3, 0.3, 0.3, 0.05);
    }
}
