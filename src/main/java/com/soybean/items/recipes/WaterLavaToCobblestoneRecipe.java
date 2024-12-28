package com.soybean.items.recipes;

import com.soybean.config.InitValue;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class WaterLavaToCobblestoneRecipe extends SpecialCraftingRecipe {

    public WaterLavaToCobblestoneRecipe(CraftingRecipeCategory category) {
        super(CraftingRecipeCategory.MISC);
    }
    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        boolean hasWaterBucket = false;
        boolean hasLavaBucket = false;

        // 使用 CraftingRecipeInput 提供的方法
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i); // 使用 getStackInSlot 获取物品
            if (stack.getItem() == Items.WATER_BUCKET) hasWaterBucket = true;
            if (stack.getItem() == Items.LAVA_BUCKET) hasLavaBucket = true;
        }

        return hasWaterBucket && hasLavaBucket;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return new ItemStack(Items.COBBLESTONE);
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2; // 需要至少 2 个格子
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup lookup) {
        // 结果为圆石
        return new ItemStack(Items.COBBLESTONE);
    }
    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(input.getSize(), ItemStack.EMPTY);

        // 遍历输入物品，替换水桶和熔岩桶为空桶
        for (int i = 0; i < input.getSize(); i++) {
            ItemStack stack = input.getStackInSlot(i);
            if (stack.getItem() == Items.WATER_BUCKET) {
                remainders.set(i, new ItemStack(Items.WATER_BUCKET)); // 水桶
            }else if( stack.getItem() == Items.LAVA_BUCKET){
                remainders.set(i, new ItemStack(Items.LAVA_BUCKET)); //岩浆桶
            }
        }

        return remainders;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WATER_LAVA_TO_COBBLESTONE; // 你定义的序列化器
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of(); // 使用 of() 而不是 ofSize()
        ingredients.add(Ingredient.ofItems(Items.WATER_BUCKET));    // 使用 add 而不是 set
        ingredients.add(Ingredient.ofItems(Items.LAVA_BUCKET));
        return ingredients;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return false; // 显示在配方书中
    }
    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.COBBLESTONE); // 圆石作为图标
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}
