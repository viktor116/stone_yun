package com.soybean.screen.client;

import com.soybean.screen.handler.FallFurnaceScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FallFurnaceScreen extends HandledScreen<FallFurnaceScreenHandler> {

    private static final Identifier TEXTURE = Identifier.of("stone", "textures/gui/fall_furnace.png");

    public FallFurnaceScreen(FallFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (width - backgroundWidth) / 2;
        int j = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, backgroundWidth, backgroundHeight);

        int cookTime = handler.getPropertyDelegate().get(2);
        int cookTimeTotal = handler.getPropertyDelegate().get(3);
        if (cookTimeTotal > 0 && cookTime > 0) {
            int l = 24 * cookTime / cookTimeTotal;
            context.drawTexture(TEXTURE, i + 83, j + 35, 176, 14, l + 1, 16);
        }
    }
}
