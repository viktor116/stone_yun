package com.soybean.items.item;

import com.soybean.entity.custom.MaceProjectileEntity;
import net.minecraft.component.DataComponentTypes;
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

public class MaceCrossbowItem extends CrossbowItem {

    public MaceCrossbowItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            this.shootAll(world, user, hand, stack, 3.15F, 1.0F, null);
        }
        return TypedActionResult.consume(stack);
    }

    @Override
    public void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, LivingEntity target) {
        if (world instanceof ServerWorld serverWorld) {
            ItemStack charged = stack.get(DataComponentTypes.CHARGED_PROJECTILES) != null
                    ? stack.get(DataComponentTypes.CHARGED_PROJECTILES).getProjectiles().stream().findFirst().orElse(new ItemStack(Items.MACE))
                    : new ItemStack(Items.MACE);

            MaceProjectileEntity projectile = new MaceProjectileEntity(serverWorld, shooter, charged.copyWithCount(1));
            projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, 3.5F, 1.0F);
            serverWorld.spawnEntity(projectile);

            world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                    SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);

            ItemStack normalCrossbow = new ItemStack(Items.CROSSBOW);
            normalCrossbow.setDamage(stack.getDamage());
            normalCrossbow.remove(DataComponentTypes.CHARGED_PROJECTILES);
            shooter.setStackInHand(hand, normalCrossbow);
        }
    }
}
