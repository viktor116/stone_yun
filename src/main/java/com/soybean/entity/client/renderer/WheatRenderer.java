package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.WheatModel;
import com.soybean.entity.custom.WheatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class WheatRenderer extends GeoEntityRenderer<WheatEntity> {
    public WheatRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WheatModel());
    }

    @Override
    public void render(WheatEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        if(entity.isBaby()){
            poseStack.scale(.6f,.6f,.6f);
        }
        poseStack.scale(1f,1f,1f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
