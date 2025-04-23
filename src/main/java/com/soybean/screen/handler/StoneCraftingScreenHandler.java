package com.soybean.screen.handler;

import com.soybean.block.ModBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;

/**
 * @author soybean
 * @date 2024/11/1 10:09
 * @description
 */
public class StoneCraftingScreenHandler extends CraftingScreenHandler {
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    // 添加一个符合工厂方法签名的构造函数
    public StoneCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public StoneCraftingScreenHandler(int syncId, PlayerInventory playerInventory,
                                      ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
        this.context = context;
        this.player = playerInventory.player;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        // 确保只有在方块存在的时候才能使用
        return canUse(context, player, ModBlock.STONE_CRAFT_TABLE) ||
                canUse(context, player, ModBlock.AIR_CRAFT_TABLE) ||
                canUse(context,player, ModBlock.DIRT_CRAFT_TABLE);
    }
}