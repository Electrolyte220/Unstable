package com.electrolyte.unstable.datastorage.endsiege;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record EntityDataStorage(
        EntityType<?> entity,
        List<MobEffectInstance> effects,
        List<Map<InteractionHand, ItemStack>> equipment,
        List<Map<EquipmentSlot, ItemStack>> armor) {

    private static final ArrayList<EntityDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static void addEntries(EntityDataStorage dataStorage) {
        MASTER_STORAGE.add(dataStorage);
    }

    public static ArrayList<EntityDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}
