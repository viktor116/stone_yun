package com.soybean.block.custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SugarWireBlock extends AbstractPowderWireBlock {

    public SugarWireBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected ItemStack getDropStack() {
        return new ItemStack(Items.SUGAR);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, net.minecraft.util.hit.BlockHitResult hit) {
        if (!world.isClient) {
            player.getHungerManager().add(1, 0.1F);
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.5f, 1.0f);
            world.removeBlock(pos, false);
        }
        return ActionResult.SUCCESS;
    }
}