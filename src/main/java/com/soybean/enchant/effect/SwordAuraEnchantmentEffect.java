package com.soybean.enchant.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record SwordAuraEnchantmentEffect(EnchantmentLevelBasedValue amount) implements EnchantmentEntityEffect {
    private static final Map<UUID, Integer> playerComboMap = new HashMap<>();
    
    public static final MapCodec<SwordAuraEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnchantmentLevelBasedValue.CODEC.fieldOf("amount").forGetter(SwordAuraEnchantmentEffect::amount)
    ).apply(instance, SwordAuraEnchantmentEffect::new));

    public static void createSwordAura(World world, PlayerEntity player) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        
        // 获取玩家的连击次数
        int combo = playerComboMap.getOrDefault(player.getUuid(), 0);
        
        if (combo == 0) {
            // 第一击：横向月牙
            createHorizontalSlash(serverWorld, player);
        } else {
            // 第二击：十字斩
            createCrossSlash(serverWorld, player);
        }
        
        // 更新连击计数
        playerComboMap.put(player.getUuid(), (combo + 1) % 2);
    }

    private static void createHorizontalSlash(ServerWorld world, PlayerEntity player) {
        Vec3d pos = player.getEyePos();
        Vec3d look = player.getRotationVector();
        double distance = 30.0;
        
        // 计算起始点：在玩家前方2格处
        Vec3d startPos = pos.add(look.multiply(2.0));
        
        // 创建水平直线型剑气
        for (double x = -10.0; x <= 10.0; x += 0.2) { // 横向范围更大，更密集
            // 在玩家视线方向的垂直平面上创建粒子
            Vec3d right = look.crossProduct(new Vec3d(0, 1, 0)).normalize();
            Vec3d particleOffset = right.multiply(x);
            
            // 从起始点开始向前延伸
            for (double d = 0; d < distance; d += 0.3) {
                Vec3d particlePos = startPos.add(
                    look.x * d + particleOffset.x,
                    look.y * d + particleOffset.y,
                    look.z * d + particleOffset.z
                );
                
                spawnParticlesAndDamage(world, player, particlePos);
            }
        }
        
        playSlashEffect(world, player, 1.2f);
    }

    private static void createCrossSlash(ServerWorld world, PlayerEntity player) {
        Vec3d pos = player.getEyePos();
        Vec3d look = player.getRotationVector();
        double distance = 30.0;
        Vec3d startPos = pos.add(look.multiply(2.0));
        
        // 创建水平线
        for (double x = -10.0; x <= 10.0; x += 0.2) {
            Vec3d right = look.crossProduct(new Vec3d(0, 1, 0)).normalize();
            Vec3d particleOffset = right.multiply(x);
            
            for (double d = 0; d < distance; d += 0.3) {
                Vec3d particlePos = startPos.add(
                    look.x * d + particleOffset.x,
                    look.y * d + particleOffset.y,
                    look.z * d + particleOffset.z
                );
                spawnParticlesAndDamage(world, player, particlePos);
            }
        }
        
        // 创建垂直线
        for (double y = -4.0; y <= 4.0; y += 0.2) {
            for (double d = 0; d < distance; d += 0.3) {
                Vec3d particlePos = startPos.add(
                    look.x * d,
                    y,
                    look.z * d
                );
                spawnParticlesAndDamage(world, player, particlePos);
            }
        }
        
        playSlashEffect(world, player, 0.8f);
    }

    private static void spawnParticlesAndDamage(ServerWorld world, PlayerEntity player, Vec3d particlePos) {
        // 生成黑色粒子
        world.spawnParticles(ParticleTypes.SMOKE,
            particlePos.x, particlePos.y, particlePos.z,
            1, 0, 0, 0, 0);
        
        // 检测实体并造成伤害
        Box box = new Box(
            particlePos.x - 1.0, particlePos.y - 1.0, particlePos.z - 1.0,
            particlePos.x + 1.0, particlePos.y + 1.0, particlePos.z + 1.0
        );
        
        List<Entity> entities = world.getEntitiesByClass(Entity.class, box,
            entity -> entity != player && entity instanceof LivingEntity);
        
        for (Entity entity : entities) {
            entity.damage(world.getDamageSources().playerAttack(player), 500.0f);
        }
    }

    private static void playSlashEffect(ServerWorld world, PlayerEntity player, float pitch) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
            SoundCategory.PLAYERS, 1.0f, pitch);
    }

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        // 这个方法可以留空，因为我们主要通过右键事件触发效果
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
} 