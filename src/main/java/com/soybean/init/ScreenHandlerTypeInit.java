package com.soybean.init;

import com.soybean.config.InitValue;
import com.soybean.screen.handler.WitherSkeletonInteractionHandler;
import com.soybean.screen.client.WitherSkeletonMerchantScreen;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerTypeInit {
    public static final ScreenHandlerType<WitherSkeletonInteractionHandler> WITHER_SKELETON_MERCHANT_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, InitValue.id("witherskeleton_merchant"),
                    new ScreenHandlerType<>((syncId, inventory) ->
                            new WitherSkeletonInteractionHandler(syncId, inventory),
                            FeatureSet.empty() // 使用原版特性集
                    )
            );

    public static <T extends ScreenHandler, D extends CustomPayload> ExtendedScreenHandlerType<T, D> register(String name, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<? super RegistryByteBuf, D> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, InitValue.id(name), new ExtendedScreenHandlerType<>(factory, codec));
    }
    public static void initialize() {
    }

    public static void initializeClient() {
        HandledScreens.register(ScreenHandlerTypeInit.WITHER_SKELETON_MERCHANT_SCREEN_HANDLER, WitherSkeletonMerchantScreen::new);
    }
}
