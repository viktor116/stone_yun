package com.soybean.items.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
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
public class WardenBucketItem extends Item {

    public WardenBucketItem(Settings settings) {
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
                WardenEntity wardenEntity = EntityType.WARDEN.create(world);
                if (wardenEntity != null) {
                    // 设置位置
                    wardenEntity.refreshPositionAndAngles(
                            spawnPos.getX() + 0.5,
                            spawnPos.getY(),
                            spawnPos.getZ() + 0.5,
                            world.random.nextFloat() * 360.0F,
                            0.0F
                    );

                    // 初始化Warden
                    wardenEntity.initialize(
                            (ServerWorld)world,
                            world.getLocalDifficulty(spawnPos),
                            SpawnReason.SPAWN_EGG,
                            null
                    );

                    // 设置Warden的目标为生成它的玩家
                    wardenEntity.setPersistent(); // 设置为持久化，防止自然消失
                    world.spawnEntity(wardenEntity);
                    world.playSound(
                            null,
                            spawnPos,
                            SoundEvents.ENTITY_WARDEN_EMERGE,
                            SoundCategory.HOSTILE,
                            5.0F,
                            1.0F
                    );

                    // 触发Warden的感知
                    wardenEntity.increaseAngerAt(player, 150, true);
                }
                return TypedActionResult.success(stack2, world.isClient());
            }
        }
        return super.use(world, player, hand);
    }


    public static ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
        return !player.isInCreativeMode() ? new ItemStack(Items.BUCKET) : stack;
    }
}
