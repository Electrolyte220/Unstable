package dev.electrolyte.unstable.compat.jei;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.client.screen.MysticalTableScreen;
import dev.electrolyte.unstable.compat.jei.category.EndSiegeCategory;
import dev.electrolyte.unstable.compat.jei.category.MysticalTableCategory;
import dev.electrolyte.unstable.compat.jei.category.PropertyRegressionCategory;
import dev.electrolyte.unstable.compat.jei.category.TransmutationCategory;
import dev.electrolyte.unstable.menu.MysticalTableMenu;
import dev.electrolyte.unstable.registry.ModBlocks;
import dev.electrolyte.unstable.registry.ModItems;
import dev.electrolyte.unstable.registry.ModMenus;
import dev.electrolyte.unstable.registry.ModRecipes;
import dev.electrolyte.unstable.registry.ModTools;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

@JeiPlugin
public class UnstablePlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new EndSiegeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new TransmutationCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PropertyRegressionCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MysticalTableCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.BEACON), EndSiegeCategory.END_SIEGE_RECIPE_TYPE);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.DIVISION_SIGIL_ACTIVATED.get()), EndSiegeCategory.END_SIEGE_RECIPE_TYPE);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModTools.REVERSING_HOE.get()), TransmutationCategory.TRANSMUTATION_RECIPE_TYPE);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModTools.REVERSING_HOE.get()), PropertyRegressionCategory.PROPERTY_REGRESSION_RECIPE_TYPE);
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MYSTICAL_TABLE_ITEM.get()), MysticalTableCategory.MYSTICAL_TABLE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(EndSiegeCategory.END_SIEGE_RECIPE_TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipes.END_SIEGE_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(TransmutationCategory.TRANSMUTATION_RECIPE_TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipes.TRANSMUTATION_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(PropertyRegressionCategory.PROPERTY_REGRESSION_RECIPE_TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipes.PROPERTY_REGRESSION_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(MysticalTableCategory.MYSTICAL_TABLE_RECIPE_TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipes.MYSTICAL_TABLE_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(MysticalTableScreen.class, 88, 32, 28, 23, MysticalTableCategory.MYSTICAL_TABLE_RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(MysticalTableMenu.class, ModMenus.MYSTICAL_TABLE_MENU.get(), MysticalTableCategory.MYSTICAL_TABLE_RECIPE_TYPE, 1, 9, 10, 36);
    }
}
