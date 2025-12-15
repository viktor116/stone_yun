package com.soybean.mixin;

import com.soybean.items.item.UnbreakablePickaxeItem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    /**
     * 注入到 setAngles 方法的开头，该方法在模型渲染前设置手臂的姿势。
     * T livingEntity 是渲染的实体。
     */
    @Inject(
            method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V",
            at = @At("HEAD")
    )
    private void preventPickaxeHoldingPose(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {

        // 检查主手是否持有该物品
        ItemStack mainHandStack = livingEntity.getStackInHand(Hand.MAIN_HAND);
        if (mainHandStack.getItem() instanceof UnbreakablePickaxeItem) {
            // 如果主手持有，强制设置为默认空闲姿势
            this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        }

        // 检查副手是否持有该物品
        ItemStack offHandStack = livingEntity.getStackInHand(Hand.OFF_HAND);
        if (offHandStack.getItem() instanceof UnbreakablePickaxeItem) {
            // 如果副手持有，强制设置为默认空闲姿势
            this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        }

        // 注意：由于这是 HEAD 注入，设置后 setAngles 的后续逻辑会读取这些值，
        // 从而忽略原版设置的 HOLDING 姿势，达到取消动作的目的。
    }
}
