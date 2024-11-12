package com.soybean.event;

import com.soybean.config.InitValue;
import com.soybean.items.ItemsRegister;
import com.soybean.items.material.AirMaterial;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResult;

public class EventRegister {
    public static void Initialize(){
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
//            Item item = player.getMainHandStack().getItem();
//            if (!player.isSpectator() && item == ItemsRegister.AIR_PICKAXE) {
//                // Restore durability to max before block break
//                ItemStack mainHand = player.getMainHandStack();
//                int maxDurability = mainHand.getMaxDamage();
//                mainHand.setDamage(0);
//                InitValue.LOGGER.info("Restored durability to max: {}", maxDurability);
//            }
        });
    }
}
