package com.soybean.screen.client;

import com.soybean.screen.handler.SkullScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2025/4/23 17:51
 * @description
 */
public class SkullScreen extends HandledScreen<SkullScreenHandler> implements ScreenHandlerProvider<SkullScreenHandler> {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/generic_54.png");
    private final int rows;

    public SkullScreen(SkullScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.rows = handler.getRows();
        this.backgroundHeight = 114 + this.rows * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
        context.drawTexture(TEXTURE, i, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
    }
}
