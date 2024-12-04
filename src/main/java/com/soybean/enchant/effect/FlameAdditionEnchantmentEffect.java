package com.soybean.enchant.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.soybean.enchant.EnchantmentRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author soybean
 * @date 2024/12/4 11:51
 * @description
 */
public record FlameAdditionEnchantmentEffect(EnchantmentLevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<FlameAdditionEnchantmentEffect> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(
                    EnchantmentLevelBasedValue.CODEC.fieldOf("amount")
                            .forGetter(FlameAdditionEnchantmentEffect::amount))
                    .apply(instance, FlameAdditionEnchantmentEffect::new));

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

    public static void handleDrop(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        ItemStack stack = player.getMainHandStack();
        // 检查是否有 FlameAdditionEnchantmentEffect 附魔
        int level = EnchantmentRegister.getEnchantmentLevel(world, EnchantmentRegister.FLAME_ADDITION, stack);
        if (level > 0 && world instanceof ServerWorld serverWorld) {
            // 先取消原始掉落
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            // 获取原始掉落物
            List<ItemStack> drops = Block.getDroppedStacks(state, serverWorld, pos, blockEntity, player, stack);
            List<ItemStack> new_drops = new ArrayList<>();
            // 遍历并替换矿石为对应的锭
            for (int i = 0; i < drops.size(); ++i) {
                ItemStack drop = drops.get(i);
                ItemStack smeltedResult = getSmeltingResult(drop);

                if (smeltedResult != null) {
                    new_drops.add(smeltedResult);
                } else {
                    new_drops.add(drop);
                }
            }

            new_drops.forEach(drop -> {
                ItemEntity itemEntity = new ItemEntity(world,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        drop);
                world.spawnEntity(itemEntity);
            });

            // 添加特效
            serverWorld.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f);
            serverWorld.addParticle(ParticleTypes.FLAME,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    0, 0, 0);
        }
    }

    private static ItemStack getSmeltingResult(ItemStack input) {
        if (input.isOf(Items.RAW_IRON)) {
            return new ItemStack(Items.IRON_INGOT, input.getCount());
        }
        if (input.isOf(Items.RAW_GOLD)) {
            return new ItemStack(Items.GOLD_INGOT, input.getCount());
        }
        if (input.isOf(Items.RAW_COPPER)) {
            return new ItemStack(Items.COPPER_INGOT, input.getCount());
        }
        return null;
    }
}
