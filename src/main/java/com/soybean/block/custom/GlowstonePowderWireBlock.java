package com.soybean.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class GlowstonePowderWireBlock extends AbstractPowderWireBlock {

    public GlowstonePowderWireBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected ItemStack getDropStack() {
        return new ItemStack(Items.GLOWSTONE_DUST);
    }
}