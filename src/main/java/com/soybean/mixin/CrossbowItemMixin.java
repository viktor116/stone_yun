package com.soybean.mixin;

import com.soybean.items.ItemsRegister;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;
import java.util.List;
import java.util.ArrayList;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    // 让弩可以识别床为弹药
    @Inject(method = "getProjectiles", at = @At("RETURN"), cancellable = true)
    private void addBedAsProjectile(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Predicate<ItemStack> original = cir.getReturnValue();
        cir.setReturnValue(stack -> original.test(stack) || isBed(stack));
    }

    @Inject(method = "getHeldProjectiles", at = @At("RETURN"), cancellable = true)
    private void addBedAsHeldProjectile(CallbackInfoReturnable<Predicate<ItemStack>> cir) {
        Predicate<ItemStack> original = cir.getReturnValue();
        cir.setReturnValue(stack -> original.test(stack) || isBed(stack));
    }

    private static boolean isBed(ItemStack stack) {
        // 你可以更精细地判断所有床
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

    // 修改loadProjectiles方法，在装载床时直接替换为床弩
    @Inject(method = "loadProjectiles", at = @At("HEAD"), cancellable = true)
    private static void handleBedLoading(LivingEntity shooter, ItemStack crossbow, CallbackInfoReturnable<Boolean> cir) {
        ItemStack projectileStack = shooter.getProjectileType(crossbow);
        
        // 如果弹药是床，并且玩家手持普通弩
        if (isBed(projectileStack) && crossbow.isOf(Items.CROSSBOW) && shooter instanceof PlayerEntity) {
            // 创建一个新的床弩物品
            ItemStack bedCrossbow = new ItemStack(ItemsRegister.CROSSBOW_BED);
            
            // 将床作为弹药添加到床弩中
            List<ItemStack> projectiles = new ArrayList<>();
            projectiles.add(projectileStack.copyWithCount(1));
            bedCrossbow.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectiles));
            
            // 减少床的数量
            projectileStack.decrement(1);
            
            // 替换玩家手中的物品
            if (shooter instanceof PlayerEntity player) {
                Hand hand = player.getActiveHand();
                player.setStackInHand(hand, bedCrossbow);
            }
            
            cir.setReturnValue(true);
        }
    }
    
    // 为原版loadProjectiles添加床作为有效弹药
    @Inject(method = "loadProjectiles", at = @At("RETURN"), cancellable = true)
    private static void allowBedLoading(LivingEntity shooter, ItemStack crossbow, CallbackInfoReturnable<Boolean> cir) {
        // 如果原方法已经成功加载弹药，我们不需要再处理
        if (cir.getReturnValueZ()) {
            return;
        }
        
        // 检查是否有床作为弹药
        ItemStack projectileStack = shooter.getProjectileType(crossbow);
        if (isBed(projectileStack)) {
            List<ItemStack> projectiles = new ArrayList<>();
            projectiles.add(projectileStack.copyWithCount(1));
            crossbow.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectiles));
            projectileStack.decrement(1);
            cir.setReturnValue(true);
        }
    }
}
