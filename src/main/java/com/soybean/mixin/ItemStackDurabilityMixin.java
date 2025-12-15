package com.soybean.mixin;

import com.soybean.config.InitValue;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackDurabilityMixin {

    // 方案1：修改getDamage方法，让当前损坏值显示为最大值的一半
    @Inject(method = "getDamage", at = @At("HEAD"), cancellable = true)
    private void modifyCurrentDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        // 检查是否是木制工具
        if (isHalfDurationStack(stack) || isBitDurationStack(stack)) {
            // 检查是否有half_durability标签
            NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
            if (customData != null) {
                NbtCompound nbt = customData.copyNbt();
                if (nbt.getBoolean("bit_durability")) {
                    int originalDamage = stack.getOrDefault(DataComponentTypes.DAMAGE, 0);
                    int maxDamage = stack.getMaxDamage();
                    // 如果当前损坏值为0，设置为最大值的一半来显示耐久条
                    if (originalDamage == 0) {
                        cir.setReturnValue(maxDamage-1);
                    }
                }
                if (nbt.getBoolean("half_durability")) {
                    // 获取原始损坏值
                    int originalDamage = stack.getOrDefault(DataComponentTypes.DAMAGE, 0);
                    int maxDamage = stack.getMaxDamage();

                    // 如果当前损坏值为0，设置为最大值的一半来显示耐久条
                    if (originalDamage == 0) {
                        cir.setReturnValue(maxDamage / 2);
                    }
                }
            }
        }
    }

    private boolean isHalfDurationStack(ItemStack stack){
        return stack.getItem() == Items.WOODEN_AXE || stack.getItem() == Items.WOODEN_PICKAXE || stack.getItem() == Items.STONE_PICKAXE || stack.getItem() == Items.STONE_AXE;
    }

    private boolean isBitDurationStack(ItemStack stack){
        return stack.getItem() == Items.IRON_AXE || stack.getItem() == Items.IRON_SWORD || stack.getItem() == Items.IRON_PICKAXE || stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.IRON_BOOTS || stack.getItem() == Items.SHIELD;
    }
}
