package com.soybean.event.impl;

import com.soybean.manager.HeadlessPlayerManager;
import com.soybean.network.HeadlessPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

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
    }
    public static void registerClient(){
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            HeadlessPlayerManager.tick();
        });
    }
}
