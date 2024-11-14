package com.soybean.screen.client;

import com.soybean.screen.WitherSkeletonInteractionHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

/**
 * @author soybean
 * @date 2024/11/14 13:38
 * @description
 */
public class WitherSkeletonMerchantScreen extends HandledScreen<WitherSkeletonInteractionHandler> {

    public WitherSkeletonMerchantScreen(WitherSkeletonInteractionHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }
}
