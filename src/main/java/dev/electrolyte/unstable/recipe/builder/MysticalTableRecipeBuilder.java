package dev.electrolyte.unstable.recipe.builder;

import dev.electrolyte.unstable.recipe.MysticalTableRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements.Strategy;
import net.minecraft.advancements.AdvancementRewards.Builder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.Map;
import java.util.Objects;

public class MysticalTableRecipeBuilder extends ShapedRecipeBuilder {
    public MysticalTableRecipeBuilder(ItemStack result) {
        super(RecipeCategory.MISC, result);
    }

    public static MysticalTableRecipeBuilder shaped(ItemStack result) {
        return new MysticalTableRecipeBuilder(result);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        ShapedRecipePattern recipePattern = this.ensureValid(id);
        Advancement.Builder builder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(Builder.recipe(id)).requirements(Strategy.OR);
        Map<String, Criterion<?>> map = this.criteria;
        Objects.requireNonNull(builder);
        map.forEach(builder::addCriterion);
        MysticalTableRecipe recipe = new MysticalTableRecipe(recipePattern, this.resultStack, false);
        recipeOutput.accept(id, recipe, builder.build(id.withPrefix("recipes/mystical_table/")));
    }
}
