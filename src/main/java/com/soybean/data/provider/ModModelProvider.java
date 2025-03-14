package com.soybean.data.provider;

import com.soybean.block.ModBlock;
import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * @author soybean
 * @date 2024/11/13 14:56
 * @description
 */
public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlock.REACTOR);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlock.CONCRETE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlock.LEAF_ORE);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemsRegister.GOLDEN_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.HORSE_ARMOR_NETHERITE, Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.RAW_COAL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.INVERT_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.INVERT_BOAT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.COW_PLANT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.WHEAT_MEAL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.FLINT_AND_STEEL_CUSTOM,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.BLAZE_PEARL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.ENDER_ROD,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.ENDER_POWDER,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.PURPLE_BOAT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TRANSPARENT_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TRANSPARENT_BUCKET_WATER,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.TRANSPARENT_BUCKET_LAVA,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.CONCRETE_PICKAXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.CACTUS_AXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.CACTUS_SWORD,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.CACTUS_PICKAXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.CACTUS_HELMET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.CACTUS_CHESTPLATE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.CACTUS_LEGGINGS,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.CACTUS_BOOTS,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.MAIN_WORLD_INGOT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.MAIN_WORLD_SCRAP,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.GOLD_DEBRIS,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.CACTUS_STICK,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.HUMAN_FLESH,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.COOKED_HUMAN_FLESH,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.BIT_DIAMOND_SWORD,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.BIT_DIAMOND_AXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.BIT_DIAMOND_PICKAXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.ENDER_SWORD,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.FLAME_SWORD,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.BROKEN_BOW,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.MAIN_WORLD_HELMET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.MAIN_WORLD_CHESTPLATE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.MAIN_WORLD_LEGGINGS,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.MAIN_WORLD_BOOTS,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.ASHES,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.APPLE_INGOT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.GOLD_APPLE_INGOT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.APPLE_INGOT_SWORD,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.GOLD_CARROT_NUGGET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.DIAMOND_STICK_AXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.DIAMOND_STICK_SWORD,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.DIAMOND_STICK_PICKAXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.NETHER_FISH,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.GUARDIAN_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.WARDEN_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.LOW_ENCHANT_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.COPPER_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.DIRT_STICK,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.DIRT_SWORD,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.DIRT_PICKAXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.DIRT_AXE,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.FLIP_BOW,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.FLIP_FISHING_ROD,Models.HANDHELD);
        itemModelGenerator.register(ItemsRegister.DIRT_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.DIRT_WATER_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.DIRT_LAVA_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.FRIED_EGG,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.COAL_INGOT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.LEAF_INGOT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.LEAF_HELMET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.LEAF_CHESTPLATE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.LEAF_LEGGINGS,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.LEAF_BOOTS,Models.GENERATED);

        itemModelGenerator.register(ItemsRegister.SAND_BLOCK_SPAWN_EGG, new Model(Optional.of(Identifier.ofVanilla("block/sand")), Optional.empty()));
        itemModelGenerator.register(ItemsRegister.DIRT_BLOCK_SPAWN_EGG, new Model(Optional.of(Identifier.ofVanilla("block/dirt")), Optional.empty()));

    }
}
