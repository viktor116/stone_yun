package com.soybean.data.provider;

import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider<Item> {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ITEM, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(ItemsRegister.GOLDEN_APPLE)
                .add(ItemsRegister.APPLE_INGOT_SWORD)
                .add(ItemsRegister.STONE_SWORD)
                .add(ItemsRegister.CACTUS_SWORD)
                .add(ItemsRegister.ENDER_SWORD)
                .add(ItemsRegister.FLAME_SWORD)
                .add(ItemsRegister.BIT_DIAMOND_SWORD)
                .add(ItemsRegister.DIAMOND_STICK_SWORD)
                .add(ItemsRegister.DIRT_SWORD)
                .add(ItemsRegister.LICHEN_SWORD)
                .add(ItemsRegister.TRANSPARENT_SWORD);

        getOrCreateTagBuilder(ItemTags.AXES)
                .add(ItemsRegister.STONE_AXE)
                .add(ItemsRegister.CACTUS_AXE)
                .add(ItemsRegister.BIT_DIAMOND_AXE)
                .add(ItemsRegister.DIAMOND_STICK_AXE)
                .add(ItemsRegister.DIRT_AXE)
                .add(ItemsRegister.TRANSPARENT_AXE);

        getOrCreateTagBuilder(ItemTags.WOLF_FOOD)
                .add(ItemsRegister.SKELETON_ITEM);

        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ItemsRegister.STONE_PICKAXE)
                .add(ItemsRegister.CACTUS_PICKAXE)
                .add(ItemsRegister.TRANSPARENT_PICKAXE)
                .add(ItemsRegister.BIT_DIAMOND_PICKAXE)
                .add(ItemsRegister.DIAMOND_STICK_PICKAXE)
                .add(ItemsRegister.DIRT_PICKAXE)
                .add(ItemsRegister.LICHEN_PICKAXE)
                .add(ItemsRegister.COPPER_PICKAXE)
                .add(ItemsRegister.CONCRETE_PICKAXE);

        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR)
                .add(ItemsRegister.COAL_HELMET)
                .add(ItemsRegister.MINECART_HAT)
                .add(ItemsRegister.CACTUS_HELMET)
                .add(ItemsRegister.MAIN_WORLD_HELMET)
                .add(ItemsRegister.LEAF_HELMET)
                .add(ItemsRegister.LICHEN_HELMET)
                .add(ItemsRegister.INVERT_BUCKET);

        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR)
                .add(ItemsRegister.CACTUS_CHESTPLATE)
                .add(ItemsRegister.MAIN_WORLD_CHESTPLATE)
                .add(ItemsRegister.LEAF_CHESTPLATE)
                .add(ItemsRegister.LICHEN_CHESTPLATE)
                .add(ItemsRegister.COAL_CHESTPLATE);

        getOrCreateTagBuilder(ItemTags.LEG_ARMOR)
                .add(ItemsRegister.CACTUS_LEGGINGS)
                .add(ItemsRegister.MAIN_WORLD_LEGGINGS)
                .add(ItemsRegister.LEAF_LEGGINGS)
                .add(ItemsRegister.LICHEN_LEGGINGS)
                .add(ItemsRegister.COAL_LEGGINGS);

        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
                .add(ItemsRegister.CACTUS_BOOTS)
                .add(ItemsRegister.MAIN_WORLD_BOOTS)
                .add(ItemsRegister.LEAF_BOOTS)
                .add(ItemsRegister.LICHEN_BOOTS)
                .add(ItemsRegister.COAL_BOOTS);

        getOrCreateTagBuilder(ItemTags.BOATS)
                .add(ItemsRegister.INVERT_MINECART);
        getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
                .add(ItemsRegister.GRASS)
                .add(ItemsRegister.WHEAT);
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ItemsRegister.HORSE_ARMOR_NETHERITE);
        getOrCreateTagBuilder(ConventionalItemTags.FISHING_ROD_TOOLS)
                .add(ItemsRegister.COD_FISHING_ROD);

        getOrCreateTagBuilder(ItemTags.BOW_ENCHANTABLE)
                .add(ItemsRegister.WITHER_BOW)
                .add(ItemsRegister.THE_END_BOW);

        getOrCreateTagBuilder(ItemTags.HORSE_FOOD)
                .add(ItemsRegister.DOUBLE_ENCHANTED_GOLDEN_APPLE);

    }
}
