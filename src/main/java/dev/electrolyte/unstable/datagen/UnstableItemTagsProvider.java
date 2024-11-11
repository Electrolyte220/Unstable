package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.registry.ModItems;
import dev.electrolyte.unstable.registry.ModTags;
import dev.electrolyte.unstable.registry.ModTools;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class UnstableItemTagsProvider extends ItemTagsProvider {

    public UnstableItemTagsProvider(PackOutput packOutput, CompletableFuture<Provider> provider, CompletableFuture<TagLookup<Block>> blockTagLookup, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, provider, blockTagLookup, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        this.tag(ItemTags.BREAKS_DECORATED_POTS).add(ModTools.DESTRUCTION_PICKAXE.get());
        this.tag(ItemTags.BREAKS_DECORATED_POTS).add(ModTools.EROSION_SHOVEL.get());
        this.tag(ItemTags.BREAKS_DECORATED_POTS).add(ModTools.ETHERIC_SWORD.get());
        this.tag(ItemTags.BREAKS_DECORATED_POTS).add(ModTools.HEALING_AXE.get());
        this.tag(ItemTags.BREAKS_DECORATED_POTS).add(ModTools.REVERSING_HOE.get());
        this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(ModTools.DESTRUCTION_PICKAXE.get());
        this.tag(ItemTags.AXES).add(ModTools.HEALING_AXE.get());
        this.tag(ItemTags.HOES).add(ModTools.REVERSING_HOE.get());
        this.tag(ItemTags.PICKAXES).add(ModTools.DESTRUCTION_PICKAXE.get());
        this.tag(ItemTags.SHOVELS).add(ModTools.EROSION_SHOVEL.get());
        this.tag(ItemTags.SWORDS).add(ModTools.ETHERIC_SWORD.get());
        this.tag(Tags.Items.TOOLS_SHEAR).add(ModTools.PRECISION_SHEARS.get());

        this.tag(ModTags.UNSTABLE_INGOTS).add(ModItems.UNSTABLE_INGOT.get(), ModItems.UNSTABLE_STABLE_INGOT.get());
    }
}

