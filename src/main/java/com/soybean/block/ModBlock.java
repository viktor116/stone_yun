package com.soybean.block;

import com.soybean.block.client.renderer.CowPlantBlockRenderer;
import com.soybean.block.client.renderer.FlipWhiteBedBlockEntityRenderer;
import com.soybean.block.client.renderer.InvertRedBlockEntityRenderer;
import com.soybean.block.custom.*;
import com.soybean.block.custom.entity.BeefFurnaceBlockEntity;
import com.soybean.block.custom.entity.CowPlantBlockEntity;
import com.soybean.block.custom.entity.FlipWhiteBedEntity;
import com.soybean.block.custom.entity.InvertRedBedEntity;
import com.soybean.block.custom.inventory.entity.DemoBlockEntity;
import com.soybean.config.InitValue;
import com.soybean.screen.handler.StoneCraftingScreenHandler;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.function.ToIntFunction;

/**
 * @author soybean
 * @date 2024/10/24 12:10
 * @description
 */
public class ModBlock {
    public static final Block STONE_CRAFT_TABLE = register("stone_crafting_table", new StoneCraftTableBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASS).strength(2.5F).sounds(BlockSoundGroup.STONE)), true);
    public static final Block AIR_CRAFT_TABLE = register("air_crafting_table", new StoneCraftTableBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASS).strength(2.5F).sounds(BlockSoundGroup.STONE).nonOpaque(),StoneCraftTableBlock.AIR_BLOCK_TITLE_KEY), true);
    public static final Block DIRT_CRAFT_TABLE = register("dirt_crafting_table", new StoneCraftTableBlock(AbstractBlock.Settings.create().mapColor(MapColor.BROWN).instrument(NoteBlockInstrument.BASS).strength(2.5F).sounds(BlockSoundGroup.STONE),StoneCraftTableBlock.DIRT_BLOCK_TITLE_KEY), true);
    public static final Block CACTUS = register("cactus", new DemoBlock(AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).ticksRandomly().strength(0.4F).sounds(BlockSoundGroup.WOOL).pistonBehavior(PistonBehavior.DESTROY).nonOpaque()), true);
    public static final Block SOUL_TORCH_BLOCK = register("soul_torch", new TorchBlock(ParticleTypes.SOUL_FIRE_FLAME, AbstractBlock.Settings.create().noCollision().breakInstantly().luminance((state) -> {
        return 10;
    })), false);
    public static final Block SOUL_WALL_TORCH = register("soul_wall_torch", new WallTorchBlock(ParticleTypes.SOUL_FIRE_FLAME, AbstractBlock.Settings.create().noCollision().breakInstantly().luminance((state) -> {
        return 10;
    }).sounds(BlockSoundGroup.WOOD).dropsLike(SOUL_TORCH_BLOCK).pistonBehavior(PistonBehavior.DESTROY)),false);
    public static final Block STATIC_FIRE = register("static_fire",new CanDigFireBlock(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).strength(0.5f).noCollision().breakInstantly().luminance((state) -> {
        return 15;
    }).sounds(BlockSoundGroup.WOOL)),true);
    public static final Block FIRE = register("fire",new FireBlock(AbstractBlock.Settings.create().mapColor(MapColor.BRIGHT_RED).replaceable().noCollision().breakInstantly().luminance((state) -> {
        return 15;
    }).sounds(BlockSoundGroup.WOOL).pistonBehavior(PistonBehavior.DESTROY)),true);
    public static final Block NETHER_PORTAL = register("nether_portal",new NetherPortalBlock(AbstractBlock.Settings.create().nonOpaque().mapColor(MapColor.PALE_PURPLE)),true);
    public static final Block HORIZONTAL_NETHER_PORTAL = register("horizontal_nether_portal", new CustomPortalBlock(AbstractBlock.Settings.create().mapColor(MapColor.PALE_PURPLE).noCollision().strength(-1.0F).nonOpaque().sounds(BlockSoundGroup.GLASS).luminance((state) -> 11)), true);
    public static final Block COAL_ORE = register("coal_ore", new ExperienceDroppingBlock(UniformIntProvider.create(0, 2), AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F)),true);
    public static final Block REACTOR = register("reactor", new Block(AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F)),true);
    public static final Block TRANSPARENT_BLOCK = register("transparent_block", new Block(AbstractBlock.Settings.create().mapColor(MapColor.GRAY).instrument(NoteBlockInstrument.BASEDRUM).nonOpaque().strength(2F,3F) ),true);
    public static final Block CONCRETE = register("concrete", new Block(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(2F, 3.0F)),true);
    public static final Block MAIN_WORLD_DEBRIS = register("main_world_debris", new Block(AbstractBlock.Settings.create().mapColor(MapColor.BLACK).requiresTool().strength(30.0F, 1200.0F).sounds(BlockSoundGroup.ANCIENT_DEBRIS)),true);
    public static final Block OBSIDIAN = register("obsidian", new Block(AbstractBlock.Settings.create().mapColor(MapColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(50.0F, 1200.0F)),true);
    public static final Block BIG_TORCH_BLOCK = register("big_torch", new BigTorchBlock(ParticleTypes.FLAME, AbstractBlock.Settings.create().noCollision().breakInstantly().luminance((state) -> {
        return 15;
    })), false);
    public static final Block BIG_WALL_TORCH = register("big_wall_torch", new BigWallTorchBlock(ParticleTypes.FLAME, AbstractBlock.Settings.create().noCollision().breakInstantly().luminance((state) -> {
        return 15;
    }).sounds(BlockSoundGroup.WOOD).dropsLike(BIG_TORCH_BLOCK).pistonBehavior(PistonBehavior.DESTROY)),false);
    public static final Block APPLE_BLOCK = register("apple_block", new Block(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASS).strength(1F).sounds(BlockSoundGroup.GRASS)), true);
    public static final Block WOODEN_ANVIL =  register("wooden_anvil", new AnvilBlock(AbstractBlock.Settings.create().mapColor(MapColor.IRON_GRAY).requiresTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroup.ANVIL).pistonBehavior(PistonBehavior.BLOCK)),true);
    public static final Block LEAF_ORE = register("leaf_ore", new Block(AbstractBlock.Settings.create().mapColor(MapColor.GREEN).instrument(NoteBlockInstrument.BASS).strength(2F, 3.0F)),true);
    public static final Block FLIP_WHITE_BED = register("flip_white_bed",createBedBlock(DyeColor.WHITE),false);
    public static final Block INVERT_RED_BED = register("invert_red_bed",createBedBlock(DyeColor.RED),false);
    public static final Block BEEF_FURNACE = register("beef_furnace", new BeefFurnaceBlock(AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASS).strength(2.5F).luminance(createLightLevelFromLitBlockState(13)).sounds(BlockSoundGroup.STONE)), true);

    public static final Block BLOW_BERRY_TORCH_BLOCK = register("glow_berry_torch", new TorchBlock(ParticleTypes.FLAME, AbstractBlock.Settings.create().noCollision().breakInstantly().luminance((state) -> {
        return 14;
    })), false);
    public static final Block BLOW_BERRY_WALL_TORCH = register("glow_berry_wall_torch", new WallTorchBlock(ParticleTypes.FLAME, AbstractBlock.Settings.create().noCollision().breakInstantly().luminance((state) -> {
        return 14;
    }).sounds(BlockSoundGroup.WOOD).dropsLike(SOUL_TORCH_BLOCK).pistonBehavior(PistonBehavior.DESTROY)),false);
    public static final Block CAKE = register("cake", new CakeBlock(AbstractBlock.Settings.create().solid().strength(0.5F).sounds(BlockSoundGroup.WOOL).pistonBehavior(PistonBehavior.DESTROY)),false);
    public static final Block COMPRESS_GOLD_BLOCK = register("compress_gold_block", new Block(AbstractBlock.Settings.create().mapColor(MapColor.YELLOW).instrument(NoteBlockInstrument.BASS).requiresTool().strength(1.5F, 1.5F)),true);
    public static final Block COOKED_COBBLESTONE = register("cooked_cobblestone", new Block(AbstractBlock.Settings.create().mapColor(MapColor.ORANGE).instrument(NoteBlockInstrument.BASS).requiresTool().strength(0.5F)),false);

    public static final Block COW_PLANT = register("cow_plant", new CowPlantBlock(AbstractBlock.Settings.create()
            .nonOpaque()
            .noCollision()
            .ticksRandomly()
            .breakInstantly()
            .sounds(BlockSoundGroup.CROP)),false);

    public static final ScreenHandlerType<StoneCraftingScreenHandler> STONE_CRAFTING_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(InitValue.MOD_ID, "stone_crafting_table"),
                    new ScreenHandlerType<>(StoneCraftingScreenHandler::new, FeatureSet.empty()));

    public static final BlockEntityType<DemoBlockEntity> DEMO_BLOCK_ENTITY =Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(InitValue.MOD_ID, "demo_block_entity"),
            BlockEntityType.Builder.create(DemoBlockEntity::new, CACTUS).build(null));

    public static final BlockEntityType<CowPlantBlockEntity> COW_PLANT_TYPE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "cow_plant"),
            FabricBlockEntityTypeBuilder.create(CowPlantBlockEntity::new, ModBlock.COW_PLANT).build(null)
    );

    public static final BlockEntityType<FlipWhiteBedEntity> FLIP_WHITE_BED_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "flip_white_bed"),
            BlockEntityType.Builder.create(FlipWhiteBedEntity::new, FLIP_WHITE_BED).build(null));

    public static final BlockEntityType<InvertRedBedEntity> INVERT_RED_BED_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "invert_red_bed"),
            BlockEntityType.Builder.create(InvertRedBedEntity::new, INVERT_RED_BED).build(null));

    public static final BlockEntityType<BeefFurnaceBlockEntity> BEEF_FURNACE_TYPE =
            Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(InitValue.MOD_ID, "beef_furnace"),
                    BlockEntityType.Builder.create(BeefFurnaceBlockEntity::new, BEEF_FURNACE).build()
            );

    public static void initialize() {
        FuelRegistry.INSTANCE.add(Items.RAW_IRON, 1600);
    }

    public static void initializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(STONE_CRAFT_TABLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AIR_CRAFT_TABLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SOUL_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(SOUL_WALL_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BIG_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BIG_WALL_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CACTUS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FIRE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(STATIC_FIRE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(NETHER_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(HORIZONTAL_NETHER_PORTAL, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(TRANSPARENT_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(CONCRETE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(APPLE_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(WOODEN_ANVIL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLOW_BERRY_TORCH_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BLOW_BERRY_WALL_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BEEF_FURNACE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), COW_PLANT);

        BlockEntityRendererRegistry.register(COW_PLANT_TYPE, CowPlantBlockRenderer::new);
        BlockEntityRendererFactories.register(FLIP_WHITE_BED_ENTITY, FlipWhiteBedBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(INVERT_RED_BED_ENTITY, InvertRedBlockEntityRenderer::new);
    }

    public static Block register(String id, Block block, boolean shouldRegisterItem) {
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, itemID, blockItem);
        }
        return Registry.register(Registries.BLOCK, itemID, block);
    }

    public static Block register(String id, Block block, boolean shouldRegisterItem,int maxCount) {
        Identifier itemID = Identifier.of(InitValue.MOD_ID, id);
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings().maxCount(maxCount));
            Registry.register(Registries.ITEM, itemID, blockItem);
        }
        return Registry.register(Registries.BLOCK, itemID, block);
    }

    public static CropBlock registerCrop(String id, Block block) {
        Identifier cropIdentifier = Identifier.of(InitValue.MOD_ID, id);
        return (CropBlock) Registry.register(Registries.BLOCK, cropIdentifier, block);
    }

    private static Block createBedBlock(DyeColor color) {
        return new CustomBedBlock(AbstractBlock.Settings.create().mapColor((state) -> {
            return state.get(BedBlock.PART) == BedPart.FOOT ? color.getMapColor() : MapColor.WHITE_GRAY;
        }).sounds(BlockSoundGroup.WOOD).strength(0.2F).nonOpaque().burnable().pistonBehavior(PistonBehavior.DESTROY),color);
    }

    public static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return (state) -> {
            return (Boolean)state.get(Properties.LIT) ? litLevel : 0;
        };
    }
}
