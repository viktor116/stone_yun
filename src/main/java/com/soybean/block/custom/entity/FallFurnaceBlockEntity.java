package com.soybean.block.custom.entity;

import com.soybean.block.ModBlock;
import com.soybean.block.custom.inventory.ImplementedInventory;
import com.soybean.items.recipes.FallFurnaceRecipe;
import com.soybean.items.recipes.FallFurnaceRecipeLoader;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FallFurnaceBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT_1 = 1;
    public static final int SLOT_OUTPUT_2 = 2;
    public static final int INVENTORY_SIZE = 3;

    public int burnTime;
    public int burnTimeTotal;
    public int cookTime;
    public int cookTimeTotal;

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);

    private final PropertyDelegate propertyDelegate;

    public FallFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.FALL_FURNACE_TYPE, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int size() {
                return 4;
            }

            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> burnTime;
                    case 1 -> burnTimeTotal;
                    case 2 -> cookTime;
                    case 3 -> cookTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int val) {
                switch (i) {
                    case 0 -> burnTime = val;
                    case 1 -> burnTimeTotal = val;
                    case 2 -> cookTime = val;
                    case 3 -> cookTimeTotal = val;
                }
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    public Inventory getInventory() {
        return this;
    }

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        Inventories.readNbt(nbt, items, registries);
        burnTime = nbt.getInt("BurnTime");
        burnTimeTotal = nbt.getInt("BurnTimeTotal");
        cookTime = nbt.getInt("CookTime");
        cookTimeTotal = nbt.getInt("CookTimeTotal");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        Inventories.writeNbt(nbt, items, registries);
        nbt.putInt("BurnTime", burnTime);
        nbt.putInt("BurnTimeTotal", burnTimeTotal);
        nbt.putInt("CookTime", cookTime);
        nbt.putInt("CookTimeTotal", cookTimeTotal);
    }

    public static void tick(World world, BlockPos pos, BlockState state, FallFurnaceBlockEntity be) {
        if (world.isClient()) return;

        boolean dirty = false;

        boolean shouldBeLit = be.canSmelt();
        if (shouldBeLit) {
            if (be.burnTimeTotal == 0) {
                be.burnTimeTotal = 200;
            }
            if (be.burnTime == 0) {
                be.burnTime = be.burnTimeTotal;
            }
            be.burnTime--;
            if (be.burnTime <= 0) {
                be.burnTime = be.burnTimeTotal;
            }

            Optional<FallFurnaceRecipe> recipeOpt = FallFurnaceRecipeLoader.findRecipe(be.items.get(SLOT_INPUT));
            if (recipeOpt.isPresent()) {
                FallFurnaceRecipe recipe = recipeOpt.get();
                if (be.cookTimeTotal != recipe.getCookTime()) {
                    be.cookTimeTotal = recipe.getCookTime();
                    dirty = true;
                }

                be.cookTime++;
                if (be.cookTime >= be.cookTimeTotal) {
                    be.cookTime = 0;
                    Optional<FallFurnaceRecipe> resultOpt = FallFurnaceRecipeLoader.findRecipe(be.items.get(SLOT_INPUT));
                    if (resultOpt.isPresent()) {
                        FallFurnaceRecipe resultRecipe = resultOpt.get();
                        be.items.get(SLOT_INPUT).decrement(1);
                        List<FallFurnaceRecipe.ResultEntry> results = resultRecipe.getResults();
                        for (int i = 0; i < results.size() && i < 2; i++) {
                            be.insertIntoSlot(i + SLOT_OUTPUT_1, results.get(i).item, results.get(i).count);
                        }
                        be.cookTimeTotal = resultRecipe.getCookTime();
                    }
                    dirty = true;
                }
            }
        } else {
            if (be.burnTime != 0 || be.burnTimeTotal != 0 || be.cookTime != 0) {
                be.burnTime = 0;
                be.burnTimeTotal = 0;
                be.cookTime = 0;
                dirty = true;
            }
        }

        boolean wasLit = state.get(AbstractFurnaceBlock.LIT);
        if (wasLit != shouldBeLit) {
            world.setBlockState(pos, state.with(AbstractFurnaceBlock.LIT, shouldBeLit));
            dirty = true;
        }

        if (dirty) {
            be.markDirty();
        }
    }

    private boolean canSmelt() {
        if (items.get(SLOT_INPUT).isEmpty()) return false;
        Optional<FallFurnaceRecipe> recipeOpt = FallFurnaceRecipeLoader.findRecipe(items.get(SLOT_INPUT));
        if (recipeOpt.isEmpty()) return false;
        List<FallFurnaceRecipe.ResultEntry> results = recipeOpt.get().getResults();
        for (int i = 0; i < results.size() && i < 2; i++) {
            if (!canInsertIntoSlot(i + SLOT_OUTPUT_1, results.get(i).item, results.get(i).count)) return false;
        }
        return true;
    }

    private boolean canInsertIntoSlot(int slot, ItemStack stack, int count) {
        if (stack.isEmpty()) return true;
        if (slot >= items.size()) return false;
        ItemStack existing = items.get(slot);
        if (existing.isEmpty()) return true;
        if (ItemStack.areItemsAndComponentsEqual(existing, stack)) {
            return existing.getCount() + count <= existing.getMaxCount();
        }
        return false;
    }

    private void insertIntoSlot(int slot, ItemStack stack, int count) {
        if (stack.isEmpty() || slot >= items.size()) return;
        ItemStack existing = items.get(slot);
        if (existing.isEmpty()) {
            ItemStack copy = stack.copy();
            copy.setCount(count);
            items.set(slot, copy);
        } else if (ItemStack.areItemsAndComponentsEqual(existing, stack)) {
            existing.increment(count);
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.stone.fall_furnace");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new com.soybean.screen.handler.FallFurnaceScreenHandler(syncId, playerInventory, this);
    }
}
