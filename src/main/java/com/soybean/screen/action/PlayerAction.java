package com.soybean.screen.action;

import com.soybean.screen.ScreenRegister;
import com.soybean.screen.handler.SkullScreenHandler;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;

/**
 * @author soybean
 * @date 2025/4/23 17:56
 * @description
 */
public class PlayerAction {
    private static final Text CONTAINER_TITLE = Text.of("骷髅身体");

    public static void openSkullScreen(PlayerEntity player, SkeletonEntity skeleton) {
        SimpleInventory simpleInventory = new SimpleInventory(27);
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, playerInventory, playerEntity) ->
                        new SkullScreenHandler(
                                ScreenRegister.SKULL_SCREEN_HANDLER,
                                syncId,
                                playerInventory,
                                simpleInventory,
                                3,
                                skeleton),
                CONTAINER_TITLE));
//        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
//                (syncId, playerInventory, playerEntity) ->
//                        new GenericContainerScreenHandler(
//                                ScreenHandlerType.GENERIC_9X3,
//                                syncId,
//                                playerInventory,
//                                inventory,
//                                3),
//                CONTAINER_TITLE
//        ));
    }
}
