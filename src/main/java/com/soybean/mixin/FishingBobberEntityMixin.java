package com.soybean.mixin;

import com.soybean.items.ItemsRegister;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
    @Shadow
    private int hookCountdown;

    @Inject(
        method = "removeIfInvalid",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 0),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void checkCustomFishingRod(PlayerEntity player, CallbackInfoReturnable<Boolean> cir, ItemStack mainHand, ItemStack offHand) {
        boolean hasCustomRod = mainHand.isOf(ItemsRegister.COD_FISHING_ROD) || offHand.isOf(ItemsRegister.COD_FISHING_ROD);
        if (hasCustomRod && !player.isRemoved() && player.isAlive() && !(((FishingBobberEntity)(Object)this).squaredDistanceTo(player) > 1024.0)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "use",
            at = @At("HEAD"),
            cancellable = true
    )
    private void useCustomLootTable(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        if (usedItem.isOf(ItemsRegister.COD_FISHING_ROD) && this.hookCountdown > 0) {
            FishingBobberEntity bobber = (FishingBobberEntity)(Object)this;
            if (!bobber.getWorld().isClient && bobber.getWorld() instanceof ServerWorld serverWorld) {
                PlayerEntity playerEntity = bobber.getPlayerOwner();

                if (playerEntity != null) {
                    // 生成物品
                    ItemStack fishingRodStack = new ItemStack(Items.FISHING_ROD);
                    fishingRodStack.setCount(1);

                    ItemEntity itemEntity = new ItemEntity(serverWorld,
                            bobber.getX(), bobber.getY(), bobber.getZ(),
                            fishingRodStack);

                    double d = playerEntity.getX() - bobber.getX();
                    double e = playerEntity.getY() - bobber.getY();
                    double f = playerEntity.getZ() - bobber.getZ();
                    itemEntity.setVelocity(d * 0.1,
                            e * 0.1 + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08,
                            f * 0.1);
                    serverWorld.spawnEntity(itemEntity);
                    // 自动收杆
                    bobber.discard();
                    // playerEntity.fishHook = null;
                    cir.setReturnValue(1);
                }
            }
        }
    }
} 