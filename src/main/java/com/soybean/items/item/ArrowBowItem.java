package com.soybean.items.item;

import com.soybean.entity.custom.BowProjectileEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class ArrowBowItem extends BowItem {

    public ArrowBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.isOf(Items.BOW);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int useTicks = this.getMaxUseTime(stack, user) - remainingUseTicks;
            float pullProgress = getPullProgress(useTicks);
            if (pullProgress >= 0.1f) {
                ItemStack projectile = player.getProjectileType(stack);
                if (!projectile.isOf(Items.BOW)) return;
                if (!world.isClient) {
                    BowProjectileEntity proj = new BowProjectileEntity(world, player, projectile.copyWithCount(1));
                    proj.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 4.0f * pullProgress, 1.0f);
                    world.spawnEntity(proj);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 2.0f);
                    stack.damage(1, player, EquipmentSlot.MAINHAND);
                    if (!player.getAbilities().creativeMode) {
                        projectile.decrement(1);
                    }
                }
            }
        }
    }
}
