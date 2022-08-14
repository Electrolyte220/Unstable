package com.electrolyte.unstable.endsiege;

import java.util.ArrayList;

public class UnstableEntityDataStorageManager {

    private static final ArrayList<UnstableEntityDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static void addEntries(UnstableEntityDataStorage dataStorage) {
        MASTER_STORAGE.add(dataStorage);
    }

    public static ArrayList<UnstableEntityDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}