package com.soybean.enchant;

import com.mojang.serialization.MapCodec;
import com.soybean.config.InitValue;
import com.soybean.enchant.effect.FlameAdditionEnchantmentEffect;
import com.soybean.enchant.effect.SnatchEnchantmentEffect;
import com.soybean.enchant.effect.SwordAuraEnchantmentEffect;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * @author soybean
 * @date 2024/11/13 10:57
 * @description
 */
public class EnchantmentRegister {

    public static final RegistryKey<Enchantment> SNATCH = of("snatch");
    public static final RegistryKey<Enchantment> FLAME_ADDITION = of("flame_addition");
    public static final RegistryKey<Enchantment> SWORD_AURA = of("sword_aura");

    public static final MapCodec<SnatchEnchantmentEffect> SNATCH_EFFECT = register("snatch", SnatchEnchantmentEffect.CODEC);
    public static final MapCodec<FlameAdditionEnchantmentEffect> FLAME_EFFECT = register("flame_addition", FlameAdditionEnchantmentEffect.CODEC);
    public static final MapCodec<SwordAuraEnchantmentEffect> SWORD_AURA_EFFECT = register("sword_aura", SwordAuraEnchantmentEffect.CODEC);
    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String name ,MapCodec<T> codec){
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE,getId(name),codec);
    }

    public static void Initialize(){

    }

    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(InitValue.MOD_ID, name));
    }

    public static Identifier getId(String name){
        return Identifier.of(InitValue.MOD_ID, name);
    }

    public static Optional<RegistryEntry.Reference<Enchantment>> getEnchantmentEntry(World world, RegistryKey<Enchantment> enchantmentKey) {
        DynamicRegistryManager manager = world.getRegistryManager();
        return manager.get(RegistryKeys.ENCHANTMENT).getEntry(enchantmentKey);
    }

    public static int getEnchantmentLevel(World world, RegistryKey<Enchantment> enchantmentKey, ItemStack stack) {
        Optional<RegistryEntry.Reference<Enchantment>> entry = getEnchantmentEntry(world, enchantmentKey);
        return entry.map(reference -> EnchantmentHelper.getLevel(reference, stack)).orElse(0);
    }
    public static ItemStack enchantBook(ItemStack eBook, World world, Identifier enchantmentId, int level) {
        DynamicRegistryManager manager = world.getRegistryManager();
        Optional<RegistryEntry.Reference<Enchantment>> reference = manager.get(RegistryKeys.ENCHANTMENT).getEntry(enchantmentId);
        if (reference.isEmpty()) {
            return eBook;
        }

        Optional<RegistryKey<Enchantment>> key = reference.get().getKey();
        if (key.isEmpty()) {
            return eBook;
        }

        RegistryEntryLookup.RegistryLookup registryLookup = manager.createRegistryLookup();
        Enchantment enchantment = registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(key.get()).value();
        RegistryEntry<Enchantment> entry = RegistryEntry.of(enchantment);

        ItemEnchantmentsComponent comp = eBook.getEnchantments();
        ItemEnchantmentsComponent.Builder builder = new ItemEnchantmentsComponent.Builder(comp);
        builder.add(entry, level);
        EnchantmentHelper.set(eBook, comp);

        return eBook;
    }
}
