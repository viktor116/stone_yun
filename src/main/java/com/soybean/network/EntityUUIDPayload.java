package com.soybean.network;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.LichenSwordEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.UUID;

public record EntityUUIDPayload(UUID uuidEntity) implements CustomPayload {
    public static final Id<EntityUUIDPayload> ID = new Id<>(InitValue.id("entity_uuid"));
    public static final PacketCodec<RegistryByteBuf, EntityUUIDPayload> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, EntityUUIDPayload value) {
            buf.writeUuid(value.uuidEntity());
        }

        @Override
        public EntityUUIDPayload decode(RegistryByteBuf buf) {
            UUID entityuuid = buf.readUuid();
            return new EntityUUIDPayload(entityuuid);
        }
    };

    public static void receive(EntityUUIDPayload payload, ServerPlayNetworking.Context context) {
        ServerPlayerEntity player = context.player();
        if(player.getWorld().isClient) return;

        ServerWorld world = (ServerWorld)player.getWorld();
        Entity entity = world.getEntity(payload.uuidEntity);
        if(entity instanceof LichenSwordEntity lichenSwordEntity){
            lichenSwordEntity.startReturning(player);
        }

    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
