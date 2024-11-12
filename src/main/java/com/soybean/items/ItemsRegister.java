package com.soybean.items;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.items.armor.ModArmorMaterials;
import com.soybean.items.custom.GrassItem;
import com.soybean.items.custom.InvertMinecartItem;
import com.soybean.items.custom.MinecartHatItem;
import com.soybean.items.custom.WheatItem;
import com.soybean.items.item.UnbreakablePickaxeItem;
import com.soybean.items.material.AirMaterial;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
    public static final Item STONE_SWORD = register(new SwordItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.STONE, 3, -2.4F)).maxCount(1)),"stone_sword");
    public static final Item STONE_AXE = register(new AxeItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.STONE, 7F, -3.2F)).maxCount(1)),"stone_axe");
    public static final Item STONE_PICKAXE = register(new PickaxeItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.STONE, 1.0F, -2.8F)).maxCount(1)),"stone_pickaxe");
    public static final Item SOUL_TORCH = register((BlockItem) (new VerticallyAttachableBlockItem(ModBlock.SOUL_TORCH_BLOCK, ModBlock.SOUL_WALL_TORCH, new Item.Settings(), Direction.DOWN)),"soul_torch");
    public static final Item COAL_HELMET = register(new ArmorItem(ModArmorMaterials.COAL_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(15))), "coal_helmet");
    public static final Item COAL_CHESTPLATE = register(new ArmorItem(ModArmorMaterials.COAL_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(15))), "coal_chestplate");
    public static final Item COAL_LEGGINGS = register(new ArmorItem(ModArmorMaterials.COAL_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(15))), "coal_leggings");
    public static final Item COAL_BOOTS = register(new ArmorItem(ModArmorMaterials.COAL_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(15))), "coal_boots");
    public static final Item AIR_PICKAXE = register(new UnbreakablePickaxeItem(AirMaterial.INSTANCE,new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(AirMaterial.INSTANCE, 999.0F, 999F)).maxCount(1).maxDamage(-1)),"air_pickaxe");
    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, FUN_ITEM_GROUP_KEY, ABSTRACT_CUSTOM_ITEM_GROUP);
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
        });
    }

    //物品组注册
    public static final RegistryKey<ItemGroup> FUN_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(InitValue.MOD_ID, "item_group"));
    public static final ItemGroup ABSTRACT_CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.ARROW))
            .displayName(Text.translatable("itemGroup.soybean"))
            .build();


    //通用注册
    public static Item register(Item item,String id){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        return Registry.register(Registries.ITEM, itemID, item);
    }

    //注册原版方块
    public static Block register(String id, Block block) {
        Identifier itemID = Identifier.of(InitValue.MINECRAFT, id);
        return Registry.register(Registries.BLOCK, itemID, block);
    }

}
