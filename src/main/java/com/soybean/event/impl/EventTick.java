package com.soybean.event.impl;

import com.soybean.config.InitValue;
import com.soybean.manager.HeadlessPlayerManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author soybean
 * @date 2024/12/23 17:35
 * @description
 */
public class EventTick {
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            HeadlessPlayerManager.setServer(server);
        });
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            HeadlessPlayerManager.tick();
        });
        ServerTickEvents.START_SERVER_TICK.register((MinecraftServer server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.isTouchingWater()) {
                    // 冻结值每tick上升
                    player.setFrozenTicks(player.getFrozenTicks() + 3);
                }
            }
        });
        codDamageHandler();

    }
    public static void registerClient(){
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            HeadlessPlayerManager.tick();
        });
    }

    public static void codDamageHandler() {
        AtomicInteger codNum = new AtomicInteger();
        ServerTickEvents.END_WORLD_TICK.register((ServerWorld world) -> {
            //鳕鱼
            for (Entity entity : world.iterateEntities()) {
//                if (entity instanceof CodEntity cod) {
//                    if(codNum.get() / 20 > 1){
//                        cod.damage(world.getDamageSources().generic(), 1.0F);
//                        codNum.set(0);
//                    }else {
//                        codNum.getAndIncrement();
//                    }
//                }
            }
        });
    }
}
