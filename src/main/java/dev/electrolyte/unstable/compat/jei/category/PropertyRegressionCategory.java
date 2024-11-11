
package dev.electrolyte.unstable.compat.jei.category;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.recipe.reversinghoe.PropertyRegressionRecipe;
import dev.electrolyte.unstable.registry.ModTools;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class PropertyRegressionCategory implements IRecipeCategory<PropertyRegressionRecipe> {

    public static final RecipeType<PropertyRegressionRecipe> PROPERTY_REGRESSION_RECIPE_TYPE = RecipeType.create(Unstable.MOD_ID, "property_regression", PropertyRegressionRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public PropertyRegressionCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModTools.REVERSING_HOE.get()));
        background = helper.createDrawable(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "textures/gui/transmutation.png"), 5, 3, 73, 40);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("unstable.jei.property_regression.title");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public RecipeType<PropertyRegressionRecipe> getRecipeType() {
        return PROPERTY_REGRESSION_RECIPE_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PropertyRegressionRecipe recipe, IFocusGroup focuses) {
        ItemStack blockStack = new ItemStack(BuiltInRegistries.BLOCK.get(recipe.block()));
        int val = BuiltInRegistries.BLOCK.get(recipe.block()).getStateDefinition().getProperty(recipe.property()).getPossibleValues().size() - 1;
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 4, 12).addItemStack(blockStack).addRichTooltipCallback((v, i) -> i.add(Component.translatable(String.format("Changed Property: [%s=%s]", recipe.property(), val))));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 54, 12).addItemStack(blockStack).addRichTooltipCallback((v, i) -> i.add(Component.translatable(String.format("Changed Property: [%s=%s]", recipe.property(), val - 1))));
    }
}
