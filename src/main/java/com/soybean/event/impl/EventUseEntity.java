package com.soybean.event.impl;

import com.mojang.authlib.GameProfile;
import com.soybean.config.InitValue;
import com.soybean.items.ItemsRegister;
import com.soybean.items.item.WardenBucketItem;
import com.soybean.manager.HeadlessPlayerManager;
import com.soybean.screen.WitherSkeletonInteractionHandler;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.village.Merchant;
import net.minecraft.village.SimpleMerchant;
import net.minecraft.village.VillagerProfession;

/**
 * @author soybean
 * @date 2024/12/4 12:04
 * @description
 */
public class EventUseEntity {
    public static void register(){
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (entity instanceof EnderDragonEntity || entity instanceof EnderDragonPart) {
                WitherSkeletonInteractionHandler.handleRightClickOnDragon(player);
                return ActionResult.SUCCESS;
            }
            if (entity instanceof WitherSkeletonEntity) {
                WitherSkeletonInteractionHandler.handleRightClick(player);
                return ActionResult.SUCCESS;
            }
            if(entity instanceof SheepEntity){
                WitherSkeletonInteractionHandler.handleRightClickOnSheep(player);
                return ActionResult.SUCCESS;
            }
            if(entity instanceof PiglinEntity){
                WitherSkeletonInteractionHandler.handleRightClickOnPiglin(player);
                return ActionResult.SUCCESS;
            }
            if(entity instanceof EndermanEntity){
                WitherSkeletonInteractionHandler.handleRightClickOnEnderman(player);
                return ActionResult.SUCCESS;
            }
            if(entity instanceof BlazeEntity){
                WitherSkeletonInteractionHandler.handleRightClickOnBlaze(player);
                return ActionResult.SUCCESS;
            }
            if(entity instanceof VillagerEntity villager){
                VillagerProfession profession = villager.getVillagerData().getProfession();
                if(profession == VillagerProfession.NONE){
                    WitherSkeletonInteractionHandler.handleRightClickOnCommonVillager(player);
                }
                return ActionResult.SUCCESS;
            }
            if(entity instanceof WardenEntity wardenEntity){
                ItemStack stackInHand = player.getStackInHand(hand);
                if(stackInHand.getItem() instanceof BucketItem){
                    wardenEntity.discard();
                    ItemStack wardenBucket  = ItemsRegister.WARDEN_BUCKET.getDefaultStack();
                    if (stackInHand.getCount() == 1) {
                        player.setStackInHand(hand, wardenBucket);
                    } else {
                        stackInHand.decrement(1);
                        if (!player.getInventory().insertStack(wardenBucket)) {
                            // 如果物品栏满了，在玩家位置生成物品实体
                            double x = player.getX();
                            double y = player.getY();
                            double z = player.getZ();
                            ItemEntity itemEntity = new ItemEntity(world, x, y, z, wardenBucket);
                            world.spawnEntity(itemEntity);
                        }
                    }
                }
                return ActionResult.SUCCESS;
            }

            if (entity instanceof PlayerEntity targetPlayer && !world.isClient) {
                ItemStack stackInHand = player.getStackInHand(hand);
                if (HeadlessPlayerManager.isPlayerHeadless(targetPlayer.getUuid())) {
                    if(stackInHand.getItem() instanceof PlayerHeadItem playerHeadItem){
                        String nameHead = playerHeadItem.getName(stackInHand).getString();
                        if(nameHead.contains(targetPlayer.getName().getString())){
                            HeadlessPlayerManager.removeHeadlessState(targetPlayer.getUuid());
                            if(!player.isInCreativeMode()){
                                stackInHand.decrement(1);
                            }
                            return ActionResult.SUCCESS;
                        }
                    }
                    return ActionResult.PASS;
                }

                if(stackInHand.getItem() instanceof ShearsItem){
                    if(!player.isInCreativeMode()){
                        stackInHand.setDamage(stackInHand.getDamage()+1);
                    }
                    // 创建玩家头颅物品
                    ItemStack headStack = new ItemStack(Items.PLAYER_HEAD);

                    // 设置头颅所有者
                    GameProfile gameProfile = targetPlayer.getGameProfile();
                    ProfileComponent profile = new ProfileComponent(gameProfile);
                    headStack.set(DataComponentTypes.PROFILE, profile);

                    // 在目标玩家位置生成掉落物
                    ItemEntity itemEntity = new ItemEntity(
                            world,
                            targetPlayer.getX()-0.5+Math.random(),
                            targetPlayer.getY() + targetPlayer.getEyeHeight(targetPlayer.getPose()),
                            targetPlayer.getZ()-0.5+Math.random(),
                            headStack
                    );

                    // 设置玩家为无头状态（持续1min * 60）
                    HeadlessPlayerManager.setPlayerHeadless(targetPlayer, 1200*60);

                    // 设置物品的速度，使其稍微弹起
                    itemEntity.setVelocity(
                            (Math.random() - 0.5) * 0.2,  // x方向随机速度
                            0.2,                          // y方向向上的速度
                            (Math.random() - 0.5) * 0.2   // z方向随机速度
                    );

                    itemEntity.setPickupDelay(20);

                    // 将物品实体添加到世界中
                    world.spawnEntity(itemEntity);

                    // 播放剪刀使用的音效
                    world.playSound(
                            null,
                            targetPlayer.getX(),
                            targetPlayer.getY(),
                            targetPlayer.getZ(),
                            SoundEvents.ENTITY_SHEEP_SHEAR,
                            SoundCategory.PLAYERS,
                            1.0F,
                            1.0F
                    );

                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }
}
