package com.soybean.items.item;

import net.minecraft.block.AbstractBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

// WitherSkullWandItem.java —— 普通凋零之首
// WitherSkullWandItem.java —— 普通凋零之首（高伤害+大爆炸+黑色粒子）
public class WitherSkullWandItem extends Item {

    public WitherSkullWandItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!world.isClient) {
            Vec3d look = user.getRotationVec(1.0F);

            // 发射时粒子特效（黑烟爆发）
            spawnLaunchParticles(world, user, look, false);

            // 生成凋零之首实体
            WitherSkullEntity skull = new WitherSkullEntity(
                    EntityType.WITHER_SKULL, world);
            skull.setOwner(user);
            skull.setPosition(
                    user.getX() + look.x * 1.5,
                    user.getEyeY() - 0.1,
                    user.getZ() + look.z * 1.5
            );
            // 设置速度（数值越大飞行越快）
            skull.addVelocity(look.x * 2.0, look.y * 2.0, look.z * 2.0);
            world.spawnEntity(skull);

            world.playSound(null, user.getBlockPos(),
                    SoundEvents.ENTITY_WITHER_SHOOT,
                    SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
        return TypedActionResult.success(stack);
    }

    private void spawnLaunchParticles(World world, PlayerEntity user, Vec3d look, boolean charged) {
        ServerWorld serverWorld = (ServerWorld) world;
        double x = user.getX() + look.x * 1.5;
        double y = user.getEyeY() - 0.1;
        double z = user.getZ() + look.z * 1.5;

        // 黑色烟雾爆发
        serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE,
                x, y, z, 20, 0.3, 0.3, 0.3, 0.05);
        // 灵魂粒子
        serverWorld.spawnParticles(ParticleTypes.SOUL,
                x, y, z, 10, 0.2, 0.2, 0.2, 0.02);
        // 黑色传送门粒子
        serverWorld.spawnParticles(ParticleTypes.PORTAL,
                x, y, z, 30, 0.3, 0.3, 0.3, 0.5);
    }
}
