package com.soybean.items.recipes;

import net.minecraft.item.ItemStack;

import java.util.List;

public class FallFurnaceRecipe {

    private final net.minecraft.recipe.Ingredient input;
    private final List<ResultEntry> results;
    private final int cookTime;

    public FallFurnaceRecipe(net.minecraft.recipe.Ingredient input, List<ResultEntry> results, int cookTime) {
        this.input = input;
        this.results = results;
        this.cookTime = cookTime;
    }

    public boolean matches(ItemStack stack) {
        return input.test(stack);
    }

    public int getCookTime() {
        return cookTime;
    }

    public List<ResultEntry> getResults() {
        return results;
    }

    public static class ResultEntry {
        public final ItemStack item;
        public final int count;

        public ResultEntry(ItemStack item, int count) {
            this.item = item;
            this.count = count;
        }
    }
}
