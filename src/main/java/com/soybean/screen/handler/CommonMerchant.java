package com.soybean.screen.handler;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
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
                    MerchantScreenHandler handler = new MerchantScreenHandler(syncId, inv, this) {
                        @Override
                        public void onClosed(PlayerEntity player) {
                            super.onClosed(player); // 调用原始逻辑
                            onClosedPiglin(player);
                        }
                    };
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

    public static void onClosedPiglin(PlayerEntity player) {
        // 获取当前玩家的位置信息
        BlockPos pos = player.getBlockPos();
        World world = player.getWorld();

        if (world.isClient) {
            return;
        }
        // 创建目标筛选器，寻找 PiglinEntity
        TargetPredicate targetPredicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(15.0);

        // 在玩家周围寻找 PiglinEntity
        PiglinEntity piglin = world.getClosestEntity(PiglinEntity.class,
                targetPredicate,
                player,
                pos.getX(), pos.getY(), pos.getZ(),
                new Box(pos).expand(10)); // 搜索范围可以根据需要调整

        // 如果找到了 PiglinEntity 并且它有我们的静止标签
        if (piglin != null) {
            // 读取实体的 NBT
            NbtCompound nbt = new NbtCompound();
            piglin.writeNbt(nbt);

            // 检查自定义标签
//            if (nbt.getBoolean("IsTrading")) {
            piglin.setAiDisabled(false);

//                piglin.readNbt(nbt); // 重新写回
//            }
        }
    }
}