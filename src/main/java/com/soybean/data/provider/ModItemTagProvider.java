package com.soybean.data.provider;

import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
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
                .add(ItemsRegister.STONE_SWORD);
        getOrCreateTagBuilder(ItemTags.AXES)
                .add(ItemsRegister.STONE_AXE);
        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ItemsRegister.STONE_PICKAXE);
        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR)
                .add(ItemsRegister.COAL_HELMET);
        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR)
                .add(ItemsRegister.COAL_CHESTPLATE);
        getOrCreateTagBuilder(ItemTags.LEG_ARMOR)
                .add(ItemsRegister.COAL_LEGGINGS);
        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
                .add(ItemsRegister.COAL_BOOTS);
    }
}
