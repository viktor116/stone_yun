package com.soybean.event;

import com.soybean.config.InitValue;
import com.soybean.event.impl.EventBreak;
import com.soybean.event.impl.EventUseEntity;
import com.soybean.event.impl.EventUseItem;
import com.soybean.screen.WitherSkeletonInteractionHandler;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.ActionResult;

public class EventRegister {
    public static void Initialize(){
        EventBreak.register();
        EventUseEntity.register();
        EventUseItem.register();
    }
}
