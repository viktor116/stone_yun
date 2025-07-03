package com.soybean.event.impl;

import com.mojang.authlib.GameProfile;
import com.soybean.items.ItemsRegister;
import com.soybean.manager.HeadlessPlayerManager;
import com.soybean.screen.action.PlayerAction;
import com.soybean.screen.handler.WitherSkeletonInteractionHandler;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.VillagerProfession;
import net.minecraft.block.Blocks;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;

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
            //末影人
            if(entity instanceof EndermanEntity){
                handleEntityAsItem(entity,player);
//                WitherSkeletonInteractionHandler.handleRightClickOnEnderman(player);
                return ActionResult.SUCCESS;
            }
            //烈焰人
            if(entity instanceof BlazeEntity){
                handleEntityAsItem(entity,player);
//                WitherSkeletonInteractionHandler.handleRightClickOnBlaze(player);
                return ActionResult.SUCCESS;
            }
            if(entity instanceof SpiderEntity){
                WitherSkeletonInteractionHandler.handleRightClickOnSpider(player);
                return ActionResult.SUCCESS;
            }
            //骷髅
            if(entity instanceof SkeletonEntity skeleton){
//                PlayerAction.openSkullScreen(player,skeleton); //打开骷髅身体
                handleEntityAsItem(entity,player);
                return ActionResult.SUCCESS;
            }
            //苦力怕
            if(entity instanceof CreeperEntity creeperEntity){
                handleEntityAsItem(entity,player);
                return ActionResult.SUCCESS;
            }

            if(entity instanceof VillagerEntity villager){
                VillagerProfession profession = villager.getVillagerData().getProfession();
                if(profession == VillagerProfession.NONE){
                    WitherSkeletonInteractionHandler.handleRightClickOnCommonVillager(player);
                }
                return ActionResult.SUCCESS;
            }
            //监守者
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
            //守卫者
            if(entity instanceof GuardianEntity guardianEntity){
                ItemStack stackInHand = player.getStackInHand(hand);
                if(stackInHand.getItem() instanceof BucketItem){
                    guardianEntity.discard();
                    ItemStack wardenBucket  = ItemsRegister.GUARDIAN_BUCKET.getDefaultStack();
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
            
            // 铁傀儡
            if (entity instanceof IronGolemEntity ironGolem) {
                if (!world.isClient) {
                    // 获取铁傀儡的位置
                    BlockPos golemPos = ironGolem.getBlockPos();
                    
                    // 生成4个铁块，按照铁傀儡的正确摆放方式
                    // 铁傀儡的摆放方式：上面3个铁块（T字形），下面中间1个铁块，中间是南瓜头
                    world.setBlockState(golemPos, Blocks.IRON_BLOCK.getDefaultState()); // 底部中心
                    world.setBlockState(golemPos.up(), Blocks.IRON_BLOCK.getDefaultState()); // 中间（身体）
                    world.setBlockState(golemPos.up().west(), Blocks.IRON_BLOCK.getDefaultState()); // 上面北边（左臂）
                    world.setBlockState(golemPos.up().east(), Blocks.IRON_BLOCK.getDefaultState()); // 上面东边（右臂）
                    
                    // 给玩家一个南瓜头
                    ItemStack pumpkinStack = new ItemStack(Items.CARVED_PUMPKIN);
                    if (!player.getInventory().insertStack(pumpkinStack)) {
                        // 如果物品栏满了，在玩家位置生成物品实体
                        ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), pumpkinStack);
                        world.spawnEntity(itemEntity);
                    }
                    
                    // 播放铁傀儡攻击音效
                    world.playSound(null, golemPos, SoundEvents.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    
                    // 让铁傀儡消失
                    ironGolem.discard();
                    
                    return ActionResult.SUCCESS;
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

    public static void handleEntityAsItem(Entity entity,PlayerEntity player){
        World world = player.getWorld();
        BlockPos blockPos = entity.getBlockPos();

        // 播放砍伐音效
        world.playSound(null, blockPos, SoundEvents.ENTITY_SNIFFER_DROP_SEED, SoundCategory.PLAYERS, 1.0F, 1.0F);

        // 生成树皮物品
        ItemStack stack = null;
        if(entity instanceof CreeperEntity){
            stack = ItemsRegister.CREEPER_ITEM.getDefaultStack();
        }else if(entity instanceof SkeletonEntity){
            stack = ItemsRegister.SKELETON_ITEM.getDefaultStack();
        }else if(entity instanceof EndermanEntity){
            stack = ItemsRegister.ENDER_MAN_ITEM.getDefaultStack();
        }else if(entity instanceof BlazeEntity){
            stack = ItemsRegister.FLAME_MAN_ITEM.getDefaultStack();
        }

        stack.setCount(1);

        // 在方块周围随机位置生成物品实体
        double randomX = blockPos.getX() + 0.2 + world.getRandom().nextDouble() * 0.6; // 0.2-0.8范围
        double randomY = blockPos.getY() + 0.5;
        double randomZ = blockPos.getZ() + 0.2 + world.getRandom().nextDouble() * 0.6; // 0.2-0.8范围

        ItemEntity itemEntity = new ItemEntity(
                world,
                randomX,
                randomY,
                randomZ,
                stack
        );
        entity.discard();
        world.spawnEntity(itemEntity);
    }
}
