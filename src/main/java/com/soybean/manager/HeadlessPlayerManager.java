package com.soybean.manager;

import com.soybean.config.InitValue;
import com.soybean.network.HeadlessPayload;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

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
    private static MinecraftServer server;

    public static void setServer(MinecraftServer mcServer) {
        server = mcServer;
    }

    public static void setPlayerHeadless(PlayerEntity player, int durationTicks) {
        UUID playerId = player.getUuid();
        headlessPlayers.put(playerId, durationTicks);

        if (!player.getWorld().isClient() && server != null) {
            HeadlessPayload payload = new HeadlessPayload(playerId, durationTicks);
            for (ServerPlayerEntity serverPlayer : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }
        }
    }

    public static void handlePayload(HeadlessPayload payload) {
        if (payload.durationTicks() <= 0) {
            headlessPlayers.remove(payload.playerId());
        } else {
            headlessPlayers.put(payload.playerId(), payload.durationTicks());
        }
    }

    public static void tick() {
        if (server == null) return;

        Iterator<Map.Entry<UUID, Integer>> iterator = headlessPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int remainingTicks = entry.getValue() - 1;

            if (remainingTicks <= 0) {
                UUID playerId = entry.getKey();
                iterator.remove();

                HeadlessPayload payload = new HeadlessPayload(playerId, 0);
                for (ServerPlayerEntity serverPlayer : server.getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(serverPlayer, payload);
                }
            } else {
                entry.setValue(remainingTicks);
            }
        }
    }

    public static boolean isPlayerHeadless(UUID playerId) {
        return headlessPlayers.containsKey(playerId);
    }

    public static void removeHeadlessState(UUID playerId) {
        headlessPlayers.remove(playerId);
        if (server != null) {
            HeadlessPayload payload = new HeadlessPayload(playerId, 0);
            for (ServerPlayerEntity serverPlayer : server.getPlayerManager().getPlayerList()) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }
        }
    }

    public static int getRemainingTicks(UUID playerId) {
        return headlessPlayers.getOrDefault(playerId, 0);
    }
}