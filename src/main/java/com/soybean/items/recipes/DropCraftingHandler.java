package com.soybean.items.recipes;

import com.soybean.block.ModBlock;
import com.soybean.items.ItemsRegister;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class DropCraftingHandler {

    // ---------------------------------------------------------------
    // 配方定义
    // ---------------------------------------------------------------

    /**
     * 每条配方：所需物品（允许重复，表示需要多个）+ 产物
     */
    private record DropRecipe(
            List<Item> ingredients,   // 所需材料（含重复）
            ItemStack result,         // 合成产物
            CraftEffect effect        // 粒子/音效风格
    ) {}

    /** 粒子风格枚举 */
    private enum CraftEffect {
        BLAZE,    // 烈焰人蛋 — 火焰 + 灵魂火焰
        SPAWNER,  // 刷怪笼   — 巫师粒子 + 烟雾
        SOUL_SAND // 灵魂沙   — 大量灵魂粒子
    }

    private static final List<DropRecipe> RECIPES = List.of(

            // ② 灵魂 + 火焰弹 + 红石 → 烈焰人刷怪蛋
            new DropRecipe(
                    List.of(ItemsRegister.SOUL_ITEM, ModBlock.FIRE.asItem(), Items.REDSTONE),
                    new ItemStack(Items.BLAZE_SPAWN_EGG, 1),
                    CraftEffect.BLAZE
            ),

            // ③ 灵魂 + 红石 + 铁链 → 刷怪笼
            new DropRecipe(
                    List.of(ItemsRegister.SOUL_ITEM, Items.REDSTONE, Items.CHAIN),
                    new ItemStack(Items.SPAWNER, 1),
                    CraftEffect.SPAWNER
            ),

            // ④ 灵魂 x8 + 沙子 → 灵魂沙
            new DropRecipe(
                    List.of(
                            ItemsRegister.SOUL_ITEM, ItemsRegister.SOUL_ITEM, ItemsRegister.SOUL_ITEM,
                            ItemsRegister.SOUL_ITEM, ItemsRegister.SOUL_ITEM, ItemsRegister.SOUL_ITEM,
                            ItemsRegister.SOUL_ITEM, ItemsRegister.SOUL_ITEM,
                            Items.SAND
                    ),
                    new ItemStack(Items.SOUL_SAND, 1),
                    CraftEffect.SOUL_SAND
            )
    );

    // ---------------------------------------------------------------
    // 检测半径（方块）
    // ---------------------------------------------------------------
    private static final double DETECT_RADIUS = 1.0;

    public static void onWorldTick(ServerWorld world) {
        List<ServerPlayerEntity> players = world.getPlayers();
        if (players.isEmpty()) return;

        // 收集玩家附近所有候选物品
        Set<ItemEntity> candidates = new HashSet<>();
        for (ServerPlayerEntity player : players) {
            Box searchBox = Box.of(player.getPos(), 16, 16, 16);
            world.getEntitiesByClass(
                    ItemEntity.class,
                    searchBox,
                    e -> !e.isRemoved() && e.isAlive()
            ).forEach(candidates::add);
        }

        if (candidates.isEmpty()) return;

        // 已处理过的物品不重复作为触发中心
        Set<ItemEntity> checked = new HashSet<>();

        for (ItemEntity center : candidates) {
            if (checked.contains(center)) continue;
            checked.add(center);

            // 以该物品为中心，搜索半径 1.5 格内所有物品
            // 调整这个数字可以控制合成判定范围
            double radius = 0.2;
            Box nearbyBox = Box.of(center.getPos(), radius * 2, radius * 2, radius * 2);

            List<ItemEntity> nearby = world.getEntitiesByClass(
                    ItemEntity.class,
                    nearbyBox,
                    e -> !e.isRemoved() && e.isAlive()
            );

            if (nearby.size() < 2) continue;

            tryAllRecipes(world, nearby);
        }
    }

    // ---------------------------------------------------------------
    // 尝试所有配方
    // ---------------------------------------------------------------
    private static void tryAllRecipes(ServerWorld world, List<ItemEntity> entities) {
        for (DropRecipe recipe : RECIPES) {
            tryRecipe(world, entities, recipe);
        }
    }

    // ---------------------------------------------------------------
    // 尝试单条配方
    // ---------------------------------------------------------------
    private static void tryRecipe(ServerWorld world, List<ItemEntity> entities, DropRecipe recipe) {

        // 统计配方所需物品数量
        Map<Item, Integer> needed = new HashMap<>();
        for (Item item : recipe.ingredients()) {
            needed.merge(item, 1, Integer::sum);
        }

        // 在实体列表里寻找满足需求的实体
        // key = Item, value = (实体, 本次消耗数量)
        Map<Item, List<ItemEntity>> candidates = new HashMap<>();
        for (ItemEntity entity : entities) {
            Item item = entity.getStack().getItem();
            if (needed.containsKey(item)) {
                candidates.computeIfAbsent(item, k -> new ArrayList<>()).add(entity);
            }
        }

        // 验证每种物品总数量是否足够
        for (Map.Entry<Item, Integer> entry : needed.entrySet()) {
            Item item = entry.getKey();
            int require = entry.getValue();
            List<ItemEntity> found = candidates.getOrDefault(item, List.of());
            int total = found.stream().mapToInt(e -> e.getStack().getCount()).sum();
            if (total < require) return; // 数量不足，跳过
        }

        // ── 全部满足，执行合成 ──

        // 计算粒子生成位置（取所有参与物品的重心）
        Vec3d center = computeCenter(candidates.values().stream()
                .flatMap(Collection::stream).toList());

        // 消耗材料（从物品实体里减去所需数量）
        for (Map.Entry<Item, Integer> entry : needed.entrySet()) {
            consumeItems(candidates.get(entry.getKey()), entry.getValue());
        }

        // 播放粒子和音效
        spawnEffect(world, center, recipe.effect());

        // 生成产物
        ItemEntity resultEntity = new ItemEntity(
                world,
                center.x, center.y + 0.2, center.z,
                recipe.result().copy()
        );
        resultEntity.setVelocity(0, 0.2, 0);
        world.spawnEntity(resultEntity);
    }

    // ---------------------------------------------------------------
    // 从物品实体列表中消耗指定数量
    // ---------------------------------------------------------------
    private static void consumeItems(List<ItemEntity> entities, int amount) {
        int remaining = amount;
        for (ItemEntity entity : entities) {
            if (remaining <= 0) break;
            ItemStack stack = entity.getStack();
            int take = Math.min(stack.getCount(), remaining);
            stack.decrement(take);
            remaining -= take;
            if (stack.isEmpty()) {
                entity.discard();
            }
        }
    }

    // ---------------------------------------------------------------
    // 计算重心
    // ---------------------------------------------------------------
    private static Vec3d computeCenter(List<ItemEntity> entities) {
        double x = 0, y = 0, z = 0;
        for (ItemEntity e : entities) {
            x += e.getX(); y += e.getY(); z += e.getZ();
        }
        int n = entities.size();
        return new Vec3d(x / n, y / n + 0.3, z / n);
    }

    // ---------------------------------------------------------------
    // 粒子 + 音效
    // ---------------------------------------------------------------
    private static void spawnEffect(ServerWorld world, Vec3d pos, CraftEffect effect) {
        double x = pos.x, y = pos.y, z = pos.z;

        switch (effect) {

            case BLAZE -> {
                // 火焰 + 灵魂火焰 — 与烈焰人、地狱主题契合
                world.spawnParticles(ParticleTypes.FLAME,
                        x, y, z, 80, 0.4, 0.4, 0.4, 0.05);
                world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        x, y, z, 40, 0.3, 0.3, 0.3, 0.02);
                world.playSound(null, x, y, z,
                        SoundEvents.ENTITY_BLAZE_SHOOT,
                        SoundCategory.BLOCKS, 1f, 0.8f);
            }

            case SPAWNER -> {
                // 巫师粒子 + 烟雾 + 灵魂 — 暗黑魔法、刷怪笼主题
                world.spawnParticles(ParticleTypes.WITCH,
                        x, y, z, 60, 0.5, 0.5, 0.5, 0.02);
                world.spawnParticles(ParticleTypes.LARGE_SMOKE,
                        x, y, z, 30, 0.3, 0.4, 0.3, 0.02);
                world.spawnParticles(ParticleTypes.SOUL,
                        x, y, z, 20, 0.2, 0.2, 0.2, 0.01);
                world.playSound(null, x, y, z,
                        SoundEvents.BLOCK_PORTAL_TRIGGER,
                        SoundCategory.BLOCKS, 0.5f, 0.5f);
            }

            case SOUL_SAND -> {
                // 大量灵魂粒子向上汇聚 — 灵魂沙主题
                world.spawnParticles(ParticleTypes.SOUL,
                        x, y, z, 100, 0.6, 0.3, 0.6, 0.03);
                world.spawnParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        x, y, z, 60, 0.5, 0.5, 0.5, 0.02);
                world.spawnParticles(ParticleTypes.ASH,
                        x, y + 0.5, z, 40, 0.4, 0.2, 0.4, 0.01);
                world.playSound(null, x, y, z,
                        SoundEvents.BLOCK_SOUL_SAND_HIT,
                        SoundCategory.BLOCKS, 1f, 0.6f);
                world.playSound(null, x, y, z,
                        SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP,
                        SoundCategory.AMBIENT, 0.5f, 1.2f);
            }
        }
    }
}