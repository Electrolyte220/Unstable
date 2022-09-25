package com.electrolyte.unstable.helper;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.UnstableConfig;
import com.electrolyte.unstable.be.CursedEarthBlockEntity;
import com.electrolyte.unstable.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class ActivationRitualHelper {

    public static void updateRedstone(Level level, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0)
                    continue;
                BlockPos pos1 = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                BlockState state = level.getBlockState(pos1);
                level.setBlock(pos1, state.setValue(RedStoneWireBlock.POWER, 15), 2);
            }
        }
    }

    public static void updateBlocks(Level level, BlockPos pos) {
        if(ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(UnstableConfig.ACTIVATION_BLOCK.get()))) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    BlockPos pos1 = new BlockPos(pos.below().getX() + x, pos.below().getY(), pos.below().getZ() + z);
                    if (ForgeRegistries.BLOCKS.getValue(new ResourceLocation(UnstableConfig.ACTIVATION_BLOCK.get())) == ModBlocks.CURSED_EARTH.get()) {
                        level.setBlock(pos1, ModBlocks.CURSED_EARTH.get().defaultBlockState(), 2);
                        CursedEarthBlockEntity be = ModBlocks.CURSED_EARTH_BE.get().getBlockEntity(level, pos1);
                        be.getTileData().putBoolean("createdByRitual", true);
                    } else {
                        level.setBlock(pos1, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(UnstableConfig.ACTIVATION_BLOCK.get())).defaultBlockState(), 2);
                    }
                }
            }
        } else {
            Unstable.LOGGER.error("Unable to update natural earth to block: {}, as it does not exist in the block registry.", UnstableConfig.ACTIVATION_BLOCK.get());
        }
    }

    public static boolean checkRedstone(Level level, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0)
                    continue;
                BlockPos pos1 = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                if (level.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE) return false;
            }
        }
        return true;
    }
    public static boolean checkTime(long time) {
        time %= 24000L;
        return time >= 18000 - 500 && time <= 18000 + 500;
    }

    public static boolean checkEnchantTable(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE;
    }

    public static boolean checkLight(Level level, BlockPos pos) {
        return level.getBrightness(LightLayer.BLOCK, pos) < 8;
    }

    public static boolean checkSky(Level level, BlockPos pos) {
        return level.canSeeSky(pos);
    }

    public static boolean checkNatural(Level level, BlockPos pos) {
        for(int x = -2; x <= 2; x++) {
            for(int z = -2; z <= 2; z++) {
                BlockPos pos1 = new BlockPos(pos.below().getX() + x, pos.below().getY(), pos.below().getZ() + z);
                if(level.getBlockState(pos1).getTags().noneMatch(tag -> tag.equals(BlockTags.DIRT))) return false;
            }
        }
        return true;
    }
}