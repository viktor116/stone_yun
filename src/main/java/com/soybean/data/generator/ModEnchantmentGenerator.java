package com.soybean.data.generator;

import com.soybean.enchant.EnchantmentRegister;
import com.soybean.enchant.effect.SnatchEnchantmentEffect;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

/**
 * @author soybean
 * @date 2024/11/13 11:52
 * @description
 */
public class ModEnchantmentGenerator extends FabricDynamicRegistryProvider {

    public ModEnchantmentGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        RegistryWrapper<Item> itemLookup = registries.getWrapperOrThrow(RegistryKeys.ITEM);
        register(entries, EnchantmentRegister.SNATCH,Enchantment.builder(
           Enchantment.definition(
                   itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                   15, //probability
                   1, //max level
                    Enchantment.leveledCost(1,10), //cost per level(base)
                    Enchantment.leveledCost(1,15),  //cost per level(max)
                    7, //anvil cost
                   AttributeModifierSlot.HAND
           )).addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                EnchantmentEffectTarget.ATTACKER,
                EnchantmentEffectTarget.VICTIM,
                new SnatchEnchantmentEffect(EnchantmentLevelBasedValue.linear(0.5f))));
    }

    private static void register(Entries entries, RegistryKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions){
        entries.add(key, builder.build(key.getValue()),resourceConditions);
    }

    @Override
    public String getName() {
        return "Enchantment Generator";
    }
}
