package com.soybean.items;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.entity.custom.CustomBlockEntity;
import com.soybean.items.armor.ModArmorMaterials;
import com.soybean.items.custom.*;
import com.soybean.items.custom.InvertBoatItem;
import com.soybean.items.food.FoodRegister;
import com.soybean.items.item.*;
import com.soybean.items.material.AirMaterial;
import com.soybean.items.material.StoneMaterial;
import com.soybean.items.potion.PotionRegister;
import com.soybean.items.recipes.ModRecipes;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

/**
 * @author soybean
 * @date 2024/10/8 15:44
 * @description
 */
public class ItemsRegister {

    public static final Item STONE_STICK = register(new Item(new Item.Settings().maxCount(64)), "stone_stick");
    //空手撸石头
    //public static final Block STONE = register("stone", new Block(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F)));
    public static final Item INVERT_MINECART = register(new InvertMinecartItem(new Item.Settings().maxCount(1)), "invert_minecart");
    public static final Item MINECART_HAT = register(new MinecartHatItem(), "minecart_hat");
    public static final Item GRASS = register(new GrassItem(new Item.Settings()),"grass");
    public static final Item WHEAT = register(new WheatItem(new Item.Settings()),"wheat");
    public static final Item WHEAT_MEAL = register(new BoneMealItem(new Item.Settings()),"wheat_meal");
    public static final Item STONE_SWORD = register(new SwordItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.STONE, 3, -2.4F)).maxCount(1).maxDamage(131)),"stone_sword");
    public static final Item STONE_AXE = register(new AxeItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.STONE, 7F, -3.2F)).maxCount(1).maxDamage(131)),"stone_axe");
    public static final Item STONE_PICKAXE = register(new PickaxeItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.STONE, 1.0F, -2.8F)).maxCount(1).maxDamage(131)),"stone_pickaxe");
    public static final Item SOUL_TORCH = register(new VerticallyAttachableBlockItem(ModBlock.SOUL_TORCH_BLOCK, ModBlock.SOUL_WALL_TORCH, new Item.Settings(), Direction.DOWN),"soul_torch");
    public static final Item BIG_TORCH = register(new VerticallyAttachableBlockItem(ModBlock.BIG_TORCH_BLOCK, ModBlock.BIG_WALL_TORCH, new Item.Settings(), Direction.DOWN),"big_torch");
    public static final Item COAL_HELMET = register(new ArmorItem(ModArmorMaterials.COAL_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))), "coal_helmet");
    public static final Item COAL_CHESTPLATE = register(new ArmorItem(ModArmorMaterials.COAL_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))), "coal_chestplate");
    public static final Item COAL_LEGGINGS = register(new ArmorItem(ModArmorMaterials.COAL_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))), "coal_leggings");
    public static final Item COAL_BOOTS = register(new ArmorItem(ModArmorMaterials.COAL_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))), "coal_boots");
    public static final Item AIR_PICKAXE = register(new UnbreakablePickaxeItem(AirMaterial.INSTANCE,new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(AirMaterial.INSTANCE, 999.0F, 999F)).maxCount(1)),"air_pickaxe");
    public static final Item HORSE_ARMOR_NETHERITE = register(new AnimalArmorItem(ModArmorMaterials.COAL_MATERIAL, AnimalArmorItem.Type.EQUESTRIAN,false, new Item.Settings().maxCount(1)),"coal_horse_armor");
    public static final Item GOLDEN_APPLE = register(new SwordItem(StoneMaterial.INSTANCE,new Item.Settings().maxCount(1).maxDamage(131).rarity(Rarity.RARE)),"golden_apple");
    public static final Item RAW_COAL = register(new Item(new Item.Settings()),"raw_coal");
    public static final Item INVERT_BUCKET = register(new InvertBucketItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET,new Item.Settings().maxCount(1)),"invert_bucket");
    public static final Item INVERT_BOAT = register(new InvertBoatItem(ArmorMaterials.LEATHER,ArmorItem.Type.HELMET,new Item.Settings().maxCount(1)),"invert_boat");
    public static final Item COW_PLANT = register(new AliasedBlockItem(ModBlock.COW_PLANT,new Item.Settings()),"cow_plant");
    public static final Item FLINT_AND_STEEL_CUSTOM =register(new FlintAndSteelCustomItem(new Item.Settings().maxCount(1).maxDamage(64)),"flint_and_steel");
    public static final Item WITHER_SPAWN_EGG = register(new SpawnEggItem(EntityType.WITHER, // 实体类型为凋零
            0x303030, 0xA0A0A0, new Item.Settings()), "wither_spawn_egg");
    public static final Item BLAZE_PEARL = register(new EnderPearlItem(new Item.Settings().maxCount(16)),"blaze_pearl");
    public static final Item ENDER_ROD = register(new Item(new Item.Settings()),"ender_rod");
    public static final Item ENDER_POWDER = register(new Item(new Item.Settings()),"ender_powder");
    public static final Item PURPLE_BOAT = register(new PurpleBoatItem(new Item.Settings()),"purple_boat");
    public static final Item COD_FISHING_ROD = register(new CodFishingRodItem(new Item.Settings().maxCount(1).maxDamage(64)),"cod_fishing_rod");
    public static final Item TRANSPARENT_STICK= register(new Item(new Item.Settings()),"transparent_stick");
    public static final Item TRANSPARENT_SWORD= register(new SwordItem(ToolMaterials.WOOD,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.WOOD, 3, -2.4F))),"transparent_sword");
    public static final Item TRANSPARENT_AXE= register(new AxeItem(ToolMaterials.WOOD, (new Item.Settings()).attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.WOOD, 6.0F, -3.2F))),"transparent_axe");
    public static final Item TRANSPARENT_PICKAXE = register(new PickaxeItem(ToolMaterials.WOOD, (new Item.Settings()).attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.WOOD, 1.0F, -2.8F))),"transparent_pickaxe");
    public static final Item TRANSPARENT_BUCKET = register(new TransparentBucketItem(Fluids.EMPTY, new Item.Settings().maxCount(16)),"transparent_bucket");
    public static final Item TRANSPARENT_BUCKET_WATER = register(new TransparentBucketItem(Fluids.WATER, new Item.Settings().maxCount(1).recipeRemainder(TRANSPARENT_BUCKET)),"water_transparent_bucket");
    public static final Item TRANSPARENT_BUCKET_LAVA = register(new TransparentBucketItem(Fluids.LAVA, new Item.Settings().maxCount(1).recipeRemainder(TRANSPARENT_BUCKET)),"lava_transparent_bucket");
    public static final Item CONCRETE_PICKAXE = register(new PickaxeItem(ToolMaterials.IRON, (new Item.Settings()).attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.IRON, 1.0F, -2.8F))),"concrete_pickaxe");
    public static final Item CACTUS_HELMET = register(new ArmorItem(ModArmorMaterials.CACTUS_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))), "cactus_helmet");
    public static final Item CACTUS_CHESTPLATE = register(new ArmorItem(ModArmorMaterials.CACTUS_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))), "cactus_chestplate");
    public static final Item CACTUS_LEGGINGS = register(new ArmorItem(ModArmorMaterials.CACTUS_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))), "cactus_leggings");
    public static final Item CACTUS_BOOTS = register(new ArmorItem(ModArmorMaterials.CACTUS_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))), "cactus_boots");
    public static final Item CACTUS_SWORD= register(new SwordItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.STONE, 3, -2.4F))),"cactus_sword");
    public static final Item CACTUS_AXE= register(new AxeItem(ToolMaterials.STONE, (new Item.Settings()).attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.STONE, 6.0F, -3.2F))),"cactus_axe");
    public static final Item CACTUS_PICKAXE = register(new PickaxeItem(ToolMaterials.STONE, (new Item.Settings()).attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.STONE, 1.0F, -2.8F))),"cactus_pickaxe");
    public static final Item MAIN_WORLD_INGOT = register(new Item(new Item.Settings()),"main_world_ingot");
    public static final Item MAIN_WORLD_SCRAP = register(new Item(new Item.Settings()),"main_world_scrap");
    public static final Item GOLD_DEBRIS = register(new Item(new Item.Settings()),"gold_debris");
    public static final Item CACTUS_STICK = register(new Item(new Item.Settings()),"cactus_stick");
    public static final Item HUMAN_FLESH = register(new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(3).saturationModifier(0.6F).build())),"human_flesh");
    public static final Item COOKED_HUMAN_FLESH = register(new Item(new Item.Settings().food(new FoodComponent.Builder().nutrition(8).saturationModifier(0.8F).build())),"cooked_human_flesh");
    public static final Item BIT_DIAMOND_SWORD = register(new SwordItem(ToolMaterials.DIAMOND,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))),"bit_diamond_sword");
    public static final Item BIT_DIAMOND_AXE = register(new AxeItem(ToolMaterials.DIAMOND,new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 5.0F, -3.0F))),"bit_diamond_axe");
    public static final Item BIT_DIAMOND_PICKAXE = register(new PickaxeItem(ToolMaterials.DIAMOND, (new Item.Settings()).attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 1.0F, -2.8F))),"bit_diamond_pickaxe");
    public static final Item ENDER_SWORD = register(new EnderSword(ToolMaterials.NETHERITE,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 3, -2.4F)).maxDamage(100)),"ender_sword");
    public static final Item FLAME_SWORD = register(new FlameSword(ToolMaterials.NETHERITE,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 3, -2.4F))),"flame_sword");
    public static final Item BROKEN_BOW = register(new Item(new Item.Settings()),"broken_bow");
    public static final Item WITHER_BOW = register(new BowItem(new Item.Settings().maxDamage(384)),"wither_bow");
    public static final Item MAIN_WORLD_HELMET = register(new ArmorItem(ModArmorMaterials.MAIN_WORD_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))), "main_world_helmet");
    public static final Item MAIN_WORLD_CHESTPLATE = register(new ArmorItem(ModArmorMaterials.MAIN_WORD_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))), "main_world_chestplate");
    public static final Item MAIN_WORLD_LEGGINGS = register(new ArmorItem(ModArmorMaterials.MAIN_WORD_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))), "main_world_leggings");
    public static final Item MAIN_WORLD_BOOTS = register(new ArmorItem(ModArmorMaterials.MAIN_WORD_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))), "main_world_boots");
    public static final Item THE_END_BOW = register(new TheEndBowItem(new Item.Settings().maxDamage(778)),"the_end_bow");
    public static final Item ASHES = register(new Item(new Item.Settings()),"ashes");
    public static final Item APPLE_INGOT = register(new Item((new Item.Settings()).rarity(Rarity.RARE).food(FoodComponents.GOLDEN_APPLE)), "apple_ingot");
    public static final Item APPLE_INGOT_SWORD = register(new SwordItem(StoneMaterial.INSTANCE,new Item.Settings().maxCount(1).maxDamage(131)),"apple_ingot_sword");
    public static final Item GOLD_APPLE_INGOT = register(new Item(new Item.Settings().rarity(Rarity.EPIC).food(FoodComponents.ENCHANTED_GOLDEN_APPLE).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)), "gold_apple_ingot");
    public static final Item GOLD_CARROT_NUGGET = register(new Item(new Item.Settings().food(FoodComponents.GOLDEN_CARROT)), "gold_carrot_nugget");
    public static final Item AIR = register(new Item(new Item.Settings()), "air");
    public static final Item SAND_BLOCK_SPAWN_EGG = register(new SpawnEggItem(CustomBlockEntity.SAND_BLOCK, 0xC1C1C1, 0x494949, new Item.Settings()),"sand_block_spawn_egg");
    public static final Item DIRT_BLOCK_SPAWN_EGG = register(new SpawnEggItem(CustomBlockEntity.DIRT_BLOCK, 0xC1C1C1, 0x494949, new Item.Settings()),"dirt_block_spawn_egg");
    public static final Item DIAMOND_STICK_SWORD = register(new SwordItem(ToolMaterials.DIAMOND,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))),"diamond_stick_sword");
    public static final Item DIAMOND_STICK_AXE = register(new AxeItem(ToolMaterials.DIAMOND,new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 5.0F, -3.0F))),"diamond_stick_axe");
    public static final Item DIAMOND_STICK_PICKAXE = register(new PickaxeItem(ToolMaterials.DIAMOND, (new Item.Settings()).attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 1.0F, -2.8F))),"diamond_stick_pickaxe");
    public static final Item FLINT_AND_STEEL_DIG_FIRE = register(new FlintAndSteelDigFireItem(new Item.Settings().maxCount(1).maxDamage(64)), "flint_and_steel_dig_fire");
    public static final Item COD_SPAWN_EGG = register(new SpawnEggItem(EntityType.COD,0xac976b, 0xddd6c8,new Item.Settings()),"cod_spawn_egg");
    public static final Item NETHER_FISH = register(new Item(new Item.Settings().food(FoodComponents.COD)),"nether_fish");
    public static final Item WARDEN_BUCKET = register(new WardenBucketItem(new Item.Settings().maxCount(1)),"warden_bucket");
    public static final Item GUARDIAN_BUCKET = register(new GuardianBucketItem(new Item.Settings().maxCount(1)), "guardian_bucket");
    public static final Item LOW_ENCHANT_APPLE = register(new Item(new Item.Settings().food(FoodRegister.LOW_ENCHANT_APPLE).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,true).rarity(Rarity.EPIC)), "low_enchant_apple");
    public static final Item COPPER_APPLE = register(new Item(new Item.Settings().food(FoodRegister.COPPER_APPLE).rarity(Rarity.RARE)), "copper_apple");
    public static final Item DIRT_STICK = register(new Item(new Item.Settings()), "dirt_stick");
    public static final Item DIRT_SWORD = register(new SwordItem(ToolMaterials.WOOD,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.WOOD, 3, -2.4F))), "dirt_sword");
    public static final Item DIRT_AXE = register(new AxeItem(ToolMaterials.WOOD, (new Item.Settings()).attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.WOOD, 6.0F, -3.2F))), "dirt_axe");
    public static final Item DIRT_PICKAXE = register(new PickaxeItem(ToolMaterials.WOOD, (new Item.Settings()).attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.WOOD, 1.0F, -2.8F))), "dirt_pickaxe");
    public static final Item DIRT_BUCKET = register(new DirtBucketItem(Fluids.EMPTY, new Item.Settings().maxCount(16)), "dirt_bucket");
    public static final Item DIRT_LAVA_BUCKET = register(new DirtBucketItem(Fluids.LAVA, new Item.Settings().maxCount(1)), "dirt_lava_bucket");
    public static final Item DIRT_WATER_BUCKET = register(new DirtBucketItem(Fluids.WATER, new Item.Settings().maxCount(1)), "dirt_water_bucket");
    public static final Item FRIED_EGG = register(new Item(new Item.Settings().food(FoodRegister.FRIED_EGG)), "fried_egg");
    public static final Item FLIP_BOW= register(new Item(new Item.Settings().maxCount(1)), "flip_bow");
    public static final Item FLIP_FISHING_ROD = register(new Item(new Item.Settings().maxCount(1)), "flip_fishing_rod");
    public static final Item COAL_INGOT = register(new Item(new Item.Settings()), "coal_ingot");
    public static final Item LEAF_INGOT = register(new Item(new Item.Settings()), "leaf_ingot");
    public static final Item LEAF_HELMET = register(new ArmorItem(ModArmorMaterials.LEAF_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))), "leaf_helmet");
    public static final Item LEAF_CHESTPLATE = register(new ArmorItem(ModArmorMaterials.LEAF_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))), "leaf_chestplate");
    public static final Item LEAF_LEGGINGS = register(new ArmorItem(ModArmorMaterials.LEAF_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))), "leaf_leggings");
    public static final Item LEAF_BOOTS = register(new ArmorItem(ModArmorMaterials.LEAF_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))), "leaf_boots");
    public static final Item FLIP_WHITE_BED = register(new BedItem(ModBlock.FLIP_WHITE_BED,new Item.Settings().maxCount(1)), "flip_white_bed");
    public static final Item GLOW_BERRY_TORCH = register(new VerticallyAttachableBlockItem(ModBlock.BLOW_BERRY_TORCH_BLOCK, ModBlock.BLOW_BERRY_WALL_TORCH, new Item.Settings(), Direction.DOWN),"glow_berry_torch");
    public static final Item INVERT_RED_BED = register(new BedItem(ModBlock.INVERT_RED_BED,new Item.Settings().maxCount(1)), "invert_red_bed");
    public static final Item COPPER_PICKAXE = register(new PickaxeItem(ToolMaterials.DIAMOND, (new Item.Settings()).attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 1.0F, -2.8F))), "copper_pickaxe");
    public static final Item LICHEN_PICKAXE = register(new PickaxeItem(ToolMaterials.DIAMOND, (new Item.Settings()).attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.DIAMOND, 1.0F, -2.8F))), "lichen_pickaxe");
    public static final Item LICHEN_SWORD = register(new SwordItem(ToolMaterials.DIAMOND,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4F))), "lichen_sword");
    public static final Item LICHEN_HELMET = register(new LightEmittingArmorItem(ModArmorMaterials.LICHEN_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15)), 10), "lichen_helmet");
    public static final Item LICHEN_CHESTPLATE = register(new LightEmittingArmorItem(ModArmorMaterials.LICHEN_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15)), 10), "lichen_chestplate");
    public static final Item LICHEN_LEGGINGS = register(new LightEmittingArmorItem(ModArmorMaterials.LICHEN_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15)), 10), "lichen_leggings");
    public static final Item LICHEN_BOOTS = register(new LightEmittingArmorItem(ModArmorMaterials.LICHEN_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15)), 10), "lichen_boots");
    public static final Item ENCHANT_GOLD_CARROT = register(new Item(new Item.Settings().food(FoodComponents.GOLDEN_APPLE).component(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,true).rarity(Rarity.EPIC)), "enchanted_golden_carrot");
    public static final Item BLAZE_EYE = register(new Item(new Item.Settings()), "blaze_eye");
    public static final Item CAKE = register(new BlockItem(ModBlock.CAKE,(new Item.Settings()).maxCount(1)), "cake");

    public static void initialize() {
        ModRecipes.registerRecipes();
        PotionRegister.init(); //药水初始化
        Registry.register(Registries.ITEM_GROUP, FUN_ITEM_GROUP_KEY, ABSTRACT_CUSTOM_ITEM_GROUP);
        Registry.register(Registries.ITEM_GROUP, TRANSPARENT_GROUP_KEY, TRANSPARENT_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(FUN_ITEM_GROUP_KEY).register(itemGroup->{
            itemGroup.add(AIR_PICKAXE);
            itemGroup.add(STONE_STICK);
            itemGroup.add(ModBlock.STONE_CRAFT_TABLE.asItem());
            itemGroup.add(INVERT_MINECART);
            itemGroup.add(MINECART_HAT);
            itemGroup.add(GRASS);
            itemGroup.add(WHEAT);
            itemGroup.add(STONE_SWORD);
            itemGroup.add(STONE_AXE);
            itemGroup.add(STONE_PICKAXE);
            itemGroup.add(SOUL_TORCH);
            itemGroup.add(COAL_HELMET);
            itemGroup.add(COAL_CHESTPLATE);
            itemGroup.add(COAL_LEGGINGS);
            itemGroup.add(COAL_BOOTS);
            itemGroup.add(HORSE_ARMOR_NETHERITE);
            itemGroup.add(GOLDEN_APPLE);
            itemGroup.add(WITHER_SPAWN_EGG);
            itemGroup.add(ModBlock.CACTUS.asItem());
            itemGroup.add(ModBlock.NETHER_PORTAL.asItem());
            itemGroup.add(ModBlock.FIRE.asItem());
            itemGroup.add(RAW_COAL);
            itemGroup.add(ModBlock.COAL_ORE);
            itemGroup.add(INVERT_BUCKET);
            itemGroup.add(INVERT_BOAT);
            itemGroup.add(COW_PLANT);
            itemGroup.add(WHEAT_MEAL);
            itemGroup.add(ModBlock.REACTOR);
            itemGroup.add(FLINT_AND_STEEL_CUSTOM);
            itemGroup.add(ENDER_ROD);
            itemGroup.add(ENDER_POWDER);
            itemGroup.add(BLAZE_PEARL);
            itemGroup.add(PURPLE_BOAT);
            itemGroup.add(COD_FISHING_ROD);
            itemGroup.add(ModBlock.CONCRETE);
            itemGroup.add(CONCRETE_PICKAXE);
            itemGroup.add(CACTUS_HELMET);
            itemGroup.add(CACTUS_CHESTPLATE);
            itemGroup.add(CACTUS_LEGGINGS);
            itemGroup.add(CACTUS_BOOTS);
            itemGroup.add(CACTUS_STICK);
            itemGroup.add(CACTUS_SWORD);
            itemGroup.add(CACTUS_AXE);
            itemGroup.add(CACTUS_PICKAXE);
            itemGroup.add(MAIN_WORLD_INGOT);
            itemGroup.add(MAIN_WORLD_SCRAP);
            itemGroup.add(ModBlock.MAIN_WORLD_DEBRIS);
            itemGroup.add(GOLD_DEBRIS);
            itemGroup.add(ModBlock.HORIZONTAL_NETHER_PORTAL);
            itemGroup.add(ModBlock.OBSIDIAN);
            itemGroup.add(HUMAN_FLESH);
            itemGroup.add(COOKED_HUMAN_FLESH);
            itemGroup.add(BIG_TORCH);
            itemGroup.add(BIT_DIAMOND_SWORD);
            itemGroup.add(BIT_DIAMOND_AXE);
            itemGroup.add(BIT_DIAMOND_PICKAXE);
            itemGroup.add(ENDER_SWORD);
            itemGroup.add(FLAME_SWORD);
            itemGroup.add(BROKEN_BOW);
            itemGroup.add(WITHER_BOW);
            itemGroup.add(MAIN_WORLD_HELMET);
            itemGroup.add(MAIN_WORLD_CHESTPLATE);
            itemGroup.add(MAIN_WORLD_LEGGINGS);
            itemGroup.add(MAIN_WORLD_BOOTS);
            itemGroup.add(ASHES);
            itemGroup.add(THE_END_BOW);
            itemGroup.add(APPLE_INGOT);
            itemGroup.add(APPLE_INGOT_SWORD);
            itemGroup.add(GOLD_APPLE_INGOT);
            itemGroup.add(ModBlock.APPLE_BLOCK);
            itemGroup.add(GOLD_CARROT_NUGGET);
            itemGroup.add(SAND_BLOCK_SPAWN_EGG);
            itemGroup.add(DIRT_BLOCK_SPAWN_EGG);
            itemGroup.add(DIAMOND_STICK_SWORD);
            itemGroup.add(DIAMOND_STICK_AXE);
            itemGroup.add(DIAMOND_STICK_PICKAXE);
            itemGroup.add(ModBlock.WOODEN_ANVIL);
            itemGroup.add(FLINT_AND_STEEL_DIG_FIRE);
            itemGroup.add(COD_SPAWN_EGG);
            itemGroup.add(NETHER_FISH);
            itemGroup.add(GUARDIAN_BUCKET);
            itemGroup.add(WARDEN_BUCKET);
            itemGroup.add(LOW_ENCHANT_APPLE);
            itemGroup.add(COPPER_APPLE);
            itemGroup.add(ModBlock.DIRT_CRAFT_TABLE);
            itemGroup.add(DIRT_STICK);
            itemGroup.add(DIRT_SWORD);
            itemGroup.add(DIRT_AXE);
            itemGroup.add(DIRT_PICKAXE);
            itemGroup.add(DIRT_BUCKET);
            itemGroup.add(DIRT_LAVA_BUCKET);
            itemGroup.add(DIRT_WATER_BUCKET);
            itemGroup.add(FRIED_EGG);
            itemGroup.add(FLIP_WHITE_BED);
            itemGroup.add(FLIP_BOW);
            itemGroup.add(FLIP_FISHING_ROD);
            itemGroup.add(COAL_INGOT);
            itemGroup.add(ModBlock.LEAF_ORE);
            itemGroup.add(LEAF_INGOT);
            itemGroup.add(LEAF_HELMET);
            itemGroup.add(LEAF_CHESTPLATE);
            itemGroup.add(LEAF_LEGGINGS);
            itemGroup.add(LEAF_BOOTS);
            itemGroup.add(GLOW_BERRY_TORCH);
            itemGroup.add(INVERT_RED_BED);
            itemGroup.add(COPPER_PICKAXE);
            itemGroup.add(LICHEN_PICKAXE);
            itemGroup.add(LICHEN_SWORD);
            itemGroup.add(LICHEN_HELMET);
            itemGroup.add(LICHEN_CHESTPLATE);
            itemGroup.add(LICHEN_LEGGINGS);
            itemGroup.add(LICHEN_BOOTS);
            itemGroup.add(ENCHANT_GOLD_CARROT);
            itemGroup.add(BLAZE_EYE);
            itemGroup.add(CAKE);
        });
        ItemGroupEvents.modifyEntriesEvent(TRANSPARENT_GROUP_KEY).register(itemGroup->{
            itemGroup.add(ModBlock.TRANSPARENT_BLOCK);
            itemGroup.add(TRANSPARENT_STICK);
            itemGroup.add(TRANSPARENT_SWORD);
            itemGroup.add(TRANSPARENT_AXE);
            itemGroup.add(TRANSPARENT_PICKAXE);
            itemGroup.add(ModBlock.AIR_CRAFT_TABLE);
            itemGroup.add(TRANSPARENT_BUCKET);
            itemGroup.add(TRANSPARENT_BUCKET_WATER);
            itemGroup.add(TRANSPARENT_BUCKET_LAVA);
            itemGroup.add(AIR);
        });
    }

    //物品组注册
    public static final RegistryKey<ItemGroup> FUN_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "item_group"));
    public static final RegistryKey<ItemGroup> TRANSPARENT_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "transparent_group"));
    public static final ItemGroup ABSTRACT_CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.ARROW))
            .displayName(Text.translatable("itemGroup.soybean"))
            .build();
    public static final ItemGroup TRANSPARENT_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.GLASS))
            .displayName(Text.translatable("transparent_item_group.soybean"))
            .build();


    //通用注册
    public static Item register(Item item, String id){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
//        return registerKey(RegistryKey.of(Registries.ITEM.getKey(), itemID), item);
    }

//    public static Item registerKey(RegistryKey<Item> key, Item item) {
//        if (item instanceof BlockItem) {
//            ((BlockItem)item).appendBlocks(Item.BLOCK_ITEMS, item);
//        }
//
//        return (Item)Registry.register(Registries.ITEM, key, item);
//    }
    //注册原版方块
    public static Block registerForMinecraft(String id, Block block) {
        Identifier itemID = Identifier.of(InitValue.MINECRAFT, id);
        return Registry.register(Registries.BLOCK, itemID, block);
    }

}
