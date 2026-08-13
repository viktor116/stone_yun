package com.soybean.items.item;

import com.soybean.entity.custom.MaceProjectileEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class MaceBowItem extends BowItem {

    public MaceBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.isOf(Items.MACE);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int useTicks = this.getMaxUseTime(stack, user) - remainingUseTicks;
            float pullProgress = getPullProgress(useTicks);
            if (pullProgress >= 0.1f) {
                ItemStack projectile = player.getProjectileType(stack);
                if (!projectile.isOf(Items.MACE)) return;
                if (!world.isClient) {
                    MaceProjectileEntity proj = new MaceProjectileEntity(world, player, projectile.copyWithCount(1));
                    proj.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 3.5f * pullProgress, 1.0f);
                    world.spawnEntity(proj);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f,
                            1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f));
                    stack.damage(1, player, EquipmentSlot.MAINHAND);
                    if (!player.getAbilities().creativeMode) {
                        projectile.decrement(1);
                    }
                }
            }
        }
    }
}
