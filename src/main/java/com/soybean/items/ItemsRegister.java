package com.soybean.items;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.items.armor.ModArmorMaterials;
import com.soybean.items.custom.*;
import com.soybean.items.custom.InvertBoatItem;
import com.soybean.items.item.CodFishingRodItem;
import com.soybean.items.item.PurpleBoatItem;
import com.soybean.items.item.TransparentBucketItem;
import com.soybean.items.item.UnbreakablePickaxeItem;
import com.soybean.items.material.AirMaterial;
import com.soybean.items.material.StoneMaterial;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
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

    public static void initialize() {
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
            itemGroup.add(CACTUS_SWORD);
            itemGroup.add(CACTUS_AXE);
            itemGroup.add(CACTUS_PICKAXE);
            itemGroup.add(MAIN_WORLD_INGOT);
            itemGroup.add(MAIN_WORLD_SCRAP);
            itemGroup.add(ModBlock.MAIN_WORLD_DEBRIS);
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
