package com.soybean.block.custom;

import com.mojang.serialization.MapCodec;
import com.soybean.block.custom.inventory.ImplementedInventory;
import com.soybean.block.custom.inventory.entity.DemoBlockEntity;
import com.soybean.config.InitValue;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author soybean
 * @date 2024/11/13 17:08
 * @description
 */
public class DemoBlock extends BlockWithEntity implements BlockEntityProvider {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);

    public static final MapCodec<DemoBlock> CODEC = createCodec(DemoBlock::new);

    public DemoBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit)  {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DemoBlockEntity) {
                player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                        (syncId, playerInventory, playerEntity) ->
                                GenericContainerScreenHandler.createGeneric9x3(
                                        syncId,
                                        playerInventory,
                                        (DemoBlockEntity) blockEntity
                                ),
                        Text.translatable("container." + InitValue.MOD_ID + ".cactus")
                ));
            }
        }
        return ActionResult.SUCCESS;
    }
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DemoBlockEntity) {
                ItemScatterer.spawn(world, pos, (DemoBlockEntity)blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        //        if (world.isClient) return ItemActionResult.SUCCESS;
//
//        if (!(world.getBlockEntity(pos) instanceof DemoBlockEntity blockEntity)) {
//            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
//        }
//
//        if (!player.getStackInHand(hand).isEmpty()) {
//            // Check what is the first open slot and put an item from the player's hand there
//            if (blockEntity.getStack(0).isEmpty()) {
//                // Put the stack the player is holding into the inventory
//                blockEntity.setStack(0, player.getStackInHand(hand).copy());
//                // Remove the stack from the player's hand
//                player.getStackInHand(hand).setCount(0);
//            } else if (blockEntity.getStack(1).isEmpty()) {
//                blockEntity.setStack(1, player.getStackInHand(hand).copy());
//                player.getStackInHand(hand).setCount(0);
//            } else {
//                // If the inventory is full we'll notify the player
//                player.sendMessage(Text.literal("The inventory is full! The first slot holds ")
//                        .append(blockEntity.getStack(0).getName())
//                        .append(" and the second slot holds ")
//                        .append(blockEntity.getStack(1).getName()));
//            }
//        }else {
//            // If the player is not holding anything we'll get give him the items in the block entity one by one
//            // Find the first slot that has an item and give it to the player
//            if (!blockEntity.getStack(1).isEmpty()) {
//                // Give the player the stack in the inventory
//                player.getInventory().offerOrDrop(blockEntity.getStack(1));
//                // Remove the stack from the inventory
//                blockEntity.removeStack(1);
//            } else if (!blockEntity.getStack(0).isEmpty()) {
//                player.getInventory().offerOrDrop(blockEntity.getStack(0));
//                blockEntity.removeStack(0);
//            } else {
//                return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
//            }
//        }
//        return ItemActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DemoBlockEntity(pos, state);
    }
}