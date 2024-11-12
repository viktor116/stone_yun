package com.soybean.items.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UnbreakablePickaxeItem extends PickaxeItem {
    public UnbreakablePickaxeItem(ToolMaterial material, Item.Settings settings) {
        super(material, settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        // 直接返回 true，避免耐久损耗
        return true;
    }

}
