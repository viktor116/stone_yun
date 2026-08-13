package com.soybean.block.custom.entity;

import com.soybean.block.ModBlock;
import com.soybean.block.custom.ExtinguishTorchBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ExtinguishTorchBlockEntity extends BlockEntity {
    public static final long LIT_DURATION_MS = 60_000L;

    private long litStartTime = -1L;

    public ExtinguishTorchBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlock.EXTINGUISH_TORCH_TYPE, pos, state);
    }

    public void ignite() {
        this.litStartTime = this.world.getTime();
        this.markDirty();
    }

    public boolean isLit() {
        return this.litStartTime > 0;
    }

    public static void tick(World world, BlockPos pos, BlockState state, ExtinguishTorchBlockEntity be) {
        if (!world.isClient() && be.isLit()) {
            long now = world.getTime();
            if (now - be.litStartTime >= LIT_DURATION_MS / 50L) {
                be.extinguish();
            }
        }
    }

    private void extinguish() {
        if (this.world == null || this.world.isClient()) return;
        this.litStartTime = -1L;
        BlockState state = this.world.getBlockState(this.pos);
        if (state.getBlock() instanceof ExtinguishTorchBlock) {
            this.world.setBlockState(this.pos, state.with(ExtinguishTorchBlock.LIT, false));
            this.world.playSound(null, this.pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.7f, 0.8f);
            Random r = this.world.getRandom();
            for (int i = 0; i < 5; i++) {
                double dx = (double)this.pos.getX() + 0.5 + (r.nextDouble() - 0.5) * 0.4;
                double dy = (double)this.pos.getY() + 0.6 + r.nextDouble() * 0.3;
                double dz = (double)this.pos.getZ() + 0.5 + (r.nextDouble() - 0.5) * 0.4;
                this.world.addParticle(net.minecraft.particle.ParticleTypes.CLOUD, dx, dy, dz, 0.0, 0.02, 0.0);
            }
        }
        this.markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.readNbt(nbt, lookup);
        this.litStartTime = nbt.getLong("LitStartTime");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        super.writeNbt(nbt, lookup);
        nbt.putLong("LitStartTime", this.litStartTime);
    }
}
