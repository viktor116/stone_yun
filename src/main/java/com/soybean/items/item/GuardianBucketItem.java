package com.soybean.items.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2025/2/18 9:39
 * @description
 */
public class GuardianBucketItem extends Item {

    public GuardianBucketItem(Settings settings) {
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
            if(!world.isClient){
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockPos spawnPos = blockPos.up(); // 在目标方块上方生成
                ItemStack stack2 = ItemUsage.exchangeStack(itemStack, player, getEmptiedStack(itemStack, player));
                GuardianEntity guardianEntity = EntityType.GUARDIAN.create(world);
                assert guardianEntity != null;
                guardianEntity.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
                world.spawnEntity(guardianEntity);
                world.playSound(player, player.getBlockPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                return TypedActionResult.success(stack2, world.isClient());
            }
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }


    public static ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
        return !player.isInCreativeMode() ? new ItemStack(Items.BUCKET) : stack;
    }
}
