package com.soybean.items.item;

import com.soybean.entity.client.renderer.RocketLauncherRenderer;
import com.soybean.entity.custom.MagneticBombEntity;
import com.soybean.entity.custom.RocketEntity;
import com.soybean.items.ItemsRegister;
import com.soybean.sound.SoundRegister;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
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
public class RocketLauncherItem extends Item implements GeoItem {
    private static final RawAnimation LAUNCHER_ANIMATION = RawAnimation.begin().thenPlay("launcher");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenPlay("idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int animationTime = 0; // 用于控制动画播放时间
    private boolean isAnimating = false; // 用于标记是否正在播放动画
//    private static ComponentType<Boolean> IS_ROTATING = ComponentTypeRegister.COMMON_BOOL;
    public RocketLauncherItem(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getStackInHand(hand);

        // 检查玩家是否有火箭弹（创造模式跳过检查）
        if (!player.getAbilities().creativeMode) {
            boolean hasRocket = false;
            ItemStack rocketToConsume = null;

            // 检查背包中是否有火箭弹
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if (stack.getItem() == ItemsRegister.ROCKET) {
                    hasRocket = true;
                    rocketToConsume = stack;
                    break;
                }
            }

            // 如果没有火箭弹，返回失败
            if (!hasRocket) {
                return TypedActionResult.fail(handStack);
            }

            // 消耗一个火箭弹
            rocketToConsume.decrement(1);
        }

        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld)world;
            RocketEntity rocketEntity = new RocketEntity(world, player, ItemsRegister.ROCKET.getDefaultStack());

            // 设置投掷位置（从眼部高度开始）
            rocketEntity.setPosition(
                    player.getX(),
                    player.getY() + player.getEyeHeight(player.getPose()) - 0.1,
                    player.getZ()
            );

            // 计算投掷方向和力度
            Vec3d lookDirection = player.getRotationVec(1.0F);
            float velocity = 3F; // 根据蓄力程度调整速度，最大1.5倍

            // 设置投掷速度（包含抛物线弧度）
            rocketEntity.setVelocity(
                    lookDirection.x * velocity,
                    lookDirection.y * velocity, // 添加向上的分量形成抛物线
                    lookDirection.z * velocity
            );

            // 如果你的实体继承自 ProjectileEntity，可以使用这个方法：
            // magneticBombEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, velocity, 1.0F);
            world.spawnEntity(rocketEntity);
        }

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundRegister.ROCKET_LAUNCHER, player.getSoundCategory(), 1.0F, 1.0F);
        ExecuteAnimation(player, "launcher");
        return TypedActionResult.pass(handStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Activation", 0, state -> PlayState.STOP)
                .triggerableAnim("launcher", LAUNCHER_ANIMATION)
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
                return new RocketLauncherRenderer();
            }
        });
    }

}
