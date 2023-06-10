package electrolyte.unstable.datastorage.endsiege;

import electrolyte.unstable.UnstableEnums;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record ChestDataStorage(
        UnstableEnums.CHEST_LOCATION chestLocation,
        List<Ingredient> chestContents) {
    private static final ArrayList<ChestDataStorage> MASTER_STORAGE = new ArrayList<>();

    public static ArrayList<ChestDataStorage> getMasterStorage() {
        return MASTER_STORAGE;
    }
}