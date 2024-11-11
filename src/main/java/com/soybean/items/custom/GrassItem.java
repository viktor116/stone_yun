package com.soybean.items.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/11/11 12:09
 * @description
 */
public class GrassItem extends Item {
    public GrassItem(Settings settings) {
        super(settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

//    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//        return super.use(world, user, hand);
//    }
}
