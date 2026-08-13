package com.soybean.block.client;

import com.soybean.block.ModBlock;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;

public class ModBlockColors {

    public static void register() {
        // 火药粉：深灰色
        int gunpowder = 0xFF404040;
        // 萤石粉：亮黄色
        int glowstone = 0xFFFFD85A;
        // 烈焰粉：橙红色
        int blaze = 0xFFFF8C00;
        // 糖：纯白色
        int sugar = 0xFFFFFFFF;

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> gunpowder, ModBlock.GUNPOWDER_WIRE);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> glowstone, ModBlock.GLOWSTONE_POWDER_WIRE);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> blaze, ModBlock.BLAZE_POWDER_WIRE);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> sugar, ModBlock.SUGAR_WIRE);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> gunpowder, ModBlock.GUNPOWDER_WIRE.asItem());
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> glowstone, ModBlock.GLOWSTONE_POWDER_WIRE.asItem());
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> blaze, ModBlock.BLAZE_POWDER_WIRE.asItem());
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> sugar, ModBlock.SUGAR_WIRE.asItem());
    }
}