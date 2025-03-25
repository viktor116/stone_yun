package com.soybean.mixin;

import com.soybean.items.ItemsRegister;
import com.soybean.utils.DynamicLightSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author soybean
 * @date 2025/03/25
 * @description 实现动态光源效果的Mixin
 */
@Mixin(WorldRenderer.class)
public class DynamicLightMixin {
    private static final Map<Entity, BlockPos> LIGHT_SOURCES = new HashMap<>();
    private static final Map<Entity, Integer> LIGHT_LEVELS = new HashMap<>();
    private static final Map<BlockPos, BlockState> ORIGINAL_STATES = new HashMap<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        ClientPlayerEntity player = client.player;
        
        if (world == null || player == null) return;
        
        // 清除不再存在的实体的光源
        LIGHT_SOURCES.keySet().removeIf(entity -> entity.isRemoved() || !entity.isAlive());
        LIGHT_LEVELS.keySet().removeIf(entity -> entity.isRemoved() || !entity.isAlive());
        
        // 更新玩家的光源
        updateEntityLight(player, world);
        
        // 更新其他实体的光源
        for (Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity livingEntity && entity != player) {
                updateEntityLight(livingEntity, world);
            }
        }
    }
    
    private void updateEntityLight(LivingEntity entity, ClientWorld world) {
        // 快速检查：如果实体没有装备任何可能发光的物品，直接返回
        if (!hasLightEmittingItem(entity)) {
            // 如果之前有光源，移除它
            BlockPos oldPos = LIGHT_SOURCES.remove(entity);
            if (oldPos != null) {
                LIGHT_LEVELS.remove(entity);
                removeLight(world, oldPos);
            }
            return;
        }
        
        int lightLevel = 0;
        
        // 检查主手物品
        ItemStack mainHandItem = entity.getMainHandStack();
        if (!mainHandItem.isEmpty()) {
            lightLevel = Math.max(lightLevel, getLightLevelForItem(mainHandItem.getItem()));
        }
        
        // 检查副手物品
        ItemStack offHandItem = entity.getOffHandStack();
        if (!offHandItem.isEmpty()) {
            lightLevel = Math.max(lightLevel, getLightLevelForItem(offHandItem.getItem()));
        }
        
        // 检查盔甲
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armorItem = entity.getEquippedStack(slot);
                if (!armorItem.isEmpty()) {
                    lightLevel = Math.max(lightLevel, getLightLevelForItem(armorItem.getItem()));
                }
            }
        }
        
        // 如果有光源，更新光源位置
        if (lightLevel > 0) {
            BlockPos entityPos = entity.getBlockPos();
            BlockPos oldPos = LIGHT_SOURCES.get(entity);
            Integer oldLevel = LIGHT_LEVELS.get(entity);
            
            // 如果位置变化或光照等级变化，更新光源
            if (oldPos == null || !oldPos.equals(entityPos) || oldLevel == null || oldLevel != lightLevel) {
                // 移除旧的光源
                if (oldPos != null) {
                    removeLight(world, oldPos);
                }
                
                // 尝试在实体周围找到一个合适的空气方块放置光源
                BlockPos lightPos = findSuitablePosition(world, entityPos);
                
                if (lightPos != null) {
                    // 设置新的光源
                    LIGHT_SOURCES.put(entity, lightPos);
                    LIGHT_LEVELS.put(entity, lightLevel);
                    
                    // 添加新的光源
                    addLight(world, lightPos, lightLevel);
                }
            }
        } else {
            // 如果没有光源但之前有，移除光源
            BlockPos oldPos = LIGHT_SOURCES.remove(entity);
            LIGHT_LEVELS.remove(entity);
            if (oldPos != null) {
                removeLight(world, oldPos);
            }
        }
    }
    
    /**
     * 快速检查实体是否装备了任何可能发光的物品
     */
    private boolean hasLightEmittingItem(LivingEntity entity) {
        // 检查主手物品
        ItemStack mainHandItem = entity.getMainHandStack();
        if (!mainHandItem.isEmpty() && isLightEmittingItem(mainHandItem.getItem())) {
            return true;
        }
        
        // 检查副手物品
        ItemStack offHandItem = entity.getOffHandStack();
        if (!offHandItem.isEmpty() && isLightEmittingItem(offHandItem.getItem())) {
            return true;
        }
        
        // 检查盔甲
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armorItem = entity.getEquippedStack(slot);
                if (!armorItem.isEmpty() && isLightEmittingItem(armorItem.getItem())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 快速检查物品是否可能发光
     */
    private boolean isLightEmittingItem(Item item) {
        // 检查是否是 LICHEN 系列物品
        return item == ItemsRegister.LICHEN_SWORD || 
               item == ItemsRegister.LICHEN_PICKAXE ||
               item == ItemsRegister.LICHEN_HELMET || 
               item == ItemsRegister.LICHEN_CHESTPLATE || 
               item == ItemsRegister.LICHEN_LEGGINGS || 
               item == ItemsRegister.LICHEN_BOOTS ||
               item instanceof DynamicLightSource;
    }
    
    /**
     * 在实体周围找到一个适合放置光源的位置
     */
    private BlockPos findSuitablePosition(World world, BlockPos entityPos) {
        // 首先尝试实体所在位置
        if (canPlaceLightAt(world, entityPos)) {
            return entityPos;
        }
        
        // 如果实体所在位置不适合，尝试实体上方的位置
        BlockPos abovePos = entityPos.up();
        if (canPlaceLightAt(world, abovePos)) {
            return abovePos;
        }
        
        // 如果上方也不适合，尝试周围的位置
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // 跳过实体所在位置
                    
                    BlockPos pos = entityPos.add(x, y, z);
                    if (canPlaceLightAt(world, pos)) {
                        return pos;
                    }
                }
            }
        }
        
        // 如果找不到适合的位置，返回null
        return null;
    }
    
    /**
     * 检查是否可以在指定位置放置光源
     */
    private boolean canPlaceLightAt(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        // 只在空气方块上放置光源
        return state.isAir();
    }
    
    private void addLight(World world, BlockPos pos, int level) {
        // 保存原始方块状态
        if (!ORIGINAL_STATES.containsKey(pos)) {
            ORIGINAL_STATES.put(pos, world.getBlockState(pos));
        }
        
        // 根据光照等级选择光源方块
        BlockState lightBlock = Blocks.LIGHT.getDefaultState();
        
        // 设置光源方块
        world.setBlockState(pos, lightBlock, 3);
    }
    
    private void removeLight(World world, BlockPos pos) {
        // 恢复原始方块状态
        BlockState originalState = ORIGINAL_STATES.remove(pos);
        if (originalState != null) {
            world.setBlockState(pos, originalState, 3);
        }
    }
    
    private int getLightLevelForItem(Item item) {
        // 在这里定义哪些物品应该发光以及它们的光照等级
        if (item == ItemsRegister.LICHEN_SWORD || 
                   item == ItemsRegister.LICHEN_PICKAXE) {
            return 15;
        } else if (item == ItemsRegister.LICHEN_HELMET || 
                  item == ItemsRegister.LICHEN_CHESTPLATE || 
                  item == ItemsRegister.LICHEN_LEGGINGS || 
                  item == ItemsRegister.LICHEN_BOOTS) {
            return 15;
        }
        
        // 如果物品实现了DynamicLightSource接口，使用其亮度值
        if (item instanceof DynamicLightSource) {
            return ((DynamicLightSource) item).getLightLevel();
        }
        
        return 0; // 默认不发光
    }
}
