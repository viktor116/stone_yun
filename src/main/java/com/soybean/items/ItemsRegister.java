package com.soybean.items;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.items.custom.GrassItem;
import com.soybean.items.custom.InvertMinecartItem;
import com.soybean.items.custom.MinecartHatItem;
import com.soybean.items.custom.WheatItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
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
    //public static final Block STONE = register("stone", new Block(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).strength(1.5F, 6.0F)));
    public static final Item INVERT_MINECART = register(new InvertMinecartItem(new Item.Settings().maxCount(1)), "invert_minecart");
    public static final Item MINECART_HAT = register(new MinecartHatItem(), "minecart_hat");
    public static final Item GRASS = register(new GrassItem(new Item.Settings()),"grass");
    public static final Item WHEAT = register(new WheatItem(new Item.Settings()),"wheat");
    public static final Item STONE_SWORD = register(new SwordItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.STONE, 3, -2.4F)).maxCount(1)),"stone_sword");
    public static final Item STONE_AXE = register(new AxeItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(AxeItem.createAttributeModifiers(ToolMaterials.STONE, 7F, -3.2F)).maxCount(1)),"stone_axe");
    public static final Item STONE_PICKAXE = register(new PickaxeItem(ToolMaterials.STONE,new Item.Settings().attributeModifiers(PickaxeItem.createAttributeModifiers(ToolMaterials.STONE, 1.0F, -2.8F)).maxCount(1)),"stone_pickaxe");
    public static final Item SOUL_TORCH = register((BlockItem) (new VerticallyAttachableBlockItem(ModBlock.SOUL_TORCH_BLOCK, ModBlock.SOUL_WALL_TORCH, new Item.Settings(), Direction.DOWN)),"soul_torch");
    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP, FUN_ITEM_GROUP_KEY, ABSTRACT_CUSTOM_ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(FUN_ITEM_GROUP_KEY).register(itemGroup->{
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
        return (Block)Registry.register(Registries.BLOCK, id, block);
    }

}
