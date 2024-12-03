package com.soybean.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

/**
 * @author soybean
 * @date 2024/12/3 10:56
 * @description
 */
public class HimEntity extends PathAwareEntity {
    public HimEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    // 设置实体属性
    public static DefaultAttributeContainer.Builder createAttributes() {
        return PathAwareEntity.createLivingAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 16.0);
    }

    // 配置实体的AI目标
    @Override
    protected void initGoals() {
        // 随机走动
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 1.0));
        // 避开实体
        this.goalSelector.add(2, new FleeEntityGoal<>(this, PathAwareEntity.class, 4.0f, 1.2, 1.5));
        // 随机看周围
        this.goalSelector.add(3, new LookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        // 设置无敌状态，持续100tick（10秒）
        if (this.age <= 200) {
            this.setInvulnerable(true);
        } else {
            this.setInvulnerable(false);
        }
    }
}
