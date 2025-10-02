package com.soybean.entity.client.renderer;

import com.soybean.entity.client.model.BucketHatModel;
import com.soybean.entity.client.model.MinecartHatModel;
import com.soybean.items.custom.BucketHatItem;
import com.soybean.items.custom.MinecartHatItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib.renderer.GeoArmorRenderer;


@Environment(EnvType.CLIENT)
public class BucketHatRenderer extends GeoArmorRenderer<BucketHatItem> {
    public BucketHatRenderer() {
        super(new BucketHatModel());
    }
}
