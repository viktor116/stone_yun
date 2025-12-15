package com.soybean.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Block.class)
public class BlockPlaceMixin {
    /**
     * 注入到 Block.onBlockAdded 方法的末尾，该方法在方块被放置或加载时调用。
     */
    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack,CallbackInfo ci) {

        // 1. 仅在服务器端执行逻辑
        if (world.isClient) {
            return;
        }

        // 2. 检查是否为哭泣黑曜石
        if (state.isOf(Blocks.CRYING_OBSIDIAN)) {

            // 3. 检查是否在下界 (Nether)
            if (world.getRegistryKey() == World.NETHER) {

                // 4. 执行蒸发替换

                // 4a. 播放蒸发音效 (使用熔岩熄灭的声音)
                world.playSound(
                        null,                       // 玩家，null 表示由世界发出
                        pos,                        // 位置
                        SoundEvents.BLOCK_LAVA_EXTINGUISH, // 蒸发音效
                        SoundCategory.BLOCKS,       // 音效类别
                        1.0F,                       // 音量
                        0.8F + world.random.nextFloat() * 0.4F // 音调变化
                );

                // 4b. 生成蒸发粒子效果 (使用 LARGE_SMOKE 模拟蒸汽)
                if (world instanceof ServerWorld serverWorld) {
                    serverWorld.spawnParticles(
                            ParticleTypes.LARGE_SMOKE, // 粒子类型
                            pos.getX() + 0.5,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5,
                            15,                         // 粒子数量
                            0.3, 0.3, 0.3,              // 随机偏移 (dx, dy, dz)
                            0.1                         // 速度
                    );
                }

                // 4c. 将哭泣黑曜石替换为普通黑曜石
                // 标记 3: 通知客户端并执行方块更新
                world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState(), 3);
            }
        }
    }
}
