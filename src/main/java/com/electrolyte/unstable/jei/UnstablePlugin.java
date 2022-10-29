package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.endsiege.chests.UnstableChestDataStorageManager;
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
        UnstableChestDataStorageManager.getMasterStorage().forEach(dataStorage -> {
            EndSiegeRecipe recipe = new EndSiegeRecipe(dataStorage.chestLocation(), dataStorage.chestContents());
            recipes.add(recipe);
        });
        registration.addRecipes(EndSiegeCategory.END_SIEGE_RECIPE_TYPE, recipes);
    }
}
