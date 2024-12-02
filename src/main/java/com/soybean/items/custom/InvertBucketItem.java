package com.soybean.items.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/2 13:23
 * @description
 */
public class InvertBucketItem extends ArmorItem {

    private static final ArmorMaterial IRON_ARMOR = ArmorMaterials.IRON.value();

    public InvertBucketItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            if (world.getFluidState(blockPos).isOf(Fluids.WATER)) {
                // 只装水不放置
                if (!world.isClient) {
                    world.setBlockState(blockPos,Blocks.AIR.getDefaultState());
                    world.playSound(null, user.getX(), user.getY(), user.getZ(),
                            SoundEvents.ITEM_BUCKET_FILL, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    user.incrementStat(Stats.USED.getOrCreateStat(this));
                }
                return TypedActionResult.success(itemStack);
            }
        }

        return TypedActionResult.pass(itemStack);
    }

//    @Override
//    public EquipmentSlot getSlotType() {
//        return EquipmentSlot.HEAD;
//    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantability() {
        return IRON_ARMOR.enchantability();
    }


}