package com.soybean.init;

import com.soybean.block.custom.inventory.ExampleInventoryBlock;
import com.soybean.config.InitValue;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.List;

public class BlockInit {
    public static final ExampleInventoryBlock EXAMPLE_INVENTORY_BLOCK = registerWithItem("example_inventory_block",
            new ExampleInventoryBlock(AbstractBlock.Settings.create()
                    .strength(1.5F, 6.0F)
                    .requiresTool()));

    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, InitValue.id(name), block);
    }

    public static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings) {
        T registered = register(name, block);
        ItemInit.register(name, new BlockItem(registered, settings));
        return registered;
    }

    public static <T extends Block> T registerWithItem(String name, T block) {
        return registerWithItem(name, block, new Item.Settings());
    }

    private static SuspiciousStewEffectsComponent createStewEffects(SuspiciousStewEffectsComponent.StewEffect... effects) {
        return new SuspiciousStewEffectsComponent(List.of(effects));
    }

    public static void initialize() {
    }
}
