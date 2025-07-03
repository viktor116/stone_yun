package com.soybean.items.custom;

import com.soybean.utils.ServerEachTickTaskManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 梦魇药水物品，食用后会在玩家视野前方产生从中心向外扩散的黑色粒子效果
 */
public class NightmareEffectItem extends Item {
    
    private static final int EFFECT_DURATION = 300; // 效果持续15秒
    
    public NightmareEffectItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            // 播放音效
            world.playSound(null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            
            // 启动粒子效果任务
            startNightmareParticleEffect(player);
        }
        
        return super.finishUsing(stack, world, user);
    }
    
    private void startNightmareParticleEffect(PlayerEntity player) {
        // 获取玩家所在的服务器世界
        ServerWorld serverWorld = (ServerWorld) player.getWorld();
        
        // 创建一个计数器，用于追踪效果已经持续了多久
        final int[] tickCounter = {0};
        
        // 判断任务是否已完成的标志
        final AtomicBoolean taskCompleted = new AtomicBoolean(false);
        
        // 使用ServerEachTickTaskManager来调度任务
        ServerEachTickTaskManager.scheduleTask(EFFECT_DURATION, 1, () -> {
            if (taskCompleted.get() || !player.isAlive() || player.isRemoved()) {
                // 如果玩家不存在了，标记任务为完成
                taskCompleted.set(true);
                return;
            }
            
            // 增加计数器
            tickCounter[0]++;
            
            // 根据时间计算粒子密度和扩散半径
            float progress = Math.min(1.0f, (float) tickCounter[0] / EFFECT_DURATION);
            
            // 初期、中期和后期的不同半径
            float maxRadius;
            if (progress < 0.3) {
                // 初期半径
                maxRadius = 8.0f * progress;
            } else if (progress < 0.7) {
                // 中期半径更大
                maxRadius = 8.0f * 0.3f + (16.0f - 8.0f * 0.3f) * ((progress - 0.3f) / 0.4f);
            } else {
                // 后期半径最大
                maxRadius = 16.0f + (25.0f - 16.0f) * ((progress - 0.7f) / 0.3f);
            }
            
            // 粒子数量也随着进度增加
            int baseParticleCount = (int) (150 * progress);
            
            // 获取玩家视线方向的向量
            Vec3d lookVec = player.getRotationVector();
            
            // 计算玩家面前的中心点位置（距离玩家3格远）
            Vec3d centerPos = player.getEyePos().add(lookVec.multiply(3));
            
            // 从中心点向外扩散粒子
            for (int i = 0; i < baseParticleCount; i++) {
                // 随机生成一个方向向量
                double theta = player.getRandom().nextDouble() * Math.PI * 2; // 水平角度
                double phi = player.getRandom().nextDouble() * Math.PI; // 垂直角度
                
                // 将球坐标转换为笛卡尔坐标
                double x = Math.sin(phi) * Math.cos(theta);
                double y = Math.sin(phi) * Math.sin(theta);
                double z = Math.cos(phi);
                
                // 随机半径，越靠近中心点密度越大
                double radiusFactor = Math.pow(player.getRandom().nextDouble(), 1.2); // 更平均的分布
                double radius = maxRadius * radiusFactor;
                
                // 计算粒子的最终位置
                double particleX = centerPos.x + x * radius;
                double particleY = centerPos.y + y * radius;
                double particleZ = centerPos.z + z * radius;
                
                // 根据进度选择不同比例的粒子类型
                boolean useSmoke = progress < 0.3 ? true : // 初期全部使用SMOKE
                                   progress < 0.7 ? player.getRandom().nextFloat() < 0.8 : // 中期80%是SMOKE
                                   player.getRandom().nextFloat() < 0.6; // 后期60%是SMOKE
                
                // 生成粒子
                serverWorld.spawnParticles(
                        useSmoke ? ParticleTypes.SMOKE : ParticleTypes.ASH,
                        particleX, particleY, particleZ,
                        1, // 粒子数量
                        0.05, 0.05, 0.05, // 小偏移，使分布更自然
                        0.002 // 低速度，让粒子停留更久
                );
            }
            
            // 在中后期，增加额外的密集粒子区域
            if (progress > 0.4) {
                // 额外的密集粒子
                int extraParticles = (int)(100 * (progress - 0.4) / 0.6); // 最多100个额外粒子
                float innerRadius = maxRadius * 0.3f; // 内部区域半径
                
                for (int i = 0; i < extraParticles; i++) {
                    // 随机生成一个方向向量（球坐标）
                    double theta = player.getRandom().nextDouble() * Math.PI * 2;
                    double phi = player.getRandom().nextDouble() * Math.PI;
                    
                    // 将球坐标转换为笛卡尔坐标
                    double x = Math.sin(phi) * Math.cos(theta);
                    double y = Math.sin(phi) * Math.sin(theta);
                    double z = Math.cos(phi);
                    
                    // 在内部区域生成更密集的粒子
                    double radius = innerRadius * player.getRandom().nextDouble();
                    
                    // 计算粒子位置
                    double particleX = centerPos.x + x * radius;
                    double particleY = centerPos.y + y * radius;
                    double particleZ = centerPos.z + z * radius;
                    
                    // 在内部区域主要使用LARGE_SMOKE粒子
                    serverWorld.spawnParticles(
                            player.getRandom().nextFloat() < 0.85 ? ParticleTypes.LARGE_SMOKE : ParticleTypes.SMOKE,
                            particleX, particleY, particleZ,
                            1,
                            0.03, 0.03, 0.03,
                            0.001
                    );
                }
            }
            
            // 在后期，在扩散圆周边缘添加更多粒子，形成梦魇边界
            if (progress > 0.6) {
                int borderParticles = (int)(80 * (progress - 0.6) / 0.4); // 增加边界粒子数量到80个
                for (int i = 0; i < borderParticles; i++) {
                    double angle = Math.PI * 2 * i / borderParticles;
                    
                    // 在圆周上均匀分布粒子
                    for (int layer = 0; layer < 8; layer++) { // 增加到8层
                        double layerRadius = maxRadius - layer * 0.3;
                        double offsetX = Math.cos(angle) * layerRadius;
                        double offsetY = Math.sin(angle) * layerRadius;
                        
                        // 交替使用两种粒子
                        serverWorld.spawnParticles(
                                layer % 2 == 0 ? ParticleTypes.SMOKE : ParticleTypes.ASH,
                                centerPos.x + offsetX, centerPos.y + offsetY, centerPos.z,
                                1, 
                                0.1, 0.1, 0.1, // 小偏移，使边界更自然
                                0.001 // 几乎静止
                        );
                    }
                }
            }
            
            // 最后阶段，添加一些向外飞溅的粒子，增强动态效果
            if (progress > 0.85) {
                int splashParticles = (int)(50 * (progress - 0.85) / 0.15);
                
                for (int i = 0; i < splashParticles; i++) {
                    // 从中心点向外飞溅
                    double angle = player.getRandom().nextDouble() * Math.PI * 2;
                    double upwardAngle = player.getRandom().nextDouble() * Math.PI / 2; // 上半球
                    
                    double speedFactor = 0.2 + player.getRandom().nextDouble() * 0.4;
                    double x = Math.cos(angle) * Math.cos(upwardAngle) * speedFactor;
                    double y = Math.sin(upwardAngle) * speedFactor;
                    double z = Math.sin(angle) * Math.cos(upwardAngle) * speedFactor;
                    
                    // 随机选择粒子类型
                    serverWorld.spawnParticles(
                            player.getRandom().nextFloat() < 0.7 ? ParticleTypes.LARGE_SMOKE : ParticleTypes.ASH,
                            centerPos.x, centerPos.y, centerPos.z,
                            0, // 设置为0，使用速度来控制粒子移动
                            x, y, z, // 速度向量
                            0.3 // 速度乘数
                    );
                }
            }
        });
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return super.use(world, user, hand);
    }
} 