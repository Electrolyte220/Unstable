package electrolyte.unstable.jei.category;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import electrolyte.unstable.Unstable;
import electrolyte.unstable.datastorage.reversinghoe.PropertyRegressionDataStorage;
import electrolyte.unstable.init.ModTools;
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
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class PropertyRegressionCategory implements IRecipeCategory<PropertyRegressionDataStorage> {

    public static final RecipeType<PropertyRegressionDataStorage> PROPERTY_REGRESSION_RECIPE_TYPE = RecipeType.create(Unstable.MOD_ID, "property_regression", PropertyRegressionDataStorage.class);
    private final IDrawable background;
    private final IDrawable icon;

    public PropertyRegressionCategory(IGuiHelper helper) {
        icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModTools.REVERSING_HOE.get()));
        background = helper.createDrawable(new ResourceLocation(Unstable.MOD_ID, "textures/gui/property_regression.png"), 4, 3, 168, 75);
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
    public RecipeType<PropertyRegressionDataStorage> getRecipeType() {
        return PROPERTY_REGRESSION_RECIPE_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PropertyRegressionDataStorage recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST).addItemStack(new ItemStack(recipe.block()));
    }

    @Override
    public void draw(PropertyRegressionDataStorage recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        int val = recipe.block().getStateDefinition().getProperty(recipe.property()).getPossibleValues().size() - 1;
        int minX = 0;
        int maxX = 0;
        boolean mouseInValidLocation = false;
        if(mouseX >= 3 && mouseX <= 68 && mouseY >= 0 && mouseY <= 75) {
            minX = 3;
            maxX = 68;
            mouseInValidLocation = true;
        } else if (mouseX >= 100 && mouseX <= 165 && mouseY >= 0 && mouseY <= 75) {
            minX = 100;
            maxX = 165;
            mouseInValidLocation = true;
        }
        if(mouseInValidLocation) {
            stack.pushPose();
            RenderSystem.disableDepthTest();
            RenderSystem.colorMask(true, true, true, false);
            GuiComponent.fill(stack, minX, 0, maxX, 75, 0x80FFFFFF);
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.enableDepthTest();
            stack.popPose();
        }
        stack.pushPose();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        BlockState state = recipe.block().defaultBlockState();
        stack.translate(0, 50, 0);
        stack.scale(50F, -50F, 50F);
        stack.mulPose(Vector3f.XP.rotationDegrees(45));
        stack.mulPose(Vector3f.YP.rotationDegrees(45));
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state.setValue((Property<Integer>) recipe.block().getStateDefinition().getProperty(recipe.property()), val), stack, buffer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
        stack.popPose();

        stack.pushPose();
        stack.translate(97, 50, 0);
        stack.scale(50F, -50F, 50F);
        stack.mulPose(Vector3f.XP.rotationDegrees(45));
        stack.mulPose(Vector3f.YP.rotationDegrees(45));
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state.setValue((Property<Integer>) recipe.block().getStateDefinition().getProperty(recipe.property()), val - 1), stack, buffer, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
        stack.popPose();
        buffer.endBatch();
       IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }

    @Override
    public List<Component> getTooltipStrings(PropertyRegressionDataStorage recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltips = new ArrayList<>();
        int val = recipe.block().getStateDefinition().getProperty(recipe.property()).getPossibleValues().size() - 1;
        if(mouseY >= 0 && mouseY <= 75) {
            if (mouseX >= 100 && mouseX <= 165) {
                val -=1;
            } else if(!(mouseX >= 3 && mouseX <= 68)){
                return List.of();
            }
            tooltips.add(Component.translatable(new ItemStack(recipe.block().asItem()).getHoverName().getString() + "[" + recipe.property() + "=" + (val) + "]"));
            if (Minecraft.getInstance().options.advancedItemTooltips) {
                tooltips.add(Component.translatable(ForgeRegistries.BLOCKS.getKey(recipe.block()).toString()).withStyle(ChatFormatting.DARK_GRAY));
            }
            tooltips.add(Component.translatable(ForgeRegistries.BLOCKS.getKey(recipe.block()).toString().substring(0, 1).toUpperCase() + ForgeRegistries.BLOCKS.getKey(recipe.block()).getNamespace().substring(1)).withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
            return tooltips;
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}
