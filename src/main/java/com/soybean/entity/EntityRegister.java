package com.soybean.entity;


import com.soybean.config.InitValue;
import com.soybean.entity.client.model.HimModel;
import com.soybean.entity.client.renderer.HimRenderer;
import com.soybean.entity.client.renderer.InvertBoatRenderer;
import com.soybean.entity.client.renderer.InvertMinecartRenderer;
import com.soybean.entity.client.renderer.WheatRenderer;
import com.soybean.entity.custom.HimEntity;
import com.soybean.entity.custom.InvertMinecartEntity;
import com.soybean.entity.custom.WheatEntity;
import com.soybean.entity.vehicle.InvertBoatEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/19 10:48
 * @description
 */
public class EntityRegister {

    public static final EntityType<InvertBoatEntity> INVERT_BOAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "invert_boat"),
            FabricEntityTypeBuilder.<InvertBoatEntity>create(SpawnGroup.MISC, InvertBoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public static final EntityType<HimEntity> HIM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "him"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HimEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build()
    );

    public static final EntityModelLayer HIM_LAYER = new EntityModelLayer(Identifier.of(InitValue.MOD_ID, "him"), "main");

    public static void initialize(){
        FabricDefaultAttributeRegistry.register(WheatEntity.WHEAT, WheatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HIM, HimEntity.createAttributes());
    }

    public static void initializeClient(){
        EntityRendererRegistry.register(InvertMinecartEntity.MINECART, InvertMinecartRenderer::new);
        EntityRendererRegistry.register(WheatEntity.WHEAT, WheatRenderer::new);
        EntityRendererRegistry.register(EntityRegister.INVERT_BOAT, InvertBoatRenderer::new);
        EntityRendererRegistry.register(HIM, (context) -> new HimRenderer(context, HIM_LAYER));
        // 注册模型层
        EntityModelLayerRegistry.registerModelLayer(HIM_LAYER, () ->
                TexturedModelData.of(HimModel.getModelData(), 64, 64)
        );
    }
}
