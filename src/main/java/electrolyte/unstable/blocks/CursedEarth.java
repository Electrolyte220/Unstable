package electrolyte.unstable.blocks;

import electrolyte.unstable.UnstableConfig;
import electrolyte.unstable.be.CursedEarthBlockEntity;
import electrolyte.unstable.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CursedEarth extends BaseEntityBlock {

    public CursedEarth(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        if(!canBeCursedEarth(pState, pLevel, pPos)) {
            if(!pLevel.isAreaLoaded(pPos, 1)) return;
            pLevel.setBlockAndUpdate(pPos, Blocks.DIRT.defaultBlockState());
        } else {
            if(!pLevel.isAreaLoaded(pPos, 3)) return;
            if(pLevel.getMaxLocalRawBrightness(pPos.above()) < 7) {
                for(int i = 0; i < 4; ++i) {
                    BlockPos pos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(5) - 3, pRandom.nextInt(3) - 1);
                    if(pLevel.getBlockState(pos).is(BlockTags.DIRT) && (canBeCursedEarth(this.defaultBlockState(), pLevel, pos))) {
                        pLevel.setBlockAndUpdate(pos, this.defaultBlockState());
                    }
                }
            }
        }

        BlockPos pos = pPos.above();
        BlockState state = pLevel.getBlockState(pos);
        if(state.isAir() && pLevel.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            if(pLevel.getBrightness(LightLayer.BLOCK, pos) > 1) {
                pLevel.getEntities(null, new AABB(pos, pos).inflate(1)).forEach(entity -> {
                    if(!(entity instanceof Player)) entity.hurt(DamageSource.CACTUS, 0.5f);
                });
            }
            if (pLevel.getBrightness(LightLayer.BLOCK, pos) > 9) {
                if(pRandom.nextInt(24) == 0) pLevel.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
            } else if (pLevel.isDay() && pLevel.canSeeSky(pos)) {
                pLevel.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
            }
        }
        if (state.is(BlockTags.FIRE)) {
            pLevel.setBlockAndUpdate(pPos, Blocks.DIRT.defaultBlockState());
        }
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return direction == Direction.UP;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 150;
    }

    //Modified from SpreadingSnowyDirtBlock#canBeGrass
    private static boolean canBeCursedEarth(BlockState pState, LevelReader pLevelReader, BlockPos pPos) {
        BlockPos blockpos = pPos.above();
        BlockState blockstate = pLevelReader.getBlockState(blockpos);
        return LayerLightEngine.getLightBlockInto(pLevelReader, pState, pPos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(pLevelReader, blockpos)) < 7;
    }

    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {
        if(UnstableConfig.CURSED_EARTH_PARTICLES.get()) {
            for (int i = 0; i < pRand.nextInt(1) + 1; ++i) {
                this.spawnParticle(pLevel, pPos, pState);
            }
        }
    }

    private void spawnParticle(Level pLevel, BlockPos pPos, BlockState pState) {
        if (pState.getFluidState().isEmpty() && !(pLevel.random.nextFloat() < 0.3F)) {
            VoxelShape voxelshape = pState.getCollisionShape(pLevel, pPos);
            double d0 = voxelshape.max(Direction.Axis.Y);
            BlockPos blockpos = pPos.above();
            BlockState blockstate = pLevel.getBlockState(blockpos);
            if (d0 >= 1.0D && blockstate.isAir()) {
                VoxelShape voxelshape1 = blockstate.getCollisionShape(pLevel, blockpos);
                double d2 = voxelshape1.max(Direction.Axis.Y);
                if (d2 < 1.0D) {
                    this.spawnParticle(pLevel, pPos, voxelshape, (double) blockpos.getY() + 0.05D);
                }
            }
        }
    }

    private void spawnParticle(Level pLevel, BlockPos pPos, VoxelShape pShape, double pY) {
        this.spawnFluidParticle(pLevel, (double)pPos.getX() + pShape.min(Direction.Axis.X), (double)pPos.getX() + pShape.max(Direction.Axis.X), (double)pPos.getZ() + pShape.min(Direction.Axis.Z), (double)pPos.getZ() + pShape.max(Direction.Axis.Z), pY);
    }

    private void spawnFluidParticle(Level pParticleData, double pX1, double pX2, double pZ1, double pZ2, double pY) {
        pParticleData.addParticle(ParticleTypes.REVERSE_PORTAL, Mth.lerp(pParticleData.random.nextDouble(), pX1, pX2), pY, Mth.lerp(pParticleData.random.nextDouble(), pZ1, pZ2), 0.0D, 0.0D, 0.0D);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CursedEarthBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlocks.CURSED_EARTH_BE.get(), pLevel.isClientSide ? (level, pos, state, blockEntity) -> CursedEarthBlockEntity.clientTick() : CursedEarthBlockEntity::serverTick);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
