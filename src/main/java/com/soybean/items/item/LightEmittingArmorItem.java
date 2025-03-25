package com.soybean.items.item;

import com.soybean.utils.DynamicLightSource;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * @author soybean
 * @date 2025/03/25
 * @description u5e26u5149u6e90u6548u679cu7684u76d4u7532u7269u54c1
 */
public class LightEmittingArmorItem extends ArmorItem implements DynamicLightSource {
    private final int lightLevel;

    public LightEmittingArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings, int lightLevel) {
        super(material, type, settings);
        this.lightLevel = Math.min(15, Math.max(0, lightLevel)); // u786eu4fddu5149u7167u7b49u7ea7u5728 0-15 u8303u56f4u5185
    }

    @Override
    public int getLightLevel() {
        return lightLevel;
    }
}
