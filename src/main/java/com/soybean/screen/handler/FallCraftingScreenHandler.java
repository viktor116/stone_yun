package com.soybean.screen.handler;

import com.soybean.block.ModBlock;
import com.soybean.items.recipes.FallCraftingRecipe;
import com.soybean.items.recipes.FallCraftingRecipeLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.Optional;

public class FallCraftingScreenHandler extends ScreenHandler {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_START = 1;
    private static final int OUTPUT_COUNT = 9;
    private static final int INV_START = OUTPUT_START + OUTPUT_COUNT;

    private final SimpleInventory inputInventory = new SimpleInventory(1);
    private final SimpleInventory outputInventory = new SimpleInventory(9);
    private final PlayerInventory playerInventory;
    private boolean isUpdating = false;
    private boolean consumed = false;

    public FallCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModBlock.FALL_CRAFTING_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;

        this.addSlot(new Slot(this.inputInventory, 0, 35, 34) {
            @Override
            public int getMaxItemCount() {
                return 64;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slotIndex = col + row * 3;
                this.addSlot(new OutputSlot(this.outputInventory, slotIndex, 94 + col * 18, 16 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        this.inputInventory.addListener(inv -> updateRecipes());
        this.outputInventory.addListener(inv -> onOutputChanged());
    }

    private void updateRecipes() {
        if (consumed) {
            if (this.inputInventory.getStack(INPUT_SLOT).isEmpty()) {
                return;
            }
            consumed = false;
        }
        isUpdating = true;
        this.outputInventory.clear();
        ItemStack input = this.inputInventory.getStack(INPUT_SLOT);
        if (!input.isEmpty()) {
            Optional<FallCraftingRecipe> recipeOpt = FallCraftingRecipeLoader.findRecipe(input);
            if (recipeOpt.isPresent()) {
                FallCraftingRecipe recipe = recipeOpt.get();
                if (input.getCount() >= recipe.getConsumeCount()) {
                    ItemStack[] slots = recipe.getGridResults();
                    for (int i = 0; i < 9; i++) {
                        this.outputInventory.setStack(i, slots[i]);
                    }
                }
            }
        }
        isUpdating = false;
    }

    private void onOutputChanged() {
        if (isUpdating || consumed) return;

        ItemStack input = this.inputInventory.getStack(INPUT_SLOT);
        Optional<FallCraftingRecipe> recipeOpt = FallCraftingRecipeLoader.findRecipe(input);
        if (recipeOpt.isPresent()) {
            int consume = recipeOpt.get().getConsumeCount();
            if (input.getCount() >= consume) {
                this.inputInventory.setStack(INPUT_SLOT, new ItemStack(input.getItem(), input.getCount() - consume));
                consumed = true;
            }
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasStack()) return ItemStack.EMPTY;
        ItemStack original = slot.getStack();
        ItemStack result = original.copy();

        if (index == INPUT_SLOT) {
            if (!this.insertItem(original, INV_START, INV_START + 36, true)) {
                return ItemStack.EMPTY;
            }
        } else if (index >= OUTPUT_START && index < OUTPUT_START + OUTPUT_COUNT) {
            isUpdating = true;
            for (int i = 0; i < 9; i++) {
                ItemStack s = this.outputInventory.getStack(i);
                if (!s.isEmpty()) {
                    ItemStack copy = s.copy();
                    if (!this.insertItem(copy, INV_START, INV_START + 36, true)) {
                        playerInventory.player.dropItem(copy, false);
                    }
                }
            }
            this.outputInventory.clear();
            isUpdating = false;
            this.onOutputChanged();
            return ItemStack.EMPTY;
        } else {
            if (!this.insertItem(original, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (original.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }
        return result;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        if (!player.getWorld().isClient()) {
            ItemStack input = this.inputInventory.getStack(INPUT_SLOT);
            if (!input.isEmpty()) {
                player.dropItem(input, false);
                this.inputInventory.setStack(INPUT_SLOT, ItemStack.EMPTY);
            }
        }
        super.onClosed(player);
    }

    private class OutputSlot extends Slot {
        public OutputSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }
    }
}
