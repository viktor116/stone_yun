package com.soybean.items.item;

import com.soybean.entity.client.renderer.RocketLauncherRenderer;
import com.soybean.entity.client.renderer.RocketRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

/**
 * @author soybean
 * @date 2025/2/12 10:49
 * @description
 */
public class RocketItem extends Item implements GeoItem {
    private static final RawAnimation ROTATION_ANIMATION = RawAnimation.begin().thenPlay("rotation");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenPlay("idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
//    private static ComponentType<Boolean> IS_ROTATING = ComponentTypeRegister.COMMON_BOOL;
    public RocketItem(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(entity instanceof PlayerEntity player && !world.isClient) {
            ServerWorld serverWorld = (ServerWorld) world;
            ItemStack mainHandStack = player.getMainHandStack();
//            if (player.isUsingItem() && (player.getActiveItem() == mainHandStack)) {
//                ExecuteAnimation(player,"rotation");
//                breakBlockInFront(world, player);
//            }else {
                ExecuteAnimation(player,"idle");
//            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        player.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Activation", 0, state -> PlayState.STOP)
                .triggerableAnim("rotation", ROTATION_ANIMATION)
                .triggerableAnim("idle",IDLE_ANIMATION));
    }

    private void ExecuteAnimation(LivingEntity entity, String animationName) {
        if (!entity.getWorld().isClient()) {  // 确保在服务器端执行
            ServerWorld serverWorld = (ServerWorld) entity.getWorld();
            triggerAnim(entity, GeoItem.getOrAssignId(entity.getMainHandStack(), serverWorld), "Activation", animationName);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            @Override
            public @Nullable BuiltinModelItemRenderer getGeoItemRenderer() {
                return new RocketRenderer();
            }
        });
    }

}
