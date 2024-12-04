package com.soybean.world.portals;

import com.soybean.items.ItemsRegister;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

/**
 * @author soybean
 * @date 2024/11/11 11:01
 * @description
 */
public class PortalsRegister {

    public static void Initialize(){
        registerPortals();
    }

    public static void registerPortals(){
        //泥土
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.DIRT)
                .lightWithItem(ItemsRegister.GRASS)
                .destDimID(Identifier.of("world"))
                .tintColor(120, 84, 57)
                .registerPortal();

        //末地框架门
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.END_PORTAL_FRAME)
                .lightWithItem(Items.ENDER_EYE)
                .destDimID(Identifier.of("the_end"))
                .tintColor(219, 227, 162)
                .registerPortal();

        //末地石
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.END_STONE)
                .lightWithItem(Items.ENDER_EYE)
                .destDimID(Identifier.of("the_end"))
                .tintColor(219, 227, 162)
                .registerPortal();

        //天堂之门
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.GLOWSTONE)
                .lightWithItem(Items.WATER_BUCKET)
                .destDimID(Identifier.of("world"))
                .tintColor(35, 79, 204)
                .registerPortal();
    }
}
