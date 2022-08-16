package com.electrolyte.unstable.blocks;

import com.electrolyte.unstable.UnstableConfig;
import com.electrolyte.unstable.be.CursedEarthBlockEntity;
import com.electrolyte.unstable.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CursedEarth extends BaseEntityBlock {

    public CursedEarth(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull Random pRandom) {
        if(!canBeCursedEarth(pState, pLevel, pPos)) {
            if(!pLevel.isAreaLoaded(pPos, 1)) return;
            pLevel.setBlockAndUpdate(pPos, ModBlocks.CURSED_EARTH.get().defaultBlockState());
        } else {
            if(!pLevel.isAreaLoaded(pPos, 3)) return;
            if(pLevel.getMaxLocalRawBrightness(pPos.above()) < 7) {
                for(int i = 0; i < 4; ++i) {
                    BlockPos pos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(5) - 3, pRandom.nextInt(3) - 1);
                    if(pLevel.getBlockState(pos).is(BlockTags.DIRT) && (canBeCursedEarth(this.defaultBlockState(), pLevel, pos) && !pLevel.getFluidState(pos).is(FluidTags.WATER))) {
                        pLevel.setBlockAndUpdate(pos, this.defaultBlockState());
                    }
                }
            }
        }

    }

    //Modified from SpreadingSnowyDirtBlock#canBeGrass
    private static boolean canBeCursedEarth(BlockState pState, LevelReader pLevelReader, BlockPos pPos) {
        BlockPos blockpos = pPos.above();
        BlockState blockstate = pLevelReader.getBlockState(blockpos);
        if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            return LayerLightEngine.getLightBlockInto(pLevelReader, pState, pPos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(pLevelReader, blockpos)) < 7;
        }
    }

    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Random pRand) {
        if(UnstableConfig.CURSED_EARTH_PARTICLES.get()) {
            for (int i = 0; i < pRand.nextInt(1) + 1; ++i) {
                this.trySpawnDripParticles(pLevel, pPos, pState);
            }
        }
    }

    private void trySpawnDripParticles(Level pLevel, BlockPos pPos, BlockState pState) {
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
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new CursedEarthBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlocks.CURSED_EARTH_BE.get(), pLevel.isClientSide ? CursedEarthBlockEntity::clientTick : CursedEarthBlockEntity::serverTick);
    }

    //TODO: Figure out why the sides do not work with the lightmap.
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }
}
