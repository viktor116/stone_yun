package com.soybean.items.item;

import com.soybean.entity.client.renderer.MagneticBombItemRenderer;
import com.soybean.entity.custom.MagneticBombEntity;
import com.soybean.sound.SoundRegister;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author soybean
 * @date 2025/2/12 10:49
 * @description
 */
public class MagneticBombItem extends Item implements GeoItem {
    private static final RawAnimation ROTATION_ANIMATION = RawAnimation.begin().thenPlay("rotation");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenPlay("idle");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public MagneticBombItem(Settings settings) {
        super(settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
//        if(entity instanceof PlayerEntity player && !world.isClient) {
//            ExecuteAnimation(player,"idle");
//        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        player.setCurrentHand(hand);
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundRegister.ALREADY_BOMB, SoundCategory.PLAYERS, 1F, 1.0F);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            float f = getPullProgress(i);
            if (!((double)f < 0.1)) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverWorld = (ServerWorld)world;
                    MagneticBombEntity magneticBombEntity = new MagneticBombEntity(world, user, stack);

                    // 设置投掷位置（从眼部高度开始）
                    magneticBombEntity.setPosition(
                            user.getX(),
                            user.getY() + user.getEyeHeight(user.getPose()) - 0.1,
                            user.getZ()
                    );

                    // 计算投掷方向和力度
                    Vec3d lookDirection = user.getRotationVec(1.0F);
                    float velocity = f * 1.5F; // 根据蓄力程度调整速度，最大1.5倍

                    // 设置投掷速度（包含抛物线弧度）
                    magneticBombEntity.setVelocity(
                            lookDirection.x * velocity,
                            lookDirection.y * velocity + 0.2F, // 添加向上的分量形成抛物线
                            lookDirection.z * velocity
                    );

                    // 如果你的实体继承自 ProjectileEntity，可以使用这个方法：
                    // magneticBombEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, velocity, 1.0F);

                    world.spawnEntity(magneticBombEntity);

                    if(user instanceof PlayerEntity player && !player.isCreative()){
                        stack.decrement(1);
                    }

                    // 停止蓄力时播放 idle 动画
                    if (!world.isClient) {
                        ExecuteAnimation(playerEntity, "idle");
                    }
                }


                world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            }
        }
    }

    public static float getPullProgress(int useTicks) {
        float f = (float)useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
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
                return new MagneticBombItemRenderer();
            }
        });
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW; // 使用弓的使用动作
    }

}
