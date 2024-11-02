package com.soybean.entity.client.renderer;

import com.soybean.config.InitValue;
import com.soybean.entity.client.model.MinecartHatModel;
import com.soybean.items.custom.MinecartHatItem;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MinecartHatRenderer extends GeoArmorRenderer<MinecartHatItem> {
    public MinecartHatRenderer() {
        super(new MinecartHatModel());
    }

}
