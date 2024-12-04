package com.soybean.entity.client.model;

import com.soybean.config.InitValue;
import com.soybean.entity.custom.HimEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

/**
 * @author soybean
 * @date 2024/12/3 10:58
 * @description
 */
public class HimModel extends SinglePartEntityModel<HimEntity> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public static final EntityModelLayer HIM_LAYER = new EntityModelLayer(Identifier.of(InitValue.MOD_ID, "him"), "main");

    public HimModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    public static ModelData getModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        // 严格按照原版人型生物的UV映射
        modelPartData.addChild("head",
                ModelPartBuilder.create()
                        .uv(0, 0)  // 头部在贴图的(0,0)位置
                        .cuboid(-4F, -8F, -4F, 8, 8, 8),
                ModelTransform.pivot(0F, 0F, 0F));

        modelPartData.addChild("body",
                ModelPartBuilder.create()
                        .uv(16, 16)  // 身体在贴图的(16,16)位置
                        .cuboid(-4F, 0F, -2F, 8, 12, 4),
                ModelTransform.pivot(0F, 0F, 0F));

        modelPartData.addChild("right_arm",
                ModelPartBuilder.create()
                        .uv(40, 16)  // 右臂在贴图的(40,16)位置
                        .cuboid(-3F, -2F, -2F, 4, 12, 4),
                ModelTransform.pivot(-5F, 2F, 0F));

        modelPartData.addChild("left_arm",
                ModelPartBuilder.create()
                        .uv(40, 16)  // 左臂使用相同的UV，但模型渲染会翻转
                        .cuboid(-1F, -2F, -2F, 4, 12, 4),
                ModelTransform.pivot(5F, 2F, 0F));

        modelPartData.addChild("right_leg",
                ModelPartBuilder.create()
                        .uv(0, 16)  // 右腿在贴图的(0,16)位置
                        .cuboid(-2F, 0F, -2F, 4, 12, 4),
                ModelTransform.pivot(-2F, 12F, 0F));

        modelPartData.addChild("left_leg",
                ModelPartBuilder.create()
                        .uv(0, 16)  // 左腿使用相同的UV，但模型渲染会翻转
                        .cuboid(-2F, 0F, -2F, 4, 12, 4),
                ModelTransform.pivot(2F, 12F, 0F));

        return modelData;
    }

    @Override
    public ModelPart getPart() {
        return root;
    }

    @Override
    public void setAngles(HimEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // 设置基本动画姿势
        this.head.yaw = netHeadYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;

        // 摆动腿部和手臂
        this.rightArm.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount * 0.5F;
        this.leftArm.pitch = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
        this.rightLeg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
    }
}