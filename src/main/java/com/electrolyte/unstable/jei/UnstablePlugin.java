package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.listener.EndSiegeChestDataReloadListener;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class UnstablePlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(Unstable.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EndSiegeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.BEACON), EndSiegeCategory.PLUGIN_UID);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        List recipes = new ArrayList<>();
        EndSiegeChestDataReloadListener.CHEST_CONTENTS.forEach((chestLocation, ingredients) -> {
            EndSiegeRecipe recipe = new EndSiegeRecipe(chestLocation, ingredients);
            recipes.add(recipe);
        });
        registration.addRecipes(EndSiegeCategory.END_SIEGE_RECIPE_TYPE, recipes);
    }
}
