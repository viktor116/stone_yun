package com.soybean.event.impl;

import com.mojang.authlib.GameProfile;
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
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

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
            if (entity instanceof PlayerEntity targetPlayer && !world.isClient) {
                if (HeadlessPlayerManager.isPlayerHeadless(targetPlayer.getUuid())) {
                    return ActionResult.PASS;
                }
                ItemStack stackInHand = player.getStackInHand(hand);
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

                    // 设置玩家为无头状态（持续10秒）
                    HeadlessPlayerManager.setPlayerHeadless(targetPlayer, 200);

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
