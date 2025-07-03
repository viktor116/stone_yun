package com.soybean.entity.custom;

import com.soybean.entity.EntityRegister;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

/**
 * 床弹射物实体，当碰撞时会产生爆炸
 */
public class BedProjectileEntity extends ProjectileEntity {
    private static final TrackedData<ItemStack> BED_ITEM = DataTracker.registerData(BedProjectileEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    
    public BedProjectileEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public BedProjectileEntity(World world, LivingEntity owner, ItemStack bedItem) {
        this(EntityRegister.BED_PROJECTILE_ENTITY, world);
        this.setOwner(owner);
        this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
        this.setBedItem(bedItem);
    }
    
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(BED_ITEM, new ItemStack(Items.RED_BED));
    }
    
    public ItemStack getBedItem() {
        return this.dataTracker.get(BED_ITEM);
    }
    
    public void setBedItem(ItemStack stack) {
        if (stack.getItem() instanceof net.minecraft.item.BedItem || isBed(stack.getItem())) {
            this.dataTracker.set(BED_ITEM, stack.copyWithCount(1));
        }
    }
    
    private boolean isBed(Item item) {
        return item == Items.RED_BED || item == Items.BLUE_BED || item == Items.YELLOW_BED
                || item == Items.WHITE_BED || item == Items.BLACK_BED
                || item == Items.GREEN_BED || item == Items.PINK_BED
                || item == Items.PURPLE_BED || item == Items.CYAN_BED
                || item == Items.BROWN_BED || item == Items.GRAY_BED
                || item == Items.LIGHT_BLUE_BED || item == Items.LIGHT_GRAY_BED
                || item == Items.LIME_BED || item == Items.MAGENTA_BED
                || item == Items.ORANGE_BED;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // 检查碰撞
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onCollision(hitResult);
        }
        
        // 更新位置
        Vec3d velocity = this.getVelocity();
        double x = this.getX() + velocity.x;
        double y = this.getY() + velocity.y;
        double z = this.getZ() + velocity.z;
        this.setPosition(x, y, z);
        
        // 粒子效果
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.CLOUD, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        
        // 应用重力
        if (!this.hasNoGravity()) {
            Vec3d velocity2 = this.getVelocity();
            this.setVelocity(velocity2.x, velocity2.y - 0.05, velocity2.z);
        }
        
        // 碰撞处理和移动
        ProjectileUtil.setRotationFromVelocity(this, 0.5F);
        this.checkBlockCollision();
        
        // 如果速度过小，自动爆炸
        if (velocity.lengthSquared() < 0.01) {
            explode();
        }
    }
    
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        
        // 当碰到实体时爆炸
        explode();
    }
    
    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        
        // 当碰到方块时爆炸
        explode();
    }
    
    private void explode() {
        if (!this.getWorld().isClient) {
            this.getWorld().createExplosion(
                    this,
                    this.getX(), this.getY(), this.getZ(),
                    6.0F, // 爆炸强度
                    false, // 不生成火焰
                    World.ExplosionSourceType.TNT
            );
            
            this.discard();
        }
    }
} 