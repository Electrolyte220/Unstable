package electrolyte.unstable.datagen;

import electrolyte.unstable.init.ModItems;
import electrolyte.unstable.init.ModTags;
import electrolyte.unstable.init.ModTools;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class UnstableItemTagsProvider extends ItemTagsProvider {
    
    protected UnstableItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, "unstable", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(ModTools.DESTRUCTION_PICKAXE.get());
        this.tag(ItemTags.AXES).add(ModTools.HEALING_AXE.get());
        this.tag(ItemTags.HOES).add(ModTools.REVERSING_HOE.get());
        this.tag(ItemTags.PICKAXES).add(ModTools.DESTRUCTION_PICKAXE.get());
        this.tag(ItemTags.SHOVELS).add(ModTools.EROSION_SHOVEL.get());
        this.tag(ItemTags.SWORDS).add(ModTools.ETHERIC_SWORD.get());
        this.tag(Tags.Items.SHEARS).add(ModTools.PRECISION_SHEARS.get());

        this.tag(ModTags.COOKED_FISH).add(Items.COOKED_COD, Items.COOKED_SALMON);
        this.tag(ModTags.COOKED_MEAT).add(Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT);

        this.tag(ModTags.UNSTABLE_INGOTS).add(ModItems.UNSTABLE_INGOT.get(), ModItems.UNSTABLE_STABLE_INGOT.get());
    }
}

