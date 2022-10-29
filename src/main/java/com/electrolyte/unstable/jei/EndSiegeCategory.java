package com.electrolyte.unstable.jei;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.UnstableEnums;
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
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

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
        int currInput = 0;
        for(Map<UnstableEnums.NBT_TYPE, Ingredient> ingredientMap : recipe.getInputs()) {
            int x = 5 + spaceBetweenItems * currInput;
            int y = 14;
                if(currInput > 8 && currInput < 18) {
                    x = 5 + spaceBetweenItems * (currInput - 9);
                    y = 32;
                }
                else if (currInput > 17 && currInput < 27) {
                    x = 5 + spaceBetweenItems * (currInput - 18);
                    y = 50;
                }
            Map.Entry<UnstableEnums.NBT_TYPE, Ingredient> ingredientEntry = ingredientMap.entrySet().stream().toList().get(0);
            if(ingredientEntry.getKey() == UnstableEnums.NBT_TYPE.ALL_NBT) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(Ingredient.of(ingredientEntry.getValue().getItems())).addTooltipCallback((recipeSlotView, tooltip) -> {
                        tooltip.add(new TranslatableComponent("unstable.jei.tooltip.all_nbt").withStyle(ChatFormatting.RED));
                        if(!Screen.hasShiftDown()) {
                            tooltip.add(new TranslatableComponent("unstable.jei.tooltip.shift_nbt"));
                        } else {
                            tooltip.add(new TranslatableComponent(ingredientEntry.getValue().getItems()[0].getTag().getAsString()).withStyle(ChatFormatting.GRAY));
                        }
                    });
                } else if(ingredientEntry.getKey() == UnstableEnums.NBT_TYPE.PARTIAL_NBT) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(Ingredient.of(ingredientEntry.getValue().getItems())).addTooltipCallback((recipeSlotView, tooltip) -> {
                        tooltip.add(new TranslatableComponent("unstable.jei.tooltip.partial_nbt").withStyle(ChatFormatting.RED));
                        if(!Screen.hasShiftDown()) {
                            tooltip.add(new TranslatableComponent("unstable.jei.tooltip.shift_nbt"));
                        } else {
                            PartialNBTIngredient entry = (PartialNBTIngredient) ingredientEntry.getValue();
                            String tag = entry.toJson().getAsJsonObject().get("nbt").getAsString();
                            tooltip.add(new TranslatableComponent(tag).withStyle(ChatFormatting.GRAY));
                        }
                    });
                } else {
                    builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(Ingredient.of(ingredientEntry.getValue().getItems()));
                }
                currInput++;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(@NotNull IUnstableEndSiegeRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
        Minecraft.getInstance().font.draw(stack, new TranslatableComponent("unstable.jei.chest_" + recipe.getLocation().toString().toLowerCase()), (background.getWidth() - Minecraft.getInstance().font.width("unstable.jei.chest_" + recipe.getLocation().toString().toLowerCase())) + 5, 1, 0xFF808080);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Component> getTooltipStrings(@NotNull IUnstableEndSiegeRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if(mouseX >= 0 && mouseX <= 175) {
            if(mouseY >= 0 && mouseY <= 15) {
                return List.of(new TranslatableComponent("unstable.jei.tooltip.chest_hover"));
            }
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}
