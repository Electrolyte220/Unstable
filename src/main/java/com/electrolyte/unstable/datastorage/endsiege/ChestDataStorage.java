package com.electrolyte.unstable.datastorage.endsiege;

import com.electrolyte.unstable.UnstableEnums;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record ChestDataStorage(
        UnstableEnums.CHEST_LOCATION chestLocation,
        List<Map<UnstableEnums.NBT_TYPE, Ingredient>> chestContents) {
    private static final ArrayList<ChestDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static ArrayList<ChestDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}