package com.soybean.event.impl;

import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.registry.FuelRegistry;

public class EventItemFuel {
    public static void register() {
        // 在这里注册物品作为燃料
        FuelRegistry.INSTANCE.add(ItemsRegister.TREE_CARBON, 5 * 20);
    }
}
