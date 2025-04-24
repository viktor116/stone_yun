package com.soybean.mixin;

import com.soybean.entity.custom.LichenSwordEntity;
import com.soybean.network.EntityUUIDPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author soybean
 * @date 2025/4/24 12:12
 * @description
 */
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public ClientWorld world;

    @Shadow public abstract void enterReconfiguration(Screen reconfigurationScreen);

    private static final double RECALL_DISTANCE = 100.0; // 召回最大距离：100格

    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    private void onDoItemUse(CallbackInfo ci) {
        if (player != null && world != null && player.getMainHandStack().isEmpty()) {
            // 玩家空手时处理召回逻辑
            if (player.isUsingItem()) {
                return; // 如果玩家正在使用物品，不执行召回
            }

            // 进行长距离射线检测，寻找玩家的剑
            Vec3d eyePos = player.getEyePos();
            Vec3d lookVec = player.getRotationVec(1.0F).multiply(RECALL_DISTANCE);
            Vec3d targetPos = eyePos.add(lookVec);

            // 创建一个射线检测的边界框
            double width = 1.0; // 射线宽度，可以根据需要调整
            Box box = new Box(
                    eyePos.x - width, eyePos.y - width, eyePos.z - width,
                    eyePos.x + width + lookVec.x, eyePos.y + width + lookVec.y, eyePos.z + width + lookVec.z
            ).expand(1.0); // 稍微扩大一点检测范围

            // 获取路径上的所有LichenSwordEntity
            List<LichenSwordEntity> swords = world.getEntitiesByClass(
                    LichenSwordEntity.class,
                    box,
                    entity -> {
                        LichenSwordEntity lichenSwordEntity =  (LichenSwordEntity) entity;
                        return lichenSwordEntity.getOwner() != null &&lichenSwordEntity.getOwner() == player && !lichenSwordEntity.isReturning;
                    }
            );

            // 如果找到剑，按距离排序，找最近的一个
            if (!swords.isEmpty()) {
                Optional<LichenSwordEntity> nearestSword = swords.stream()
                        .min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(player)));

                if (nearestSword.isPresent()) {
                    // 向服务器发送召回数据包
                    ClientPlayNetworking.send(new EntityUUIDPayload(nearestSword.get().getUuid()));
                    player.swingHand(Hand.MAIN_HAND); // 播放挥手动画
                    ci.cancel(); // 取消原有的交互逻辑
                }
            } else {
                // 没有找到直接路径上的剑，尝试找附近100格内的任何属于玩家的剑
                List<LichenSwordEntity> allSwords = world.getEntitiesByClass(
                        LichenSwordEntity.class,
                        player.getBoundingBox().expand(RECALL_DISTANCE),
                        entity -> {
                            LichenSwordEntity lichenSwordEntity =  (LichenSwordEntity) entity;
                            return lichenSwordEntity.getOwner() != null &&lichenSwordEntity.getOwner() == player && !lichenSwordEntity.isReturning;
                        }
                );

                if (!allSwords.isEmpty()) {
                    Optional<LichenSwordEntity> nearestSword = allSwords.stream()
                            .min(Comparator.comparingDouble(entity -> entity.squaredDistanceTo(player)));

                    if (nearestSword.isPresent()) {
                        // 向服务器发送召回数据包
                        ClientPlayNetworking.send(new EntityUUIDPayload(nearestSword.get().getUuid()));
                        player.swingHand(Hand.MAIN_HAND); // 播放挥手动画
                        ci.cancel(); // 取消原有的交互逻辑
                    }
                }
            }
        }
    }
}
