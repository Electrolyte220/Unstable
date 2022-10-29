package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.UnstableEnums;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;

public interface IUnstableEndSiegeRecipe {

    @Unmodifiable List<Map<UnstableEnums.NBT_TYPE, Ingredient>> getInputs();

    UnstableEnums.CHEST_LOCATION getLocation();
}
