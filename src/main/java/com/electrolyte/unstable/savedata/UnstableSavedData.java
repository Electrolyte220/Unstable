package com.electrolyte.unstable.savedata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class UnstableSavedData extends SavedData {

    private boolean isEndSiegeOccurring;
    private int playersParticipating;
    private int totalKills;
    private int[] startingLocation;

    private ListTag playersWithActivationSigil;

    @Nonnull
    public static UnstableSavedData get(Level level) {
        if(level.isClientSide) throw new RuntimeException("Don't access this from the client-side!");
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        return storage.computeIfAbsent(UnstableSavedData::new, UnstableSavedData::new, "unstable_end_siege_data");
    }

    public UnstableSavedData() {}

    public UnstableSavedData(CompoundTag tag) {
        this.isEndSiegeOccurring = tag.getBoolean("isEndSiegeOccurring");
        this.playersParticipating = tag.getInt("playersParticipating");
        this.totalKills = tag.getInt("totalKills");
        this.playersWithActivationSigil = tag.getList("playersWithActivationSigil",9);
        this.startingLocation = tag.getIntArray("startingLocation");
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        pCompoundTag.putBoolean("isEndSiegeOccurring", this.isEndSiegeOccurring);
        pCompoundTag.putInt("playersParticipating", this.playersParticipating);
        pCompoundTag.putInt("totalKills", this.totalKills);
        pCompoundTag.put("playersWithActivationSigil", this.playersWithActivationSigil);
        pCompoundTag.putIntArray("startingLocation", this.startingLocation);
        return pCompoundTag;
    }

    public boolean isEndSiegeOccurring() {
        return isEndSiegeOccurring;
    }

    public void setEndSiegeOccurring(boolean endSiegeOccurring) {
        isEndSiegeOccurring = endSiegeOccurring;
        this.setDirty();
    }

    public int getPlayersParticipating() {
        return playersParticipating;
    }

    public void setPlayersParticipating(int playersParticipating) {
        this.playersParticipating = playersParticipating;
        this.setDirty();
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
        this.setDirty();
    }

    public ListTag getPlayersWithActivationSigil() {
        return playersWithActivationSigil;
    }

    public void setPlayersWithActivationSigil(ListTag playersWithActivationSigil) {
        this.playersWithActivationSigil = playersWithActivationSigil;
        this.setDirty();
    }

    public int[] getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(int[] startingLocation) {
        this.startingLocation = startingLocation;
        this.setDirty();
    }

    public void resetData() {
        this.isEndSiegeOccurring = false;
        this.totalKills = 0;
        this.playersParticipating = 0;
        this.playersWithActivationSigil = new ListTag();
        this.startingLocation = new int[3];
        this.setDirty();
    }
}
