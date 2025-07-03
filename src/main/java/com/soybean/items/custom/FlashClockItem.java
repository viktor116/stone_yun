package com.soybean.items.custom;

import com.soybean.event.impl.EventHud;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * 闪烁时钟，使用时耐久减1，并在屏幕上产生全白闪烁效果
 */
public class FlashClockItem extends Item {

    public FlashClockItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        
        // 在客户端触发闪烁效果
        if (world.isClient) {
            // 触发HUD闪烁效果
            EventHud.triggerFlash();
        }
        
        if (!world.isClient) {
            // 播放闪烁音效
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 0.8F, 1.2F);
            
            // 减少耐久度
            if (!player.getAbilities().creativeMode) {
                stack.damage(1, player, EquipmentSlot.MAINHAND);
            }
            
            // 设置冷却时间（2秒）
            player.getItemCooldownManager().set(this, 40);
        }
        
        return TypedActionResult.success(stack);
    }
} 