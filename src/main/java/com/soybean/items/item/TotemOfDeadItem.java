package com.soybean.items.item;

import com.soybean.entity.custom.TotemOfDeadEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * @author soybean
 * @date 2025/4/24 13:24
 * @description
 */
public class TotemOfDeadItem extends Item{
    public TotemOfDeadItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.NONE);
        if (blockHitResult.getType() == HitResult.Type.BLOCK && world.getBlockState(blockHitResult.getBlockPos()).isOf(Blocks.END_PORTAL_FRAME)) {
            return TypedActionResult.pass(itemStack);
        } else {
            user.setCurrentHand(hand);
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld)world;
                BlockPos blockPos = user.getBlockPos().add(0,10,0);
                if (blockPos != null) {
                    TotemOfDeadEntity eyeOfEnderEntity = new TotemOfDeadEntity(world, user.getX(), user.getBodyY(0.5), user.getZ());
                    eyeOfEnderEntity.setItem(itemStack);
                    eyeOfEnderEntity.initTargetPos(blockPos);
                    world.emitGameEvent(GameEvent.PROJECTILE_SHOOT, eyeOfEnderEntity.getPos(), GameEvent.Emitter.of(user));
                    world.spawnEntity(eyeOfEnderEntity);
                    if (user instanceof ServerPlayerEntity) {
                        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)user;
                        Criteria.USED_ENDER_EYE.trigger(serverPlayerEntity, blockPos);
                    }

                    float f = MathHelper.lerp(world.random.nextFloat(), 0.33F, 0.5F);
                    world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 1.0F, f);
                    itemStack.decrementUnlessCreative(1, user);
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                    user.swingHand(hand, true);
                    return TypedActionResult.success(itemStack);
                }
            }

            return TypedActionResult.consume(itemStack);
        }
    }
}
