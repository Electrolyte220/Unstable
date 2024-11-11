package dev.electrolyte.unstable.helper;


import dev.electrolyte.unstable.UnstableEnums.ChestLocation;
import dev.electrolyte.unstable.recipe.endsiege.EndSiegeChestRecipe;
import dev.electrolyte.unstable.registry.ModRecipes;
import dev.electrolyte.unstable.savedata.UnstableSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.List;
import java.util.UUID;

public class PseudoInversionRitualHelper {

    public static boolean checkBeacon(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == Blocks.BEACON;
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

    public static boolean checkChestsPos(Level level, BlockPos pos) {
        return level.getBlockState(pos.north(5)).getBlock() == Blocks.CHEST && level.getBlockState(pos.south(5)).getBlock() == Blocks.CHEST &&
                level.getBlockState(pos.east(5)).getBlock() == Blocks.CHEST && level.getBlockState(pos.west(5)).getBlock() == Blocks.CHEST;
    }

    public static boolean checkIndividualChestContents(Level level, BlockPos pos, ChestLocation location) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof ChestBlockEntity cbe) {
            List<RecipeHolder<EndSiegeChestRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.END_SIEGE_RECIPE_TYPE.get());
                for(RecipeHolder<EndSiegeChestRecipe> r : recipes) {
                    if(r.value().getChestLocation() == location) {
                        if(!r.value().matches(cbe)) {
                            return false;
                        }
                    }
                }
        }
        return true;
    }

    public static boolean checkAllChestContents(Level level, BlockPos pos) {
        return PseudoInversionRitualHelper.checkIndividualChestContents(level, pos.below().north(5), ChestLocation.NORTH) &&
                PseudoInversionRitualHelper.checkIndividualChestContents(level, pos.below().south(5), ChestLocation.SOUTH) &&
                PseudoInversionRitualHelper.checkIndividualChestContents(level, pos.below().east(5), ChestLocation.EAST) &&
                PseudoInversionRitualHelper.checkIndividualChestContents(level, pos.below().west(5), ChestLocation.WEST);
    }

    public static void destroyBeaconAndChests(Level level, BlockPos pos) {
        BlockPos northChest = pos.north(5);
        BlockPos southChest = pos.south(5);
        BlockPos eastChest = pos.east(5);
        BlockPos westChest = pos.west(5);

        ChestBlockEntity northChestTE = (ChestBlockEntity) level.getBlockEntity(northChest);
        ChestBlockEntity southChestTE = (ChestBlockEntity) level.getBlockEntity(southChest);
        ChestBlockEntity eastChestTE = (ChestBlockEntity) level.getBlockEntity(eastChest);
        ChestBlockEntity westChestTE = (ChestBlockEntity) level.getBlockEntity(westChest);

        northChestTE.clearContent();
        southChestTE.clearContent();
        eastChestTE.clearContent();
        westChestTE.clearContent();

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 2.5F, Level.ExplosionInteraction.BLOCK);

        level.setBlock(northChest, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, northChest.getX(), northChest.getY(), northChest.getZ(), 2.5F, Level.ExplosionInteraction.BLOCK);

        level.setBlock(southChest, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, southChest.getX(), southChest.getY(), southChest.getZ(), 2.5F, Level.ExplosionInteraction.BLOCK);

        level.setBlock(eastChest, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, eastChest.getX(), eastChest.getY(), eastChest.getZ(), 2.5F, Level.ExplosionInteraction.BLOCK);

        level.setBlock(westChest, Blocks.AIR.defaultBlockState(), 2);
        level.explode(null, westChest.getX(), westChest.getY(), westChest.getZ(), 2.5F, Level.ExplosionInteraction.BLOCK);
    }

    public static void sendSiegeMessage(MutableComponent translateKey, PlayerList list, UnstableSavedData data) {
        data.getPlayersParticipating().forEach(uuid -> {
            ServerPlayer player = list.getPlayer(UUID.fromString(uuid.getAsString()));
            if(player != null) {
                player.displayClientMessage(translateKey, true);
            }
        });
    }
}
