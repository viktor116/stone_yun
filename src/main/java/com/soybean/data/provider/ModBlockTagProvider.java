package com.soybean.data.provider;

import com.soybean.block.ModBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

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
    }
}
