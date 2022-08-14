package com.electrolyte.unstable.init;

import com.electrolyte.unstable.Unstable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModTags {
    public static final TagKey<Item> COOKED_FISH = Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).createTagKey(new ResourceLocation(Unstable.MOD_ID, "cooked_fish"));
    public static final TagKey<Item> COOKED_MEAT = Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).createTagKey(new ResourceLocation(Unstable.MOD_ID, "cooked_meat"));
}
