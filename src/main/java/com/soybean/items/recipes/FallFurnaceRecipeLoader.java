package com.soybean.items.recipes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.soybean.config.InitValue;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FallFurnaceRecipeLoader implements SimpleSynchronousResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private static final String DIRECTORY = "recipes/fall_furnace";

    private static final List<FallFurnaceRecipe> RECIPES = new ArrayList<>();

    public static Optional<FallFurnaceRecipe> findRecipe(ItemStack input) {
        for (FallFurnaceRecipe recipe : RECIPES) {
            if (recipe.matches(input)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    @Override
    public Identifier getFabricId() {
        return InitValue.id("fall_furnace_recipes");
    }

    @Override
    public void reload(ResourceManager manager) {
        RECIPES.clear();

        for (Map.Entry<Identifier, Resource> entry : manager.findResources(DIRECTORY, id -> id.toString().endsWith(".json")).entrySet()) {
            Identifier id = entry.getKey();
            try (InputStream is = entry.getValue().getInputStream();
                 InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);

                String type = json.has("type") ? json.get("type").getAsString() : "";
                if (!type.equals(InitValue.MOD_ID + ":fall_furnace")) {
                    continue;
                }

                Ingredient input = parseIngredient(json.get("input").getAsJsonObject());
                if (input == null) continue;

                int cookTime = json.has("cookTime") ? json.get("cookTime").getAsInt() : 200;

                List<FallFurnaceRecipe.ResultEntry> results = parseResults(json);
                if (results != null && !results.isEmpty()) {
                    RECIPES.add(new FallFurnaceRecipe(input, results, cookTime));
                }
            } catch (Exception e) {
                InitValue.LOGGER.error("Failed to load fall furnace recipe: {}", id, e);
            }
        }

        InitValue.LOGGER.info("Loaded {} fall furnace recipes", RECIPES.size());
    }

    private Ingredient parseIngredient(JsonObject json) {
        if (json.has("item")) {
            String itemId = json.get("item").getAsString();
            Item item = Registries.ITEM.get(Identifier.of(itemId));
            if (item != null && item != net.minecraft.item.Items.AIR) {
                return Ingredient.ofItems(item);
            }
        }
        return null;
    }

    private List<FallFurnaceRecipe.ResultEntry> parseResults(JsonObject json) {
        if (!json.has("results")) return null;
        List<FallFurnaceRecipe.ResultEntry> results = new ArrayList<>();
        JsonArray resultsArray = json.getAsJsonArray("results");
        for (int i = 0; i < resultsArray.size(); i++) {
            JsonObject obj = resultsArray.get(i).getAsJsonObject();
            String itemId = obj.get("item").getAsString();
            Item item = Registries.ITEM.get(Identifier.of(itemId));
            if (item == null || item == net.minecraft.item.Items.AIR) {
                InitValue.LOGGER.warn("[FallFurnaceRecipeLoader] Skipping unknown result item: {}", itemId);
                continue;
            }
            int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
            results.add(new FallFurnaceRecipe.ResultEntry(new ItemStack(item), count));
        }
        return results;
    }
}
