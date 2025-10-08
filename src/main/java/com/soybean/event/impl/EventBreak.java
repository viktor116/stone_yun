package com.soybean.event.impl;

import com.soybean.enchant.EnchantmentRegister;
import com.soybean.enchant.effect.FlameAdditionEnchantmentEffect;
import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;


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
            Block block = state.getBlock();
            if(player.getMainHandStack().getItem() == Items.WOODEN_PICKAXE){
                if(block == Blocks.STONE){
                    world.breakBlock(pos, false, player);
                    Block.dropStack(world, pos, new ItemStack(Items.STONE));
                }
            }
            return true; // 没有附魔就正常破坏
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            Block block = state.getBlock();
            // 判断是不是叶子
            if (isLeafBlock(block)) {
                if (!world.isClient && world instanceof ServerWorld serverWorld) {
                    Random random = serverWorld.getRandom();
                    if (random.nextFloat() < 0.25f) { // 25% 掉落
                        Block.dropStack(world, pos, new ItemStack(ItemsRegister.BRANCH));
                    }
                }
            }
        });
    }


    // 判断是否是树叶方块（你可以加自己的自定义叶子）
    private static boolean isLeafBlock(Block block) {
        return block == Blocks.OAK_LEAVES ||
                block == Blocks.SPRUCE_LEAVES ||
                block == Blocks.BIRCH_LEAVES ||
                block == Blocks.JUNGLE_LEAVES ||
                block == Blocks.ACACIA_LEAVES ||
                block == Blocks.DARK_OAK_LEAVES;
    }
}
