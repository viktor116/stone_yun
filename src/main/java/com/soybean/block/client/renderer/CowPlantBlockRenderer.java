package com.soybean.block.client.renderer;

import com.soybean.block.custom.CowPlantBlock;
import com.soybean.block.custom.entity.CowPlantBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;

public class CowPlantBlockRenderer implements BlockEntityRenderer<CowPlantBlockEntity> {
    private CowEntity cowEntity;

    public CowPlantBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        // 空构造函数，不在这里创建实体
    }

    private void ensureCowEntity() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (cowEntity == null && client.world != null) {
            cowEntity = new CowEntity(EntityType.COW, client.world);
            cowEntity.setNoGravity(true);
            cowEntity.setInvulnerable(true);
            // 禁用AI和移动
            cowEntity.setAiDisabled(true);
            cowEntity.setVelocity(0, 0, 0);
            // 固定pitch和yaw
            cowEntity.setPitch(0);
            cowEntity.setYaw(0);
            cowEntity.prevYaw = 0;
            cowEntity.prevPitch = 0;
            // 禁用头部转动
            cowEntity.setHeadYaw(0);
            cowEntity.prevHeadYaw = 0;
            cowEntity.bodyYaw = 0;
            cowEntity.prevBodyYaw = 0;
        }
    }

    @Override
    public void render(CowPlantBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ensureCowEntity();
        if (cowEntity == null) return;
        // 在每次渲染时重置姿态，防止动画
        cowEntity.setHeadYaw(0);
        cowEntity.prevHeadYaw = 0;
        cowEntity.bodyYaw = 0;
        cowEntity.prevBodyYaw = 0;
        cowEntity.setPitch(0);
        cowEntity.setYaw(0);
        cowEntity.prevYaw = 0;
        cowEntity.prevPitch = 0;
        cowEntity.age = 0; // 防止年龄动画
        cowEntity.limbAnimator.setSpeed(0); // 停止肢体动画
        cowEntity.handSwinging = false; // 停止手臂摆动
        matrices.push();
        BlockState state = entity.getCachedState();
        if (state.getBlock() instanceof CowPlantBlock cowPlantBlock) {
            float scale = cowPlantBlock.getScale(state);
            matrices.translate(0.5, 0.0, 0.5);
            matrices.scale(scale, scale, scale);
            matrices.translate(0, 0.01, 0);

            MinecraftClient.getInstance().getEntityRenderDispatcher().render(
                    cowEntity, 0.0, 0.0, 0.0, 0.0F, tickDelta, matrices, vertexConsumers, light
            );
        }
        matrices.pop();
    }
}
