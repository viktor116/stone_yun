package com.soybean.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class MainRespawnAnchorBlock extends RespawnAnchorBlock {
    public static final IntProperty CHARGES = Properties.CHARGES;

    public MainRespawnAnchorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(CHARGES, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(CHARGES);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, net.minecraft.util.Hand hand, BlockHitResult hit) {
        int charges = state.get(CHARGES);
        if (stack.isOf(Items.GLOW_LICHEN) && charges < 4) {
            if (!world.isClient) {
                world.setBlockState(pos, state.with(CHARGES, charges + 1));
                stack.decrementUnlessCreative(1, player);
                world.playSound(null, pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            return ItemActionResult.success(world.isClient);
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        int charges = state.get(CHARGES);
        if (charges == 0) return ActionResult.PASS;
        if (!(player instanceof net.minecraft.server.network.ServerPlayerEntity serverPlayer)) return ActionResult.CONSUME;

        // 原版行为：直接设重生点，不消耗充能
        serverPlayer.setSpawnPoint(
                world.getRegistryKey(),
                pos,
                0.0f,
                false,
                true
        );
        world.playSound(null,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.BLOCKS, 1.0f, 1.0f);
        return ActionResult.SUCCESS;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        int charges = state.get(CHARGES);
        if (charges == 0) return;

        if (random.nextInt(100) == 0) {
            world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_RESPAWN_ANCHOR_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
        }

        double x = pos.getX() + 0.5 + random.nextDouble() * 0.4 - 0.2;
        double y = pos.getY() + 1.0 + random.nextDouble() * 0.2;
        double z = pos.getZ() + 0.5 + random.nextDouble() * 0.4 - 0.2;
        world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
    }
}
