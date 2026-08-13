package com.soybean.utils;

import com.soybean.entity.custom.MaceBobberEntity;
import com.soybean.items.ItemsRegister;
import com.soybean.items.item.MaceFishingRodItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;

public class ModModelPredicates {
    public static void registerModelPredicates(){
        registerCustomBow(ItemsRegister.WITHER_BOW);
        registerCustomBow(ItemsRegister.THE_END_BOW);
        registerCustomBow(ItemsRegister.BLUE_BOW);
        registerCustomBow(ItemsRegister.END_BOW);
        registerCustomBow(ItemsRegister.FLIP_BOW);
        registerCustomBow(ItemsRegister.MACE_BOW);
        registerCustomBow(ItemsRegister.ARROW_BOW);

        registerCastRod(ItemsRegister.MACE_FISHING_ROD);
    }
    public static void registerCustomBow(Item item){
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("pull"), (stack, world, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getActiveItem() != stack ? 0.0F : (float)(stack.getMaxUseTime(entity) - entity.getItemUseTimeLeft()) / 20.0F;
            }
        });
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("pulling"), (stack, world, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
        });
    }

    public static void registerCastRod(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.ofVanilla("cast"), (stack, world, entity, seed) -> {
            if (entity instanceof PlayerEntity playerEntity && world != null) {
                // 先简化：只查 owner，不查 UUID，和原版钓鱼竿一样
                MaceBobberEntity bobber = MaceFishingRodItem.findActiveBobber(world, playerEntity, null);
                return bobber != null ? 1.0F : 0.0F;
            }
            return 0.0F;
        });
    }
}
