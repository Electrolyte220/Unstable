package electrolyte.unstable.init;

import electrolyte.unstable.Unstable;
import electrolyte.unstable.recipes.UnstableIngotRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Unstable.MOD_ID);

    public static void init() {
       RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<RecipeSerializer<?>> UNSTABLE_INGOT_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("unstable_ingot",
            UnstableIngotRecipe.Serializer::new);
}
