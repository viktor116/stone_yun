package com.soybean.entity.custom;

import com.soybean.config.InitValue;
import com.soybean.items.ItemsRegister;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class InvertMinecartEntity extends MinecartEntity {
    public static final EntityType<InvertMinecartEntity> MINECART = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "invert_minecart"),
            FabricEntityTypeBuilder.<InvertMinecartEntity>create(SpawnGroup.MISC, InvertMinecartEntity::new)
                    .dimensions(EntityDimensions.fixed(0.98F, 0.7F))
                    .trackRangeBlocks(10)
                    .build()
    );
    public InvertMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    // 添加这个方法来控制掉落物品
    @Override
    public ItemStack getPickBlockStack() {
        // 返回你的自定义矿车物品
        return new ItemStack(ItemsRegister.INVERT_MINECART); // 确保这里引用你注册的物品实例
    }
    @Override
    public Item asItem() {
        return ItemsRegister.INVERT_MINECART;
    }
}
