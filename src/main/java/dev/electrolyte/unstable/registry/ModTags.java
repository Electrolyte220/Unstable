package dev.electrolyte.unstable.registry;


import dev.electrolyte.unstable.Unstable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Item> UNSTABLE_INGOTS = TagKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "unstable_ingots"));
    public static final TagKey<Block> INCORRECT_FOR_UNSTABLE_TOOL = TagKey.create(BuiltInRegistries.BLOCK.key(), ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "incorrect_for_unstable_tool"));
}
