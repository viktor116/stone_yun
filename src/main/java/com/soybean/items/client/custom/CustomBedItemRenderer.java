package com.soybean.items.client.custom;

import com.soybean.block.ModBlock;
import com.soybean.block.custom.entity.CustomBedBlockEntity;
import com.soybean.config.InitValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

/**
 * @author soybean
 * @date 2025/3/14 13:31
 * @description
 */
//@Environment(EnvType.CLIENT)
//public class CustomBedItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
//    private BlockRenderManager blockRenderManager;
//
//    public CustomBedItemRenderer() {
//        MinecraftClient client = MinecraftClient.getInstance();
//        this.blockRenderManager = client.getBlockRenderManager();
//    }
//
//    @Override
//    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices,
//                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
//        if (blockRenderManager == null) {
//            MinecraftClient client = MinecraftClient.getInstance();
//            this.blockRenderManager = client.getBlockRenderManager();
//        }
//        matrices.push();
//
//        this.blockRenderManager.renderBlockAsEntity(
//                ModBlock.FLIP_WHITE_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD),
//                matrices, vertexConsumers, light, overlay);
//
////        this.blockRenderManager.renderBlockAsEntity(
////                Blocks.YELLOW_BED.getDefaultState().with(BedBlock.PART, BedPart.HEAD),
////                matrices, vertexConsumers, light, overlay);
//        matrices.pop();
//    }
//}

@Environment(EnvType.CLIENT)
public class CustomBedItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private ModelPart bedHead;
    private ModelPart bedFoot;
    private boolean initialized = false;

    public CustomBedItemRenderer() {
        // Don't initialize here - we'll do it in the render method
    }

    private void ensureInitialized() {
        if (!initialized) {
            EntityModelLoader modelLoader = MinecraftClient.getInstance().getEntityModelLoader();
            if (modelLoader != null) {
                this.bedHead = modelLoader.getModelPart(EntityModelLayers.BED_HEAD);
                this.bedFoot = modelLoader.getModelPart(EntityModelLayers.BED_FOOT);
                this.initialized = true;
            }
        }
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        // Ensure models are initialized
        ensureInitialized();

        // Skip rendering if not initialized yet
        if (!initialized) {
            return;
        }

        // Get the bed color/type from NBT if needed
        String bedType = "flip_white"; // Default or extract from stack.getNbt()

        // Create sprite identifier similar to what's used in the block entity renderer
        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(
                TexturedRenderLayers.BEDS_ATLAS_TEXTURE,
                Identifier.of(InitValue.MOD_ID, "entity/bed/" + bedType)
        );

        // Render both parts with appropriate transformations
        matrices.push();
        // Render the head part
        renderPart(matrices, vertexConsumers, bedHead, Direction.SOUTH, spriteIdentifier, light, overlay, false);

        // Render the foot part
        renderPart(matrices, vertexConsumers, bedFoot, Direction.SOUTH, spriteIdentifier, light, overlay, true);

        matrices.pop();
    }

    private void renderPart(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelPart part, Direction direction, SpriteIdentifier sprite, int light, int overlay, boolean isFoot) {
        matrices.push();
        matrices.translate(0.0F, 0.5625F, isFoot ? -1.0F : 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
        matrices.translate(0.5F, 0.5F, 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F + direction.asRotation()));
        matrices.translate(-0.5F, -0.5F, -0.5F);
        VertexConsumer vertexConsumer = sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        part.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
    }
}