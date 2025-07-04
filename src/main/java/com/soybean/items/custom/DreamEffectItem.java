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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 梦幻药水物品，食用后会在玩家周围5格内产生从中心向外扩散的白色粒子效果
 */
public class DreamEffectItem extends Item {
    
    private static final int EFFECT_DURATION = 300; // 效果持续15秒
    private static final float EFFECT_RADIUS = 5.0f; // 粒子效果半径为5格
    
    public DreamEffectItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            // 播放音效
            world.playSound(null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.PLAYERS, 1.0F, 1.0F);
            
            // 启动粒子效果任务
            startDreamParticleEffect(player);
        }
        
        return super.finishUsing(stack, world, user);
    }
    
    private void startDreamParticleEffect(PlayerEntity player) {
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
                maxRadius = EFFECT_RADIUS * progress;
            } else if (progress < 0.7) {
                // 中期半径更大
                maxRadius = EFFECT_RADIUS * 0.3f + (EFFECT_RADIUS * 2 - EFFECT_RADIUS * 0.3f) * ((progress - 0.3f) / 0.4f);
            } else {
                // 后期半径最大
                maxRadius = EFFECT_RADIUS * 2 + (EFFECT_RADIUS * 3 - EFFECT_RADIUS * 2) * ((progress - 0.7f) / 0.3f);
            }
            
            // 粒子数量也随着进度增加
            int baseParticleCount = (int) (400 * progress);
            
            // 使用玩家位置作为中心点
            Vec3d centerPos = player.getPos().add(0, 1.0, 0); // 在玩家腰部位置
            
            // 从中心点向外扩散粒子（360度全方位）
            for (int i = 0; i < baseParticleCount; i++) {
                // 随机生成一个方向向量（球坐标）
                double theta = player.getRandom().nextDouble() * Math.PI * 2; // 水平角度
                double phi = player.getRandom().nextDouble() * Math.PI; // 垂直角度
                
                // 将球坐标转换为笛卡尔坐标
                double x = Math.sin(phi) * Math.cos(theta);
                double y = Math.sin(phi) * Math.sin(theta);
                double z = Math.cos(phi);
                
                // 随机确定半径（从0到最大半径）
                double radius = player.getRandom().nextDouble() * maxRadius;
                
                // 计算粒子位置
                double particleX = centerPos.x + x * radius;
                double particleY = centerPos.y + y * radius;
                double particleZ = centerPos.z + z * radius;
                
                // 根据距离中心的远近选择不同的粒子
                float distanceRatio = (float)(radius / maxRadius);
                
                if (distanceRatio < 0.3) {
                    // 内部区域使用更亮的粒子
                    serverWorld.spawnParticles(
                            ParticleTypes.END_ROD,
                            particleX, particleY, particleZ,
                            1,
                            0.02, 0.02, 0.02,
                            0.001
                    );
                } else if (distanceRatio < 0.7) {
                    // 中间区域混合使用两种粒子
                    serverWorld.spawnParticles(
                            ParticleTypes.END_ROD ,
                            particleX, particleY, particleZ,
                            1,
                            0.03, 0.03, 0.03,
                            0.001
                    );
                } else {
                    // 外部区域主要使用雪花粒子
                    serverWorld.spawnParticles(
                            ParticleTypes.END_ROD,
                            particleX, particleY, particleZ,
                            1,
                            0.04, 0.04, 0.04,
                            0.001
                    );
                }
            }
            
            if (progress > 0.4) {
                // 额外的密集粒子
                int extraParticles = (int)(300 * (progress - 0.4) / 0.6); // 最多120个额外粒子
                float innerRadius = maxRadius * 0.4f; // 内部区域半径
                
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
                    
                    // 在内部区域主要使用END_ROD粒子
                    serverWorld.spawnParticles(
                            ParticleTypes.END_ROD,
                            particleX, particleY, particleZ,
                            1,
                            0.03, 0.03, 0.03,
                            0.001
                    );
                }
            }
            
            // 在后期，在扩散圆周边缘添加更多粒子，形成梦幻边界
            if (progress > 0.6) {
                int borderParticles = (int)(300 * (progress - 0.6) / 0.4); // 边界粒子数量
                
                for (int i = 0; i < borderParticles; i++) {
                    // 随机生成一个方向向量（球坐标）
                    double theta = Math.PI * 2 * i / borderParticles; // 均匀分布在圆周上
                    double phi = player.getRandom().nextDouble() * Math.PI; // 随机垂直角度
                    
                    // 将球坐标转换为笛卡尔坐标
                    double x = Math.sin(phi) * Math.cos(theta);
                    double y = Math.sin(phi) * Math.sin(theta);
                    double z = Math.cos(phi);
                    
                    // 在边界生成粒子
                    double radius = maxRadius * (0.9 + player.getRandom().nextDouble() * 0.1); // 接近最大半径
                    
                    // 计算粒子位置
                    double particleX = centerPos.x + x * radius;
                    double particleY = centerPos.y + y * radius;
                    double particleZ = centerPos.z + z * radius;
                    
                    // 交替使用两种粒子
                    serverWorld.spawnParticles(
                            ParticleTypes.END_ROD,
                            particleX, particleY, particleZ,
                            1,
                            0.02, 0.02, 0.02,
                            0.001
                    );
                }
            }
            
            // 最后阶段，添加一些向外飞溅的粒子，增强动态效果
            if (progress > 0.85) {
                int splashParticles = (int)(300 * (progress - 0.85) / 0.15);
                
                for (int i = 0; i < splashParticles; i++) {
                    // 随机生成一个方向向量（球坐标）
                    double theta = player.getRandom().nextDouble() * Math.PI * 2;
                    double phi = player.getRandom().nextDouble() * Math.PI;
                    
                    // 将球坐标转换为笛卡尔坐标
                    double x = Math.sin(phi) * Math.cos(theta);
                    double y = Math.sin(phi) * Math.sin(theta);
                    double z = Math.cos(phi);
                    
                    // 计算粒子位置和速度
                    double speedFactor = 0.2 + player.getRandom().nextDouble() * 0.3;
                    
                    // 从中心点向外飞溅
                    serverWorld.spawnParticles(
                            ParticleTypes.END_ROD,
                            centerPos.x, centerPos.y, centerPos.z,
                            0, // 设置为0，使用速度来控制粒子移动
                            x * speedFactor, y * speedFactor, z * speedFactor, // 速度向量
                            0.2 // 速度乘数
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