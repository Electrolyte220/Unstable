package dev.electrolyte.unstable.recipe.endsiege;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.electrolyte.unstable.UnstableEnums.ChestLocation;
import dev.electrolyte.unstable.registry.ModRecipes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class EndSiegeChestRecipe implements Recipe<RecipeInput> {

    private final ChestLocation chestLocation;
    private final List<Ingredient> ingredients;
    private final boolean isSimple;

    public EndSiegeChestRecipe(ChestLocation chestLocation, List<Ingredient> ingredients) {
        this.chestLocation = chestLocation;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    public boolean matches(Container pContainer) {
        StackedContents stackedContents = new StackedContents();
        List<ItemStack> inputs = new ArrayList<>();
        int i = 0;
        for(int j = 0; j < pContainer.getContainerSize(); j++) {
            ItemStack itemStack = pContainer.getItem(j);
            if(!itemStack.isEmpty()) {
                i++;
                if(isSimple) {
                    stackedContents.accountStack(itemStack, 1);
                } else {
                    inputs.add(itemStack);
                }
            }
        }

        return i == this.ingredients.size() && (isSimple ? stackedContents.canCraft(this, null) : RecipeMatcher.findMatches(inputs, this.ingredients) != null);
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return true;
    }

    @Override
    public ItemStack assemble(RecipeInput pRecipeInput, Provider pRegistries) {
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
        return ModRecipes.END_SIEGE_CHEST_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.END_SIEGE_RECIPE_TYPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public ChestLocation getChestLocation() {
        return chestLocation;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.copyOf(ingredients);
    }

    public static class Serializer implements RecipeSerializer<EndSiegeChestRecipe> {
        private static final MapCodec<EndSiegeChestRecipe> CODEC = RecordCodecBuilder.mapCodec(
                i -> i.group(
                        StringRepresentable.fromEnum(ChestLocation::values).fieldOf("chest_location").forGetter(EndSiegeChestRecipe::getChestLocation),
                        Ingredient.CODEC_NONEMPTY.sizeLimitedListOf(27).fieldOf("ingredients").forGetter(EndSiegeChestRecipe::getIngredients)
                ).apply(i, EndSiegeChestRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, EndSiegeChestRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        private static EndSiegeChestRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            ChestLocation location = buf.readEnum(ChestLocation.class);
            int i = buf.readVarInt();
            NonNullList<Ingredient> list = NonNullList.withSize(i, Ingredient.EMPTY);
            list.replaceAll(j -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            return new EndSiegeChestRecipe(location, list);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, EndSiegeChestRecipe recipe) {
            buf.writeEnum(recipe.chestLocation);
            buf.writeVarInt(recipe.ingredients.size());
            for(Ingredient ing : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ing);
            }
        }

        @Override
        public MapCodec<EndSiegeChestRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EndSiegeChestRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
