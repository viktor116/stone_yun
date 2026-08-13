package com.soybean.items.item;

import com.soybean.entity.custom.MaceBobberEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.UUID;

public class MaceFishingRodItem extends Item {

    private static final String UUID_KEY = "maceRodUuid";

    public MaceFishingRodItem(Settings settings) {
        super(settings);
    }

    public static UUID ensureItemUuid(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        NbtCompound nbt = customData != null ? customData.copyNbt() : new NbtCompound();
        if (!nbt.containsUuid(UUID_KEY)) {
            nbt.putUuid(UUID_KEY, UUID.randomUUID());
            NbtComponent.set(DataComponentTypes.CUSTOM_DATA, stack, nbt);
        }
        return nbt.getUuid(UUID_KEY);
    }

    public static UUID getItemUuid(ItemStack stack) {
        NbtComponent customData = stack.get(DataComponentTypes.CUSTOM_DATA);
        if (customData != null) {
            NbtCompound nbt = customData.getNbt();
            if (nbt.containsUuid(UUID_KEY)) {
                return nbt.getUuid(UUID_KEY);
            }
        }
        return null;
    }

    public static MaceBobberEntity findActiveBobber(World world, PlayerEntity player, UUID itemUuid) {
        Box area = player.getBoundingBox().expand(256.0);
        for (MaceBobberEntity bobber : world.getEntitiesByClass(MaceBobberEntity.class, area, e -> e.getOwner() == player)) {
            if (bobber.isRemoved()) continue;
            // uuid 为 null 时跳过 UUID 检查（简化模式，和原版钓鱼竿一样）
            if (itemUuid == null || itemUuid.equals(bobber.getItemUuid())) {
                return bobber;
            }
        }
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            UUID itemUuid = ensureItemUuid(itemStack);
            MaceBobberEntity bobber = findActiveBobber(world, user, itemUuid);
            if (bobber != null) {
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL,
                        1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                bobber.discard();
                itemStack.damage(1, user,
                        hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            } else {
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL,
                        0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

                MaceBobberEntity mace = new MaceBobberEntity(world, user);
                mace.setItemUuid(itemUuid);
                mace.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 2.5f, 1.0f);
                world.spawnEntity(mace);
            }
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public int getEnchantability() {
        return 1;
    }
}
