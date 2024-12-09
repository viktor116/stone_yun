package com.soybean.data.provider;

import com.soybean.block.ModBlock;
import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.util.Identifier;

import java.util.Optional;

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
        blockStateModelGenerator.registerSimpleCubeAll(ModBlock.REACTOR);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ItemsRegister.GOLDEN_APPLE,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.HORSE_ARMOR_NETHERITE, Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.RAW_COAL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.INVERT_BUCKET,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.INVERT_BOAT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.COW_PLANT,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.WHEAT_MEAL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.FLINT_AND_STEEL_CUSTOM,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.BLAZE_PEARL,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.ENDER_ROD,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.ENDER_POWDER,Models.GENERATED);
        itemModelGenerator.register(ItemsRegister.PURPLE_BOAT,Models.GENERATED);
    }
}
