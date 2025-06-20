package com.soybean.particles;

import com.mojang.serialization.MapCodec;
import com.soybean.config.InitValue;
import com.soybean.particles.custom.BlueThunderParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.SonicBoomParticle;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

/**
 * @author soybean
 * @date 2025/1/10 14:27
 * @description
 */
public class ParticlesRegister {
//    public static final SimpleParticleType SPARKLE_PARTICLE = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(InitValue.MOD_ID, "alchemymod"), FabricParticleTypes.simple());
//    public static final SimpleParticleType ALCHEMYMOD = Registry.register(Registries.PARTICLE_TYPE, Identifier.of(InitValue.MOD_ID, "alchemymod"), FabricParticleTypes.simple());

    public static final SimpleParticleType BLUE_THUNDER = register("blue_thunder");

    public static void initialize(){

    }

    public static void initializeClient(){
        ParticleFactoryRegistry.getInstance().register(BLUE_THUNDER, BlueThunderParticle.Factory::new);
    }

    private static SimpleParticleType register(String name) {
        return (SimpleParticleType)Registry.register(Registries.PARTICLE_TYPE, Identifier.of(InitValue.MOD_ID, name), FabricParticleTypes.simple());
    }
    private static <T extends ParticleEffect> ParticleType<T> register(String name, final Function<ParticleType<T>, MapCodec<T>> codecGetter, final Function<ParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter) {
        return (ParticleType)Registry.register(Registries.PARTICLE_TYPE, name, new ParticleType<T>(true) {
            public MapCodec<T> getCodec() {
                return (MapCodec)codecGetter.apply(this);
            }
            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                return (PacketCodec)packetCodecGetter.apply(this);
            }
        });
    }

}
