package dev.electrolyte.unstable.datastorage.endsiege;

import dev.electrolyte.unstable.codecs.EntityEquipmentCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record EntityDataStorage(
        EntityType<?> entity,
        Optional<List<MobEffectInstance>> effects,
        Optional<EntityEquipmentCodec> equipment) {

    private static final ArrayList<EntityDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static ArrayList<EntityDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}
