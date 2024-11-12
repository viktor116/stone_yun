package com.soybean.entity.custom;

import com.soybean.config.InitValue;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;

public class WheatEntity extends AnimalEntity implements GeoEntity {
    // 现有的静态字段和缓存
    public static final EntityType<WheatEntity> WHEAT = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(InitValue.MOD_ID, "wheat_entity"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WheatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.5f, 1.5f))
                    .build()
    );
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // 添加年龄相关字段
    private int growingAge;

    protected WheatEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0) // 添加生命值
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.GENERIC_JUMP_STRENGTH, 0.0);
    }

    // 重写繁殖相关方法
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.BONE_MEAL); // 使用骨粉作为繁殖物品
    }

    @Override
    public boolean isReadyToBreed() {
        return this.getBreedingAge() == 0 && !this.isBaby();  // 确保是成年且不在冷却中
    }

    @Override
    public void breed(ServerWorld world, AnimalEntity other) {
        WheatEntity baby = (WheatEntity)this.createChild(world, other);
        if (baby != null) {
            // 设置婴儿年龄为负数（表示成长时间）
            baby.setBaby(true);
            baby.setBreedingAge(-6000);
            baby.setPosition(this.getX(), this.getY(), this.getZ());

            // 重置父母的繁殖冷却时间
            this.setBreedingAge(600);
            ((WheatEntity)other).setBreedingAge(600);

            world.spawnEntity(baby);
        }
        // 繁殖效果
        world.spawnParticles(ParticleTypes.HEART,
                this.getX(), this.getY() + 0.5, this.getZ(),
                7, 0.3, 0.3, 0.3, 0.0);
    }


    // 成长相关方法
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // 检查是否在繁殖冷却中
        if (this.getBreedingAge() == 0 && this.isReadyToBreed()) {
            if (this.isBreedingItem(itemStack)) {
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                return ActionResult.success(this.getWorld().isClient);
            }
        }

        // 如果是幼年且手持小麦，则加速生长
        if (this.isBaby() && itemStack.isOf(Items.BONE_MEAL)) {
            // 加速生长
            this.growUp((int)(this.getBreedingAge()), true);
            // 消耗物品
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }
            // 生成进食粒子效果
            this.getWorld().addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    this.getX(),
                    this.getY() + 0.5,
                    this.getZ(),
                    0.0, 0.0, 0.0
            );
            return ActionResult.success(this.getWorld().isClient);
        }

        return super.interactMob(player, hand);
    }

    // 添加一个辅助方法来处理进食
    public void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
        }
        // 生成进食粒子效果
        this.getWorld().addParticle(
                ParticleTypes.HAPPY_VILLAGER,
                this.getX(),
                this.getY() + 0.5,
                this.getZ(),
                0.0, 0.0, 0.0
        );
    }

    // 掉落物相关
    @Override
    public void dropLoot(DamageSource source, boolean causedByPlayer) {
        super.dropLoot(source, causedByPlayer);
        // 死亡时掉落1-3个小麦
        int count = this.random.nextBetween(1, 3);
        this.dropItem(Items.WHEAT, count);
    }

    // AI目标
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new TemptGoal(this, 1.2,
                Ingredient.ofItems(Items.BONE_MEAL), false)); // 被骨粉吸引
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    // 现有的动画相关方法
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controller) {
        controller.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<GeoAnimatable> animationState) {
        animationState.getController().setAnimation(RawAnimation.begin()
                .then("animation.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return WHEAT.create(world);
    }

    // 必要的 NBT 数据保存
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Age", this.getBreedingAge());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setBreedingAge(nbt.getInt("Age"));
    }
}
