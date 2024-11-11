package dev.electrolyte.unstable.recipe.reversinghoe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.electrolyte.unstable.registry.ModRecipes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record PropertyRegressionRecipe(ResourceLocation block, String property) implements Recipe<RecipeInput> {

    @Override
    public boolean matches(RecipeInput recipeInput, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, Provider pRegistries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(Provider pRegistries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PROPERTY_REGRESSION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.PROPERTY_REGRESSION_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PropertyRegressionRecipe> {

        private static final MapCodec<PropertyRegressionRecipe> CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                        ResourceLocation.CODEC.fieldOf("block").forGetter(PropertyRegressionRecipe::block),
                        Codec.STRING.fieldOf("property").forGetter(PropertyRegressionRecipe::property)
                ).apply(i, PropertyRegressionRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, PropertyRegressionRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        private static PropertyRegressionRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            ResourceLocation input = ResourceLocation.STREAM_CODEC.decode(buf);
            String property = buf.readUtf();
            return new PropertyRegressionRecipe(input, property);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, PropertyRegressionRecipe recipe) {
            ResourceLocation.STREAM_CODEC.encode(buf, recipe.block);
            buf.writeUtf(recipe.property);
        }

        @Override
        public MapCodec<PropertyRegressionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PropertyRegressionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
