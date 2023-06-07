package com.electrolyte.unstable.datastorage.reversinghoe;

import net.minecraft.world.level.block.Block;

import java.util.ArrayList;

public record PropertyRegressionDataStorage(
        Block block,
        String property) {

    private static final ArrayList<PropertyRegressionDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static ArrayList<PropertyRegressionDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}
