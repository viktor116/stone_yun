package com.soybean.screen.client;

import com.soybean.screen.handler.BeefFurnaceScreenHandler;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2025/4/23 15:19
 * @description
 */
public class BeefFurnaceScreen extends AbstractFurnaceScreen<BeefFurnaceScreenHandler> {

    // Use your mod's identifier for the texture
    private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.ofVanilla("container/furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.ofVanilla("container/furnace/burn_progress");
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/furnace.png");

    public BeefFurnaceScreen(BeefFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new FurnaceRecipeBookScreen(), inventory, title, TEXTURE, LIT_PROGRESS_TEXTURE, BURN_PROGRESS_TEXTURE);
    }

}
