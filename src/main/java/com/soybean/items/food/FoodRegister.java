package com.soybean.items.food;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

/**
 * @author soybean
 * @date 2025/3/14 10:15
 * @description
 */
public class FoodRegister {
    public static final FoodComponent COPPER_APPLE = new FoodComponent.Builder().nutrition(4).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20*20, 0), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 20, 0), 1.0f).alwaysEdible().build();
    public static final FoodComponent FRIED_EGG = new FoodComponent.Builder().nutrition(5).saturationModifier(1.2f).build();
}
