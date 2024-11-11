package dev.electrolyte.unstable.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.electrolyte.unstable.registry.ModRecipes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

public class MysticalTableRecipe extends ShapedRecipe {

    public MysticalTableRecipe(ShapedRecipePattern pRecipePattern, ItemStack pResult, boolean pShowNotification) {
        super("", CraftingBookCategory.MISC, pRecipePattern, pResult, pShowNotification);
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MYSTICAL_TABLE_RECIPE_TYPE.get();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MYSTICAL_TABLE_RECIPE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<MysticalTableRecipe> {

        public static final MapCodec<MysticalTableRecipe> CODEC = RecordCodecBuilder.mapCodec(
                p -> p.group(
                        ShapedRecipePattern.MAP_CODEC.forGetter(g -> g.pattern),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(g -> g.result),
                        Codec.BOOL.optionalFieldOf("show_notification", Boolean.valueOf(true)).forGetter(g -> g.showNotification)
                        ).apply(p, MysticalTableRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, MysticalTableRecipe> STREAM_CODEC = StreamCodec.of(
                MysticalTableRecipe.Serializer::toNetwork, MysticalTableRecipe.Serializer::fromNetwork);


        @Override
        public MapCodec<MysticalTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MysticalTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static MysticalTableRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
            ShapedRecipePattern pattern = ShapedRecipePattern.STREAM_CODEC.decode(buf);
            ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
            boolean showNotification = buf.readBoolean();
            return new MysticalTableRecipe(pattern, stack, showNotification);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buf, MysticalTableRecipe recipe) {
            ShapedRecipePattern.STREAM_CODEC.encode(buf, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buf, recipe.result);
            buf.writeBoolean(recipe.showNotification);
        }
    }
}
