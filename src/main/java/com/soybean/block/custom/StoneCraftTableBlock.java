package com.soybean.block.custom;

import com.mojang.serialization.MapCodec;
import com.soybean.Stone;
import com.soybean.screen.StoneCraftingScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StoneCraftTableBlock extends Block {
    public static final MapCodec<StoneCraftTableBlock> CODEC = createCodec(StoneCraftTableBlock::new);
    private final Text TITLE ;
    private static String STONE_TITLE_KEY = "block.stone.stone_crafting_table";
    public static String AIR_BLOCK_TITLE_KEY = "block.stone.air_crafting_table";
    public MapCodec<? extends StoneCraftTableBlock> getCodec() {
        return CODEC;
    }

    public StoneCraftTableBlock(AbstractBlock.Settings settings) {
        super(settings);
        TITLE = Text.translatable(STONE_TITLE_KEY);
    }

    public StoneCraftTableBlock(AbstractBlock.Settings settings,String title) {
        super(settings);
        TITLE = Text.translatable(title);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = this.createScreenHandlerFactory(state, world, pos);
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
                player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> {
            // 使用ScreenHandlerContext来确保合成表与方块位置关联
            return new StoneCraftingScreenHandler(
                    syncId,
                    inventory,
                    ScreenHandlerContext.create(world, pos)
            );
        }, TITLE);
    }
}
