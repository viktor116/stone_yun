package com.soybean.items.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/2/18 9:39
 * @description
 */
public class ObsidianBucketItem extends Item {

    public ObsidianBucketItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, player,RaycastContext.FluidHandling.NONE);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction side = blockHitResult.getSide();

            // 使用 side 来获取目标方块的偏移位置
            BlockPos targetPos = blockPos.offset(side);
            world.setBlockState(targetPos, Blocks.OBSIDIAN.getDefaultState());
            world.playSound(
                    null,
                    targetPos, SoundEvents.BLOCK_STONE_PLACE,
                    player.getSoundCategory(),
                    1.0F,
                    1.0F
            );
            ItemStack stack2 = ItemUsage.exchangeStack(itemStack, player, getEmptiedStack(itemStack, player));
            return TypedActionResult.success(stack2, world.isClient());
        }
    }


    public static ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
        return !player.isInCreativeMode() ? new ItemStack(Items.BUCKET) : stack;
    }
}
