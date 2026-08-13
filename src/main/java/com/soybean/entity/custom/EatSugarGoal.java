package com.soybean.entity.custom;

import com.soybean.block.custom.SugarWireBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;

public class EatSugarGoal extends Goal {

    private final RoachEntity roach;
    private BlockPos sugarTarget;
    private int searchCooldown;

    private static final double SEARCH_RANGE = 10.0;
    private static final double REACH_DIST_SQ = 2.25;
    private static final int SEARCH_INTERVAL = 10;

    public EatSugarGoal(RoachEntity roach) {
        this.roach = roach;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }
        searchCooldown = SEARCH_INTERVAL;
        sugarTarget = findClosestSugar();
        return sugarTarget != null;
    }

    @Override
    public void start() {
        roach.getNavigation().startMovingTo(
                sugarTarget.getX() + 0.5, sugarTarget.getY(), sugarTarget.getZ() + 0.5, 1.0);
    }

    @Override
    public boolean shouldContinue() {
        if (sugarTarget == null) return false;
        World world = roach.getWorld();
        if (!(world.getBlockState(sugarTarget).getBlock() instanceof SugarWireBlock)) {
            sugarTarget = null;
            return false;
        }
        double dx = roach.getX() - (sugarTarget.getX() + 0.5);
        double dz = roach.getZ() - (sugarTarget.getZ() + 0.5);
        return dx * dx + dz * dz > REACH_DIST_SQ;
    }

    @Override
    public void tick() {
        if (sugarTarget == null) return;

        roach.getLookControl().lookAt(
                sugarTarget.getX() + 0.5, sugarTarget.getY(), sugarTarget.getZ() + 0.5,
                30.0f, 30.0f);

        double dx = roach.getX() - (sugarTarget.getX() + 0.5);
        double dz = roach.getZ() - (sugarTarget.getZ() + 0.5);

        if (dx * dx + dz * dz <= REACH_DIST_SQ) {
            World world = roach.getWorld();
            if (world.getBlockState(sugarTarget).getBlock() instanceof SugarWireBlock) {
                world.removeBlock(sugarTarget, false);
                roach.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f, 1.2f);
            }
            sugarTarget = null;
            roach.getNavigation().stop();
        } else if (roach.getNavigation().isIdle()) {
            roach.getNavigation().startMovingTo(
                    sugarTarget.getX() + 0.5, sugarTarget.getY(), sugarTarget.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public void stop() {
        roach.getNavigation().stop();
        sugarTarget = null;
    }

    private BlockPos findClosestSugar() {
        BlockPos pos = roach.getBlockPos();
        BlockPos closest = null;
        double closestDist = SEARCH_RANGE * SEARCH_RANGE;
        for (BlockPos p : BlockPos.iterateOutwards(pos, 10, 3, 10)) {
            if (roach.getWorld().getBlockState(p).getBlock() instanceof SugarWireBlock) {
                double d = roach.getPos().distanceTo(Vec3d.ofCenter(p));
                if (d < closestDist) {
                    closestDist = d;
                    closest = p.toImmutable();
                }
            }
        }
        return closest;
    }
}
