package com.soybean.mixin;

import com.soybean.config.InitValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author soybean
 * @date 2024/12/24 15:21
 * @description
 */
@Mixin(Item.class)
public abstract class EdibleMixin {
    @Shadow
    public abstract Item asItem();

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void edibleUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (isEdible(stack)) {
            if (user.canConsume(false)) { // 检查玩家是否可以食用
                user.setCurrentHand(hand); // 开始食用动画
                cir.setReturnValue(TypedActionResult.consume(stack));
            } else {
                cir.setReturnValue(TypedActionResult.fail(stack));
            }
        }
    }

    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    private void makeEdible(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
        if (isEdible(stack)) {
            cir.setReturnValue(UseAction.EAT); // 设置为食物动作
        }
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void setMaxUseTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
        if (isEdible(stack)) {
            cir.setReturnValue(32); // 设置食用时间为 32 tick
        }
    }

    @Inject(method = "finishUsing", at = @At("HEAD"), cancellable = true)
    private void addEatingEffect(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (stack.isOf(Items.LEATHER)) {
            if (user instanceof PlayerEntity player) {
                player.getHungerManager().add(4, 1.0f);
                stack.decrement(1); // 消耗物品
            }
            cir.setReturnValue(stack);
        } else if (stack.isOf(Items.SUGAR_CANE)) {
            if (user instanceof PlayerEntity player) {
                player.getHungerManager().add(1, 1.0f);
                stack.decrement(1);
            }
            cir.setReturnValue(stack);
        } else if (stack.isOf(Items.SUGAR)) {
            if (user instanceof PlayerEntity player) {
                player.getHungerManager().add(4, 1.0f);
                stack.decrement(1);
            }
            cir.setReturnValue(stack);
        } else if (stack.isOf(Items.RABBIT_HIDE)) {
            if (user instanceof PlayerEntity player) {
                player.getHungerManager().add(5, 1.5f);
                stack.decrement(1);
            }
            cir.setReturnValue(stack);
        } else if (stack.isOf(Items.PUMPKIN)) {
            if (user instanceof PlayerEntity player) {
                player.getHungerManager().add(5, 1.0f);
                stack.decrement(1);
            }
            cir.setReturnValue(stack);
        }
    }

    private static boolean isEdible(ItemStack stack) {
        return stack.isOf(Items.LEATHER) ||
                stack.isOf(Items.SUGAR_CANE) ||
                stack.isOf(Items.SUGAR) ||
                stack.isOf(Items.RABBIT_HIDE) ||
                stack.isOf(Items.PUMPKIN);
    }
}

