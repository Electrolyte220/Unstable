package electrolyte.unstable.init;

import electrolyte.unstable.Unstable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTags {
    public static final TagKey<Item> COOKED_FISH = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(Unstable.MOD_ID, "cooked_fish"));
    public static final TagKey<Item> COOKED_MEAT = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(Unstable.MOD_ID, "cooked_meat"));
    public static final TagKey<Item> UNSTABLE_INGOTS = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(Unstable.MOD_ID, "unstable_ingots"));
}
