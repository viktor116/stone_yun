package com.soybean.items.item;

import com.soybean.enchant.EnchantmentRegister;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

/**
 * 末影弓 - 发射魔法阵风格的橙色和黑色粒子环
 */
public class EndBowItem extends BowItem {

    // 配置常量
    private static final class Config {
        // 粒子类型
        static final SimpleParticleType PRIMARY_PARTICLE = ParticleTypes.FLAME;
        static final SimpleParticleType SECONDARY_PARTICLE = ParticleTypes.SMOKE;
        static final SimpleParticleType ACCENT_PARTICLE = ParticleTypes.SOUL_FIRE_FLAME;
        static final SimpleParticleType MAGIC_PARTICLE = ParticleTypes.ENCHANT;

        // 拉弓效果配置
        static final double BASE_RING_RADIUS = 1.2;
        static final double PROGRESS_RING_RADIUS = 0.8;
        static final double FORWARD_OFFSET = 4.0;
        static final int BASE_PARTICLE_COUNT = 20;
        static final int PROGRESS_PARTICLE_COUNT = 10;

        // 六芒星配置
        static final double HEXAGRAM_SIZE_BASE = 10.0;
        static final double HEXAGRAM_SIZE_PROGRESS = 5.0;
        static final double HEXAGRAM_DISTANCE = 5.0;
        static final double HEXAGRAM_HEIGHT_OFFSET = 3.0;

        // 能量环配置
        static final float RING_VELOCITY = 0.5f;
        static final int RING_MAX_LIFETIME = 800;
        static final float RING_SIZE_BASE = 5.0f;
        static final float RING_SIZE_MULTIPLIER = 0.3f;
        static final int RING_DESTROY_RADIUS = 20;
        static final float RING_DAMAGE = 500.0f;

        // 可调节的距离参数
        static final double MAX_RING_DISTANCE = 200.0; // 能量环最大飞行距离
        static final double DISTANCE_MULTIPLIER = 1.0; // 距离倍增器
    }

    // 状态管理
    private final Map<UUID, List<RingParticle>> activeRings = new HashMap<>();
    private int roundTick = 0;

    // 能量环数据结构
    private static class RingParticle {
        Vec3d position;
        Vec3d velocity;
        Vec3d startPosition;
        int lifetime;
        float strength;
        UUID id;
        float rotationAngle;
        float ringSize;
        int destroyRadius;
        int maxLifetime;
        double maxDistance;
        float initialRingSize; // Store initial ring size for growth calculation
        int initialDestroyRadius; // Store initial destroy radius for growth calculation
        float maxGrowthMultiplier = 3.0f; // Maximum growth multiplier (3x)

        RingParticle(Vec3d pos, Vec3d vel, float strength) {
            this.position = pos;
            this.startPosition = pos;
            this.velocity = vel;
            this.lifetime = 0;
            this.strength = strength;
            this.id = UUID.randomUUID();
            this.rotationAngle = 0;
            this.initialRingSize = Config.RING_SIZE_BASE + strength * Config.RING_SIZE_MULTIPLIER;
            this.ringSize = this.initialRingSize;
            this.initialDestroyRadius = Config.RING_DESTROY_RADIUS;
            this.destroyRadius = this.initialDestroyRadius;
            this.maxLifetime = Config.RING_MAX_LIFETIME;
            this.maxDistance = Config.MAX_RING_DISTANCE * Config.DISTANCE_MULTIPLIER;
        }

        boolean isExpired() {
            return lifetime > maxLifetime || getDistanceTraveled() > maxDistance;
        }

        double getDistanceTraveled() {
            return startPosition.distanceTo(position);
        }
        
