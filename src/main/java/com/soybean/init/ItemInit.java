package com.soybean.init;

import com.soybean.config.InitValue;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ItemInit {
    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, InitValue.id(name), item);
    }

    public static void initialize() {
    }
}
