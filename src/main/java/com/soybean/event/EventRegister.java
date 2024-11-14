package com.soybean.event;

import com.soybean.screen.WitherSkeletonInteractionHandler;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.util.ActionResult;

public class EventRegister {
    public static void Initialize(){
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof WitherSkeletonEntity witherSkeleton) {
                WitherSkeletonInteractionHandler.handleRightClick(witherSkeleton, player);
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }
}
