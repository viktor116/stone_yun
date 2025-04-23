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
    public static final FoodComponent LOW_ENCHANT_APPLE = new FoodComponent.Builder().nutrition(4).saturationModifier(1.2f)
            .statusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20*60*4, 0), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 20*60*4, 0), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*60*2, 1), 1.0f)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 20*20, 0), 1.0f)
            .alwaysEdible().build();
    public static final FoodComponent FRIED_EGG = new FoodComponent.Builder().nutrition(5).saturationModifier(1.2f).build();
    public static final FoodComponent ENCHANTED_GOLDEN_CARROT = new FoodComponent.Builder().nutrition(4).saturationModifier(1.2F)
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20*40, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20*60*10, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*60*10, 0), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 20*60*4, 3), 1.0F).alwaysEdible().build();
    public static final FoodComponent COOKED_COBBLESTONE = new FoodComponent.Builder().nutrition(8).saturationModifier(2f).build();
    public static final FoodComponent WARDEN_BUCKET = new FoodComponent.Builder().nutrition(12).saturationModifier(4f).build();
    public static final FoodComponent SUPER_STRENGTH_POTION = new FoodComponent.Builder().nutrition(0).saturationModifier(0)
            .statusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 20*30, 19), 1.0f)
            .alwaysEdible().build();
}
