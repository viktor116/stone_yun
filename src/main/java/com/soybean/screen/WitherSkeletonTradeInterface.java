package com.soybean.screen;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author soybean
 * @date 2024/11/14 11:41
 * @description
 */
public class WitherSkeletonTradeInterface {
    public static final List<TradeOffer> TRADES = new ArrayList<>();

    // 初始化交易列表
    static {
        // 示例交易1: 32个骨头换1个凋零骷髅头
        TRADES.add(new TradeOffer(
                new TradedItem(Items.BONE, 32),  // 第一个输入物品(物品, 数量, 最大数量)
                Optional.empty(),                     // 第二个输入物品(可选)
                new ItemStack(Items.WITHER_SKELETON_SKULL),  // 输出物品
                0,      // 当前使用次数
                8,      // 最大使用次数
                1,      // 经验值
                0.05f   // 价格乘数
        ));

        // 示例交易2: 2个钻石和5个黑曜石换1个下界合金碎片
        TRADES.add(new TradeOffer(
                new TradedItem(Items.DIAMOND, 2),  // 第一个输入物品
                Optional.of(new TradedItem(Items.OBSIDIAN, 5)),  // 第二个输入物品
                new ItemStack(Items.NETHERITE_SCRAP),  // 输出物品
                0,      // 当前使用次数
                4,      // 最大使用次数
                5,      // 经验值
                0.1f    // 价格乘数
        ));

        // 示例交易3: 单物品交易，64个煤炭换1个钻石
        TRADES.add(new TradeOffer(
                new TradedItem(Items.COAL, 64),
                Optional.empty(),
                new ItemStack(Items.DIAMOND),
                0,
                12,
                2,
                0.05f
        ));
    }
}
