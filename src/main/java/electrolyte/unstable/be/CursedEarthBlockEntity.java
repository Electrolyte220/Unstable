package electrolyte.unstable.be;

import electrolyte.unstable.UnstableConfig;
import electrolyte.unstable.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

public class CursedEarthBlockEntity extends BlockEntity {

    private final int spawnTimerMin = UnstableConfig.MIN_SPAWN_DELAY.get();
    private final int spawnTimerMax = UnstableConfig.MAX_SPAWN_DELAY.get();
    private int spawnTimer = Mth.nextInt(RandomSource.create(), spawnTimerMin, spawnTimerMax);

    public CursedEarthBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlocks.CURSED_EARTH_BE.get(), pWorldPosition, pBlockState);
    }

    public static void clientTick() {}

    public static void serverTick(Level level, BlockPos pos, BlockState state, CursedEarthBlockEntity blockEntity) {
        if(level.getDifficulty() != Difficulty.PEACEFUL && blockEntity.spawnTimer % 100 == 0) {
            level.getEntities(null, new AABB(pos.above(), pos.above()).inflate(1)).forEach(entity -> {
                if(entity instanceof Mob mob) {
                    if(mob instanceof Creeper creeper && !creeper.getTags().contains("noLingeringEffects")) {
                        creeper.addTag("noLingeringEffects");
                    }
                    mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 1, true, true));
                    mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, true, true));
                    mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 1, true, true));
                }
            });
        }
        if (blockEntity.spawnTimer > 0) {
            blockEntity.spawnTimer--;
        } else if (blockEntity.spawnTimer == 0) {
            WeightedRandomList<MobSpawnSettings.SpawnerData> validEntities = level.getBiome(pos).value().getMobSettings().getMobs(MobCategory.MONSTER);
            validEntities.getRandom(RandomSource.create()).ifPresent(spawnerData -> {
                EntityType<?> type = spawnerData.type;
                if (blockEntity.getPersistentData().getBoolean("createdByRitual") || SpawnPlacements.checkSpawnRules(type, (ServerLevelAccessor) level, MobSpawnType.SPAWNER, pos.above(), RandomSource.create())) {
                    Mob mob = (Mob) type.create(level);
                    mob.setPos(pos.getX() + 0.5, pos.above().getY(), pos.getZ() + 0.5);
                    mob.setYHeadRot(new Random().nextFloat() * 360.0F);
                    //todo: check collisions here & end siege spawning
                    BlockCollisions<BlockPos> collisions = new BlockCollisions<>(level, mob, mob.getBoundingBox(), false, (blockpos, shape) -> blockpos);
                    if (!collisions.hasNext() && level.getNearbyEntities(Mob.class, TargetingConditions.DEFAULT, mob, (new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 0.5, pos.above().getY(), pos.getZ() + 0.5).inflate(5))).size() < 25) {
                        ForgeEventFactory.onFinalizeSpawn(mob, (ServerLevelAccessor) level, level.getCurrentDifficultyAt(pos), MobSpawnType.SPAWNER, null, null);
                        level.addFreshEntity(mob);
                    }
                }
            });
            blockEntity.spawnTimer = Mth.nextInt(RandomSource.create(), blockEntity.spawnTimerMin, blockEntity.spawnTimerMax);
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        this.spawnTimer = pTag.getInt("spawnTimer");
        super.load(pTag);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.putInt("spawnTimer", this.spawnTimer);
        super.saveAdditional(pTag);
    }
}
