package com.soybean.items.potion;

import com.soybean.config.InitValue;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author soybean
 * @date 2025/2/10 11:00
 * @description
 */
public class PotionRegister {

    public static final Potion RESISTANCE = Registry.register(Registries.POTION,InitValue.id("resistance"),
                                    new Potion("resistance", new StatusEffectInstance(
                                    StatusEffects.RESISTANCE,
                                    20*60*5,
                                    0)));
    public static final Potion ABSORPTION = Registry.register(Registries.POTION,InitValue.id("absorption"),
            new Potion("absorption", new StatusEffectInstance(
                    StatusEffects.ABSORPTION,
                    20*60*2,
                    3)));
    public static final Potion WITHER = Registry.register(Registries.POTION,InitValue.id("wither"),
            new Potion("wither", new StatusEffectInstance(
                    StatusEffects.WITHER,
                    20*20,
                    1)));
    public static final Potion HUNGER = Registry.register(Registries.POTION,InitValue.id("hunger"),
            new Potion("hunger", new StatusEffectInstance(
                    StatusEffects.HUNGER,
                    20*60*3,
                    0)));

    public static void init(){
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.WATER, Items.POTATO, Registries.POTION.getEntry(RESISTANCE));
        });
    }
}
