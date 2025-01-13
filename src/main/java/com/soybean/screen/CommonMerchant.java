package com.soybean.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalInt;

/**
 * @author soybean
 * @date 2024/12/25 11:53
 * @description
 */
public class CommonMerchant implements Merchant {
    private final PlayerEntity player;
    private TradeOfferList offers;
    private int levelProgress;
    private boolean canRefresh;

    public CommonMerchant(PlayerEntity player) {
        this.player = player;
        this.offers = new TradeOfferList();
        this.levelProgress = 1;
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        // 不需要实现
    }

    @Override
    public PlayerEntity getCustomer() {
        return this.player;
    }

    @Override
    public TradeOfferList getOffers() {
        return this.offers;
    }

    @Override
    public void setOffersFromServer(TradeOfferList offers) {
        this.offers = offers;
    }

    @Override
    public void trade(TradeOffer offer) {
        offer.use();
    }

    @Override
    public void onSellingItem(ItemStack stack) {}

    @Override
    public int getExperience() {
        return 0;
    }

    @Override
    public void setExperienceFromServer(int experience) {}

    @Override
    public boolean isLeveledMerchant() {
        return true;
    }

    @Override
    public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_VILLAGER_YES;
    }

    @Override
    public boolean isClient() {
        return player.getWorld().isClient;
    }

    @Override
    public void sendOffers(PlayerEntity player, Text name, int levelProgress) {
        OptionalInt screenId = player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, inv, p) -> {
                    MerchantScreenHandler handler = new MerchantScreenHandler(syncId, inv, this);
                    handler.setLeveled(true);
                    handler.setLevelProgress(levelProgress);
                    return handler;
                },
                name
        ));

        if (screenId.isPresent()) {
            // 这是关键部分：向客户端发送交易数据
            player.sendTradeOffers(
                    screenId.getAsInt(),
                    this.getOffers(),
                    levelProgress,
                    this.getExperience(),
                    this.isLeveledMerchant(),
                    this.canRefreshTrades()
            );
        }
    }
}