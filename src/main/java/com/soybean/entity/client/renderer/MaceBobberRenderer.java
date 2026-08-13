package com.soybean.entity.client.renderer;

import com.soybean.entity.custom.MaceBobberEntity;
import com.soybean.items.item.MaceFishingRodItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class MaceBobberRenderer extends EntityRenderer<MaceBobberEntity> {
    private final ItemRenderer itemRenderer;

    public MaceBobberRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.25F;
        this.shadowOpacity = 0.75F;
    }

    @Override
    public void render(MaceBobberEntity entity, float yaw, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light) {
        PlayerEntity owner = (PlayerEntity) entity.getOwner();
        if (owner == null) return;

        // 重锤模型渲染
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
                MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(
                MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch())));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));

        this.itemRenderer.renderItem(
                new ItemStack(Items.MACE),
                ModelTransformationMode.GROUND,
                light,
                OverlayTexture.DEFAULT_UV,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                entity.getId()
        );
        matrices.pop();

        // 钓鱼线渲染 - 照搬原版 FishingBobberEntityRenderer
        float swingProgress = MathHelper.sin(owner.getHandSwingProgress(tickDelta) * 3.1415927F);
        Vec3d handPos = this.getHandPos(owner, swingProgress, tickDelta);
        Vec3d bobberPos = entity.getLerpedPos(tickDelta).add(0.0, 0.25, 0.0);

        float dx = (float)(handPos.x - bobberPos.x);
        float dy = (float)(handPos.y - bobberPos.y);
        float dz = (float)(handPos.z - bobberPos.z);

        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
        MatrixStack.Entry entry = matrices.peek();

        for (int i = 0; i < 16; i++) {
            float p1 = (float) i / 16.0F;
            float p2 = (float)(i + 1) / 16.0F;
            renderFishingLineSegment(dx, dy, dz, consumer, entry, p1, p2);
        }

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    // 原版 renderFishingLine 简化版（把两个 vertex 合在一个 segment 方法里）
    private static void renderFishingLineSegment(float dx, float dy, float dz,
                                                  VertexConsumer consumer,
                                                  MatrixStack.Entry entry,
                                                  float p1, float p2) {
        // 原版用 t^2 让线有自然下垂的弧度（贝塞尔近似）
        float x1 = dx * p1;
        float y1 = dy * p1 * p1 + 0.25F;
        float z1 = dz * p1;

        float x2 = dx * p2;
        float y2 = dy * p2 * p2 + 0.25F;
        float z2 = dz * p2;

        // 计算法线：这段线的方向向量（单位化）
        float segDx = x2 - x1;
        float segDy = y2 - y1;
        float segDz = z2 - z1;
        float len = MathHelper.sqrt(segDx * segDx + segDy * segDy + segDz * segDz);
        float nx = segDx / len;
        float ny = segDy / len;
        float nz = segDz / len;

        // 黑色 = 0xFF000000 = -16777216
        consumer.vertex(entry, x1, y1, z1).color(-16777216);
        consumer.normal(entry, nx, ny, nz);
        consumer.vertex(entry, x2, y2, z2).color(-16777216);
        consumer.normal(entry, nx, ny, nz);
    }

    // 完全照搬原版 FishingBobberEntityRenderer.getHandPos
    private Vec3d getHandPos(PlayerEntity player, float swingProgress, float tickDelta) {
        int armDir = player.getMainArm() == Arm.RIGHT ? 1 : -1;

        ItemStack mainHand = player.getMainHandStack();
        if (!mainHand.isOf(com.soybean.items.ItemsRegister.MACE_FISHING_ROD)) {
            armDir = -armDir;
        }

        boolean firstPerson = this.dispatcher.gameOptions.getPerspective().isFirstPerson();
        if (!firstPerson || player != MinecraftClient.getInstance().player) {
            // 第三人称 / 旁观视角
            float bodyYawRad = MathHelper.lerp(tickDelta, player.prevBodyYaw, player.bodyYaw) * 0.017453292F;
            double sinYaw = Math.sin(bodyYawRad);
            double cosYaw = Math.cos(bodyYawRad);
            float scale = player.getScale();

            double xOff = armDir * 0.35D * scale;
            double yOff = 0.8D * scale;
            double zOff = player.isInSneakingPose() ? -0.1875D : 0.0D;

            Vec3d cameraPos = player.getCameraPosVec(tickDelta);
            double x = -cosYaw * xOff - sinYaw * yOff - zOff;
            double z = -sinYaw * xOff + cosYaw * yOff;
            return cameraPos.add(x, -0.45D * scale, z);
        } else {
            // 第一人称视角
            double fovFactor = 960.0D / (Integer) this.dispatcher.gameOptions.getFov().getValue();
            Vec3d hand = this.dispatcher.camera.getProjection().getPosition(armDir * 0.525F, -0.1F);
            hand = hand.multiply(fovFactor);
            hand = hand.rotateY(swingProgress * 0.5F);
            hand = hand.rotateX(-swingProgress * 0.7F);
            return player.getCameraPosVec(tickDelta).add(hand);
        }
    }

    @Override
    public Identifier getTexture(MaceBobberEntity entity) {
        return Identifier.ofVanilla("textures/item/mace.png");
    }
}
