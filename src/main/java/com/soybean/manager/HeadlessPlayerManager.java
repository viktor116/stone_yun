package com.soybean.manager;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author soybean
 * @date 2024/12/23 17:34
 * @description
 */
public class HeadlessPlayerManager {
    private static final Map<UUID, Integer> headlessPlayers = new HashMap<>();

    public static void setPlayerHeadless(PlayerEntity player, int durationTicks) {
        headlessPlayers.put(player.getUuid(), durationTicks);
    }

    public static boolean isPlayerHeadless(UUID playerId) {
        return headlessPlayers.containsKey(playerId);
    }

    public static void tick() {
        Iterator<Map.Entry<UUID, Integer>> iterator = headlessPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int remainingTicks = entry.getValue() - 1;
            if (remainingTicks <= 0) {
                iterator.remove();
            } else {
                entry.setValue(remainingTicks);
            }
        }
    }
}