package com.soybean.init;

import com.soybean.block.custom.inventory.entity.InventoryBlockEntity;
import com.soybean.config.InitValue;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BlockEntityTypeInit {

    public static final BlockEntityType<InventoryBlockEntity> EXAMPLE_INVENTORY_BLOCK_ENTITY = register("example_inventory_block_entity",
            BlockEntityType.Builder.create(InventoryBlockEntity::new, BlockInit.EXAMPLE_INVENTORY_BLOCK)
                    .build());

    public static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, InitValue.id(name), type);
    }

    public static void initialize() {}
}
