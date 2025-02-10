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
    }
}
