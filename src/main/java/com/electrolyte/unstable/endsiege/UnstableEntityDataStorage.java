package com.electrolyte.unstable.endsiege;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record UnstableEntityDataStorage(
        EntityType<?> entity,
        int minCount,
        int maxCount,
        List<MobEffectInstance> effects,
        List<Map<InteractionHand, ItemStack>> equipment,
        List<Map<EquipmentSlot, ItemStack>> armor) {

    private static final ArrayList<UnstableEntityDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static void addEntries(UnstableEntityDataStorage dataStorage) {
        MASTER_STORAGE.add(dataStorage);
    }

    public static ArrayList<UnstableEntityDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}
