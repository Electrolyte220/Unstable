package electrolyte.unstable.jei.category;

import electrolyte.unstable.Unstable;
import electrolyte.unstable.datastorage.reversinghoe.TransmutationDataStorage;
import electrolyte.unstable.init.ModTools;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TransmutationCategory implements IRecipeCategory<TransmutationDataStorage> {

    public static final ResourceLocation PLUGIN_UID = new ResourceLocation(Unstable.MOD_ID, "transmutation");
    public static final RecipeType<TransmutationDataStorage> TRANSMUTATION_RECIPE_TYPE = RecipeType.create(Unstable.MOD_ID, "transmutation", TransmutationDataStorage.class);

    private final IDrawable background;
    private final IDrawable icon;

    public TransmutationCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModTools.REVERSING_HOE.get()));
        background = helper.createDrawable(new ResourceLocation(Unstable.MOD_ID, "textures/gui/transmutation.png"), 4, 3, 168, 40);
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("unstable.jei.transmutation.title");
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
    public Class<? extends TransmutationDataStorage> getRecipeClass() {
        return TRANSMUTATION_RECIPE_TYPE.getRecipeClass();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TransmutationDataStorage recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 40, 12).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 12).addItemStack(new ItemStack(recipe.getOutput()));
    }
}
