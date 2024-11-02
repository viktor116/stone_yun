package com.soybean.entity;


import com.soybean.entity.client.renderer.InvertMinecartRenderer;
import com.soybean.entity.client.renderer.MinecartHatRenderer;
import com.soybean.entity.custom.InvertMinecartEntity;
import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.BlockModelRenderer;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/**
 * @author soybean
 * @date 2024/10/19 10:48
 * @description
 */
public class EntityRegister {

    public static void initialize(){


    }

    public static void initializeClient(){
        EntityRendererRegistry.register(InvertMinecartEntity.MINECART, InvertMinecartRenderer::new);

    }
}
