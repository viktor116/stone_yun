package com.soybean.entity.custom;

import com.soybean.block.ModBlock;
import com.soybean.config.InitValue;
import com.soybean.entity.EntityRegister;
import com.soybean.items.ItemsRegister;
import com.soybean.utils.CommonUtils;
import com.soybean.utils.ServerEachTickTaskManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.*;

/**
 * @author soybean
 * @date 2025/4/23 16:49
 * @description
 */
public class TotemOfDeadEntity extends Entity implements FlyingItemEntity {
    private static final TrackedData<ItemStack> ITEM;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int lifespan;
    private boolean dropsItem;
    private List<BlockPos> initListPos;

    public TotemOfDeadEntity(EntityType<? extends TotemOfDeadEntity> entityType, World world) {
        super(entityType, world);
    }

    public TotemOfDeadEntity(World world, double x, double y, double z) {
        this(EntityRegister.TOTEM_OF_DEAD_ENTITY, world);
        this.setPosition(x, y, z);
    }

    public void setItem(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            this.getDataTracker().set(ITEM, this.getItem());
        } else {
            this.getDataTracker().set(ITEM, itemStack.copyWithCount(1));
        }

    }

    public ItemStack getStack() {
        return (ItemStack) this.getDataTracker().get(ITEM);
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(ITEM, this.getItem());
    }

    public boolean shouldRender(double distance) {
        double d = this.getBoundingBox().getAverageSideLength() * 4.0;
        if (Double.isNaN(d)) {
            d = 4.0;
        }

        d *= 64.0;
        return distance < d * d;
    }

    public void initTargetPos(BlockPos pos) {
        double d = (double) pos.getX();
        int i = pos.getY();
        double e = (double) pos.getZ();
        double f = d - this.getX();
        double g = e - this.getZ();
        double h = Math.sqrt(f * f + g * g);
        if (h > 12.0) {
            this.targetX = this.getX() + f / h * 12.0;
            this.targetZ = this.getZ() + g / h * 12.0;
            this.targetY = this.getY() + 8.0;
        } else {
            this.targetX = d;
            this.targetY = (double) i;
            this.targetZ = e;
        }

        this.lifespan = 0;
        this.dropsItem = this.random.nextInt(5) > 0;
        //目标位置是30 所以减去30
        BlockPos subtractBlock = pos.down(10);
        this.initListPos = getExposedBlocks(this.getWorld(),subtractBlock);
    }


    public List<BlockPos> getExposedBlocks(World world, BlockPos origin) {
        List<BlockPos> result = new ArrayList<>();

        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -5; dy <= 5; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    BlockPos current = origin.add(dx, dy, dz);
                    BlockState state = world.getBlockState(current);

                    if (!state.isAir()) {
                        if (world.getBlockState(current.up()).isAir()) {
                            result.add(current);
                        }
                    }
                }
            }
        }
        return result;
    }

    public void setVelocityClient(double x, double y, double z) {
        this.setVelocity(x, y, z);
        if (this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
            double d = Math.sqrt(x * x + z * z);
            this.setYaw((float) (MathHelper.atan2(x, z) * 57.2957763671875));
            this.setPitch((float) (MathHelper.atan2(y, d) * 57.2957763671875));
            this.prevYaw = this.getYaw();
            this.prevPitch = this.getPitch();
        }

    }

    private void spawnFountainParticles(ServerWorld serverWorld, BlockPos blockPos, ParticleEffect blackDust, ParticleEffect purpleDust, ParticleEffect whiteDust) {
        // 总粒子计数器
        int totalParticles = 0;
        int maxParticles = 100; // 设置最大粒子数量

        // 底部较少的粒子 - 圆锥尖端
        for (int i = 0; i < 20 && totalParticles < maxParticles; i++) {
            // 在底部产生非常小的扩散
            double angle = serverWorld.random.nextDouble() * Math.PI * 2;
            double radius = 0.1 + serverWorld.random.nextDouble() * 0.1; // 非常小的半径

            // 设置水平位置(很小的偏移)
            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;

            // 设置倾斜向上的速度向量，速度随高度增加
            double velocityY = 3.0 + serverWorld.random.nextDouble() * 1.0;
            // 非常小的水平速度
            double velocityX = offsetX * 0.1;
            double velocityZ = offsetZ * 0.1;

            serverWorld.spawnParticles(
                    blackDust,
                    blockPos.getX() + 0.5 + offsetX * 0.5, // 位置偏移很小
                    blockPos.getY() + 0.1,
                    blockPos.getZ() + 0.5 + offsetZ * 0.5,
                    1,
                    velocityX,
                    velocityY,
                    velocityZ,
                    0.01
            );

            totalParticles++;
        }

        // 中部扩散更大的粒子 - 圆锥中部
        for (int i = 0; i < 20 && totalParticles < maxParticles; i++) {
            double angle = serverWorld.random.nextDouble() * Math.PI * 2;
            // 随高度增加半径
            double heightFactor = 0.3 + serverWorld.random.nextDouble() * 0.2; // 0.3-0.5之间
            double radius = 0.2 * heightFactor;

            double velocityY = 3.0 + serverWorld.random.nextDouble() * 1.0;
            // 水平速度随高度增加
            double velocityX = Math.cos(angle) * (0.1 + heightFactor * 0.2);
            double velocityZ = Math.sin(angle) * (0.1 + heightFactor * 0.2);

            serverWorld.spawnParticles(
                    i % 5 == 0 ? purpleDust : blackDust, // 偶尔添加紫色
                    blockPos.getX() + 0.5,
                    blockPos.getY() + 0.3,
                    blockPos.getZ() + 0.5,
                    1,
                    velocityX,
                    velocityY,
                    velocityZ,
                    0.01
            );

            totalParticles++;
        }

        // 顶部大扩散 - 圆锥底部
        for (int i = 0; i < 30 && totalParticles < maxParticles; i++) {
            double angle = serverWorld.random.nextDouble() * Math.PI * 2;
            // 顶部粒子的水平速度较大，创造扩散效果
            double speedFactor = 0.5 + serverWorld.random.nextDouble() * 0.5; // 0.5-1.0

            // 计算扩散角度和速度
            double velocityY = 3.0 + serverWorld.random.nextDouble() * 1.5; // 向上速度
            double velocityX = Math.cos(angle) * speedFactor; // 较大的水平速度
            double velocityZ = Math.sin(angle) * speedFactor;

            // 粒子类型
            ParticleEffect particleEffect = i % 7 == 0 ? purpleDust :
                    (i % 15 == 0 ? whiteDust : blackDust);

            serverWorld.spawnParticles(
                    particleEffect,
                    blockPos.getX() + 0.5,
                    blockPos.getY() + 0.5, // 稍微高一点的起始点
                    blockPos.getZ() + 0.5,
                    1,
                    velocityX,
                    velocityY,
                    velocityZ,
                    0.01
            );

            totalParticles++;
        }

        // 顶部大扩散 - 圆锥底部
        for (int i = 0; i < 30 && totalParticles < maxParticles; i++) {
            double angle = serverWorld.random.nextDouble() * Math.PI * 2;
            // 顶部粒子的水平速度较大，创造扩散效果
            double speedFactor = 0.5 + serverWorld.random.nextDouble() * 0.5; // 0.5-1.0

            // 计算扩散角度和速度
            double velocityY = 3.0 + serverWorld.random.nextDouble() * 4.0; // 向上速度
            double velocityX = Math.cos(angle) * speedFactor; // 较大的水平速度
            double velocityZ = Math.sin(angle) * speedFactor;

            // 粒子类型
            ParticleEffect particleEffect = i % 7 == 0 ? purpleDust :
                    (i % 15 == 0 ? whiteDust : blackDust);

            serverWorld.spawnParticles(
                    particleEffect,
                    blockPos.getX() + 0.5,
                    blockPos.getY() + 0.5, // 稍微高一点的起始点
                    blockPos.getZ() + 0.5,
                    1,
                    velocityX,
                    velocityY,
                    velocityZ,
                    0.01
            );

            totalParticles++;
        }
    }

    public void tick() {
        super.tick();
        if (this.age > 80 && initListPos != null) {
            int size = initListPos.size();
            if (size > 1 && this.getWorld().getTime() % 2 == 0) {

                Set<BlockPos> needReplaceSet = new HashSet<>();
                for (int i = 0; i < 1; i++) {
                    int randomInt = CommonUtils.getRandom().nextInt(0, size);
                    needReplaceSet.add(initListPos.get(randomInt));
                    size--;
                    initListPos.remove(randomInt);
                }
                for (BlockPos blockPos : needReplaceSet) {
                    this.getWorld().setBlockState(blockPos, ModBlock.HORIZONTAL_BLACK_NETHER_PORTAL.getDefaultState());
                    this.getWorld().playSound(null, blockPos,SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS);
                    // 确保世界是ServerWorld
                    if (this.getWorld() instanceof ServerWorld serverWorld) {
                        // 创建粒子类型
                        DustParticleEffect blackDust = new DustParticleEffect(new Vector3f(0.0f, 0.0f, 0.0f), 2.0f); // 纯黑色
                        DustParticleEffect litBlockDust = new DustParticleEffect(new Vector3f(0.5f, 0.5f, 0.5f), 1.8f); // 紫色
                        DustParticleEffect grayDust = new DustParticleEffect(new Vector3f(0.8f, 0.8f, 0.8f), 1.5f); // 白色

                        // 立即生成一次粒子效果
                        spawnFountainParticles(serverWorld, blockPos, blackDust, litBlockDust, grayDust);

                        // 安排定期生成粒子的任务
                        ServerEachTickTaskManager.scheduleTask("totem_of_dead_" + UUID.randomUUID().toString(), 10, 1, () -> {
                            if (serverWorld != null && serverWorld.isChunkLoaded(blockPos)) {
                                spawnFountainParticles(serverWorld, blockPos, blackDust, litBlockDust, grayDust);
                            }
                        });
                    }
                }
            }
        }
        Vec3d vec3d = this.getVelocity();
        double d = this.getX() + vec3d.x;
        double e = this.getY() + vec3d.y;
        double f = this.getZ() + vec3d.z;
        double g = vec3d.horizontalLength();
        this.setPitch(updateRotation(this.prevPitch, (float) (MathHelper.atan2(vec3d.y, g) * 57.2957763671875)));
        this.setYaw(updateRotation(this.prevYaw, (float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875)));
        if (!this.getWorld().isClient) {
            double h = this.targetX - d;
            double i = this.targetZ - f;
            float j = (float) Math.sqrt(h * h + i * i);
            float k = (float) MathHelper.atan2(i, h);
            double l = MathHelper.lerp(0.0025, g, (double) j);
            double m = vec3d.y;
            if (j < 1.0F) {
                l *= 0.8;
                m *= 0.8;
            }

            int n = this.getY() < this.targetY ? 1 : -1;
            vec3d = new Vec3d(Math.cos((double) k) * l, m + ((double) n - m) * 0.014999999664723873, Math.sin((double) k) * l);
            this.setVelocity(vec3d);
        }

        float o = 0.25F;
        if (this.isTouchingWater()) {
            for (int p = 0; p < 4; ++p) {
                this.getWorld().addParticle(ParticleTypes.PORTAL, d - vec3d.x * 0.25, e - vec3d.y * 0.25, f - vec3d.z * 0.25, vec3d.x, vec3d.y, vec3d.z);
            }
        } else {
            this.getWorld().addParticle(ParticleTypes.PORTAL, d - vec3d.x * 0.25 + this.random.nextDouble() * 0.6 - 0.3, e - vec3d.y * 0.25 - 0.5, f - vec3d.z * 0.25 + this.random.nextDouble() * 0.6 - 0.3, vec3d.x, vec3d.y, vec3d.z);
            if (this.getWorld().getTime() % 1 == 0) {
                float radius = 5f + 2f * (float) Math.sin(CommonUtils.getRandom().nextFloat(0, 20) * 0.2); // 随时间变化的半径（0.2是调速）
                int count = 20 * CommonUtils.getRandom().nextInt(0, 20); // 粒子数量（环的密度）

                for (int i = 0; i < count; i++) {
                    double angle = 2 * Math.PI * i / count;
                    double xOffset = Math.cos(angle) * radius;
                    double zOffset = Math.sin(angle) * radius;

                    this.getWorld().addParticle(
                            ParticleTypes.PORTAL, // 可换其他粒子
                            this.getX() + xOffset,
                            this.getY(),
                            this.getZ() + zOffset,
                            0.0, 0.01, 0.0 // 稍微有点上升感
                    );
                }
            }
        }

        if (!this.getWorld().isClient) {
            this.setPosition(d, e, f);
            ++this.lifespan;
            if (this.lifespan > 600 && !this.getWorld().isClient) {
                this.playSound(SoundEvents.ENTITY_ENDER_EYE_DEATH, 1.0F, 1.0F);
                this.discard();
                if (this.dropsItem) {
                    this.getWorld().spawnEntity(new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), this.getStack()));
                } else {
                    this.getWorld().syncWorldEvent(2003, this.getBlockPos(), 0);
                }
            }
        } else {
            this.setPos(d, e, f);
        }

    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("Item", this.getStack().encode(this.getRegistryManager()));
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Item", 10)) {
            this.setItem((ItemStack) ItemStack.fromNbt(this.getRegistryManager(), nbt.getCompound("Item")).orElse(this.getItem()));
        } else {
            this.setItem(this.getItem());
        }

    }

    private ItemStack getItem() {
        return new ItemStack(ItemsRegister.TOTEM_OF_DEAD_BACK);
    }

    public float getBrightnessAtEyes() {
        return 1.0F;
    }

    public boolean isAttackable() {
        return false;
    }

    protected static float updateRotation(float prevRot, float newRot) {
        while (newRot - prevRot < -180.0F) {
            prevRot -= 360.0F;
        }

        while (newRot - prevRot >= 180.0F) {
            prevRot += 360.0F;
        }

        return MathHelper.lerp(0.2F, prevRot, newRot);
    }

    static {
        ITEM = DataTracker.registerData(TotemOfDeadEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    }
}
