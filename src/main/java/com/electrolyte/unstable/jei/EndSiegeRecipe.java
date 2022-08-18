package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.listener.EndSiegeChestDataReloadListener;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class EndSiegeRecipe implements IUnstableEndSiegeRecipe {

    private final List<Ingredient> contents;
    private final EndSiegeChestDataReloadListener.CHEST_LOCATION chestLocation;

    public EndSiegeRecipe(EndSiegeChestDataReloadListener.CHEST_LOCATION chestLocation, List<Ingredient> contents) {
        this.chestLocation = chestLocation;
        this.contents = contents;
    }

    @Override
    public @Unmodifiable List<Ingredient> getInputs() {
        return this.contents;
    }

    @NotNull
    @Override
    public EndSiegeChestDataReloadListener.CHEST_LOCATION getLocation() {
        return this.chestLocation;
    }
}
