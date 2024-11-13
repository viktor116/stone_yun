package com.soybean.items.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class AppleSwordItem extends SwordItem {
    public AppleSwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    // 覆盖这个方法来确保物品在附魔台中可见
}
