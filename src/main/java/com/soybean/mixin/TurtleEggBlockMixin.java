package com.soybean.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public abstract class TurtleEggBlockMixin extends Block {

    public TurtleEggBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "afterBreak", at = @At("HEAD"), cancellable = true)
    private void onAfterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
        if (!world.isClient) {
            // 掉落一个蛋
            Block.dropStack(world, pos, new ItemStack(Items.TURTLE_EGG, 1));

            // 取出当前蛋的数量（1~4）
            int eggCount = state.get(TurtleEggBlock.EGGS);
            if (eggCount > 1) {
                // 只减少一个
                world.setBlockState(pos, state.with(TurtleEggBlock.EGGS, eggCount - 1), 3);
            } else {
                // 如果只剩1个，移除方块
                world.removeBlock(pos, false);
            }
            ci.cancel(); // 阻止原版的“全破坏”逻辑
        }
    }
}