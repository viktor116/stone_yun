package com.soybean.entity;

import com.soybean.entity.client.model.HimModel;
import com.soybean.entity.client.renderer.*;
import com.soybean.entity.custom.CustomBlockEntity;
import com.soybean.entity.custom.InvertMinecartEntity;
import com.soybean.entity.custom.WheatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

/**
 * @author soybean
 * @date 2024/12/4 10:55
 * @description
 */
@Environment(EnvType.CLIENT)
public class EntityRegisterClient {
    public static void initializeClient(){
        EntityRendererRegistry.register(InvertMinecartEntity.MINECART, InvertMinecartRenderer::new);
        EntityRendererRegistry.register(WheatEntity.WHEAT, WheatRenderer::new);
        EntityRendererRegistry.register(EntityRegister.INVERT_BOAT, InvertBoatRenderer::new);
        EntityRendererRegistry.register(EntityRegister.HIM, (context) -> new HimRenderer(context, HimModel.HIM_LAYER));
        EntityModelLayerRegistry.registerModelLayer(HimModel.HIM_LAYER, () ->
                TexturedModelData.of(HimModel.getModelData(), 64, 64)
        );
        EntityRendererRegistry.register(EntityRegister.PURPLE_BOAT_TYPE, PurpleBoatEntityRenderer::new);
        EntityRendererRegistry.register(CustomBlockEntity.SAND_BLOCK,CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(CustomBlockEntity.DIRT_BLOCK,CustomBlockEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegister.LICHEN_SWORD,LichenSwordEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegister.MAGNETIC_BOMB_ENTITY_TYPE, MagneticBombEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegister.ROCKET_ENTITY_TYPE, RocketEntityRenderer::new);

        EntityRendererRegistry.register(EntityRegister.BLAZE_EYE_ENTITY, (context) -> {
            return new FlyingItemEntityRenderer(context, 1.0F, true);
        });

        EntityRendererRegistry.register(EntityRegister.TOTEM_OF_DEAD_ENTITY, (context) -> {
            return new FlyingItemEntityRenderer(context, 1.0F, true);
        });

        EntityRendererRegistry.register(EntityRegister.BLUE_ARROW_ENTITY,BlueArrowEntityRenderer::new);
        
        // 注册床弹射物实体的渲染器
        EntityRendererRegistry.register(EntityRegister.BED_PROJECTILE_ENTITY, BedProjectileRenderer::new);
    }
}
