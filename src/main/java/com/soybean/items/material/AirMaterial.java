package com.soybean.items.material;

import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

public class AirMaterial implements ToolMaterial {
    public static final AirMaterial INSTANCE = new AirMaterial();

    public static int MAX_DURABILITY = 1000;
    @Override
    public int getDurability() {
        return MAX_DURABILITY;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 50F;
    }

    @Override
    public float getAttackDamage() {
        return 8;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
    }

    @Override
    public int getEnchantability() {
        return 30;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.POTATO);
    }
}
