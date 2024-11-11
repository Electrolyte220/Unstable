package dev.electrolyte.unstable.compat.jei.category;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.recipe.MysticalTableRecipe;
import dev.electrolyte.unstable.registry.ModBlocks;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class MysticalTableCategory implements IRecipeCategory<MysticalTableRecipe> {

    public static final RecipeType<MysticalTableRecipe> MYSTICAL_TABLE_RECIPE_TYPE = RecipeType.create(Unstable.MOD_ID, "mystical_recipe", MysticalTableRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final ICraftingGridHelper craftingGridHelper;

    public MysticalTableCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(ResourceLocation.fromNamespaceAndPath(ModIds.JEI_ID, "textures/jei/gui/gui_vanilla.png"), 0, 60, 116, 54);
        this.icon = helper.createDrawableItemStack(new ItemStack(ModBlocks.MYSTICAL_TABLE.get()));
        this.craftingGridHelper = helper.createCraftingGridHelper();
    }

    @Override
    public RecipeType<MysticalTableRecipe> getRecipeType() {
        return MYSTICAL_TABLE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("unstable.container.mystical_table");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MysticalTableRecipe recipe, IFocusGroup focuses) {
        craftingGridHelper.createAndSetIngredients(builder, recipe.getIngredients(), recipe.getWidth(), recipe.getHeight());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }
}
