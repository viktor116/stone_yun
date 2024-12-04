package com.soybean.event.impl;

import com.soybean.enchant.EnchantmentRegister;
import com.soybean.enchant.effect.SwordAuraEnchantmentEffect;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.TypedActionResult;

public class EventUseItem {
    public static void register() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);
            // 检查是否是剑
            if (stack.getItem() instanceof SwordItem) {
                // 检查是否有剑气附魔
                int level = EnchantmentRegister.getEnchantmentLevel(world, EnchantmentRegister.SWORD_AURA, stack);
                if (level > 0) {
                    SwordAuraEnchantmentEffect.createSwordAura(world, player);
                    // 添加冷却时间（可选）
                    player.getItemCooldownManager().set(stack.getItem(), 20); // 1秒冷却
                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(stack);
        });
    }
} 