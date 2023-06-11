package electrolyte.unstable.datagen;

import electrolyte.unstable.init.ModItems;
import electrolyte.unstable.init.ModTags;
import electrolyte.unstable.init.ModTools;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class UnstableTagDataGenerator extends ItemTagsProvider {
    
    protected UnstableTagDataGenerator(DataGenerator pGenerator, BlockTagsProvider pBlockTagsProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(pGenerator, pBlockTagsProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(ModTools.DESTRUCTION_PICKAXE.get());
        this.tag(Tags.Items.TOOLS_AXES).add(ModTools.HEALING_AXE.get());
        this.tag(Tags.Items.TOOLS_HOES).add(ModTools.REVERSING_HOE.get());
        this.tag(Tags.Items.TOOLS_PICKAXES).add(ModTools.DESTRUCTION_PICKAXE.get());
        this.tag(Tags.Items.TOOLS_SHOVELS).add(ModTools.EROSION_SHOVEL.get());
        this.tag(Tags.Items.TOOLS_SWORDS).add(ModTools.ETHERIC_SWORD.get());
        this.tag(Tags.Items.SHEARS).add(ModTools.PRECISION_SHEARS.get());

        this.tag(ModTags.COOKED_FISH).add(Items.COOKED_COD, Items.COOKED_SALMON);
        this.tag(ModTags.COOKED_MEAT).add(Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT);

        this.tag(ModTags.UNSTABLE_INGOTS).add(ModItems.UNSTABLE_INGOT.get(), ModItems.UNSTABLE_STABLE_INGOT.get());
    }
}
