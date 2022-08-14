package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.Unstable;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class EndSiegeCategory implements IRecipeCategory<IUnstableEndSiegeRecipe> {

    public static final ResourceLocation PLUGIN_UID = new ResourceLocation(Unstable.MOD_ID, "end_siege");

    public static final RecipeType<? extends IUnstableEndSiegeRecipe> END_SIEGE_RECIPE_TYPE = RecipeType.create(Unstable.MOD_ID, "end_siege", IUnstableEndSiegeRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public EndSiegeCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.BEACON));
        background = helper.createDrawable(new ResourceLocation("textures/gui/container/generic_54.png"), 3, 4, 170, 67);
    }

    @Override
    public @NotNull Component getTitle() {
        return new TranslatableComponent("unstable.jei.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public ResourceLocation getUid() {
        return PLUGIN_UID;
    }

    @Override
    public Class<? extends IUnstableEndSiegeRecipe> getRecipeClass() {
        return END_SIEGE_RECIPE_TYPE.getRecipeClass();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull IUnstableEndSiegeRecipe recipe, @NotNull IFocusGroup focuses) {
        int spaceBetweenItems = 18;
        for(int currInput = 0; currInput < recipe.getInputs().size(); currInput++) {
            if(currInput < 9) {
                builder.addSlot(RecipeIngredientRole.INPUT, 5 + spaceBetweenItems * currInput, 14).addIngredients(Ingredient.of(recipe.getInputs().get(currInput).getItems()));
            } else if (currInput < 18) {
                builder.addSlot(RecipeIngredientRole.INPUT, 5 + spaceBetweenItems * (currInput - 9), 32).addIngredients(Ingredient.of(recipe.getInputs().get(currInput).getItems()));
            } else if (currInput < 27) {
                builder.addSlot(RecipeIngredientRole.INPUT, 5 + spaceBetweenItems * (currInput - 18), 50).addIngredients(Ingredient.of(recipe.getInputs().get(currInput).getItems()));
            }
        }
    }

    @Override
    public void draw(@NotNull IUnstableEndSiegeRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
        Minecraft.getInstance().font.draw(stack, new TranslatableComponent("unstable.jei.chest_hover_" + recipe.getLocation().toString().toLowerCase()), (background.getWidth() - Minecraft.getInstance().font.width("unstable.jei.chest_hover_" + recipe.getLocation().toString().toLowerCase())) + 44, 1, 0xFF808080);
    }
}
