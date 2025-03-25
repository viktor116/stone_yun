package com.soybean.items.item;

import com.soybean.utils.DynamicLightSource;
import net.minecraft.item.Item;

/**
 * @author soybean
 * @date 2025/03/25
 * @description u5e26u5149u6e90u6548u679cu7684u7269u54c1u57fau7c7b
 */
public class LightEmittingItem extends Item implements DynamicLightSource {
    private final int lightLevel;

    public LightEmittingItem(Settings settings, int lightLevel) {
        super(settings);
        this.lightLevel = Math.min(15, Math.max(0, lightLevel)); // u786eu4fddu5149u7167u7b49u7ea7u5728 0-15 u8303u56f4u5185
    }

    @Override
    public int getLightLevel() {
        return lightLevel;
    }
}
