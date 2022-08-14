package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.listener.EndSiegeChestDataReloadListener;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nonnull;
import java.util.List;

public interface IUnstableEndSiegeRecipe {

    @Unmodifiable List<Ingredient> getInputs();

    @Nonnull EndSiegeChestDataReloadListener.CHEST_LOCATION getLocation();
}
