package com.soybean.items.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class DeadlyFoodItem extends Item {
    
    public DeadlyFoodItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            // 播放凋灵死亡音效
            world.playSound(null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 1.0F, 0.5F);

            // 立即杀死玩家
            player.damage(world.getDamageSources().generic(), Float.MAX_VALUE);
        }
        
        return super.finishUsing(stack, world, user);
    }
} 
