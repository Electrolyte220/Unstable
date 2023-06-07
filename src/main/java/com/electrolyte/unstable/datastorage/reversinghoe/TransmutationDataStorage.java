package com.electrolyte.unstable.datastorage.reversinghoe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;

public record TransmutationDataStorage(
        Ingredient input,
        ItemStack output) {

    private static final ArrayList<TransmutationDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static ArrayList<TransmutationDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}
