package com.soybean.screen;

import com.soybean.config.InitValue;
import com.soybean.screen.handler.BeefFurnaceScreenHandler;
import com.soybean.screen.handler.SkullScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2025/4/23 15:05
 * @description
 */
public class ScreenRegister {

    public static final ScreenHandlerType<BeefFurnaceScreenHandler> BEEF_FURNACE_SCREEN_HANDLER_TYPE = Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of(InitValue.MOD_ID, "beef_furnace"),
            new ScreenHandlerType<>((syncId, playerInventory) -> new BeefFurnaceScreenHandler(syncId, playerInventory), FeatureFlags.VANILLA_FEATURES));

    public static ScreenHandlerType<SkullScreenHandler> SKULL_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER,
            Identifier.of(InitValue.MOD_ID, "beef_furnace"),
            new ScreenHandlerType<>(SkullScreenHandler::createGeneric9x3, FeatureFlags.VANILLA_FEATURES));

    public static void init(){

    }
}
