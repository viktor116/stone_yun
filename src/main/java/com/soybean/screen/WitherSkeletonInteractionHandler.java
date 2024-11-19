package com.soybean.screen;

import com.soybean.config.InitValue;
import com.soybean.init.BlockInit;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author soybean
 * @date 2024/11/14 12:03
 * @description
 */
public class WitherSkeletonInteractionHandler extends MerchantScreenHandler {

    private final ScreenHandlerContext context;
    private final Merchant merchant;
    private final TradeOfferList tradeOffers;

    public WitherSkeletonInteractionHandler(int syncId, PlayerInventory playerInventory) {
        super(syncId, playerInventory, createMerchant(playerInventory.player));
        this.merchant = createMerchant(playerInventory.player);
        this.context = ScreenHandlerContext.create(playerInventory.player.getWorld(), playerInventory.player.getBlockPos());
        this.tradeOffers = new TradeOfferList();
        addDefaultTrades(this.tradeOffers);

        // 将默认交易物品设置到商人
        merchant.setOffersFromServer(this.tradeOffers);
        this.setOffers(this.tradeOffers);
    }


    public static void handleRightClick(PlayerEntity player) {
        // 打开交易界面
        TradeOfferList tradeOffers1 = new TradeOfferList();
        addDefaultTrades(tradeOffers1);
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, inv, p) -> {
                    WitherSkeletonInteractionHandler handler = new WitherSkeletonInteractionHandler(syncId, inv);
                    handler.setOffers(tradeOffers1);
                    return handler;
                },
                Text.translatable("merchant." + InitValue.MOD_ID + ".witherskeleton")
        ));
        player.incrementStat(Stats.TRADED_WITH_VILLAGER);
    }

    public static void handleRightClickOnDragon(PlayerEntity player) {
            TradeOfferList tradeOffers1 = new TradeOfferList();
            addDragonTrades(tradeOffers1);
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, p) -> {
                        WitherSkeletonInteractionHandler handler = new WitherSkeletonInteractionHandler(syncId, inv);
                        handler.setOffers(tradeOffers1);
                        return handler;
                    },
                    Text.translatable("merchant." + InitValue.MOD_ID + ".dragon")
            ));
            player.incrementStat(Stats.TRADED_WITH_VILLAGER);

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        // 获取当前玩家的位置信息
        BlockPos pos = player.getBlockPos();

        // 创建目标筛选器，排除创意模式玩家
        TargetPredicate targetPredicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(15.0);

        // 在这个位置寻找 WitherSkeletonEntity（假设是附近的）
        World world = player.getWorld();
        WitherSkeletonEntity skeleton = world.getClosestEntity(WitherSkeletonEntity.class,
                targetPredicate,
                player,
                pos.getX(), pos.getY(), pos.getZ(),
                new Box(pos).expand(10)); // 可以根据需要调整搜索范围

        // 如果找到了 WitherSkeletonEntity，杀死它
        if (skeleton != null) {
            skeleton.kill();  // 杀死 WitherSkeletonEntity
        }
    }

    private static void addDefaultTrades(TradeOfferList offers) {
        // 假设你的默认交易是交换某种物品

        offers.add(new TradeOffer(
                new TradedItem(Items.EMERALD, 64),  // 第一个输入物品(物品, 数量, 最大数量)
                Optional.empty(),                     // 第二个输入物品(可选)
                new ItemStack(Items.WITHER_SKELETON_SKULL),  // 输出物品
                0,      // 当前使用次数
                100,      // 最大使用次数
                1,      // 经验值
                0.05f   // 价格乘数
        ));
    }
    private static void addDragonTrades(TradeOfferList offers){
        offers.add(new TradeOffer(
                new TradedItem(Items.YELLOW_BED, 1),  // 第一个输入物品
                Optional.of(new TradedItem(Items.YELLOW_BED, 1)),
//                Optional.of(new TradedItem(Items.OBSIDIAN, 5)),  // 第二个输入物品
                new ItemStack(Items.DRAGON_EGG),  // 输出物品
                0,      // 当前使用次数
                100,      // 最大使用次数
                5,      // 经验值
                0.1f    // 价格乘数
        ));
        offers.add(new TradeOffer(
                new TradedItem(Items.YELLOW_BED, 1),  // 第一个输入物品
                Optional.empty(),
//                Optional.of(new TradedItem(Items.OBSIDIAN, 5)),  // 第二个输入物品
                new ItemStack(Items.EXPERIENCE_BOTTLE),  // 输出物品
                0,      // 当前使用次数
                100,      // 最大使用次数
                5,      // 经验值
                0.1f    // 价格乘数
        ));
    }

    private static Merchant createMerchant(PlayerEntity player) {

        return new Merchant() {
            private TradeOfferList offers = new TradeOfferList();
            private PlayerEntity customer;
            private int experience = 0;

            @Override
            public void setOffersFromServer(TradeOfferList offers) {
                this.offers = offers;
            }

            @Override
            public TradeOfferList getOffers() {
                return this.offers;
            }

            @Override
            public void trade(TradeOffer offer) {
                offer.use();
            }

            @Override
            public void onSellingItem(ItemStack stack) {
                // 可以在这里添加出售物品时的额外逻辑
            }

            @Override
            public int getExperience() {
                return this.experience;
            }

            @Override
            public void setExperienceFromServer(int experience) {
                this.experience = experience;
            }

            @Override
            public boolean isLeveledMerchant() {
                return false;
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
            public void setCustomer(@Nullable PlayerEntity customer) {
                this.customer = customer;
            }

            @Nullable
            @Override
            public PlayerEntity getCustomer() {
                return this.customer;
            }

            public boolean canRefreshTrades() {
                return true;
            }
        };
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        boolean bl = false;
        int i = startIndex;

        if (fromLast) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (fromLast) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack slotStack = slot.getStack();

                if (!slotStack.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack, slotStack)) {
                    int j = slotStack.getCount() + stack.getCount();
                    int maxCount = Math.min(slot.getMaxItemCount(), stack.getMaxCount());

                    if (j <= maxCount) {
                        stack.setCount(0);
                        slotStack.setCount(j);
                        slot.markDirty();
                        bl = true;
                    } else if (slotStack.getCount() < maxCount) {
                        int k = maxCount - slotStack.getCount();
                        stack.decrement(k);
                        slotStack.setCount(maxCount);
                        slot.markDirty();
                        bl = true;
                    }
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (fromLast) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (fromLast) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.slots.get(i);
                ItemStack slotStack = slot.getStack();

                if (slotStack.isEmpty() && slot.canInsert(stack)) {
                    if (stack.getCount() > slot.getMaxItemCount()) {
                        slot.setStack(stack.split(slot.getMaxItemCount()));
                    } else {
                        slot.setStack(stack.split(stack.getCount()));
                    }

                    slot.markDirty();
                    bl = true;
                    break;
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return bl;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
//        return super.quickMove(player,index);
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (index == 2) {
                // 如果是输出槽
                if (!this.insertItem(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(slotStack, itemStack);
            } else if (index != 0 && index != 1) {
                // 如果不是输入槽
                if (index >= 3 && index < 30) {
                    // 从物品栏移动到快捷栏
                    if (!this.insertItem(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39) {
                    // 从快捷栏移动到物品栏
                    if (!this.insertItem(slotStack, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                // 如果是输入槽，移动到物品栏或快捷栏
                if (!this.insertItem(slotStack, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, slotStack);
        }

        return itemStack;
    }

    // 确保交易列表可以正确更新
    public void setCanRestock(boolean canRestock) {
        this.merchant.getOffers().forEach(offer -> offer.resetUses());
    }

    public TradeOfferList getOffers() {
        return this.tradeOffers;
    }
}
