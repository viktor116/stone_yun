package com.soybean.screen;

import com.soybean.config.InitValue;
import com.soybean.init.BlockInit;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author soybean
 * @date 2024/11/14 12:03
 * @description
 */
public class WitherSkeletonInteractionHandler extends MerchantScreenHandler {

    private final ScreenHandlerContext context;
    public WitherSkeletonInteractionHandler(int syncId, PlayerInventory playerInventory) {
        super(syncId, playerInventory);
        PlayerEntity player = playerInventory.player;
        this.context = ScreenHandlerContext.create(player.getWorld(), player.getBlockPos());

        // 创建并设置默认交易物品
        TradeOfferList offers = new TradeOfferList();
        addDefaultTrades(offers);
        Merchant merchant = createMerchant(player, offers);
        // 将默认交易物品设置到商人
        merchant.setOffersFromServer(offers);
        this.setOffers(offers); // 重要！需要同时设置给ScreenHandler
    }


    public static void handleRightClick(WitherSkeletonEntity skeleton, PlayerEntity player) {
        if (skeleton instanceof IMerchantWither iMerchantWither) {
            // 创建交易清单
            TradeOfferList offers = new TradeOfferList();
            offers.addAll(WitherSkeletonTradeInterface.TRADES);
            Merchant merchant = iMerchantWither.getMerchant();
            // 设置交易选项
            merchant.setOffersFromServer(offers);

            // 打开交易界面
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, p) -> {
                        MerchantScreenHandler handler = new WitherSkeletonInteractionHandler(syncId, inv);
                        InitValue.LOGGER.info("Created merchant screen handler");
                        return handler;
                    },
                    Text.translatable("merchant." + InitValue.MOD_ID + ".witherskeleton")
            ));
            player.incrementStat(Stats.TRADED_WITH_VILLAGER);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private void addDefaultTrades(TradeOfferList offers) {
        // 假设你的默认交易是交换某种物品
        // 这里的示例是简单的，假设我们用金锭与骨头交换

        // 第一个交易：用1个金锭换1个钻石
        offers.add(new TradeOffer(
                new TradedItem(Items.BONE, 32),  // 第一个输入物品(物品, 数量, 最大数量)
                Optional.empty(),                     // 第二个输入物品(可选)
                new ItemStack(Items.WITHER_SKELETON_SKULL),  // 输出物品
                0,      // 当前使用次数
                8,      // 最大使用次数
                1,      // 经验值
                0.05f   // 价格乘数
        ));
        // 第二个交易：用1个骨头换2个铁锭
        offers.add(new TradeOffer(
                new TradedItem(Items.DIAMOND, 2),  // 第一个输入物品
                Optional.of(new TradedItem(Items.OBSIDIAN, 5)),  // 第二个输入物品
                new ItemStack(Items.NETHERITE_SCRAP),  // 输出物品
                0,      // 当前使用次数
                4,      // 最大使用次数
                5,      // 经验值
                0.1f    // 价格乘数
        ));}

    private Merchant createMerchant(PlayerEntity player, TradeOfferList offers) {
        return new Merchant() {
            private TradeOfferList tradeOffers = offers; // 添加一个成员变量存储交易列表

            @Override
            public void setOffersFromServer(TradeOfferList offers) {
                this.tradeOffers = offers; // 实现设置交易列表的方法
            }

            @Override
            public TradeOfferList getOffers() {
                return this.tradeOffers; // 返回存储的交易列表
            }

            @Override
            public void trade(TradeOffer offer) {
                offer.use(); // 实现交易逻辑
            }

            @Override
            public void onSellingItem(ItemStack stack) {

            }

            @Override
            public int getExperience() {
                return 0;
            }

            @Override
            public void setExperienceFromServer(int experience) {

            }

            @Override
            public boolean isLeveledMerchant() {
                return false;
            }

            @Override
            public SoundEvent getYesSound() {
                return SoundEvents.ENTITY_VILLAGER_YES; // 设置交易音效
            }

            @Override
            public boolean isClient() {
                return player.getWorld().isClient;
            }

            @Override
            public void setCustomer(@Nullable PlayerEntity customer) {

            }

            @Nullable
            @Override
            public PlayerEntity getCustomer() {
                return player;
            }

        };
    }
}
