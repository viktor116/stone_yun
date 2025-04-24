package com.soybean.entity;


import com.soybean.config.InitValue;
import com.soybean.entity.custom.*;
import com.soybean.entity.vehicle.InvertBoatEntity;
import com.soybean.entity.vehicle.PurpleBoatEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
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

    public static final EntityType<EyeOfBlazeEntity> BLAZE_EYE_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "blaze_eye"),
            FabricEntityTypeBuilder.<EyeOfBlazeEntity>create(SpawnGroup.MISC, EyeOfBlazeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(10)
                    .build()
    );

    public static final EntityType<TotemOfDeadEntity> TOTEM_OF_DEAD_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "totem_of_dead_entity"),
            FabricEntityTypeBuilder.<TotemOfDeadEntity>create(SpawnGroup.MISC, TotemOfDeadEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(10)
                    .build()
    );


    public static final EntityType<LichenSwordEntity> LICHEN_SWORD = register(
            "lichen_sword",
            FabricEntityTypeBuilder.<LichenSwordEntity>create(SpawnGroup.MISC, LichenSwordEntity::new)
                    .dimensions(EntityDimensions.fixed(1f, 1f))
                    .trackRangeBlocks(4)
                    .trackedUpdateRate(20)
                    .build()
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> entityType) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(InitValue.MOD_ID, name), entityType);
    }


    public static void initialize(){
        FabricDefaultAttributeRegistry.register(WheatEntity.WHEAT, WheatEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(HIM, HimEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(CustomBlockEntity.SAND_BLOCK,CustomBlockEntity.createAttributes());
        FabricDefaultAttributeRegistry.register(CustomBlockEntity.DIRT_BLOCK,CustomBlockEntity.createAttributes());
    }
}
