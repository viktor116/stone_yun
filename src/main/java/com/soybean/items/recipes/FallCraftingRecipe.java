package com.soybean.items.recipes;

import net.minecraft.item.ItemStack;

import java.util.List;

public class FallCraftingRecipe {

    private final net.minecraft.recipe.Ingredient input;
    private final int consumeCount;
    private final boolean shaped;
    private final ItemStack[] gridResults;
    private final List<ResultEntry> flatResults;

    public FallCraftingRecipe(net.minecraft.recipe.Ingredient input, int consumeCount, ItemStack[] gridResults) {
        this.input = input;
        this.consumeCount = consumeCount;
        this.shaped = true;
        this.gridResults = new ItemStack[9];
        System.arraycopy(gridResults, 0, this.gridResults, 0, 9);
        this.flatResults = null;
    }

    public FallCraftingRecipe(net.minecraft.recipe.Ingredient input, int consumeCount, List<ResultEntry> flatResults) {
        this.input = input;
        this.consumeCount = consumeCount;
        this.shaped = false;
        this.gridResults = null;
        this.flatResults = flatResults;
    }

    public boolean matches(ItemStack stack) {
        return input.test(stack);
    }

    public int getConsumeCount() {
        return consumeCount;
    }

    public boolean isShaped() {
        return shaped;
    }

    public ItemStack[] getGridResults() {
        if (shaped) {
            ItemStack[] copy = new ItemStack[9];
            for (int i = 0; i < 9; i++) {
                copy[i] = gridResults[i] != null ? gridResults[i].copy() : ItemStack.EMPTY;
            }
            return copy;
        } else {
            ItemStack[] slots = new ItemStack[9];
            for (int i = 0; i < 9 && i < flatResults.size(); i++) {
                ResultEntry e = flatResults.get(i);
                ItemStack s = e.item.copy();
                s.setCount(e.count);
                slots[i] = s;
            }
            for (int i = flatResults.size(); i < 9; i++) {
                slots[i] = ItemStack.EMPTY;
            }
            return slots;
        }
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
