package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.registry.ModBlocks;
import dev.electrolyte.unstable.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class UnstableBlockTagsProvider extends BlockTagsProvider {


    public UnstableBlockTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.CURSED_EARTH.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.MYSTICAL_TABLE.get());

        this.tag(ModTags.INCORRECT_FOR_UNSTABLE_TOOL);
    }
}
