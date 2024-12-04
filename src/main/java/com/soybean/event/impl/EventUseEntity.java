package com.soybean.event.impl;

import com.soybean.screen.WitherSkeletonInteractionHandler;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ActionResult;

/**
 * @author soybean
 * @date 2024/12/4 12:04
 * @description
 */
public class EventUseEntity {
    public static void register(){
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
