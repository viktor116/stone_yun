package com.soybean.entity;


import com.soybean.config.InitValue;
import com.soybean.entity.custom.HimEntity;
import com.soybean.entity.custom.WheatEntity;
import com.soybean.entity.vehicle.InvertBoatEntity;
import com.soybean.entity.vehicle.PurpleBoatEntity;
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

    public static final EntityType<HimEntity> HIM = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "him"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, HimEntity::new)
                    .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
                    .build()
    );

    public static final EntityType<PurpleBoatEntity> PURPLE_BOAT_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "purple_boat"),
            FabricEntityTypeBuilder.<PurpleBoatEntity>create(SpawnGroup.MISC, PurpleBoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F, 0.5625F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public static void initialize(){
        FabricDefaultAttributeRegistry.register(WheatEntity.WHEAT, WheatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HIM, HimEntity.createAttributes());
    }

}
