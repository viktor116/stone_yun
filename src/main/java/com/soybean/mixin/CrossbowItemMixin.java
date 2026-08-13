package com.soybean.mixin;

import com.soybean.items.ItemsRegister;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Inject(method = "getProjectiles", at = @At("RETURN"), cancellable = true)
    private void addSpecialAsProjectile(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Predicate<ItemStack> original = cir.getReturnValue();
        cir.setReturnValue(stack -> original.test(stack) || isBed(stack) || stack.isOf(Items.MACE));
    }

    @Inject(method = "getHeldProjectiles", at = @At("RETURN"), cancellable = true)
    private void addSpecialAsHeldProjectile(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Predicate<ItemStack> original = cir.getReturnValue();
        cir.setReturnValue(stack -> original.test(stack) || isBed(stack) || stack.isOf(Items.MACE));
    }

    @Inject(method = "loadProjectiles", at = @At("HEAD"), cancellable = true)
    private static void handleSpecialLoading(LivingEntity shooter, ItemStack crossbow, CallbackInfoReturnable<Boolean> cir) {
        ItemStack projectileStack = shooter.getProjectileType(crossbow);
        if (crossbow.isOf(Items.CROSSBOW) && shooter instanceof PlayerEntity player) {
            Hand hand = player.getActiveHand();

            if (isBed(projectileStack)) {
                ItemStack bedCrossbow = new ItemStack(ItemsRegister.CROSSBOW_BED);
                bedCrossbow.set(DataComponentTypes.CHARGED_PROJECTILES,
                        ChargedProjectilesComponent.of(List.of(projectileStack.copyWithCount(1))));
                projectileStack.decrement(1);
                player.setStackInHand(hand, bedCrossbow);
                cir.setReturnValue(true);
                return;
            }

            if (projectileStack.isOf(Items.MACE)) {
                ItemStack maceCrossbow = new ItemStack(ItemsRegister.MACE_CROSSBOW);
                maceCrossbow.set(DataComponentTypes.CHARGED_PROJECTILES,
                        ChargedProjectilesComponent.of(List.of(projectileStack.copyWithCount(1))));
                projectileStack.decrement(1);
                player.setStackInHand(hand, maceCrossbow);
                cir.setReturnValue(true);
            }
        }
    }

    private static boolean isBed(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.RED_BED || item == Items.BLUE_BED || item == Items.YELLOW_BED
                || item == Items.WHITE_BED || item == Items.BLACK_BED
                || item == Items.GREEN_BED || item == Items.PINK_BED
                || item == Items.PURPLE_BED || item == Items.CYAN_BED
                || item == Items.BROWN_BED || item == Items.GRAY_BED
                || item == Items.LIGHT_BLUE_BED || item == Items.LIGHT_GRAY_BED
                || item == Items.LIME_BED || item == Items.MAGENTA_BED
                || item == Items.ORANGE_BED;
    }
}
