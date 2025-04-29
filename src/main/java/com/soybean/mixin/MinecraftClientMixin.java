package com.soybean.mixin;

import com.soybean.entity.custom.LichenSwordEntity;
import com.soybean.network.EntityUUIDPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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

            // 创建一个射线检测，只有当玩家直接看向剑时才能召回
            Vec3d eyePos = player.getEyePos();
            Vec3d lookVec = player.getRotationVec(1.0F).multiply(RECALL_DISTANCE);

            // 执行射线追踪检测，寻找玩家看向的实体
            HitResult hit = raycast(world, player, RECALL_DISTANCE);

            // 如果射线命中了实体
            if (hit.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHit = (EntityHitResult) hit;
                Entity hitEntity = entityHit.getEntity();

                // 检查命中的实体是否是玩家的剑
                if (hitEntity instanceof LichenSwordEntity) {
                    LichenSwordEntity sword = (LichenSwordEntity) hitEntity;

                    // 确认这是玩家自己的剑且还没有处于返回状态
                    if (sword.getOwner() == player && !sword.isReturning) {
                        // 向服务器发送召回数据包
                        ClientPlayNetworking.send(new EntityUUIDPayload(sword.getUuid()));
                        player.swingHand(Hand.MAIN_HAND); // 播放挥手动画
                        ci.cancel(); // 取消原有的交互逻辑
                    }
                }
            }
        }
    }

    // 自定义射线检测方法，用于检测玩家视线方向上的实体
    private HitResult raycast(World world, PlayerEntity player, double distance) {
        Vec3d eyePos = player.getEyePos();
        Vec3d lookVec = player.getRotationVec(1.0F).multiply(distance);
        Vec3d targetPos = eyePos.add(lookVec);

        // 创建一个精确的实体射线检测
        EntityHitResult entityHit = ProjectileUtil.raycast(
                player,
                eyePos,
                targetPos,
                player.getBoundingBox().expand(distance),
                entity -> entity instanceof LichenSwordEntity && ((LichenSwordEntity) entity).getOwner() == player,
                distance * distance
        );

        // 如果找到了实体，返回实体命中结果
        if (entityHit != null) {
            return entityHit;
        }

        // 否则返回方块命中结果（这里不会使用，但是为了完整性）
        return BlockHitResult.createMissed(targetPos, Direction.getFacing(lookVec.x, lookVec.y, lookVec.z), BlockPos.ofFloored(targetPos));
    }
}
