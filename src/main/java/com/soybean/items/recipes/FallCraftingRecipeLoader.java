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

public class FallCraftingRecipeLoader implements SimpleSynchronousResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private static final String DIRECTORY = "recipes/fall_crafting";

    private static final List<FallCraftingRecipe> RECIPES = new ArrayList<>();

    public static List<FallCraftingRecipe> getRecipes() {
        return RECIPES;
    }

    public static Optional<FallCraftingRecipe> findRecipe(ItemStack input) {
        for (FallCraftingRecipe recipe : RECIPES) {
            if (recipe.matches(input)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    @Override
    public Identifier getFabricId() {
        return InitValue.id("fall_crafting_recipes");
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
                if (!type.equals(InitValue.MOD_ID + ":fall_crafting")) {
                    continue;
                }

                Ingredient input = parseIngredient(json.get("input").getAsJsonObject());
                if (input == null) continue;

                int consumeCount = json.has("consume") ? json.get("consume").getAsInt() : 1;

                String mode = json.has("mode") ? json.get("mode").getAsString() : "shapeless";

                if ("shaped".equals(mode) && json.has("pattern")) {
                    ItemStack[] grid = parseShaped(json);
                    if (grid != null) {
                        RECIPES.add(new FallCraftingRecipe(input, consumeCount, grid));
                    }
                } else {
                    List<FallCraftingRecipe.ResultEntry> results = parseShapeless(json);
                    if (results != null) {
                        RECIPES.add(new FallCraftingRecipe(input, consumeCount, results));
                    }
                }
            } catch (Exception e) {
                InitValue.LOGGER.error("Failed to load fall crafting recipe: {}", id, e);
            }
        }

        InitValue.LOGGER.info("Loaded {} fall crafting recipes", RECIPES.size());
    }

    private Ingredient parseIngredient(JsonObject json) {
        if (json.has("item")) {
            String itemId = json.get("item").getAsString();
            Item item = Registries.ITEM.get(Identifier.of(itemId));
            if (item != null) {
                return Ingredient.ofItems(item);
            }
        }
        return null;
    }

    private List<FallCraftingRecipe.ResultEntry> parseShapeless(JsonObject json) {
        if (!json.has("results")) return null;
        List<FallCraftingRecipe.ResultEntry> results = new ArrayList<>();
        JsonArray resultsArray = json.getAsJsonArray("results");
        for (int i = 0; i < resultsArray.size(); i++) {
            JsonObject obj = resultsArray.get(i).getAsJsonObject();
            String itemId = obj.get("item").getAsString();
            Item item = Registries.ITEM.get(Identifier.of(itemId));
            if (item == null) continue;
            int count = obj.has("count") ? obj.get("count").getAsInt() : 1;
            results.add(new FallCraftingRecipe.ResultEntry(new ItemStack(item), count));
        }
        return results;
    }

    private ItemStack[] parseShaped(JsonObject json) {
        ItemStack[] grid = new ItemStack[9];
        JsonArray pattern = json.getAsJsonArray("pattern");

        int startRow = 3, endRow = -1, startCol = 3, endCol = -1;
        String[] rows = new String[3];
        for (int i = 0; i < 3 && i < pattern.size(); i++) {
            rows[i] = pattern.get(i).getAsString();
            if (rows[i].length() < 3) {
                rows[i] = rows[i] + "   ".substring(0, 3 - rows[i].length());
            }
            for (int j = 0; j < 3; j++) {
                if (rows[i].charAt(j) != ' ') {
                    startRow = Math.min(startRow, i);
                    endRow = Math.max(endRow, i);
                    startCol = Math.min(startCol, j);
                    endCol = Math.max(endCol, j);
                }
            }
        }

        if (endRow < 0) return null;

        JsonObject key = json.has("key") ? json.getAsJsonObject("key") : new JsonObject();
        Map<Character, ItemStack> keyMap = new HashMap<>();
        for (Map.Entry<String, com.google.gson.JsonElement> e : key.entrySet()) {
            String itemId = e.getValue().getAsString();
            Item item = Registries.ITEM.get(Identifier.of(itemId));
            if (item != null) {
                int count = 1;
                if (e.getValue().isJsonObject() && e.getValue().getAsJsonObject().has("count")) {
                    count = e.getValue().getAsJsonObject().get("count").getAsInt();
                }
                keyMap.put(e.getKey().charAt(0), new ItemStack(item, count));
            }
        }

        int height = endRow - startRow + 1;
        int width = endCol - startCol + 1;
        int offX = (3 - width) / 2;
        int offY = (3 - height) / 2;

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char ch = rows[startRow + r].charAt(startCol + c);
                if (ch != ' ' && keyMap.containsKey(ch)) {
                    grid[(offY + r) * 3 + (offX + c)] = keyMap.get(ch).copy();
                }
            }
        }

        return grid;
    }
}
