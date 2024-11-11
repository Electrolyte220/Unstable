package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

class UnstableBlockStateProvider extends BlockStateProvider {

    public UnstableBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(ModBlocks.CURSED_EARTH.get(), new UncheckedModelFile("block/grass_block"));
        this.simpleBlock(ModBlocks.MYSTICAL_TABLE.get(), new UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "block/mystical_table")));
    }
}

class UnstableBlockModelProvider extends BlockModelProvider {

    public UnstableBlockModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("cursed_earth", "block/grass_block");
        this.withExistingParent("mystical_table", "block/enchanting_table")
                .texture("side", ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "block/mystical_table_side"))
                .texture("bottom", ResourceLocation.withDefaultNamespace("block/crying_obsidian"));
    }
}

class UnstableItemModelProvider extends ItemModelProvider {

    public UnstableItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("cursed_earth", ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "block/cursed_earth")).renderType("cutout_mipped");
        this.withExistingParent("mystical_table", ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "block/mystical_table"));
        this.withExistingParent("destruction_pickaxe", "item/handheld")
                .texture("layer0", modLoc("item/destruction_pickaxe_0"))
                .texture("layer1", modLoc("item/destruction_pickaxe_1"));
        this.withExistingParent("healing_axe", "item/handheld")
                .texture("layer0", modLoc("item/healing_axe_0"))
                .texture("layer1", modLoc("item/healing_axe_1"));
        this.withExistingParent("erosion_shovel", "item/handheld")
                .texture("layer0", modLoc("item/erosion_shovel_0"))
                .texture("layer1", modLoc("item/erosion_shovel_1"));
        this.withExistingParent("etheric_sword", "item/handheld")
                .texture("layer0", modLoc("item/etheric_sword_0"))
                .texture("layer1", modLoc("item/etheric_sword_1"));
        this.withExistingParent("reversing_hoe", "item/handheld")
                .texture("layer0", modLoc("item/reversing_hoe_0"))
                .texture("layer1", modLoc("item/reversing_hoe_1"));
        this.withExistingParent("precision_shears", "item/handheld")
                .texture("layer0", modLoc("item/precision_shears_0"))
                .texture("layer1", modLoc("item/precision_shears_1"));

        this.withExistingParent("division_sigil", "item/generated")
                .texture("layer0", modLoc("item/division_sigil"));
        this.withExistingParent("division_sigil_activated", "item/generated")
                .texture("layer0", modLoc("item/division_sigil"));
        this.withExistingParent("division_sigil_stable", "item/generated")
                .texture("layer0", modLoc("item/division_sigil"));
        this.withExistingParent("soul_fragment", "item/generated")
                .texture("layer0", modLoc("item/soul_fragment"));
        this.withExistingParent("unstable_ingot", "item/generated")
                .texture("layer0", modLoc("item/unstable_ingot"));
        this.withExistingParent("unstable_stable_ingot", "item/generated")
                .texture("layer0", modLoc("item/unstable_ingot"));
        this.withExistingParent("stable_ingot", "item/generated")
                .texture("layer0", modLoc("item/unstable_ingot"));
    }
}
