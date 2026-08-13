package com.soybean.event.impl;

import com.soybean.block.ModBlock;
import com.soybean.block.custom.BlazePowderWireBlock;
import com.soybean.block.custom.GunpowderWireBlock;
import com.soybean.block.custom.SugarWireBlock;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EventPowders {

    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;
            ItemStack stack = player.getStackInHand(hand);
            Block powderBlock = null;

            if (stack.isOf(Items.GUNPOWDER)) powderBlock = ModBlock.GUNPOWDER_WIRE;
            else if (stack.isOf(Items.GLOWSTONE_DUST)) powderBlock = ModBlock.GLOWSTONE_POWDER_WIRE;
            else if (stack.isOf(Items.BLAZE_POWDER)) powderBlock = ModBlock.BLAZE_POWDER_WIRE;
            else if (stack.isOf(Items.SUGAR)) powderBlock = ModBlock.SUGAR_WIRE;

            if (powderBlock == null) return ActionResult.PASS;

            BlockPos clickedPos = hitResult.getBlockPos();
            Direction face = hitResult.getSide();
            BlockPos placePos = clickedPos.offset(face);

            BlockState defaultState = powderBlock.getDefaultState();
            if (!defaultState.canPlaceAt(world, placePos)) return ActionResult.PASS;

            Vec3d hitOnPlace = Vec3d.ofCenter(placePos).add(face.getOffsetX() * -0.25, face.getOffsetY() * -0.25, face.getOffsetZ() * -0.25);
            BlockHitResult placeHit = new BlockHitResult(hitOnPlace, face.getOpposite(), placePos, false);
            ItemPlacementContext ctx = new ItemPlacementContext(player, hand, stack, placeHit);
            BlockState placeState = powderBlock.getPlacementState(ctx);
            if (placeState == null) placeState = defaultState;

            world.setBlockState(placePos, placeState);
            world.playSound(null, placePos, SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 0.5f, 1.0f);
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            return ActionResult.SUCCESS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;
            ItemStack stack = player.getStackInHand(hand);
            if (!stack.isOf(Items.FLINT_AND_STEEL)) return ActionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (block instanceof GunpowderWireBlock gpw) {
                gpw.ignite((net.minecraft.server.world.ServerWorld) world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 0.5f, 0.8f);
                stack.damage(1, player, hand == Hand.MAIN_HAND ?
                        net.minecraft.entity.EquipmentSlot.MAINHAND : net.minecraft.entity.EquipmentSlot.OFFHAND);
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }
}