package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.UnstableEnums;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;

public class EndSiegeRecipe implements IUnstableEndSiegeRecipe {

    private final List<Map<UnstableEnums.NBT_TYPE, Ingredient>> contents;
    private final UnstableEnums.CHEST_LOCATION chestLocation;

    public EndSiegeRecipe(UnstableEnums.CHEST_LOCATION chestLocation, List<Map<UnstableEnums.NBT_TYPE, Ingredient>> contents) {
        this.chestLocation = chestLocation;
        this.contents = contents;
    }

    @Override
    public @Unmodifiable List<Map<UnstableEnums.NBT_TYPE, Ingredient>> getInputs() {
        return this.contents;
    }

    @Override
    public UnstableEnums.CHEST_LOCATION getLocation() {
        return this.chestLocation;
    }
}
