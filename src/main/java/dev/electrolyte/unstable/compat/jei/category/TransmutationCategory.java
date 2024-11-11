package dev.electrolyte.unstable.compat.jei.category;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.recipe.reversinghoe.TransmutationRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;

public class TransmutationCategory implements IRecipeCategory<TransmutationRecipe> {

    public static final RecipeType<TransmutationRecipe> TRANSMUTATION_RECIPE_TYPE = RecipeType.create(Unstable.MOD_ID, "transmutation", TransmutationRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public TransmutationCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModTools.REVERSING_HOE.get()));
        background = helper.createDrawable(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "textures/gui/transmutation.png"), 5, 3, 73, 40);
    }

    @Override
    public Component getTitle() {
        return Component.translatable("unstable.jei.transmutation.title");
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
    public RecipeType<TransmutationRecipe> getRecipeType() {
        return TRANSMUTATION_RECIPE_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TransmutationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 12).addIngredients(Ingredient.of(BuiltInRegistries.ITEM.get(recipe.input())));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 54, 12).addItemStack(new ItemStack(BuiltInRegistries.ITEM.get(recipe.output())));
    }
}
