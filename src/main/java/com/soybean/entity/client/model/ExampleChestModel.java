package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ExampleChestModel extends Model {
	public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(InitValue.id("example_chest"), "main");
	public static final Identifier TEXTURE_LOCATION = InitValue.id("textures/entity/example_chest.png");

    private final ModelPart main;
    private final ModelPart lid;

    public ExampleChestModel(ModelPart root) {
		super(RenderLayer::getEntitySolid);
        this.main = root.getChild("main");
        this.lid = this.main.getChild("lid");
    }

    public static TexturedModelData getTexturedModelData() {
        var modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData main = modelPartData.addChild("main", ModelPartBuilder.create()
                        .uv(37, 0)
                        .cuboid(-6.0F, -3.5F, -6.0F, 12.0F, 8.0F, 1.0F,
                                new Dilation(0.0F))
                        .uv(36, 30)
                        .cuboid(-6.0F, -3.5F, 5.0F, 12.0F, 8.0F, 1.0F,
                                new Dilation(0.0F))
                        .uv(23, 30)
                        .cuboid(5.0F, -3.5F, -5.0F, 1.0F, 8.0F, 10.0F,
                                new Dilation(0.0F))
                        .uv(0, 30)
                        .cuboid(-6.0F, -3.5F, -5.0F, 1.0F, 8.0F, 10.0F,
                                new Dilation(0.0F))
                        .uv(0, 16)
                        .cuboid(-6.0F, -4.5F, -6.0F, 12.0F, 1.0F, 12.0F,
                                new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 4.5F, 0.0F));

        main.addChild("lid", ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-6.0F, 0.0F, -12.0F, 12.0F, 3.0F, 12.0F,
                                new Dilation(0.0F))
                        .uv(0, 0)
                        .cuboid(-1.0F, -2.0F, -13.0F, 2.0F, 3.0F, 1.0F,
                                new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 4.5F, 6.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        this.main.render(matrices, vertexConsumer, light, overlay, color);
    }

    public ModelPart getLid() {
        return this.lid;
    }
}