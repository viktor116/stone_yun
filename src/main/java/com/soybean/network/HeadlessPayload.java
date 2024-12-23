package com.soybean.network;

import com.soybean.config.InitValue;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public record HeadlessPayload(UUID playerId, int durationTicks) implements CustomPayload {
    public static final Id<HeadlessPayload> ID = new Id<>(InitValue.id("headless_player"));

    public static final PacketCodec<RegistryByteBuf, HeadlessPayload> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, HeadlessPayload value) {
            buf.writeUuid(value.playerId());
            buf.writeVarInt(value.durationTicks());
        }

        @Override
        public HeadlessPayload decode(RegistryByteBuf buf) {
            UUID playerId = buf.readUuid();
            int duration = buf.readVarInt();
            return new HeadlessPayload(playerId, duration);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    // 实用方法：从玩家创建payload
    public static HeadlessPayload fromPlayer(PlayerEntity player, int duration) {
        return new HeadlessPayload(player.getUuid(), duration);
    }
}
