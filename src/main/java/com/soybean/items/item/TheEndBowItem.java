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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/1/13 17:22
 * @description
 */
public class TheEndBowItem extends BowItem {
    public TheEndBowItem(Settings settings) {
        super(settings);
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

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }

        float progress = BowItem.getPullProgress(this.getMaxUseTime(stack,user) - remainingUseTicks);

        if (!world.isClient && progress >= 0.1F) {
            boolean hasInfinite = playerEntity.getAbilities().creativeMode ||
                    EnchantmentRegister.getEnchantmentLevel(user.getWorld(),Enchantments.INFINITY,stack) > 0;
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

                // 发射守卫者光束
                float strength = progress * 2.0F;
                Vec3d rotation = playerEntity.getRotationVector();

                // 在玩家视线方向创建光束粒子效果
                Vec3d startPos = playerEntity.getEyePos();
                Vec3d endPos = startPos.add(rotation.multiply(64.0)); // 64格的射程

                // 在服务端处理伤害
                Box box = new Box(startPos.x, startPos.y, startPos.z, endPos.x, endPos.y, endPos.z)
                        .expand(1.0); // 扩大碰撞箱以便更容易命中

                for (Entity entity : world.getOtherEntities(playerEntity, box)) {
                    if (entity instanceof LivingEntity target) {
                        float damage = 8.0F * strength; // 基础伤害值
                        target.damage(world.getDamageSources().magic(), damage);

                        // 给予发光效果
                        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60));
                    }
                }

                // 播放音效
                world.playSound(
                        null,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundEvents.ENTITY_GUARDIAN_ATTACK,
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
