package dev.electrolyte.unstable.recipe.reversinghoe;

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

public record TransmutationRecipe(ResourceLocation input, ResourceLocation output) implements Recipe<RecipeInput> {

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
        return ModRecipes.TRANSMUTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.TRANSMUTATION_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<TransmutationRecipe> {

        private static final MapCodec<TransmutationRecipe> CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                        ResourceLocation.CODEC.fieldOf("input").forGetter(TransmutationRecipe::input),
                        ResourceLocation.CODEC.fieldOf("output").forGetter(TransmutationRecipe::output)
                ).apply(i, TransmutationRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        private static TransmutationRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            ResourceLocation input = ResourceLocation.STREAM_CODEC.decode(buf);
            ResourceLocation output = ResourceLocation.STREAM_CODEC.decode(buf);
            return new TransmutationRecipe(input, output);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, TransmutationRecipe recipe) {
            ResourceLocation.STREAM_CODEC.encode(buf, recipe.input);
            ResourceLocation.STREAM_CODEC.encode(buf, recipe.output);
        }

        @Override
        public MapCodec<TransmutationRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TransmutationRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
