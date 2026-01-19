package com.soybean.block.client.renderer;

import com.soybean.block.custom.SheepPlantBlock;
import com.soybean.block.custom.entity.SheepPlantBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;

public class SheepPlantBlockRenderer implements BlockEntityRenderer<SheepPlantBlockEntity> {
    private SheepEntity sheepEntity;

    public SheepPlantBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        // 空构造函数，不在这里创建实体
    }

    private void ensureSheepPlant() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (sheepEntity == null && client.world != null) {
            sheepEntity = new SheepEntity(EntityType.SHEEP, client.world);
            sheepEntity.setNoGravity(true);
            sheepEntity.setInvulnerable(true);
            // 禁用AI和移动
            sheepEntity.setAiDisabled(true);
            sheepEntity.setVelocity(0, 0, 0);
            // 固定pitch和yaw
            sheepEntity.setPitch(0);
            sheepEntity.setYaw(0);
            sheepEntity.prevYaw = 0;
            sheepEntity.prevPitch = 0;
            // 禁用头部转动
            sheepEntity.setHeadYaw(0);
            sheepEntity.prevHeadYaw = 0;
            sheepEntity.bodyYaw = 0;
            sheepEntity.prevBodyYaw = 0;
        }
    }

    @Override
    public void render(SheepPlantBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ensureSheepPlant();
        if (sheepEntity == null) return;
        // 在每次渲染时重置姿态，防止动画
        sheepEntity.setHeadYaw(0);
        sheepEntity.prevHeadYaw = 0;
        sheepEntity.bodyYaw = 0;
        sheepEntity.prevBodyYaw = 0;
        sheepEntity.setPitch(0);
        sheepEntity.setYaw(0);
        sheepEntity.prevYaw = 0;
        sheepEntity.prevPitch = 0;
        sheepEntity.age = 0; // 防止年龄动画
        sheepEntity.limbAnimator.setSpeed(0); // 停止肢体动画
        sheepEntity.handSwinging = false; // 停止手臂摆动
        matrices.push();
        BlockState state = entity.getCachedState();
        if (state.getBlock() instanceof SheepPlantBlock sheepPlantBlock) {
            float scale = sheepPlantBlock.getScale(state);
            matrices.translate(0.5, 0.0, 0.5);
            matrices.scale(scale, scale, scale);
            matrices.translate(0, 0.01, 0);

            MinecraftClient.getInstance().getEntityRenderDispatcher().render(
                    sheepEntity, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, vertexConsumers, light
            );
        }
        matrices.pop();
    }
}
