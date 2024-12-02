package com.soybean.data.provider;

import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

/**
 * @author soybean
 * @date 2024/11/13 14:56
 * @description
 */
public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemsRegister.GOLDEN_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.HORSE_ARMOR_NETHERITE, Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.RAW_COAL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.INVERT_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.INVERT_BOAT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.COW_PLANT,Models.GENERATED);
    }
}
