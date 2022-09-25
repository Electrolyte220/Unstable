package com.electrolyte.unstable.helper;

import com.electrolyte.unstable.listener.EndSiegeChestDataReloadListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class PseudoInversionRitualHelper {

    public static boolean checkDimension(ResourceLocation dimension) {
        return dimension.equals(DimensionType.END_LOCATION.location());
    }

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
        return level.getBlockState(pos.north(5)).getBlock() == Blocks.CHEST && level.getBlockState(pos.south(5)).getBlock() == Blocks.CHEST &&
                level.getBlockState(pos.east(5)).getBlock() == Blocks.CHEST && level.getBlockState(pos.west(5)).getBlock() == Blocks.CHEST;
    }

    public static boolean checkChestContents(Level level, BlockPos pos, EndSiegeChestDataReloadListener.CHEST_LOCATION location) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof ChestBlockEntity) {
            ArrayList<ItemStack> chestItems = new ArrayList<>();
            for (int i = 0; i < 27; i++) {
                if (((ChestBlockEntity) te).getItem(i) != ItemStack.EMPTY) {
                    chestItems.add(((ChestBlockEntity) te).getItem(i));
                }
            }
            //Check if two stacks and their tags equal each other (for items like potions)
            for (int i = 0; i < chestItems.size(); i++) {
                ItemStack currentItem = chestItems.get(i);
                if (currentItem.getTag() != null) {
                    CompoundTag stackTag = currentItem.getTag();
                    for (int j = 0; j < chestItems.size(); j++) {
                        if (i != j) {
                            CompoundTag stackTag1 = chestItems.get(j).getTag();
                            if (currentItem.is(chestItems.get(j).getItem()) && stackTag.equals(stackTag1)) {
                                return false;
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < chestItems.size(); j++) {
                        if (i != j) {
                            if (currentItem.is(chestItems.get(j).getItem())) {
                                return false;
                            }
                        }
                    }
                }
            }
            List<Ingredient> validItems = EndSiegeChestDataReloadListener.CHEST_CONTENTS.get(location);
            if (validItems.size() != chestItems.size())
                return false;
            boolean prevItemFound = false;
            for (int i = 0; i < chestItems.size(); i++) {
                ItemStack chestStack = chestItems.get(i);
                if (!prevItemFound && i != 0)
                    return false;
                prevItemFound = false;
                for (Ingredient validItem : validItems) {
                    for (int k = 0; k < validItem.getItems().length; k++) {
                        ItemStack ingredientItem = validItem.getItems()[k];
                        if (chestStack.is(ingredientItem.getItem())) {
                            prevItemFound = true;
                            break;
                        }
                    }
                }
            }
            return prevItemFound;
        }
        return false;
    }

    public static void destroyBeaconAndChests(Level level, BlockPos pos) {
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
            level.setBlock(northChest, Blocks.AIR.defaultBlockState(), 2);
        }
        if (level.getBlockState(southChest).getBlock() == Blocks.CHEST) {
            for (int i = 0; i < southChestTE.getContainerSize(); i++) {
                ItemStack stack = southChestTE.getItem(i);
                if (stack != ItemStack.EMPTY && stack.getCount() > 0) {
                    stack.shrink(1);
                }
            }
            level.setBlock(southChest, Blocks.AIR.defaultBlockState(), 2);
        }
        if (level.getBlockState(eastChest).getBlock() == Blocks.CHEST) {
            for (int i = 0; i < eastChestTE.getContainerSize(); i++) {
                ItemStack stack = eastChestTE.getItem(i);
                if (stack != ItemStack.EMPTY && stack.getCount() > 0) {
                    stack.shrink(1);
                }
            }
            level.setBlock(eastChest, Blocks.AIR.defaultBlockState(), 2);
        }
        if (level.getBlockState(westChest).getBlock() == Blocks.CHEST) {
            for (int i = 0; i < westChestTE.getContainerSize(); i++) {
                ItemStack stack = westChestTE.getItem(i);
                if (stack != ItemStack.EMPTY && stack.getCount() > 0) {
                    stack.shrink(1);
                }
            }
            level.setBlock(westChest, Blocks.AIR.defaultBlockState(),2);
        }
        level.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 2.5F, Explosion.BlockInteraction.DESTROY);

        level.setBlock(northChest, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, northChest.getX(), northChest.getY(), northChest.getZ(), 2.5F, Explosion.BlockInteraction.DESTROY);

        level.setBlock(southChest, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, southChest.getX(), southChest.getY(), southChest.getZ(), 2.5F, Explosion.BlockInteraction.DESTROY);

        level.setBlock(eastChest, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, eastChest.getX(), eastChest.getY(), eastChest.getZ(), 2.5F, Explosion.BlockInteraction.DESTROY);

        level.setBlock(westChest, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, westChest.getX(), westChest.getY(), westChest.getZ(), 2.5F, Explosion.BlockInteraction.DESTROY);
    }

    public static void sendSiegeMessage(MutableComponent translateKey, Level level) {
        level.getServer().getLevel(Level.END).players().forEach(player -> player.displayClientMessage(translateKey, true));
    }
}
