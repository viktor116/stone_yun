package com.soybean.block.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author soybean
 * @date 2025/2/11 14:54
 * @description
 */
public class CanDigFireBlock extends Block{
    public static final MapCodec<FireBlock> CODEC = createCodec(FireBlock::new);
    public static final int field_31093 = 15;
    public static final IntProperty AGE;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty UP;
    private static final Map<Direction, BooleanProperty> DIRECTION_PROPERTIES;
    private static final VoxelShape UP_SHAPE;
    private static final VoxelShape WEST_SHAPE;
    private static final VoxelShape EAST_SHAPE;
    private static final VoxelShape NORTH_SHAPE;
    private static final VoxelShape SOUTH_SHAPE;
    private final Map<BlockState, VoxelShape> shapesByState;
    private static final int field_31085 = 60;
    private static final int field_31086 = 30;
    private static final int field_31087 = 15;
    private static final int field_31088 = 5;
    private static final int field_31089 = 100;
    private static final int field_31090 = 60;
    private static final int field_31091 = 20;
    private static final int field_31092 = 5;
    private final Object2IntMap<Block> burnChances = new Object2IntOpenHashMap();
    private final Object2IntMap<Block> spreadChances = new Object2IntOpenHashMap();

    public MapCodec<FireBlock> getCodec() {
        return CODEC;
    }

    public CanDigFireBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AGE, 0)).with(NORTH, false)).with(EAST, false)).with(SOUTH, false)).with(WEST, false)).with(UP, false));
        this.shapesByState = ImmutableMap.copyOf((Map)this.stateManager.getStates().stream().filter((state) -> {
            return (Integer)state.get(AGE) == 0;
        }).collect(Collectors.toMap(Function.identity(), CanDigFireBlock::getShapeForState)));
    }

    private static VoxelShape getShapeForState(BlockState state) {
        VoxelShape voxelShape = VoxelShapes.empty();
        if ((Boolean)state.get(UP)) {
            voxelShape = UP_SHAPE;
        }

        if ((Boolean)state.get(NORTH)) {
            voxelShape = VoxelShapes.union(voxelShape, NORTH_SHAPE);
        }

        if ((Boolean)state.get(SOUTH)) {
            voxelShape = VoxelShapes.union(voxelShape, SOUTH_SHAPE);
        }

        if ((Boolean)state.get(EAST)) {
            voxelShape = VoxelShapes.union(voxelShape, EAST_SHAPE);
        }

        if ((Boolean)state.get(WEST)) {
            voxelShape = VoxelShapes.union(voxelShape, WEST_SHAPE);
        }

        return voxelShape;
    }


    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getStateForPosition(ctx.getWorld(), ctx.getBlockPos());
    }

    protected BlockState getStateForPosition(BlockView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        if (!this.isFlammable(blockState) && !blockState.isSideSolidFullSquare(world, blockPos, Direction.UP)) {
            BlockState blockState2 = this.getDefaultState();
            Direction[] var6 = Direction.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Direction direction = var6[var8];
                BooleanProperty booleanProperty = (BooleanProperty)DIRECTION_PROPERTIES.get(direction);
                if (booleanProperty != null) {
                    blockState2 = (BlockState)blockState2.with(booleanProperty, this.isFlammable(world.getBlockState(pos.offset(direction))));
                }
            }

            return blockState2;
        } else {
            return this.getDefaultState();
        }
    }

    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP) || this.areBlocksAroundFlammable(world, pos);
    }

    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // 移除原有的熄灭逻辑，保持持续燃烧
        world.scheduleBlockTick(pos, this, getFireTickDelay(world.random));

        if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            BlockState blockState = world.getBlockState(pos.down());
            boolean bl = blockState.isIn(world.getDimension().infiniburn());
            int i = state.get(AGE);

            // 只在特定条件下熄灭
            if (!bl && world.isRaining() && this.isRainingAround(world, pos) && random.nextFloat() < 0.2F) {
                world.removeBlock(pos, false);
                return;
            }

            // 保持年龄值在较低范围内，防止自动熄灭
            int j = Math.min(7, i + random.nextInt(3) / 2);
            if (i != j) {
                state = state.with(AGE, j);
                world.setBlockState(pos, state, 4);
            }

            // 蔓延逻辑（可选）
            if (!bl && !state.canPlaceAt(world, pos)) {
                BlockPos blockPos = pos.down();
                if (!world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP)) {
                    return;
                }
            }

            // 保持火焰效果
            if (random.nextInt(4) == 0) {
                world.addParticle(ParticleTypes.FLAME,
                        pos.getX() + random.nextDouble(),
                        pos.getY() + random.nextDouble(),
                        pos.getZ() + random.nextDouble(),
                        0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected boolean isRainingAround(World world, BlockPos pos) {
        return world.hasRain(pos) || world.hasRain(pos.west()) || world.hasRain(pos.east()) || world.hasRain(pos.north()) || world.hasRain(pos.south());
    }

    private int getSpreadChance(BlockState state) {
        return state.contains(Properties.WATERLOGGED) && (Boolean)state.get(Properties.WATERLOGGED) ? 0 : this.spreadChances.getInt(state.getBlock());
    }

    private int getBurnChance(BlockState state) {
        return state.contains(Properties.WATERLOGGED) && (Boolean)state.get(Properties.WATERLOGGED) ? 0 : this.burnChances.getInt(state.getBlock());
    }



    private boolean areBlocksAroundFlammable(BlockView world, BlockPos pos) {
        Direction[] var3 = Direction.values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Direction direction = var3[var5];
            if (this.isFlammable(world.getBlockState(pos.offset(direction)))) {
                return true;
            }
        }

        return false;
    }

    private int getBurnChance(WorldView world, BlockPos pos) {
        if (!world.isAir(pos)) {
            return 0;
        } else {
            int i = 0;
            Direction[] var4 = Direction.values();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Direction direction = var4[var6];
                BlockState blockState = world.getBlockState(pos.offset(direction));
                i = Math.max(this.getBurnChance(blockState), i);
            }

            return i;
        }
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty(); // 让玩家可以穿过，但是仍然可以选中
    }
    protected boolean isFlammable(BlockState state) {
        return this.getBurnChance(state) > 0;
    }

    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        world.scheduleBlockTick(pos, this, getFireTickDelay(world.random));
    }

    private static int getFireTickDelay(Random random) {
        return 30 + random.nextInt(10);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE, NORTH, EAST, SOUTH, WEST, UP});
    }

    public void registerFlammableBlock(Block block, int burnChance, int spreadChance) {
        this.burnChances.put(block, burnChance);
        this.spreadChances.put(block, spreadChance);
    }

    static {
        AGE = Properties.AGE_15;
        NORTH = ConnectingBlock.NORTH;
        EAST = ConnectingBlock.EAST;
        SOUTH = ConnectingBlock.SOUTH;
        WEST = ConnectingBlock.WEST;
        UP = ConnectingBlock.UP;
        DIRECTION_PROPERTIES = (Map)ConnectingBlock.FACING_PROPERTIES.entrySet().stream().filter((entry) -> {
            return entry.getKey() != Direction.DOWN;
        }).collect(Util.toMap());
        UP_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
        WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
        EAST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
        SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    }
}
