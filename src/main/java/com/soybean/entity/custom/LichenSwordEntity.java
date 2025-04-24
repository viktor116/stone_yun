package com.soybean.entity.custom;

import com.soybean.entity.EntityRegister;
import com.soybean.items.ItemsRegister;
import com.soybean.utils.CommonUtils;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.UUID;

/**
 * @author soybean
 * @date 2025/4/24 11:28
 * @description
 */
public class LichenSwordEntity extends PersistentProjectileEntity {
    private ItemStack swordStack = new ItemStack(ItemsRegister.LICHEN_SWORD);
    private boolean dealtDamage;
    public static final float THROW_DAMAGE = 30.0F;
    public boolean isReturning = false;
    private int returningTicks = 0;
    private UUID ownerUuid;
    private static final int MAX_RETURNING_TICKS = 100;
    private boolean isTorchBlock = false;

    public LichenSwordEntity(EntityType<? extends LichenSwordEntity> entityType, World world) {
        super(entityType, world);
    }

    public LichenSwordEntity(World world, double x, double y, double z, ItemStack stack) {
        super(EntityRegister.LICHEN_SWORD, x, y, z, world, stack, stack);
    }

    public LichenSwordEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegister.LICHEN_SWORD, owner, world,stack,stack);
        this.swordStack = stack.copy();
        this.setDamage(THROW_DAMAGE);
        if (owner != null) {
            this.ownerUuid = owner.getUuid();
        }
        this.pickupType = PickupPermission.DISALLOWED;
    }

    @Override
    protected ItemStack asItemStack() {
        return this.swordStack.copy();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ItemsRegister.LICHEN_SWORD.getDefaultStack();
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        // 当玩家空手右键点击时，启动返回逻辑
        if (this.getWorld().isClient) {
            return ActionResult.SUCCESS;
        }

        // 只允许剑的主人召回
        if (this.ownerUuid != null && this.ownerUuid.equals(player.getUuid()) && player.getStackInHand(hand).isEmpty()) {
            this.startReturning(player);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public void startReturning(PlayerEntity player) {
        this.isReturning = true;
        this.returningTicks = 0;
        this.setNoClip(true); // 使其能穿过方块返回
        isTorchBlock = false;
//        this.setVelocity(Vec3d.ZERO); // 重置速度
        this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 1.0F, 1.0F); // 播放返回音效
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        isTorchBlock = true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 30.0F;
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.getDamageSources().trident(this, (Entity)(entity2 == null ? this : entity2));
        World var7 = this.getWorld();
        if (var7 instanceof ServerWorld serverWorld) {
            f = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), entity, damageSource, f);
        }

        this.dealtDamage = true;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            var7 = this.getWorld();
            if (var7 instanceof ServerWorld serverWorld) {
                serverWorld = (ServerWorld)var7;
                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack());
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)entity;
                this.knockback(livingEntity, damageSource);
                this.onHit(livingEntity);
            }
        }

        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Override
    public void tick() {
        if (this.isReturning) {
            this.returningTicks++;

            // 获取所有者
            PlayerEntity owner = this.getOwner() instanceof PlayerEntity ? (PlayerEntity)this.getOwner() : null;
            if (owner == null && this.ownerUuid != null) {
                owner = this.getWorld().getPlayerByUuid(this.ownerUuid);
            }

            // 如果找到所有者，设置向所有者移动的速度
            if (owner != null) {
                // 计算到所有者的方向向量
                Vec3d ownerPos = owner.getEyePos();
                Vec3d entityPos = this.getPos();
                Vec3d direction = ownerPos.subtract(entityPos).normalize();

                // 根据距离设置速度（越近速度越慢，防止错过玩家）
                double distance = this.getPos().distanceTo(ownerPos);
                double speed = Math.min(1.0 + distance * 0.05, 2.0); // 速度范围：1.0-2.0

                this.setVelocity(direction.multiply(speed));

                // 检查是否足够近以收回物品
                if (distance < 1.0 || this.returningTicks > MAX_RETURNING_TICKS) {
                    if (!owner.getInventory().insertStack(this.asItemStack())) {
                        owner.dropItem(this.asItemStack(), false);
                    }
                    this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.8F, 1.0F);
                    this.discard();
                    return;
                }
            } else {
                // 如果找不到所有者，停止返回状态
                this.isReturning = false;
                this.setNoClip(false);

                // 如果剑太久没有被捡起，可以考虑让它掉落成物品
                if (this.returningTicks > MAX_RETURNING_TICKS) {
                    this.dropStack(this.asItemStack());
                    this.discard();
                    return;
                }
            }
        }

        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if(!this.getWorld().isClient && Math.pow(this.getVelocity().x,2)+ Math.pow(this.getVelocity().y,2) + Math.pow(this.getVelocity().z,2) > 0.5 && !isTorchBlock){
            ServerWorld serverWorld=(ServerWorld)this.getWorld();
            CommonUtils.spawnSelfParticle(serverWorld,this, new DustParticleEffect(new Vector3f(129/255f, 157/255f, 132/255f),0.5f));
        }


        super.tick();
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dealtDamage = nbt.getBoolean("DealtDamage");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void age() {
        if (this.pickupType != PickupPermission.ALLOWED) {
            super.age();
        }
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
    }
}
