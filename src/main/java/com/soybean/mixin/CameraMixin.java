package com.soybean.mixin;

import com.soybean.entity.custom.TotemOfDeadEntity;
import com.soybean.utils.CommonUtils;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @author soybean
 * @date 2025/2/13 12:02
 * @description
 */
@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private float pitch;
    @Shadow
    private float yaw;
    @Shadow
    private Vec3d pos;
    @Shadow
    private @Final Quaternionf rotation;
    @Shadow
    private @Final Vector3f horizontalPlane;
    @Shadow
    private @Final Vector3f verticalPlane;
    @Shadow
    private @Final Vector3f diagonalPlane;

    @Shadow
    public static Vector3f HORIZONTAL = new Vector3f(0.0F, 0.0F, -1.0F);
    @Shadow
    public static Vector3f VERTICAL = new Vector3f(0.0F, 1.0F, 0.0F);
    @Shadow
    public static Vector3f DIAGONAL = new Vector3f(-1.0F, 0.0F, 0.0F);

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "update",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V",
                    shift = At.Shift.AFTER))
    private void onUpdateRotation(BlockView area, Entity focusedEntity, boolean thirdPerson,
                                  boolean inverseView, float tickDelta, CallbackInfo ci) {
//        if (focusedEntity instanceof PlayerEntity player) {
//            if (!thirdPerson && !player.isSleeping()) {
//                List<TotemOfDeadEntity> totemOfDeadEntities = player.getWorld().getEntitiesByClass(TotemOfDeadEntity.class, player.getBoundingBox().expand(50), entity -> true);
//                boolean hasTotem = false;
//                if(!totemOfDeadEntities.isEmpty()) hasTotem = true;
//                if(hasTotem){
//                    float rotationAngle = CommonUtils.getRandom().nextFloat() * 10;
//                    // 获取原始的视角值
//                    float originalYaw = focusedEntity.getYaw(tickDelta);
//                    float originalPitch = focusedEntity.getPitch(tickDelta);
//                    this.rotation.rotationYXZ(3.1415927F - originalYaw * 0.017453292F, -originalPitch * 0.017453292F, rotationAngle * 0.017453292F);
//                }
//            }
//        }
    }

}
