package com.soybean.entity.client.renderer;

import com.soybean.entity.custom.CustomBlockEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CustomBlockEntityRenderer extends EntityRenderer<CustomBlockEntity> {
    private final BlockRenderManager blockRenderManager;

    public CustomBlockEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.blockRenderManager = ctx.getBlockRenderManager();
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(CustomBlockEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        matrices.push();
        
        if(entity.isBaby()) {
            matrices.scale(.6f, .6f, .6f);
        }
        int overlay = OverlayTexture.DEFAULT_UV;
        if (entity.getHurtTime() > 0) {
            overlay = OverlayTexture.packUv(OverlayTexture.getU(0.0F), OverlayTexture.getV(true));
        }
        matrices.translate(-0.5D, 0.0D, -0.5D);
        if(entity.getType() == CustomBlockEntity.DIRT_BLOCK){
            this.blockRenderManager.renderBlockAsEntity(Blocks.DIRT.getDefaultState(), matrices, vertexConsumers, light, overlay);
        }else if (entity.getType() == CustomBlockEntity.SAND_BLOCK) {
            this.blockRenderManager.renderBlockAsEntity(Blocks.SAND.getDefaultState(), matrices, vertexConsumers, light, overlay);
        }
        matrices.pop();
    }

    @Override
    public Identifier getTexture(CustomBlockEntity entity) {
        // 因为我们直接渲染方块，这个方法不会被使用，但仍需要实现
        return Identifier.ofVanilla("textures/block/sand.png");
    }
} 