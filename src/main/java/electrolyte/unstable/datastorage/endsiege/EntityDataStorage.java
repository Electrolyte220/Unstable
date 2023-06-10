package electrolyte.unstable.datastorage.endsiege;

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
        List<Map<EquipmentSlot, ItemStack>> equipment) {

    private static final ArrayList<EntityDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static ArrayList<EntityDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}
