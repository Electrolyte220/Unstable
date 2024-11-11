package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UnstableLootTableProvider extends BlockLootSubProvider {

    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        return new LootTableProvider(output, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(UnstableLootTableProvider::new, LootContextParamSets.BLOCK)), provider);
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.CURSED_EARTH.get(), b -> this.createSingleItemTableWithSilkTouch(b, Blocks.DIRT));
        this.add(ModBlocks.MYSTICAL_TABLE.get(), b -> this.createSingleItemTable(ModBlocks.MYSTICAL_TABLE.get()));
}

    protected UnstableLootTableProvider(Provider provider) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream().filter(e -> e.getLootTable().location().getNamespace().equals(Unstable.MOD_ID)).collect(Collectors.toList());
    }
}