package com.soybean.event.impl;

import com.soybean.enchant.EnchantmentRegister;
import com.soybean.enchant.effect.FlameAdditionEnchantmentEffect;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author soybean
 * @date 2024/12/4 12:03
 * @description
 */
public class EventBreak {
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            ItemStack stack = player.getMainHandStack();
            int level = EnchantmentRegister.getEnchantmentLevel(world, EnchantmentRegister.FLAME_ADDITION, stack);
            if (level > 0) {
                FlameAdditionEnchantmentEffect.handleDrop(world, player, pos, state, blockEntity);
                return false; // 返回 false 来取消原始的方块破坏事件
            }
            return true; // 没有附魔就正常破坏
        });
    }
}
