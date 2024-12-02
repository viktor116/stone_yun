package com.soybean.entity;


import com.soybean.config.InitValue;
import com.soybean.entity.client.renderer.InvertBoatRenderer;
import com.soybean.entity.client.renderer.InvertMinecartRenderer;
import com.soybean.entity.client.renderer.WheatRenderer;
import com.soybean.entity.custom.InvertMinecartEntity;
import com.soybean.entity.custom.WheatEntity;
import com.soybean.entity.vehicle.InvertBoatEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
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

    public static void initialize(){
        FabricDefaultAttributeRegistry.register(WheatEntity.WHEAT, WheatEntity.createAttributes());

    }

    public static void initializeClient(){
        EntityRendererRegistry.register(InvertMinecartEntity.MINECART, InvertMinecartRenderer::new);
        EntityRendererRegistry.register(WheatEntity.WHEAT, WheatRenderer::new);
        EntityRendererRegistry.register(EntityRegister.INVERT_BOAT, InvertBoatRenderer::new);
    }
}
