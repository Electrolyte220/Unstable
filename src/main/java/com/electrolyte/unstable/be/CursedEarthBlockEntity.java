package com.electrolyte.unstable.be;

import com.electrolyte.unstable.UnstableConfig;
import com.electrolyte.unstable.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Random;

public class CursedEarthBlockEntity extends BlockEntity {

    private final int spawnTimerMin = UnstableConfig.MIN_SPAWN_DELAY.get();
    private final int spawnTimerMax = UnstableConfig.MAX_SPAWN_DELAY.get();
    private int spawnTimer = Mth.nextInt(new Random(), spawnTimerMin, spawnTimerMax);

    public CursedEarthBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlocks.CURSED_EARTH_BE.get(), pWorldPosition, pBlockState);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CursedEarthBlockEntity blockEntity) {}

    private void serverTickInternal(Level level, BlockPos pos, BlockState state, CursedEarthBlockEntity blockEntity) {
        if(level.getDifficulty() != Difficulty.PEACEFUL) {
            if (spawnTimer > 0) {
                spawnTimer--;
            } else if (spawnTimer == 0) {
                WeightedRandomList<MobSpawnSettings.SpawnerData> validEntities = level.getBiome(pos).value().getMobSettings().getMobs(MobCategory.MONSTER);
                validEntities.getRandom(new Random()).ifPresent(spawnerData -> {
                    EntityType<?> type = spawnerData.type;
                    if (SpawnPlacements.checkSpawnRules(type, (ServerLevelAccessor) level, MobSpawnType.NATURAL, pos.above(), new Random())) {
                        Mob mob = (Mob) type.create(level);
                        mob.setPos(pos.getX() + 0.5, pos.above().getY(), pos.getZ() + 0.5);
                        mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, Integer.MAX_VALUE, 3, true, true));
                        BlockCollisions collisions = new BlockCollisions(level, mob, mob.getBoundingBox(), false);
                        if (!collisions.hasNext() && level.getNearbyEntities(Mob.class, TargetingConditions.DEFAULT, mob, (new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 0.5, (double) pos.above().getY(), pos.getZ() + 0.5).inflate(5))).size() < 25) {
                            mob.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, null, null);
                            level.addFreshEntity(mob);
                        }
                    }
                });
                spawnTimer = Mth.nextInt(new Random(), spawnTimerMin, spawnTimerMax);
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CursedEarthBlockEntity blockEntity) {
        blockEntity.serverTickInternal(level, pos, state, blockEntity);
    }
}
