package com.soybean;

import com.soybean.block.ModBlock;
import com.soybean.entity.EntityRegisterClient;
import com.soybean.event.EventRegister;
import com.soybean.init.ScreenHandlerTypeInit;
import com.soybean.items.client.ItemRendererRegister;
import com.soybean.network.NetworkRegister;
import com.soybean.screen.ScreenRegisterClient;
import com.soybean.utils.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;

public class StoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemRendererRegister.init();
        ModBlock.initializeClient();
        EntityRegisterClient.initializeClient();
        ScreenHandlerTypeInit.initializeClient();
        EventRegister.InitializeClient();
        NetworkRegister.initClient();
        ModModelPredicates.registerModelPredicates();
        ScreenRegisterClient.init();
    }
}
