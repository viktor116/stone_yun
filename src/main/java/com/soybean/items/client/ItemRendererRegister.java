package com.soybean.items.client;

import com.soybean.block.ModBlock;
import com.soybean.block.client.renderer.InvertRedBlockEntityRenderer;
import com.soybean.items.ItemsRegister;
import com.soybean.items.client.custom.CustomBedItemRenderer;
import com.soybean.items.client.custom.HalfBedItemRenderer;
import com.soybean.items.client.custom.InvertRedItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

/**
 * @author soybean
 * @date 2025/3/14 13:32
 * @description
 */
public class ItemRendererRegister {
    public static void init(){
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsRegister.FLIP_WHITE_BED, new CustomBedItemRenderer("flip_white"));
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsRegister.TNT_WHITE_BED, new CustomBedItemRenderer("tnt_white"));
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsRegister.HALF_WHITE_BED, new HalfBedItemRenderer("half_white"));
        BuiltinItemRendererRegistry.INSTANCE.register(ItemsRegister.INVERT_RED_BED, new InvertRedItemRenderer());
    }
}
