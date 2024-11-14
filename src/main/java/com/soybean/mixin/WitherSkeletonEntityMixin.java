package com.soybean.mixin;

import com.soybean.screen.IMerchantWither;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author soybean
 * @date 2024/11/14 11:47
 * @description
 */
// Mixin来修改凋零骷髅的行为
@Mixin(WitherSkeletonEntity.class)
public abstract class WitherSkeletonEntityMixin extends HostileEntity implements IMerchantWither {

    @Unique
    private TradeOfferList tradeOffers;

    @Unique
    private PlayerEntity customer;

    protected WitherSkeletonEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.tradeOffers = new TradeOfferList();
    }

    @Unique
    public final Merchant merchant = new Merchant() {
        @Override
        public void setCustomer(PlayerEntity player) {
            customer = player;
        }

        @Override
        public PlayerEntity getCustomer() {
            return customer;
        }

        @Override
        public TradeOfferList getOffers() {
            return tradeOffers;
        }

        @Override
        public void setOffersFromServer(TradeOfferList offers) {
            tradeOffers = offers;
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
            return false;
        }

        @Override
        public SoundEvent getYesSound() {
            return SoundEvents.ENTITY_WITHER_SKELETON_AMBIENT;
        }

        @Override
        public boolean isClient() {
            return false;
        }
    };
    public Merchant getMerchant(){
        return merchant;
    };
}