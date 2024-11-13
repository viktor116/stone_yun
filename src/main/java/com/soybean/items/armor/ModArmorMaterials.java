package com.soybean.items.armor;

import com.soybean.config.InitValue;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmorMaterials {

//    public static final RegistryEntry<ArmorMaterial> COAL_MATERIAL = registerMaterial("coal",
//            Map.of(
//                    ArmorItem.Type.HELMET, 3,
//                    ArmorItem.Type.CHESTPLATE, 8,
//                    ArmorItem.Type.LEGGINGS, 6,
//                    ArmorItem.Type.BOOTS, 3,
//                    ArmorItem.Type.BODY, 11
//            ),
//            5,
//            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
//            () -> Ingredient.ofItems(Items.COAL),
//            0.0F,
//            0.0F,
//            false);

//
//    public static RegistryEntry<ArmorMaterial> registerMaterial(String id, Map<ArmorItem.Type, Integer> defensePoints, int enchantability, RegistryEntry<SoundEvent> equipSound, Supplier<Ingredient> repairIngredientSupplier, float toughness, float knockbackResistance, boolean dyeable) {
//        List<ArmorMaterial.Layer> layers = List.of(
//                new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, id), "", dyeable)
//        );
//        ArmorMaterial material = new ArmorMaterial(defensePoints, enchantability, equipSound, repairIngredientSupplier, layers, toughness, knockbackResistance);
//        material = Registry.register(Registries.ARMOR_MATERIAL, Identifier.of(InitValue.MOD_ID, id), material);
//        return RegistryEntry.of(material);
//    }
    public static final RegistryEntry<ArmorMaterial> COAL_MATERIAL = registerArmorMaterial("coal",
        ()-> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
            map.put(ArmorItem.Type.HELMET, 3);
            map.put(ArmorItem.Type.CHESTPLATE, 8);
            map.put(ArmorItem.Type.LEGGINGS, 6);
            map.put(ArmorItem.Type.BOOTS, 3);
            map.put(ArmorItem.Type.BODY, 11);
        }), 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(Items.COAL),
                List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "coal"))),0,0));

    public static void initialize() {

    };
    public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name,Supplier<ArmorMaterial> material){
        return Registry.registerReference(Registries.ARMOR_MATERIAL,Identifier.of(InitValue.MOD_ID,name),material.get());
    }

}
