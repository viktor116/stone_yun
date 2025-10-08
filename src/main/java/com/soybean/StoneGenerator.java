package com.soybean;

import com.soybean.data.generator.ModEnchantmentGenerator;
import com.soybean.data.provider.ModBlockLootTableProvider;
import com.soybean.data.provider.ModBlockTagProvider;
import com.soybean.data.provider.ModItemTagProvider;
import com.soybean.data.provider.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class StoneGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModEnchantmentGenerator::new);
        pack.addProvider(c::new);
        pack.addProvider(ModItemTagProvider::new);
        pack.addProvider(ModBlockLootTableProvider::new);
        pack.addProvider(ModBlockTagProvider::new);
    }
}
