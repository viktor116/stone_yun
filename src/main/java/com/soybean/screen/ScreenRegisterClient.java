package com.soybean.screen;

import com.soybean.screen.client.BeefFurnaceScreen;
import com.soybean.screen.client.SkullScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

/**
 * @author soybean
 * @date 2025/4/23 15:20
 * @description
 */
public class ScreenRegisterClient {
    public static void init() {
        HandledScreens.register(ScreenRegister.BEEF_FURNACE_SCREEN_HANDLER_TYPE, BeefFurnaceScreen::new);
        HandledScreens.register(ScreenRegister.SKULL_SCREEN_HANDLER, SkullScreen::new);
    }
}
