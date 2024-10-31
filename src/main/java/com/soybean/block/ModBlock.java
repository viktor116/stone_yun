package com.soybean.block;

import com.soybean.block.custom.StoneCraftTableBlock;
import com.soybean.config.InitValue;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/10/24 12:10
 * @description
 */
public class ModBlock {
    public static final Block STONE_CRAFT_TABLE =  register("stone_crafting_table", new StoneCraftTableBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASS).strength(2.5F).sounds(BlockSoundGroup.STONE).burnable()),true);
    public static void initialize(){

    }
    public static void initializeClient(){
        BlockRenderLayerMap.INSTANCE.putBlock(STONE_CRAFT_TABLE, RenderLayer.getCutout());

    }

    public static Block register(String id, Block block ,boolean shouldRegisterItem){
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, itemID, blockItem);
        }
        return Registry.register(Registries.BLOCK, itemID, block);
    }
}
