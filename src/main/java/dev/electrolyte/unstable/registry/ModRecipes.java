package dev.electrolyte.unstable.registry;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.recipe.MysticalTableRecipe;
import dev.electrolyte.unstable.recipe.endsiege.EndSiegeChestRecipe;
import dev.electrolyte.unstable.recipe.reversinghoe.PropertyRegressionRecipe;
import dev.electrolyte.unstable.recipe.reversinghoe.TransmutationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Unstable.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Unstable.MOD_ID);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
        RECIPE_TYPES.register(bus);
    }

    public static final Supplier<RecipeType<MysticalTableRecipe>> MYSTICAL_TABLE_RECIPE_TYPE = RECIPE_TYPES.register("mystical_table",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "mystical_table")));
    public static final Supplier<RecipeSerializer<MysticalTableRecipe>> MYSTICAL_TABLE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("mystical_table",
            MysticalTableRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<EndSiegeChestRecipe>> END_SIEGE_CHEST_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("end_siege_chest",
            EndSiegeChestRecipe.Serializer::new);
    public static final Supplier<RecipeType<EndSiegeChestRecipe>> END_SIEGE_RECIPE_TYPE = RECIPE_TYPES.register("end_siege_chest",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "end_siege_chest")));

    public static final Supplier<RecipeSerializer<TransmutationRecipe>> TRANSMUTATION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("transmutation",
            TransmutationRecipe.Serializer::new);
    public static final Supplier<RecipeType<TransmutationRecipe>> TRANSMUTATION_RECIPE_TYPE = RECIPE_TYPES.register("transmutation",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "transmutation")));

    public static final Supplier<RecipeSerializer<PropertyRegressionRecipe>> PROPERTY_REGRESSION_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("property_regression",
            PropertyRegressionRecipe.Serializer::new);
    public static final Supplier<RecipeType<PropertyRegressionRecipe>> PROPERTY_REGRESSION_RECIPE_TYPE = RECIPE_TYPES.register("property_regression",
            () -> RecipeType.simple(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "property_regression")));
}
