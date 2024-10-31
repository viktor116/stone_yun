package com.soybean;

import com.soybean.block.ModBlock;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class StoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModBlock.initializeClient();
    }
}
