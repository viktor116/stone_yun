package com.soybean.event;

import com.soybean.config.InitValue;
import com.soybean.screen.WitherSkeletonInteractionHandler;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ActionResult;

public class EventRegister {
    public static void Initialize(){
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof EnderDragonEntity || entity instanceof EnderDragonPart) {
                WitherSkeletonInteractionHandler.handleRightClickOnDragon(player);
                return ActionResult.SUCCESS;
            }
            if (entity instanceof WitherSkeletonEntity) {
                WitherSkeletonInteractionHandler.handleRightClick(player);
                return ActionResult.SUCCESS;
            }
            if(entity instanceof SheepEntity){
                WitherSkeletonInteractionHandler.handleRightClickOnSheep(player);
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

    }
}
