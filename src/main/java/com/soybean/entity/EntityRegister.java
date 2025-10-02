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
import net.minecraft.world.World;

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
                    .trackRangeBlocks(300)
                    .trackedUpdateRate(20)
                    .build()
    );

    public static final EntityType<BlueArrowEntity> BLUE_ARROW_ENTITY = register(
            "blue_arrow_entity",
            EntityType.Builder.create((EntityType<BlueArrowEntity> entityType, World world)->new BlueArrowEntity(entityType,world),SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(200)
                    .eyeHeight(0.13F)
                    .trackingTickInterval(20)
                    .build());

    public static final EntityType<MagneticBombEntity> MAGNETIC_BOMB_ENTITY_TYPE = register(
            "magnetic_bomb_entity",
            EntityType.Builder.create((EntityType<MagneticBombEntity> entityType, World world)->new MagneticBombEntity(entityType,world),SpawnGroup.MISC)
                    .dimensions(0.5f, 0.5f)
                    .maxTrackingRange(200)
                    .eyeHeight(0.13F)
                    .trackingTickInterval(20)
                    .build());

    public static final EntityType<RocketEntity> ROCKET_ENTITY_TYPE = register(
            "rocket_entity",
            EntityType.Builder.create((EntityType<RocketEntity> entityType, World world)->new RocketEntity(entityType,world),SpawnGroup.MISC)
                    .dimensions(2f, 1f)
                    .maxTrackingRange(200)
                    .eyeHeight(0.13F)
                    .trackingTickInterval(1)
                    .build());
    
    // 注册床弹射物实体
    public static final EntityType<BedProjectileEntity> BED_PROJECTILE_ENTITY = register(
            "bed_projectile",
            FabricEntityTypeBuilder.<BedProjectileEntity>create(SpawnGroup.MISC, BedProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 0.25f))
                    .trackRangeBlocks(128)
                    .trackedUpdateRate(10)
                    .build()
    );

    public static final EntityType<RideablePolarBearEntity> RIDEABLE_POLAR_BEAR = register(
            "rideable_polar_bear",
            FabricEntityTypeBuilder.<RideablePolarBearEntity>create(SpawnGroup.CREATURE, RideablePolarBearEntity::new)
                    .dimensions(EntityDimensions.fixed(1.4F, 1.4F))
                    .build()
    );

    public static final EntityType<CommonCreeperEntity> COMMON_CREEPER = Registry.register(
            Registries.ENTITY_TYPE,
            InitValue.id("common_creeper"),
            EntityType.Builder.create(CommonCreeperEntity::new, SpawnGroup.MONSTER)
                    .dimensions(0.6f, 1.7f)
                    .maxTrackingRange(8)
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
        FabricDefaultAttributeRegistry.register(RIDEABLE_POLAR_BEAR, RideablePolarBearEntity.createPolarBearAttributes());
        FabricDefaultAttributeRegistry.register(COMMON_CREEPER, CommonCreeperEntity.createCreeperAttributes());
    }
}
