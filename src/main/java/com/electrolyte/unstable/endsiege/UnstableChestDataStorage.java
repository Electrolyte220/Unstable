package com.electrolyte.unstable.endsiege;

import com.electrolyte.unstable.UnstableEnums;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record UnstableChestDataStorage (
        UnstableEnums.CHEST_LOCATION chestLocation,
        List<Map<UnstableEnums.NBT_TYPE, Ingredient>> chestContents) {
    private static final ArrayList<UnstableChestDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static void addEntries(UnstableChestDataStorage dataStorage) {
        MASTER_STORAGE.add(dataStorage);
    }

    public static ArrayList<UnstableChestDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}