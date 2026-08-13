package com.soybean.mixin;

import com.soybean.entity.custom.MaceProjectileEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Inject(method = "getProjectiles", at = @At("RETURN"), cancellable = true)
    private void addMaceAsProjectile(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Predicate<ItemStack> original = cir.getReturnValue();
        cir.setReturnValue(stack -> original.test(stack) || stack.isOf(Items.MACE));
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"), cancellable = true)
    private void onStoppedUsingWithMace(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (user instanceof PlayerEntity player && !world.isClient) {
            int useTicks = ((BowItem)(Object)this).getMaxUseTime(stack, user) - remainingUseTicks;
            float pullProgress = BowItem.getPullProgress(useTicks);
            if (pullProgress >= 0.1f) {
                ItemStack projectile = player.getProjectileType(stack);
                if (projectile.isOf(Items.MACE)) {
                    MaceProjectileEntity proj = new MaceProjectileEntity(world, player, projectile.copyWithCount(1));
                    proj.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 3.5f * pullProgress, 1.0f);
                    world.spawnEntity(proj);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f,
                            1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f));
                    stack.damage(1, player,
                            player.getActiveHand() == net.minecraft.util.Hand.MAIN_HAND
                                    ? EquipmentSlot.MAINHAND
                                    : EquipmentSlot.OFFHAND);
                    if (!player.getAbilities().creativeMode) {
                        projectile.decrement(1);
                    }
                    ci.cancel();
                }
            }
        }
    }
}
