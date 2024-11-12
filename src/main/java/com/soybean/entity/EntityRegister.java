package com.soybean.entity;


import com.soybean.entity.client.renderer.InvertMinecartRenderer;
import com.soybean.entity.client.renderer.MinecartHatRenderer;
import com.soybean.entity.client.renderer.WheatRenderer;
import com.soybean.entity.custom.InvertMinecartEntity;
import com.soybean.entity.custom.WheatEntity;
import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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
        FabricDefaultAttributeRegistry.register(WheatEntity.WHEAT, WheatEntity.createAttributes());

    }

    public static void initializeClient(){
        EntityRendererRegistry.register(InvertMinecartEntity.MINECART, InvertMinecartRenderer::new);
        EntityRendererRegistry.register(WheatEntity.WHEAT, WheatRenderer::new);

    }
}
