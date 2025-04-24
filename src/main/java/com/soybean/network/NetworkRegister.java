package com.soybean.network;

import com.soybean.manager.HeadlessPlayerManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;

import java.util.UUID;

public class NetworkRegister {

    public static void init() {
        PayloadTypeRegistry.playS2C().register(HeadlessPayload.ID, HeadlessPayload.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(EntityUUIDPayload.ID, EntityUUIDPayload.PACKET_CODEC);

        PayloadTypeRegistry.playC2S().register(HeadlessPayload.ID, HeadlessPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(EntityUUIDPayload.ID, EntityUUIDPayload.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(EntityUUIDPayload.ID, EntityUUIDPayload::receive);

        ServerPlayNetworking.registerGlobalReceiver(
                HeadlessPayload.ID,
                (payload, context) -> {
                    context.server().execute(() ->
                            HeadlessPlayerManager.handlePayload(payload)
                    );
                }
        );
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                HeadlessPayload.ID,
                (payload, context) -> {
                    MinecraftClient.getInstance().execute(() ->
                            HeadlessPlayerManager.handlePayload(payload)
                    );
                }
        );
    }
}
