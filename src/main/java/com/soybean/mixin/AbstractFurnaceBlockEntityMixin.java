package com.soybean.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author soybean
 * @date 2025/3/14 11:41
 * @description
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {

//    @Inject(method = "getFuelTime", at = @At("HEAD"), cancellable = true)
//    private static void modifyBurnTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
//        if (stack.getItem() == Items.RAW_IRON) {
//            cir.setReturnValue(1600); // 设置燃烧时间（单位是 tick，1 秒 = 20 tick）
//        }
//    }
}