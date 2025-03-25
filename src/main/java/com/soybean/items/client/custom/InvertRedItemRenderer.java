package com.soybean.items.client.custom;

import com.soybean.config.InitValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class InvertRedItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    private ModelPart bedHead;
    private ModelPart bedFoot;
    private boolean initialized = false;

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

        SpriteIdentifier spriteIdentifier = TexturedRenderLayers.BED_TEXTURES[DyeColor.RED.getId()];

        matrices.push();
        // Render the head part
        renderPart(matrices, vertexConsumers, bedHead, Direction.SOUTH, spriteIdentifier, light, overlay, false);

        renderPart(matrices, vertexConsumers, bedFoot, Direction.SOUTH, spriteIdentifier, light, overlay, true);

        matrices.pop();
    }

    private void renderPart(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelPart part, Direction direction, SpriteIdentifier sprite, int light, int overlay, boolean isFoot) {
        matrices.push();
        matrices.translate(0.0F, 0.5625F, isFoot ? -1.0F : 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
        matrices.translate(0.5F, 0.5F, 0.5F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
        //修改床的朝向
        if(direction == Direction.NORTH || direction == Direction.SOUTH) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(direction.asRotation()));
        } else {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f + direction.asRotation()));
        }

        matrices.translate(-0.5F, -0.5F, -0.5F);
        matrices.translate(0f,0f,0.5);
        VertexConsumer vertexConsumer = sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        part.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
    }
}
