package com.soybean.event.impl;

import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class EventUseBlock {
    public static void register(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult)  -> {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            ItemStack stackInHand = player.getStackInHand(hand);
            
            // 检测斧子右键原木掉落树皮
            if (stackInHand.getItem() instanceof AxeItem) {
                if (blockState.isIn(BlockTags.LOGS)) {
                    // 检查是否是去皮原木，如果是则不能掉落树皮
                    if (blockState.getBlock().toString().contains("stripped")) {
                        return ActionResult.PASS; // 去皮原木不处理
                    }
                    
                    // 播放砍伐音效
                    world.playSound(null, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    
                    // 生成树皮物品
                    ItemStack treeBarkStack = ItemsRegister.TREE_BARK.getDefaultStack();
                    treeBarkStack.setCount(1);
                    
                    // 在方块周围随机位置生成物品实体
                    double randomX = blockPos.getX() + 0.2 + world.getRandom().nextDouble() * 0.6; // 0.2-0.8范围
                    double randomY = blockPos.getY() + 0.5;
                    double randomZ = blockPos.getZ() + 0.2 + world.getRandom().nextDouble() * 0.6; // 0.2-0.8范围
                    
                    ItemEntity itemEntity = new ItemEntity(
                        world,
                        randomX,
                        randomY,
                        randomZ,
                        treeBarkStack
                    );
                    
                    // 设置物品的速度，使其向四周随机弹起
                    itemEntity.setVelocity(
                        (world.getRandom().nextDouble() - 0.5) * 0.3,  // x方向随机速度 -0.15到0.15
                        0.1 + world.getRandom().nextDouble() * 0.2,    // y方向向上的速度 0.1到0.3
                        (world.getRandom().nextDouble() - 0.5) * 0.3   // z方向随机速度 -0.15到0.15
                    );
                    
                    itemEntity.setPickupDelay(10); // 设置拾取延迟
                    world.spawnEntity(itemEntity);
                    
                    // 消耗斧子耐久度（如果不是创造模式）
//                    if (!player.getAbilities().creativeMode) {
//                        stackInHand.damage(1,player, EquipmentSlot.MAINHAND);
//                    }
                    
                    return ActionResult.PASS;
                }
            }
            
            if(stackInHand.getItem() == Items.BUCKET){
                //黑曜石
                if(blockState.getBlock() == Blocks.OBSIDIAN){
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                    world.playSound(null, blockPos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 0.5F, 2.6F);
                    ItemStack obsidianBucket  = ItemsRegister.OBSIDIAN_BUCKET.getDefaultStack();
                    if (stackInHand.getCount() == 1) {
                        player.setStackInHand(hand, obsidianBucket);
                    } else {
                        stackInHand.decrement(1);
                        if (!player.getInventory().insertStack(obsidianBucket)) {
                            // 如果物品栏满了，在玩家位置生成物品实体
                            double x = player.getX();
                            double y = player.getY();
                            double z = player.getZ();
                            ItemEntity itemEntity = new ItemEntity(world, x, y, z, obsidianBucket);
                            world.spawnEntity(itemEntity);
                        }
                    }
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        });
    }
}
