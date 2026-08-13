package com.soybean.block.custom;

import com.mojang.serialization.MapCodec;
import com.soybean.block.ModBlock;
import com.soybean.block.custom.entity.ExtinguishTorchBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExtinguishTorchBlock extends TorchBlock implements BlockEntityProvider {
    public static final MapCodec<ExtinguishTorchBlock> CODEC = createCodec(ExtinguishTorchBlock::new);
    public static final BooleanProperty LIT = Properties.LIT;

    public ExtinguishTorchBlock(Settings settings) {
        super(net.minecraft.particle.ParticleTypes.FLAME, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(LIT, false));
    }

    public MapCodec<ExtinguishTorchBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(LIT)) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!stack.isOf(net.minecraft.item.Items.TORCH)) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (world.isClient) return ItemActionResult.SUCCESS;
        world.setBlockState(pos, state.with(LIT, true));
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof ExtinguishTorchBlockEntity etbe) {
            etbe.ignite();
        }
        world.playSound(null, pos, net.minecraft.sound.SoundEvents.ITEM_FLINTANDSTEEL_USE, net.minecraft.sound.SoundCategory.BLOCKS, 0.6f, 1.0f);
        return ItemActionResult.CONSUME;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            super.randomDisplayTick(state, world, pos, random);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExtinguishTorchBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == ModBlock.EXTINGUISH_TORCH_TYPE
                ? (tWorld, tPos, tState, be) -> ExtinguishTorchBlockEntity.tick(tWorld, tPos, tState, (ExtinguishTorchBlockEntity) be)
                : null;
    }
}
