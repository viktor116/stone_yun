package com.soybean.data.provider;

import com.soybean.block.ModBlock;
import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.FIRE)
                .add(ModBlock.FIRE);
        getOrCreateTagBuilder(BlockTags.WALL_POST_OVERRIDE)
                .add(ModBlock.SOUL_WALL_TORCH);
        getOrCreateTagBuilder(BlockTags.STANDING_SIGNS)
                .add(ModBlock.SOUL_TORCH_BLOCK);
        getOrCreateTagBuilder(BlockTags.PORTALS)
                .add(ModBlock.NETHER_PORTAL);
        getOrCreateTagBuilder(BlockTags.COAL_ORES)
                .add(ModBlock.COAL_ORE);
        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlock.CONCRETE);
        // 矿石通用标签（可以视情况添加）
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlock.MAIN_WORLD_DEBRIS);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlock.MAIN_WORLD_DEBRIS)
                .add(ModBlock.COAL_ORE)
                .add(ModBlock.ALUMINUM_ORE)
                .add(ModBlock.CONCRETE)
                .add(ModBlock.COMPRESS_GOLD_BLOCK)
                .add(ModBlock.LICHEN_DIAMOND_ORE)
                .add(ModBlock.DEEPSLATE_DIAMOND_ORE);
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlock.LICHEN_DIAMOND_ORE)
                .add(ModBlock.DEEPSLATE_DIAMOND_ORE);
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(ModBlock.FALL_OAK_LOG)
                .add(ModBlock.FALL_OAK_PLANKS)
                .add(ModBlock.FALL_CRAFTING_TABLE);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlock.FALL_FURNACE);
        getOrCreateTagBuilder(BlockTags.LOGS)
                .add(ModBlock.FALL_OAK_LOG);
        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(ModBlock.FALL_OAK_PLANKS);
        getOrCreateTagBuilder(BlockTags.ANVIL)
                .add(ModBlock.WOODEN_ANVIL)
                .add(ModBlock.STONE_ANVIL);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlock.MAIN_RESPAWN_ANCHOR);

    }
}
