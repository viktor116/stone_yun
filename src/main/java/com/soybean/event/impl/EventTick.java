package com.soybean.event.impl;

import com.soybean.manager.HeadlessPlayerManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

/**
 * @author soybean
 * @date 2024/12/23 17:35
 * @description
 */
public class EventTick {
    public static void register(){
        ServerTickEvents.END_SERVER_TICK.register(server -> HeadlessPlayerManager.tick());
    }
}
