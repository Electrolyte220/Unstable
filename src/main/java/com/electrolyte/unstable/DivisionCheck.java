package com.electrolyte.unstable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DivisionCheck {

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
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos pos1 = new BlockPos(pos.below().getX() + x, pos.below().getY(), pos.below().getZ() + z);
                level.setBlock(pos1, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
            }
        }
    }

    public static boolean checkRedstone(Level level, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0)
                    continue;
                BlockPos pos1 = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                if (level.getBlockState(pos1).getBlock() != Blocks.REDSTONE_WIRE)
                    return false;
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
        return level.getLightEngine().getRawBrightness(pos, 15) == 7;
    }

    public static boolean checkSky(Level level, BlockPos pos) {
        return level.canSeeSky(pos);
    }

    public static boolean checkNatural(Level level, BlockPos pos) {
        for(int x = -2; x <= 2; x++) {
            for(int z = -2; z <= 2; z++) {
                BlockPos pos1 = new BlockPos(pos.below().getX() + x, pos.below().getY(), pos.below().getZ() + z);
                if(level.getBlockState(pos1).getBlock() != Blocks.DIRT && level.getBlockState(pos1).getBlock() != Blocks.GRASS_BLOCK) return false;
            }
        }
        return true;
    }
}