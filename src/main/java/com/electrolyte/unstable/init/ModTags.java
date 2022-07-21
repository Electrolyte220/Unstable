package com.electrolyte.unstable.init;

import com.electrolyte.unstable.Unstable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTags {

    //TODO: This is probably not needed either.
    public static final TagKey<Block> MINEABLE_WITH_EROSION_SHOVEL = ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation(Unstable.MOD_ID, "mineable/erosion_shovel"));

    //TODO: If this is not used anywhere, remove it.
    public static final TagKey<Item> UNSTABLE_INGOTS = ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation(Unstable.MOD_ID, "unstable_ingots"));
}
