package com.soybean.items.recipes;

import com.soybean.config.InitValue;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {

    public static final SpecialRecipeSerializer<WaterLavaToCobblestoneRecipe> WATER_LAVA_TO_COBBLESTONE =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    Identifier.of(InitValue.MOD_ID, "water_lava_to_cobblestone"),
                    new SpecialRecipeSerializer<>(WaterLavaToCobblestoneRecipe::new)
            );

    public static void registerRecipes() {
        InitValue.LOGGER.info("配方注册成功...");
        // 已被调用
    }
}
