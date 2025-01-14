package com.soybean;

import com.soybean.block.ModBlock;
import com.soybean.entity.EntityRegister;
import com.soybean.entity.EntityRegisterClient;
import com.soybean.event.EventRegister;
import com.soybean.init.ScreenHandlerTypeInit;
import com.soybean.items.ItemsRegister;
import com.soybean.network.NetworkRegister;
import com.soybean.screen.client.WitherSkeletonMerchantScreen;
import com.soybean.utils.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.screen.ScreenHandlerType;

public class StoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModBlock.initializeClient();
        EntityRegisterClient.initializeClient();
        ScreenHandlerTypeInit.initializeClient();
        EventRegister.InitializeClient();
        NetworkRegister.initClient();
        ModModelPredicates.registerModelPredicates();
    }
}
