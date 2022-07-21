package com.electrolyte.unstable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.ArrayList;
import java.util.List;

public class DivisionCheckEnd {

    public static boolean checkBeacon(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == Blocks.BEACON;
    }

    public static boolean checkEndBlocks(Level level, BlockPos pos) {
        for(int x = -5; x <= 5; x++) {
            for(int z = -5; z <= 5; z++) {
                BlockPos pos1 = new BlockPos(pos.getX() + x, pos.below().getY(), pos.getZ() + z);
                if(level.getBlockState(pos1).getBlock() == Blocks.AIR) return false;
            }
        }
        return true;
    }

    public static boolean checkRedstoneAndString(Level level, BlockPos pos) {
        BlockPos topLeft = pos.west(4).north(4);
        if(level.getBlockState(topLeft).getBlock() != Blocks.REDSTONE_WIRE) return false;
        for(int z = 1; z <= 8; z++) {
            if (level.getBlockState(topLeft.east(z)).getBlock() != Blocks.TRIPWIRE) return false;
        }

        BlockPos topLeft1 = topLeft.south(1);
        if(level.getBlockState(topLeft1).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft1.east(1)).getBlock() != Blocks.TRIPWIRE) return false;
        for(int z = 2; z <= 8; z++) {
            if(level.getBlockState(topLeft1.east(z)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        }

        BlockPos topLeft2 = topLeft.south(2);
        if(level.getBlockState(topLeft2).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft2.east(1)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft2.east(2)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        for(int z = 3; z <= 7; z++) {
            if(level.getBlockState(topLeft2.east(z)).getBlock() != Blocks.TRIPWIRE) return false;
        }
        if(level.getBlockState(topLeft2.east(8)).getBlock() != Blocks.REDSTONE_WIRE) return false;

        BlockPos topLeft3 = topLeft.south(3);
        if(level.getBlockState(topLeft3).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft3.east(1)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft3.east(2)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft3.east(3)).getBlock() != Blocks.TRIPWIRE) return false;
        for(int z = 4; z <= 6; z++) {
            if(level.getBlockState(topLeft3.east(z)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        }
        if(level.getBlockState(topLeft3.east(7)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft3.east(8)).getBlock() != Blocks.REDSTONE_WIRE) return false;

        BlockPos topLeft4 = topLeft.south(4);
        if(level.getBlockState(topLeft4).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft4.east(1)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft4.east(2)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft4.east(3)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft4.east(5)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft4.east(6)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft4.east(7)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft4.east(8)).getBlock() != Blocks.REDSTONE_WIRE) return false;

        BlockPos topLeft5 = topLeft.south(5);
        if(level.getBlockState(topLeft5).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft5.east(1)).getBlock() != Blocks.TRIPWIRE) return false;
        for(int z = 2; z <= 4; z++) {
            if(level.getBlockState(topLeft5.east(z)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        }
        if(level.getBlockState(topLeft5.east(5)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft5.east(6)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft5.east(7)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft5.east(8)).getBlock() != Blocks.REDSTONE_WIRE) return false;

        BlockPos topLeft6 = topLeft.south(6);
        if(level.getBlockState(topLeft6).getBlock() != Blocks.REDSTONE_WIRE) return false;
        for(int z = 1; z <= 5; z++) {
            if(level.getBlockState(topLeft6.east(z)).getBlock() != Blocks.TRIPWIRE) return false;
        }
        if(level.getBlockState(topLeft6.east(6)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        if(level.getBlockState(topLeft6.east(7)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft6.east(8)).getBlock() != Blocks.REDSTONE_WIRE) return false;

        BlockPos topLeft7 = topLeft.south(7);
        for(int z = 0; z <= 6; z++) {
            if(level.getBlockState(topLeft7.east(z)).getBlock() != Blocks.REDSTONE_WIRE) return false;
        }
        if(level.getBlockState(topLeft7.east(7)).getBlock() != Blocks.TRIPWIRE) return false;
        if(level.getBlockState(topLeft7.east(8)).getBlock() != Blocks.REDSTONE_WIRE) return false;

        BlockPos topLeft8 = topLeft.south(8);
        for(int z = 0; z <= 7; z++) {
            if(level.getBlockState(topLeft8.east(z)).getBlock() != Blocks.TRIPWIRE) return false;
        }
        return level.getBlockState(topLeft8.east(8)).getBlock() == Blocks.REDSTONE_WIRE;
    }

    public static boolean checkChests(Level level, BlockPos pos) {
        if(level.getBlockState(new BlockPos(pos.east(5).getX(), pos.getY(), pos.getZ())).getBlock() != Blocks.CHEST) return false;
        if(level.getBlockState(new BlockPos(pos.west(5).getX(), pos.getY(), pos.getZ())).getBlock() != Blocks.CHEST) return false;
        if(level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.north(5).getZ())).getBlock() != Blocks.CHEST) return false;
        if(level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.south(5).getZ())).getBlock() != Blocks.CHEST) return false;
        return true;
    }

    public static boolean checkNorthChestContents(Level level, BlockPos pos) {
        BlockPos northChest = new BlockPos(pos.getX(), pos.getY(), pos.north(5).getZ());
        BlockEntity te = level.getBlockEntity(northChest);
        if(te instanceof ChestBlockEntity) {
            return ((ChestBlockEntity) te).getItem(0).getItem() == Item.BY_BLOCK.get(Blocks.STONE) &&
                    ((ChestBlockEntity) te).getItem(1).getItem() == Items.BRICK &&
                    ((ChestBlockEntity) te).getItem(2).getItem() == Item.BY_BLOCK.get(Blocks.GLASS) &&
                    ((ChestBlockEntity) te).getItem(3).getItem() == Items.COOKED_COD &&
                    ((ChestBlockEntity) te).getItem(4).getItem() == Item.BY_BLOCK.get(Blocks.TERRACOTTA) &&
                    ((ChestBlockEntity) te).getItem(5).getItem() == Items.GREEN_DYE &&
                    ((ChestBlockEntity) te).getItem(6).getItem() == Items.CHARCOAL &&
                    ((ChestBlockEntity) te).getItem(7).getItem() == Items.COOKED_BEEF &&
                    ((ChestBlockEntity) te).getItem(8).getItem() == Items.IRON_INGOT &&
                    ((ChestBlockEntity) te).getItem(9).getItem() == Items.COOKED_CHICKEN &&
                    ((ChestBlockEntity) te).getItem(10).getItem() == Items.GOLD_INGOT &&
                    ((ChestBlockEntity) te).getItem(11).getItem() == Items.BAKED_POTATO &&
                    ((ChestBlockEntity) te).getItem(12).getItem() == Items.COOKED_PORKCHOP &&
                    ((ChestBlockEntity) te).getItem(13).getItem() == Items.NETHER_BRICK;
        } return false;
    }

    public static boolean checkSouthChestContents(Level level, BlockPos pos) {
        BlockPos southChest = new BlockPos(pos.getX(), pos.getY(), pos.south(5).getZ());
        BlockEntity te = level.getBlockEntity(southChest);
        if(te instanceof ChestBlockEntity) {
            return ((ChestBlockEntity) te).getItem(0).getItem() == Item.BY_BLOCK.get(Blocks.GRASS_BLOCK) &&
                    ((ChestBlockEntity) te).getItem(1).getItem() == Item.BY_BLOCK.get(Blocks.LAPIS_ORE) &&
                    ((ChestBlockEntity) te).getItem(2).getItem() == Item.BY_BLOCK.get(Blocks.DIRT) &&
                    ((ChestBlockEntity) te).getItem(3).getItem() == Item.BY_BLOCK.get(Blocks.OBSIDIAN) &&
                    ((ChestBlockEntity) te).getItem(4).getItem() == Item.BY_BLOCK.get(Blocks.SAND) &&
                    ((ChestBlockEntity) te).getItem(5).getItem() == Item.BY_BLOCK.get(Blocks.DIAMOND_ORE) &&
                    ((ChestBlockEntity) te).getItem(6).getItem() == Item.BY_BLOCK.get(Blocks.GRAVEL) &&
                    ((ChestBlockEntity) te).getItem(7).getItem() == Item.BY_BLOCK.get(Blocks.REDSTONE_ORE) &&
                    ((ChestBlockEntity) te).getItem(8).getItem() == Item.BY_BLOCK.get(Blocks.GOLD_ORE) &&
                    ((ChestBlockEntity) te).getItem(9).getItem() == Item.BY_BLOCK.get(Blocks.CLAY) &&
                    ((ChestBlockEntity) te).getItem(10).getItem() == Item.BY_BLOCK.get(Blocks.IRON_ORE) &&
                    ((ChestBlockEntity) te).getItem(11).getItem() == Item.BY_BLOCK.get(Blocks.EMERALD_ORE) &&
                    ((ChestBlockEntity) te).getItem(12).getItem() == Item.BY_BLOCK.get(Blocks.COAL_ORE);
        }
        return false;
    }

    public static boolean checkEastChestContents(Level level, BlockPos pos) {
        BlockPos eastChest = new BlockPos(pos.east(5).getX(), pos.getY(), pos.getZ());
        BlockEntity te = level.getBlockEntity(eastChest);
        List<String> potionList = new ArrayList<>();
        potionList.add("{Potion:\"minecraft:water\"}");
        potionList.add("{Potion:\"minecraft:thick\"}");
        potionList.add("{Potion:\"minecraft:awkward\"}");
        potionList.add("{Potion:\"minecraft:mundane\"}");

        if(te instanceof ChestBlockEntity) {
            if (((ChestBlockEntity) te).getItem(0).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(1).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(2).getItem() instanceof PotionItem  &&
                    ((ChestBlockEntity) te).getItem(3).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(4).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(5).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(6).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(7).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(8).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(9).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(10).getItem() instanceof PotionItem &&
                    ((ChestBlockEntity) te).getItem(11).getItem() instanceof PotionItem) {
                for (int i = 0; i <= potionList.size(); ++i) {
                    if (!potionList.contains(((ChestBlockEntity) te).getItem(0).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(1).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(2).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(3).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(4).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(5).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(6).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(7).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(8).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(9).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(10).getTag().toString()) &&
                            !potionList.contains(((ChestBlockEntity) te).getItem(11).getTag().toString())) return true;
                }
            }
        }
        return false;
    }

    public static boolean checkWestChestContents(Level level, BlockPos pos) {
        BlockPos westChest = new BlockPos(pos.west(5).getX(), pos.getY(), pos.getZ());
        BlockEntity te = level.getBlockEntity(westChest);
        if(te instanceof ChestBlockEntity) {
            return ((ChestBlockEntity) te).getItem(0).getItem() == Items.MUSIC_DISC_11 &&
                    ((ChestBlockEntity) te).getItem(1).getItem() == Items.MUSIC_DISC_13 &&
                    ((ChestBlockEntity) te).getItem(2).getItem() == Items.MUSIC_DISC_BLOCKS &&
                    ((ChestBlockEntity) te).getItem(3).getItem() == Items.MUSIC_DISC_CAT &&
                    ((ChestBlockEntity) te).getItem(4).getItem() == Items.MUSIC_DISC_CHIRP &&
                    ((ChestBlockEntity) te).getItem(5).getItem() == Items.MUSIC_DISC_FAR &&
                    ((ChestBlockEntity) te).getItem(6).getItem() == Items.MUSIC_DISC_MALL &&
                    ((ChestBlockEntity) te).getItem(7).getItem() == Items.MUSIC_DISC_MELLOHI &&
                    ((ChestBlockEntity) te).getItem(8).getItem() == Items.MUSIC_DISC_STAL &&
                    ((ChestBlockEntity) te).getItem(9).getItem() == Items.MUSIC_DISC_STRAD &&
                    ((ChestBlockEntity) te).getItem(10).getItem() == Items.MUSIC_DISC_WAIT &&
                    ((ChestBlockEntity) te).getItem(11).getItem() == Items.MUSIC_DISC_WARD &&
                    ((ChestBlockEntity) te).getItem(12).getItem() == Items.MUSIC_DISC_PIGSTEP;
        }
        return false;
    }

    public static void destroyBeaconAndChests(Level level, BlockPos pos) {
        ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, pos.getX(), pos.getY(), pos.getZ(),2, 1.0D, 0.0D, 0.0D, 0);
        if (level.getBlockState(pos.below()).getBlock() == Blocks.BEACON) {
            level.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 2);
            level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        BlockPos northChest = new BlockPos(pos.getX(), pos.below().getY(), pos.north(5).getZ());
        BlockPos southChest = new BlockPos(pos.getX(), pos.below().getY(), pos.south(5).getZ());
        BlockPos eastChest = new BlockPos(pos.east(5).getX(), pos.below().getY(), pos.getZ());
        BlockPos westChest = new BlockPos(pos.west(5).getX(), pos.below().getY(), pos.getZ());

        ChestBlockEntity northChestTE = (ChestBlockEntity) level.getBlockEntity(northChest);
        ChestBlockEntity southChestTE = (ChestBlockEntity) level.getBlockEntity(southChest);
        ChestBlockEntity eastChestTE = (ChestBlockEntity) level.getBlockEntity(eastChest);
        ChestBlockEntity westChestTE = (ChestBlockEntity) level.getBlockEntity(westChest);
        if (level.getBlockState(northChest).getBlock() == Blocks.CHEST) {
            for (int i = 0; i < northChestTE.getContainerSize(); i++) {
                ItemStack stack = northChestTE.getItem(i);
                if (stack != ItemStack.EMPTY && stack.getCount() > 0) {
                    stack.shrink(1);
                }
            }

            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, northChest.getX(), northChest.getY() + 0.5D, northChest.getZ() ,2, 0.0D, 0.0D, 0.0D, 0);
            level.setBlock(northChest, Blocks.AIR.defaultBlockState(), 2);
        }
        if (level.getBlockState(southChest).getBlock() == Blocks.CHEST) {
            for (int i = 0; i < southChestTE.getContainerSize(); i++) {
                ItemStack stack = southChestTE.getItem(i);
                if (stack != ItemStack.EMPTY && stack.getCount() > 0) {
                    stack.shrink(1);
                }
            }
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, southChest.getX(), southChest.getY() + 0.5D, southChest.getZ(),2, 0.0D, 0.0D, 0.0D, 0);
            level.setBlock(southChest, Blocks.AIR.defaultBlockState(), 2);
        }
        if (level.getBlockState(eastChest).getBlock() == Blocks.CHEST) {
            for (int i = 0; i < eastChestTE.getContainerSize(); i++) {
                ItemStack stack = eastChestTE.getItem(i);
                if (stack != ItemStack.EMPTY && stack.getCount() > 0) {
                    stack.shrink(1);
                }
            }
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, eastChest.getX(), eastChest.getY() + 0.5D, eastChest.getZ(),2, 0.0D, 0.0D, 0.0D, 0);
            level.setBlock(eastChest, Blocks.AIR.defaultBlockState(), 2);
        }
        if (level.getBlockState(westChest).getBlock() == Blocks.CHEST) {
            for (int i = 0; i < westChestTE.getContainerSize(); i++) {
                ItemStack stack = westChestTE.getItem(i);
                if (stack != ItemStack.EMPTY && stack.getCount() > 0) {
                    stack.shrink(1);
                }
            }
            ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, westChest.getX(), westChest.getY() + 0.5D, westChest.getZ(),2, 0.0D, 0.0D, 0.0D, 0);
            level.setBlock(westChest, Blocks.AIR.defaultBlockState(),2);
        }
    }
}
