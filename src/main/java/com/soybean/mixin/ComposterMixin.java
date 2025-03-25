package com.soybean.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 修改堆肥桶功能，使红沙可以放入并产出泥土
 */
@Mixin(ComposterBlock.class)
public abstract class ComposterMixin {

   @Shadow
   private static void registerCompostableItem(float levelIncreaseChance, ItemConvertible item) {}
   
    @Shadow
   private static BlockState emptyComposter(@Nullable Entity user, BlockState state, WorldAccess world, BlockPos pos) {
       return null; // 这里的实现会被忽略，因为这是一个Shadow方法
   }

    /**
     * 在游戏初始化时注册红沙为可堆肥物品
     */
    @Inject(method = "registerDefaultCompostableItems", at = @At("TAIL"))
    private static void registerRedSandAsCompostable(CallbackInfo ci) {
        // 注册红沙为可堆肥物品，成功率为65%
        registerCompostableItem(0.65F, Items.RED_SAND);
    }

    /**
     * 修改堆肥桶的输出物品，使其在处理红沙时产出泥土
     */
    @Inject(method = "emptyFullComposter", at = @At("HEAD"), cancellable = true)
    private static void onEmptyComposter(Entity user, BlockState state, World world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if (!world.isClient()) {
            Vec3d vec3d = Vec3d.add(pos, 0.5, 1.01, 0.5).addRandom(world.getRandom(), 0.7F);
            ItemEntity itemEntity = new ItemEntity(world, vec3d.getX(), vec3d.getY(), vec3d.getZ(), new ItemStack(Items.DIRT));
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }

        BlockState blockState =emptyComposter(user, state, world, pos);
        world.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_COMPOSTER_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        cir.setReturnValue(blockState);
        cir.cancel();
    }
}
