package com.soybean;

import com.soybean.block.ModBlock;
import com.soybean.entity.EntityRegister;
import com.soybean.init.ScreenHandlerTypeInit;
import com.soybean.items.ItemsRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;

public class StoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModBlock.initializeClient();
        EntityRegister.initializeClient();
        ScreenHandlerTypeInit.initializeClient();
    }
}
