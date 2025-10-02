package com.soybean.data.provider;

import com.soybean.block.ModBlock;
import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

/**
 * @author soybean
 * @date 2025/3/14 11:06
 * @description
 */
public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlock.LEAF_ORE);
        addDrop(ModBlock.DIRT_CRAFT_TABLE);
        addDrop(ModBlock.COMPRESS_GOLD_BLOCK);
        addDrop(ModBlock.BEEF_FURNACE);
        addDrop(ModBlock.ALUMINUM_ORE, ItemsRegister.RAW_ALUMINUM);
        addDrop(ModBlock.COMPRESS_OAK_LOG_BLOCK);
        addDrop(ModBlock.BEDROCK);
        addDrop(ModBlock.TRANSLUCENT);
        addDrop(ModBlock.POTATO_BLOCK);
        addDrop(ModBlock.COOKED_POTATO_BLOCK);
    }
}