        // Update ring size and destroy radius based on distance traveled
        void updateSizeAndRadius() {
            // Calculate growth factor (0.0 to 1.0) based on distance traveled
            double distanceFactor = Math.min(getDistanceTraveled() / (maxDistance * 0.7), 1.0);
            
            // Apply growth curve (starts slower, accelerates in the middle, then slows down)
            double growthFactor = 1.0 + (maxGrowthMultiplier - 1.0) * 
                                  (Math.sin((distanceFactor - 0.5) * Math.PI) + 1.0) / 2.0;
            
            // Update ring size and destroy radius
            ringSize = (float)(initialRingSize * growthFactor);
            destroyRadius = (int)(initialDestroyRadius * growthFactor);
        }
    }

    public EndBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient() && entity instanceof PlayerEntity player) {
            updateActiveRings(player, world);
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (world.isClient && user instanceof PlayerEntity player) {
            roundTick++;

            float progress = calculateProgress(stack, user, remainingUseTicks);
            DrawingContext context = new DrawingContext(player, progress);

            // 绘制充能效果
            drawChargingEffects(world, context);

            // 绘制背景六芒星
            if (progress > 0.2f) {
                drawBackgroundHexagram(world, context);
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player) || world.isClient) {
            return;
        }

        roundTick = 0;
        float progress = BowItem.getPullProgress(this.getMaxUseTime(stack, user) - remainingUseTicks);

        if (progress >= 0.1F && consumeArrow(player, stack)) {
            launchEnergyRing(world, player, progress);
            playLaunchSound(world, player);
            stack.damage(1, player, EquipmentSlot.MAINHAND);
        }
    }

    // ========== 充能效果绘制 ==========

    private static class DrawingContext {
        final PlayerEntity player;
        final Vec3d playerPos;
        final Vec3d lookDir;
        final Vec3d rightVec;
        final Vec3d upVec;
        final float progress;

        DrawingContext(PlayerEntity player, float progress) {
            this.player = player;
            this.playerPos = player.getEyePos();
            this.lookDir = player.getRotationVector();
            this.progress = progress;

            // 计算垂直向量
            if (Math.abs(lookDir.y) > 0.99) {
                this.rightVec = new Vec3d(1, 0, 0);
            } else {
                this.rightVec = lookDir.crossProduct(new Vec3d(0, 1, 0)).normalize();
            }
            this.upVec = rightVec.crossProduct(lookDir).normalize();
        }
    }

    private void drawChargingEffects(World world, DrawingContext ctx) {
        // 绘制多层充能环
        int[] ringOffsets = {0, 8, 10, 12};
        double[] radiusMultipliers = {1.0, 1.5, 2.0, 2.5};

        for (int i = 0; i < ringOffsets.length; i++) {
            if (roundTick >= ringOffsets[i] * 20) {
                double distance = Config.FORWARD_OFFSET + ringOffsets[i];
                Vec3d basePos = ctx.playerPos.add(ctx.lookDir.multiply(distance));
                double radius = Config.BASE_RING_RADIUS + ctx.progress * Config.PROGRESS_RING_RADIUS;
                radius *= radiusMultipliers[i];

                int particleCount = Config.BASE_PARTICLE_COUNT + (int)(ctx.progress * Config.PROGRESS_PARTICLE_COUNT);
                particleCount *= (1 + i);

                drawParticleRing(world, basePos, radius, ctx.rightVec, ctx.upVec,
                        particleCount, Config.PRIMARY_PARTICLE);
                drawParticleRing(world, basePos, radius, ctx.rightVec, ctx.upVec,
                        particleCount / 3, Config.SECONDARY_PARTICLE);
                drawMagicRunes(world, basePos, radius, ctx.lookDir, ctx.progress);
            }
        }
    }

    private void drawBackgroundHexagram(World world, DrawingContext ctx) {
        Vec3d hexagramPos = ctx.playerPos
                .subtract(ctx.lookDir.multiply(Config.HEXAGRAM_DISTANCE))
                .add(0, Config.HEXAGRAM_HEIGHT_OFFSET, 0);

        double size = Config.HEXAGRAM_SIZE_BASE + ctx.progress * Config.HEXAGRAM_SIZE_PROGRESS;
        float rotation = roundTick * 0.005f;

        drawComplexHexagram(world, hexagramPos, size, ctx.rightVec, ctx.upVec, rotation);
        drawConnectionLines(world, ctx.playerPos, hexagramPos, ctx.rightVec);
    }

    // ========== 粒子效果绘制 ==========

    private void drawParticleRing(World world, Vec3d center, double radius, Vec3d rightVec, Vec3d upVec,
                                  int particleCount, SimpleParticleType particleType) {
        for (int i = 0; i < particleCount; i++) {
            double angle = i * (Math.PI * 2 / particleCount);
            Vec3d pos = center.add(
                    rightVec.multiply(Math.cos(angle) * radius).add(
                            upVec.multiply(Math.sin(angle) * radius)
                    )
            );

            world.addParticle(particleType, pos.x, pos.y, pos.z, 0, 0, 0);

            // 添加扩散效果
            if (world.random.nextFloat() < 0.3) {
                Vec3d spreadDir = pos.subtract(center).normalize();
                double spreadSpeed = 0.15 + world.random.nextDouble() * 0.1;
                world.addParticle(
                        particleType == Config.PRIMARY_PARTICLE ? Config.SECONDARY_PARTICLE : Config.PRIMARY_PARTICLE,
                        pos.x, pos.y, pos.z,
                        spreadDir.x * spreadSpeed, spreadDir.y * spreadSpeed, spreadDir.z * spreadSpeed
                );
            }
        }
    }

    private void drawMagicRunes(World world, Vec3d center, double radius, Vec3d direction, float progress) {
        Vec3d rightVec = getRightVector(direction);
        Vec3d upVec = rightVec.crossProduct(direction).normalize();

        drawHexagram(world, center, radius * 0.8, rightVec, upVec, roundTick * 0.01f);

        // 添加魔法符文
        int runeCount = (int)(8 * progress);
        for (int i = 0; i < runeCount; i++) {
            double angle = world.random.nextDouble() * Math.PI * 2;
            double distance = radius * 0.7 + world.random.nextDouble() * radius * 0.3;

            Vec3d runePos = center.add(
                    rightVec.multiply(Math.cos(angle) * distance).add(
                            upVec.multiply(Math.sin(angle) * distance)
                    )
            );

            world.addParticle(Config.MAGIC_PARTICLE, runePos.x, runePos.y, runePos.z, 0, 0.05, 0);
        }
    }

    private void drawHexagram(World world, Vec3d center, double size, Vec3d rightVec, Vec3d upVec, float rotation) {
        // 正三角形
        drawTriangle(world, center, size, rightVec, upVec, rotation, Config.PRIMARY_PARTICLE);
        // 倒三角形
        drawTriangle(world, center, size, rightVec, upVec, rotation + (float)Math.PI, Config.PRIMARY_PARTICLE);
        // 六边形
        drawPolygon(world, center, size * 0.9, rightVec, upVec, -rotation * 0.5f, 6, Config.PRIMARY_PARTICLE);
        // 内圆
        drawCircle(world, center, size * 0.45, rightVec, upVec, rotation, Config.ACCENT_PARTICLE);
    }

    private void drawComplexHexagram(World world, Vec3d center, double size, Vec3d rightVec, Vec3d upVec, float rotation) {
        drawHexagram(world, center, size, rightVec, upVec, rotation);

        // 外环
        drawCircle(world, center, size * 1.3, rightVec, upVec, rotation * 0.7f, Config.PRIMARY_PARTICLE);
        drawCircle(world, center, size * 1.6, rightVec, upVec, -rotation * 0.5f, Config.SECONDARY_PARTICLE);
        drawCircle(world, center, size * 1.8, rightVec, upVec, rotation * 0.8f, Config.PRIMARY_PARTICLE);

        // 粒子环
        drawParticleRing(world, center, size * 1.1, rightVec, upVec, 80, Config.PRIMARY_PARTICLE);
        drawParticleRing(world, center, size * 1.2, rightVec, upVec, 40, Config.SECONDARY_PARTICLE);
    }

    private void drawConnectionLines(World world, Vec3d start, Vec3d end, Vec3d rightVec) {
        int segments = 30;
        for (int i = 0; i <= segments; i++) {
            double t = i / (double)segments;
            Vec3d pos = start.lerp(end, t);

            // 添加波浪效果
            double offset = 0.4 * Math.sin(t * Math.PI * 4 + roundTick * 0.1);
            pos = pos.add(rightVec.multiply(offset));

            world.addParticle(Config.MAGIC_PARTICLE, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }

    // ========== 几何图形绘制 ==========

    private void drawTriangle(World world, Vec3d center, double size, Vec3d rightVec, Vec3d upVec,
                              float rotation, SimpleParticleType particleType) {
        drawPolygon(world, center, size, rightVec, upVec, rotation, 3, particleType);
    }

    private void drawPolygon(World world, Vec3d center, double size, Vec3d rightVec, Vec3d upVec,
                             float rotation, int sides, SimpleParticleType particleType) {
        int particlesPerSide = 8;

        for (int i = 0; i < sides; i++) {
            double angle1 = i * (2 * Math.PI / sides) + rotation;
            double angle2 = ((i + 1) % sides) * (2 * Math.PI / sides) + rotation;

            Vec3d point1 = center.add(
                    rightVec.multiply(Math.cos(angle1) * size).add(
                            upVec.multiply(Math.sin(angle1) * size)
                    )
            );

            Vec3d point2 = center.add(
                    rightVec.multiply(Math.cos(angle2) * size).add(
                            upVec.multiply(Math.sin(angle2) * size)
                    )
            );

            for (int j = 0; j <= particlesPerSide; j++) {
                double t = j / (double)particlesPerSide;
                Vec3d pos = point1.lerp(point2, t);
                world.addParticle(particleType, pos.x, pos.y, pos.z, 0, 0, 0);
            }
        }
    }

    private void drawCircle(World world, Vec3d center, double radius, Vec3d rightVec, Vec3d upVec,
                            float rotation, SimpleParticleType particleType) {
        int particleCount = 24;

        for (int i = 0; i < particleCount; i++) {
            double angle = i * (Math.PI * 2 / particleCount) + rotation;
            Vec3d pos = center.add(
                    rightVec.multiply(Math.cos(angle) * radius).add(
                            upVec.multiply(Math.sin(angle) * radius)
                    )
            );
            world.addParticle(particleType, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }

    // ========== 能量环管理 ==========

    private void updateActiveRings(PlayerEntity player, World world) {
        UUID playerId = player.getUuid();
        List<RingParticle> rings = activeRings.get(playerId);

        if (rings == null || rings.isEmpty()) {
            return;
        }

        rings.removeIf(ring -> {
            updateRingPosition(ring);
            createRingEffects(world, ring);
            handleRingCollisions(world, ring, player);
            return ring.isExpired();
        });

        if (rings.isEmpty()) {
            activeRings.remove(playerId);
        }
    }

    private void updateRingPosition(RingParticle ring) {
        ring.position = ring.position.add(ring.velocity);
        ring.lifetime++;
        ring.rotationAngle += 0.02f;

        if (ring.rotationAngle > Math.PI * 2) {
            ring.rotationAngle -= Math.PI * 2;
        }
        
        // Update ring size and destroy radius as it travels
        ring.updateSizeAndRadius();
    }

    private void createRingEffects(World world, RingParticle ring) {
        Vec3d direction = ring.velocity.normalize();
        Vec3d rightVec = getRightVector(direction);
        Vec3d upVec = rightVec.crossProduct(direction).normalize();

        // 定义各环的半径
        double innerRadius = ring.ringSize * 0.7;
        double mainRadius = ring.ringSize;
        double outerRadius1 = ring.ringSize * 1.3;
        double outerRadius2 = ring.ringSize * 1.6;
        double orbitRadius = ring.ringSize * 1.8; // 小圆轨道半径

        // 主环
        drawRingParticles(world, ring.position, mainRadius, rightVec, upVec, ring.rotationAngle, 120);

        // 内环 - 使用灵魂火焰粒子
        drawRingParticles(world, ring.position, innerRadius, rightVec, upVec, -ring.rotationAngle, 80, Config.ACCENT_PARTICLE);

        // 外环
        drawRingParticles(world, ring.position, outerRadius1, rightVec, upVec, ring.rotationAngle * 0.7f, 150);
        drawRingParticles(world, ring.position, outerRadius2, rightVec, upVec, -ring.rotationAngle * 0.5f, 150);

        // 连接线 - 16条连接各环的线
        drawConnectionLines(world, ring, innerRadius, mainRadius, outerRadius1, outerRadius2, rightVec, upVec);
        
        // 16个旋转的小圆
        drawOrbitingCircles(world, ring, orbitRadius, rightVec, upVec);

        // 六芒星
        drawHexagram(world, ring.position, ring.ringSize * 0.5, rightVec, upVec, ring.rotationAngle);

        // 摧毁方块
        destroyBlocksInRadius(world, ring.position, ring.destroyRadius);

        // 扩散效果
        createScatterEffect(world, ring);
    }

    private void drawRingParticles(World world, Vec3d center, double radius, Vec3d rightVec, Vec3d upVec,
                                   float rotation, int particleCount) {
        drawRingParticles(world, center, radius, rightVec, upVec, rotation, particleCount, null);
    }
    
    private void drawRingParticles(World world, Vec3d center, double radius, Vec3d rightVec, Vec3d upVec,
                                   float rotation, int particleCount, SimpleParticleType forcedParticleType) {
        for (int i = 0; i < particleCount; i++) {
            double angle = i * (Math.PI * 2 / particleCount) + rotation;
            Vec3d pos = center.add(
                    rightVec.multiply(Math.cos(angle) * radius).add(
                            upVec.multiply(Math.sin(angle) * radius)
                    )
            );

            destroyBlocksInRadius(world, pos, 2);

            SimpleParticleType particleType;
            if (forcedParticleType != null) {
                particleType = forcedParticleType;
            } else {
                particleType = (i % 2 == 0) ? Config.PRIMARY_PARTICLE : Config.SECONDARY_PARTICLE;
            }
            ((ServerWorld)world).spawnParticles(particleType, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        }
    }

    private void createScatterEffect(World world, RingParticle ring) {
        for (int i = 0; i < 15; i++) {
            Vec3d randomPos = ring.position.add(
                    world.random.nextGaussian() * ring.ringSize * 0.8,
                    world.random.nextGaussian() * ring.ringSize * 0.8,
                    world.random.nextGaussian() * ring.ringSize * 0.8
            );

            destroyBlocksInRadius(world, randomPos, 1);

            SimpleParticleType particleType = world.random.nextBoolean() ?
                    Config.PRIMARY_PARTICLE : Config.SECONDARY_PARTICLE;
            ((ServerWorld)world).spawnParticles(
                    particleType, randomPos.x, randomPos.y, randomPos.z, 3, 0.5, 0.5, 0.5, 0.05
            );
        }
    }

    private void handleRingCollisions(World world, RingParticle ring, PlayerEntity player) {
        Box hitbox = Box.from(ring.position).expand(5.0);
        List<Entity> entities = world.getEntitiesByClass(
                Entity.class, hitbox,
                entity -> entity != player && (entity instanceof LivingEntity || entity instanceof EnderDragonPart)
        );

        for (Entity entity : entities) {
            float damage = Config.RING_DAMAGE * ring.strength;

            if (entity instanceof EnderDragonPart dragonPart) {
                dragonPart.damage(world.getDamageSources().playerAttack(player), damage);
            } else if (entity instanceof EnderDragonEntity) {
                entity.damage(world.getDamageSources().playerAttack(player), damage);
            } else if (entity instanceof LivingEntity livingEntity) {
                livingEntity.damage(world.getDamageSources().magic(), damage);
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60));
            }
        }
    }

    // ========== 方块摧毁 ==========

    private void destroyBlocksInRadius(World world, Vec3d center, int radius) {
        if (world.isClient) return;

        BlockPos centerPos = BlockPos.ofFloored(center);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = centerPos.add(x, y, z);
                    double distance = Math.sqrt(x*x + y*y + z*z);

                    if (distance <= radius) {
                        destroyBlock(world, pos);
                    }
                }
            }
        }
    }

    private void destroyBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!state.isAir() && state.getHardness(world, pos) >= 0) {
            world.breakBlock(pos, false);
        }
    }

    // ========== 辅助方法 ==========

    private float calculateProgress(ItemStack stack, LivingEntity user, int remainingUseTicks) {
        float progress = (getMaxUseTime(stack, user) - remainingUseTicks) / 20.0F;
        progress = (progress * progress + progress * 2.0F) / 3.0F;
        return Math.min(progress, 1.0F);
    }

    private Vec3d getRightVector(Vec3d direction) {
        if (Math.abs(direction.y) > 0.99) {
            return new Vec3d(1, 0, 0);
        }
        return direction.crossProduct(new Vec3d(0, 1, 0)).normalize();
    }

    private boolean consumeArrow(PlayerEntity player, ItemStack stack) {
        boolean hasInfinite = player.getAbilities().creativeMode ||
                EnchantmentRegister.getEnchantmentLevel(player.getWorld(), Enchantments.INFINITY, stack) > 0;
        ItemStack arrowStack = player.getProjectileType(stack);

        if (arrowStack.isEmpty() && !hasInfinite) {
            return false;
        }

        if (arrowStack.isEmpty()) {
            arrowStack = new ItemStack(Items.ARROW);
        }

        if (!hasInfinite) {
            arrowStack.decrement(1);
            if (arrowStack.isEmpty()) {
                player.getInventory().removeOne(arrowStack);
            }
        }

        return true;
    }

    private void launchEnergyRing(World world, PlayerEntity player, float progress) {
        float strength = progress * 2.0F;
        Vec3d rotation = player.getRotationVector();
        Vec3d startPos = player.getEyePos();
        Vec3d velocity = rotation.multiply(Config.RING_VELOCITY);

        RingParticle ring = new RingParticle(startPos, velocity, strength);

        UUID playerId = player.getUuid();
        activeRings.computeIfAbsent(playerId, k -> new ArrayList<>()).add(ring);
    }

    private void playLaunchSound(World world, PlayerEntity player) {
        world.playSound(
                null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT, SoundCategory.PLAYERS,
                1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F)
        );
    }

    // ========== 距离控制接口 ==========

    /**
     * 设置能量环的最大飞行距离
     * @param distance 最大距离
     */
    public static void setMaxRingDistance(double distance) {
        // 可以通过配置文件或其他方式动态修改
        // 这里提供一个接口供外部调用
    }

    /**
     * 获取当前能量环的最大飞行距离
     * @return 当前最大距离
     */
    public static double getMaxRingDistance() {
        return Config.MAX_RING_DISTANCE * Config.DISTANCE_MULTIPLIER;
    }

    /**
     * 绘制连接各环的线条
     */
    private void drawConnectionLines(World world, RingParticle ring, double innerRadius, double mainRadius, 
                                    double outerRadius1, double outerRadius2, Vec3d rightVec, Vec3d upVec) {
        // 创建16条均匀分布的连接线
        int lineCount = 16;
        
        for (int i = 0; i < lineCount; i++) {
            double angle = i * (Math.PI * 2 / lineCount) + ring.rotationAngle;
            
            // 计算各环上的点
            Vec3d innerPoint = getPointOnRing(ring.position, innerRadius, angle, rightVec, upVec);
            Vec3d mainPoint = getPointOnRing(ring.position, mainRadius, angle, rightVec, upVec);
            Vec3d outerPoint1 = getPointOnRing(ring.position, outerRadius1, angle, rightVec, upVec);
            Vec3d outerPoint2 = getPointOnRing(ring.position, outerRadius2, angle, rightVec, upVec);
            
            // 绘制内环到主环的连接线
            drawParticleLine(world, innerPoint, mainPoint, 8, i % 2 == 0 ? Config.ACCENT_PARTICLE : Config.PRIMARY_PARTICLE);
            
            // 绘制主环到外环1的连接线
            drawParticleLine(world, mainPoint, outerPoint1, 8, i % 2 == 0 ? Config.PRIMARY_PARTICLE : Config.SECONDARY_PARTICLE);
            
            // 绘制外环1到外环2的连接线
            drawParticleLine(world, outerPoint1, outerPoint2, 8, i % 2 == 0 ? Config.SECONDARY_PARTICLE : Config.PRIMARY_PARTICLE);
        }
    }
    
    /**
     * 获取环上的点
     */
    private Vec3d getPointOnRing(Vec3d center, double radius, double angle, Vec3d rightVec, Vec3d upVec) {
        return center.add(
            rightVec.multiply(Math.cos(angle) * radius).add(
                upVec.multiply(Math.sin(angle) * radius)
            )
        );
    }
    
    /**
     * 绘制粒子线
     */
    private void drawParticleLine(World world, Vec3d start, Vec3d end, int particleCount, SimpleParticleType particleType) {
        for (int i = 0; i <= particleCount; i++) {
            double t = i / (double)particleCount;
            Vec3d pos = start.lerp(end, t);
            
            ((ServerWorld)world).spawnParticles(particleType, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
            
            // 随机摧毁方块
            if (world.random.nextFloat() < 0.05) {
                destroyBlocksInRadius(world, pos, 1);
            }
        }
    }

    /**
     * 绘制围绕主环旋转的16个小圆
     */
    private void drawOrbitingCircles(World world, RingParticle ring, double orbitRadius, Vec3d rightVec, Vec3d upVec) {
        int circleCount = 16;
        double smallCircleRadius = ring.ringSize * 0.25; // 小圆的半径
        
        // 计算旋转角度 - 根据lifetime增加旋转
        float orbitAngle = ring.rotationAngle + (ring.lifetime * 0.05f);
        
        for (int i = 0; i < circleCount; i++) {
            // 计算小圆在轨道上的位置
            double angle = i * (Math.PI * 2 / circleCount) + orbitAngle;
            
            // 小圆的中心位置
            Vec3d circleCenter = getPointOnRing(ring.position, orbitRadius, angle, rightVec, upVec);
            
            // 小圆自身的旋转角度 - 每个小圆有不同的旋转速度
            float circleRotation = ring.rotationAngle * (1.0f + i * 0.1f);
            
            // 绘制小圆
            drawSmallCircle(world, circleCenter, smallCircleRadius, rightVec, upVec, circleRotation, i);
            
            // 随机摧毁方块
            destroyBlocksInRadius(world, circleCenter, 1);
        }
    }
    
    /**
     * 绘制单个小圆
     */
    private void drawSmallCircle(World world, Vec3d center, double radius, Vec3d rightVec, Vec3d upVec, 
                               float rotation, int circleIndex) {
        int particleCount = 12; // 每个小圆的粒子数量
        
        // 根据索引选择不同的粒子类型
        SimpleParticleType particleType;
        if (circleIndex % 3 == 0) {
            particleType = Config.ACCENT_PARTICLE; // 每3个圆使用一次灵魂火焰
        } else {
            particleType = (circleIndex % 2 == 0) ? Config.PRIMARY_PARTICLE : Config.SECONDARY_PARTICLE;
        }
        
        // 绘制小圆
        for (int i = 0; i < particleCount; i++) {
            double angle = i * (Math.PI * 2 / particleCount) + rotation;
            Vec3d pos = center.add(
                rightVec.multiply(Math.cos(angle) * radius).add(
                    upVec.multiply(Math.sin(angle) * radius)
                )
            );
            
            ((ServerWorld)world).spawnParticles(particleType, pos.x, pos.y, pos.z, 1, 0, 0, 0, 0);
        }
    }
}
