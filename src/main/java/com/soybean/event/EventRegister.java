package com.soybean.event;

import com.soybean.config.InitValue;
import com.soybean.screen.WitherSkeletonInteractionHandler;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.util.ActionResult;

public class EventRegister {
    public static void Initialize(){
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof WitherSkeletonEntity witherSkeleton) {
                WitherSkeletonInteractionHandler.handleRightClick(witherSkeleton, player);
                InitValue.LOGGER.info("skeleton");
                return ActionResult.SUCCESS;
            }
            if(entity instanceof EnderDragonEntity enderDragonEntity){
                WitherSkeletonInteractionHandler.handleRightClickOnDragon(player);
                InitValue.LOGGER.info("click ender dragon");
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }
}
