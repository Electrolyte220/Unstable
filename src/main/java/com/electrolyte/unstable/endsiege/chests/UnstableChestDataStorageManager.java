package com.electrolyte.unstable.endsiege.chests;

import java.util.ArrayList;

public class UnstableChestDataStorageManager {
    private static final ArrayList<UnstableChestDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static void addEntries(UnstableChestDataStorage dataStorage) {
        MASTER_STORAGE.add(dataStorage);
    }

    public static ArrayList<UnstableChestDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}
