package com.soybean.items.custom;

import com.soybean.entity.EntityRegister;
import com.soybean.entity.custom.HimEntity;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * @author soybean
 * @date 2024/12/3 10:17
 * @description
 */
public class FlintAndSteelCustomItem extends FlintAndSteelItem {
    public FlintAndSteelCustomItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity playerEntity = context.getPlayer();
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState) && !CandleCakeBlock.canBeLit(blockState)) {
            BlockPos blockPos2 = blockPos.offset(context.getSide());
            if (AbstractFireBlock.canPlaceAt(world, blockPos2, context.getHorizontalPlayerFacing())) {
                world.playSound(playerEntity, blockPos2, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
                world.setBlockState(blockPos2, blockState2, 11);
                world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
                ItemStack itemStack = context.getStack();
                if (playerEntity instanceof ServerPlayerEntity) {
                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos2, itemStack);
                    LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                    lightning.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    world.spawnEntity(lightning);
                    HimEntity himEntity = new HimEntity(EntityRegister.HIM, world);
                    himEntity.setPosition(blockPos.getX(), blockPos.getY()+2, blockPos.getZ());
                    float angle = (float) Math.toDegrees(Math.atan2(
                            playerEntity.getZ() - himEntity.getZ(),
                            playerEntity.getX() - himEntity.getX()
                    )) - 90;
                    himEntity.setYaw(angle);
                    himEntity.setHeadYaw(angle);
                    world.spawnEntity(himEntity);
                    spawnParticles(world, blockPos.up().up(), 20);
                    itemStack.damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
                }

                return ActionResult.success(world.isClient());
            } else {
                return ActionResult.FAIL;
            }
        } else {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            world.setBlockState(blockPos, (BlockState)blockState.with(Properties.LIT, true), 11);
            world.emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
            if (playerEntity != null) {
                context.getStack().damage(1, playerEntity, LivingEntity.getSlotForHand(context.getHand()));
            }

            return ActionResult.success(world.isClient());
        }
    }

    public static void spawnParticles(World world, BlockPos blockPos,int num) {
        for (int i = 0; i < num; i++) {
            // 随机偏移，让粒子围绕生成点散开
            double offsetX = (Math.random() - 0.5) * 2;
            double offsetY = Math.random() * 1.5;
            double offsetZ = (Math.random() - 0.5) * 2;

            // 多种粒子组合，增加视觉效果
            world.addParticle(
                    ParticleTypes.ENCHANT,
                    blockPos.getX() + 0.5 + offsetX,
                    blockPos.getY() + 0.5 + offsetY,
                    blockPos.getZ() + 0.5 + offsetZ,
                    0.1 * offsetX,
                    0.1 * offsetY,
                    0.1 * offsetZ
            );

            // 添加额外的粒子类型，增加层次感
            world.addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    blockPos.getX() + 0.5 + offsetX,
                    blockPos.getY() + 0.5 + offsetY,
                    blockPos.getZ() + 0.5 + offsetZ,
                    0.05 * offsetX,
                    0.05 * offsetY,
                    0.05 * offsetZ
            );

            // 可选：火花效果
            world.addParticle(
                    ParticleTypes.FIREWORK,
                    blockPos.getX() + 0.5 + offsetX,
                    blockPos.getY() + 0.5 + offsetY,
                    blockPos.getZ() + 0.5 + offsetZ,
                    0.08 * offsetX,
                    0.08 * offsetY,
                    0.08 * offsetZ
            );
        }
    }
}
