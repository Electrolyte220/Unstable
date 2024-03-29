package electrolyte.unstable.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import electrolyte.unstable.Unstable;
import electrolyte.unstable.datastorage.endsiege.ChestDataStorage;
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

import java.util.List;

public class EndSiegeCategory implements IRecipeCategory<ChestDataStorage> {

    public static final ResourceLocation PLUGIN_UID = new ResourceLocation(Unstable.MOD_ID, "end_siege");

    public static final RecipeType<ChestDataStorage> END_SIEGE_RECIPE_TYPE = RecipeType.create(Unstable.MOD_ID, "end_siege", ChestDataStorage.class);

    private final IDrawable background;
    private final IDrawable icon;

    public EndSiegeCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.BEACON));
        background = helper.createDrawable(new ResourceLocation("textures/gui/container/generic_54.png"), 3, 4, 170, 67);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("unstable.jei.end_siege.title");
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
    public ResourceLocation getUid() {
        return PLUGIN_UID;
    }

    @Override
    public Class<? extends ChestDataStorage> getRecipeClass() {
        return END_SIEGE_RECIPE_TYPE.getRecipeClass();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChestDataStorage recipe, IFocusGroup focuses) {
        int spaceBetweenItems = 18;
        int currInput = 0;
        for(Ingredient ingredient : recipe.chestContents()) {
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
                builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(Ingredient.of(ingredient.getItems()));
                currInput++;
        }
    }

    @Override
    public void draw(ChestDataStorage recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Minecraft.getInstance().font.draw(stack, new TranslatableComponent("unstable.jei.end_siege.chest_" + recipe.chestLocation().toString().toLowerCase()), (background.getWidth() - Minecraft.getInstance().font.width("unstable.jei.end_siege.chest_" + recipe.chestLocation().toString().toLowerCase())) + 57, 1, 0xFF808080);
    }

    @Override
    public List<Component> getTooltipStrings(ChestDataStorage recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if(mouseX >= 0 && mouseX <= 175) {
            if(mouseY >= 0 && mouseY <= 15) {
                return List.of(new TranslatableComponent("unstable.jei.end_siege.tooltip.chest_hover"));
            }
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}
