package com.soybean.items.item;

import com.soybean.entity.custom.BedProjectileEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

/**
 * 床弩物品，可以发射床并在命中时产生爆炸
 */
public class CrossbowBedItem extends CrossbowItem {
    
    public CrossbowBedItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        
        // 直接射击
        if (!world.isClient) {
            this.shootAll(world, user, hand, stack, 3.15F, 1.0F, null);
        }
        return TypedActionResult.consume(stack);
    }
    
    @Override
    public void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, LivingEntity target) {
        if (world instanceof ServerWorld serverWorld) {
            // 获取床作为弹药
            ItemStack bedProjectile = new ItemStack(Items.RED_BED);
            
            ChargedProjectilesComponent chargedProjectilesComponent = stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
            if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
                List<ItemStack> projectiles = chargedProjectilesComponent.getProjectiles();
                if (!projectiles.isEmpty()) {
                    bedProjectile = projectiles.get(0);
                }
            }
            
            // 发射床实体
            BedProjectileEntity projectile = new BedProjectileEntity(serverWorld, shooter, bedProjectile);
            float projectileSpeed = 2.5F;
            projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, projectileSpeed, 1.0F);
            serverWorld.spawnEntity(projectile);
            
            // 播放发射音效
            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), 
                    SoundEvents.ITEM_CROSSBOW_SHOOT, shooter.getSoundCategory(), 1.0F, 1.0F);
            
            // 替换为普通弩
            ItemStack normalCrossbow = new ItemStack(Items.CROSSBOW);
            normalCrossbow.setDamage(stack.getDamage()); // 复制耐久度
            normalCrossbow.remove(DataComponentTypes.CHARGED_PROJECTILES); // 移除弹药组件
            
            // 替换玩家手中的物品
            shooter.setStackInHand(hand, normalCrossbow);
        }
    }
} 