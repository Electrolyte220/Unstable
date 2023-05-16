package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.datastorage.endsiege.ChestDataStorage;
import com.electrolyte.unstable.datastorage.reversinghoe.PropertyRegressionDataStorage;
import com.electrolyte.unstable.datastorage.reversinghoe.TransmutationDataStorage;
import com.electrolyte.unstable.init.ModItems;
import com.electrolyte.unstable.init.ModTools;
import com.electrolyte.unstable.jei.category.EndSiegeCategory;
import com.electrolyte.unstable.jei.category.PropertyRegressionCategory;
import com.electrolyte.unstable.jei.category.TransmutationCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

@JeiPlugin
public class UnstablePlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Unstable.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EndSiegeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new TransmutationCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PropertyRegressionCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.BEACON), EndSiegeCategory.END_SIEGE_RECIPE_TYPE);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.DIVISION_SIGIL_ACTIVATED.get()), EndSiegeCategory.END_SIEGE_RECIPE_TYPE);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModTools.REVERSING_HOE.get()), TransmutationCategory.TRANSMUTATION_RECIPE_TYPE);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModTools.REVERSING_HOE.get()), PropertyRegressionCategory.PROPERTY_REGRESSION_RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(EndSiegeCategory.END_SIEGE_RECIPE_TYPE, ChestDataStorage.getMasterStorage());
        registration.addRecipes(TransmutationCategory.TRANSMUTATION_RECIPE_TYPE, TransmutationDataStorage.getMasterStorage());
        registration.addRecipes(PropertyRegressionCategory.PROPERTY_REGRESSION_RECIPE_TYPE, PropertyRegressionDataStorage.getMasterStorage());
    }
}
