package com.soybean.enchant.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author soybean
 * @date 2024/11/13 11:30
 * @description
 */
public record SnatchEnchantmentEffect(EnchantmentLevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<SnatchEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnchantmentLevelBasedValue.CODEC.fieldOf("amount").forGetter(SnatchEnchantmentEffect::amount)
    ).apply(instance, SnatchEnchantmentEffect::new));
    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if(target instanceof VillagerEntity villager){
            for (int i = 0; i < 32; i++){
                ItemEntity itemEntity = new ItemEntity(villager.getWorld(), pos.x, pos.y, pos.z, new ItemStack(Items.EMERALD));
                itemEntity.setToDefaultPickupDelay();
                itemEntity.setVelocity(villager.getRandom().nextGaussian() * 0.05, villager.getRandom().nextGaussian() * 0.05 + 0.2F, villager.getRandom().nextGaussian() * 0.05);
                // 将ItemEntity添加到世界中
                villager.getWorld().spawnEntity(itemEntity);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
