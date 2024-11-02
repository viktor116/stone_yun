package com.soybean.items.custom;

import com.soybean.entity.custom.InvertMinecartEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

import java.util.function.Consumer;

/**
 * @author soybean
 * @date 2024/11/2 10:39
 * @description
 */
public class InvertMinecartItem extends MinecartItem {
    public InvertMinecartItem(Settings settings) {
        super(AbstractMinecartEntity.Type.RIDEABLE, settings);  // 使用RIDEABLE作为默认类型
    }
    // Create our armor model/renderer and return it

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);

        if (!blockState.isIn(BlockTags.RAILS)) {
            return ActionResult.FAIL;
        }

        ItemStack itemStack = context.getStack();
        if (world instanceof ServerWorld serverWorld) {
            RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock ?
                    (RailShape)blockState.get(((AbstractRailBlock)blockState.getBlock()).getShapeProperty()) :
                    RailShape.NORTH_SOUTH;

            double d = railShape.isAscending() ? 0.5 : 0.0;

            // 创建你的自定义矿车实体而不是普通矿车
            InvertMinecartEntity minecart = new InvertMinecartEntity(
                    InvertMinecartEntity.MINECART,
                    world
            );
            minecart.setPosition(
                    (double)blockPos.getX() + 0.5,
                    (double)blockPos.getY() + 0.0625 + d,
                    (double)blockPos.getZ() + 0.5
            );

            serverWorld.spawnEntity(minecart);
            serverWorld.emitGameEvent(GameEvent.ENTITY_PLACE, blockPos,
                    GameEvent.Emitter.of(context.getPlayer(), serverWorld.getBlockState(blockPos.down())));
        }

        itemStack.decrement(1);
        return ActionResult.success(world.isClient);
    }
}
