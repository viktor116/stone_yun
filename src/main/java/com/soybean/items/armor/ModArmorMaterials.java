package com.soybean.items.armor;

import com.soybean.config.InitValue;
import com.soybean.items.ItemsRegister;
import net.minecraft.item.*;
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

    //仙人掌套
    public static final RegistryEntry<ArmorMaterial> CACTUS_MATERIAL = registerArmorMaterial("cactus",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.HELMET, 2);  // 头盔防护值 1
                map.put(ArmorItem.Type.CHESTPLATE, 5);  // 胸甲防护值 3
                map.put(ArmorItem.Type.LEGGINGS, 4);  // 裤子防护值 2
                map.put(ArmorItem.Type.BOOTS, 1);  // 靴子防护值 1
            }),
                    15,  // 总耐久度类似于链甲
                    SoundEvents.ITEM_ARMOR_EQUIP_IRON,  // 使用铁装备的音效
                    () -> Ingredient.ofItems(Items.CACTUS),  // 使用仙人掌作为修复材料
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "cactus"))), // 定义纹理
                    0,  // 硬度
                    0  // 击退抗性
            ));
    //主世界合金套
    public static final RegistryEntry<ArmorMaterial> MAIN_WORD_MATERIAL = registerArmorMaterial("main_world",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
                    15,  // 总耐久度类似于链甲
                    SoundEvents.ITEM_ARMOR_EQUIP_IRON,  // 使用铁装备的音效
                    () -> Ingredient.ofItems(Items.CACTUS),  // 使用仙人掌作为修复材料
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "main_world"))), // 定义纹理
                    3,  // 硬度
                    0.1f  // 击退抗性
            ));
    //叶绿套
    public static final RegistryEntry<ArmorMaterial> LEAF_MATERIAL = registerArmorMaterial("leaf",
            ()-> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }), 5, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(ItemsRegister.LEAF_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "leaf"))),0,0));

    //发光地衣套
    public static final RegistryEntry<ArmorMaterial> LICHEN_MATERIAL = registerArmorMaterial("lichen",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }),
                    15,  // 总耐久度类似于链甲
                    SoundEvents.ITEM_ARMOR_EQUIP_IRON,  // 使用铁装备的音效
                    () -> Ingredient.ofItems(Items.CACTUS),  // 使用仙人掌作为修复材料
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "lichen"))), // 定义纹理
                    2f,  // 硬度
                    0f  // 击退抗性
            ));
    //铜套（原版铜盔甲数值）
    public static final RegistryEntry<ArmorMaterial> COPPER_ARMOR_MATERIAL = registerArmorMaterial("copper",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.HELMET, 1);
                map.put(ArmorItem.Type.CHESTPLATE, 4);
                map.put(ArmorItem.Type.LEGGINGS, 3);
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.BODY, 6);
            }), 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(Items.COPPER_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "copper"))),0,0));
    //斑驳铜套
    public static final RegistryEntry<ArmorMaterial> EXPOSED_COPPER_ARMOR_MATERIAL = registerArmorMaterial("exposed_copper",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.HELMET, 1);
                map.put(ArmorItem.Type.CHESTPLATE, 3);
                map.put(ArmorItem.Type.LEGGINGS, 2);
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.BODY, 5);
            }), 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(Items.COPPER_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "exposed_copper"))),0,0));
    //斑驳铜套（中间阶段）
    public static final RegistryEntry<ArmorMaterial> WEATHERED_COPPER_ARMOR_MATERIAL = registerArmorMaterial("weathered_copper",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.HELMET, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 2);
                map.put(ArmorItem.Type.LEGGINGS, 2);
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.BODY, 4);
            }), 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(Items.COPPER_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "weathered_copper"))),0,0));
    //氧化铜套
    public static final RegistryEntry<ArmorMaterial> OXIDIZED_COPPER_ARMOR_MATERIAL = registerArmorMaterial("oxidized_copper",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.HELMET, 0);
                map.put(ArmorItem.Type.CHESTPLATE, 2);
                map.put(ArmorItem.Type.LEGGINGS, 1);
                map.put(ArmorItem.Type.BOOTS, 0);
                map.put(ArmorItem.Type.BODY, 3);
            }), 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(Items.COPPER_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "oxidized_copper"))),0,0));
    //涂蜡铜套（与铜套同数值）
    public static final RegistryEntry<ArmorMaterial> WAXED_COPPER_ARMOR_MATERIAL = registerArmorMaterial("waxed_copper",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.HELMET, 1);
                map.put(ArmorItem.Type.CHESTPLATE, 4);
                map.put(ArmorItem.Type.LEGGINGS, 3);
                map.put(ArmorItem.Type.BOOTS, 1);
                map.put(ArmorItem.Type.BODY, 6);
            }), 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, () -> Ingredient.ofItems(Items.COPPER_INGOT),
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "waxed_copper"))),0,0));
    //地衣钻石套
    public static final RegistryEntry<ArmorMaterial> LICHEN_DIAMOND_ARMOR_MATERIAL = registerArmorMaterial("lichen_diamond",
            () -> new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.BODY, 11);
            }), 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, () -> Ingredient.ofItems(Items.DIAMOND),
                    List.of(new ArmorMaterial.Layer(Identifier.of(InitValue.MOD_ID, "lichen_diamond"))),2f,0.2f));
    public static void initialize() {

    };
    public static RegistryEntry<ArmorMaterial> registerArmorMaterial(String name,Supplier<ArmorMaterial> material){
        return Registry.registerReference(Registries.ARMOR_MATERIAL,Identifier.of(InitValue.MOD_ID,name),material.get());
    }

}
