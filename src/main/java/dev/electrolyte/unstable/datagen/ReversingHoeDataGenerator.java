package dev.electrolyte.unstable.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.recipe.reversinghoe.PropertyRegressionRecipe;
import dev.electrolyte.unstable.recipe.reversinghoe.TransmutationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public record ReversingHoeDataGenerator(DataGenerator gen) implements DataProvider {

    @Override
    public CompletableFuture<?> run(CachedOutput pCache) {
        List<CompletableFuture<?>> list = new ArrayList<>();
        list.add(buildTransmutationBlock(pCache, Blocks.BLACK_CONCRETE, Blocks.BLACK_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.BLUE_CONCRETE, Blocks.BLUE_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.BROWN_CONCRETE, Blocks.BROWN_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE));
        list.add(buildTransmutationBlock(pCache, Blocks.COBBLESTONE, Blocks.STONE));
        list.add(buildTransmutationBlock(pCache, Blocks.CYAN_CONCRETE, Blocks.CYAN_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_FAN));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_FAN));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_FAN));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_FAN));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_BLOCK));
        list.add(buildTransmutationBlock(pCache, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_FAN));
        list.add(buildTransmutationBlock(pCache, Blocks.DIRT, Blocks.GRASS_BLOCK));
        list.add(buildTransmutationBlock(pCache, Blocks.GRAY_CONCRETE, Blocks.GRAY_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.GREEN_CONCRETE, Blocks.GREEN_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.LIGHT_BLUE_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.LIGHT_GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.LIME_CONCRETE, Blocks.LIME_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.MAGENTA_CONCRETE, Blocks.MAGENTA_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.ORANGE_CONCRETE, Blocks.ORANGE_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.PINK_CONCRETE, Blocks.PINK_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.PURPLE_CONCRETE, Blocks.PURPLE_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.RED_CONCRETE, Blocks.RED_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_LOG));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_WOOD));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_BAMBOO_BLOCK, Blocks.BAMBOO_BLOCK));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_LOG));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_WOOD));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_HYPHAE));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_STEM));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_LOG));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_WOOD));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_LOG));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_WOOD));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_LOG));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_WOOD));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_LOG));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_WOOD));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_HYPHAE));
        list.add(buildTransmutationBlock(pCache, Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_STEM));
        list.add(buildTransmutationBlock(pCache, Blocks.WHITE_CONCRETE, Blocks.WHITE_CONCRETE_POWDER));
        list.add(buildTransmutationBlock(pCache, Blocks.YELLOW_CONCRETE, Blocks.YELLOW_CONCRETE_POWDER));

        list.add(buildPropertyRegression(pCache, Blocks.BEETROOTS, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.CARROTS, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.COCOA, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.MELON_STEM, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.NETHER_WART, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.POTATOES, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.PUMPKIN_STEM, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.RESPAWN_ANCHOR, "charges"));
        list.add(buildPropertyRegression(pCache, Blocks.SEA_PICKLE, "pickles"));
        list.add(buildPropertyRegression(pCache, Blocks.SWEET_BERRY_BUSH, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.TORCHFLOWER_CROP, "age"));
        list.add(buildPropertyRegression(pCache, Blocks.TURTLE_EGG, "eggs"));
        list.add(buildPropertyRegression(pCache, Blocks.WATER_CAULDRON, "level"));
        list.add(buildPropertyRegression(pCache, Blocks.WHEAT, "age"));
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> buildTransmutationBlock(CachedOutput cache, Block input, Block output) {
        Path file = this.gen.getPackOutput().getOutputFolder().resolve("data/unstable/recipe/reversing_hoe/transmutation/" + BuiltInRegistries.BLOCK.getKey(output).getPath() + ".json");
        Optional<JsonElement> jsonResult = TransmutationRecipe.CODEC.encode(new TransmutationRecipe(BuiltInRegistries.BLOCK.getKey(input), BuiltInRegistries.BLOCK.getKey(output)), JsonOps.INSTANCE, null).result();
        return DataProvider.saveStable(cache, jsonResult.get(), file);
    }

    private CompletableFuture<?> buildPropertyRegression(CachedOutput cache, Block block, String property) {
        ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(block);
        Path file = this.gen.getPackOutput().getOutputFolder().resolve("data/unstable/recipe/reversing_hoe/property_regression/" + blockLocation.getPath() + ".json");
        Optional<JsonElement> jsonResult = PropertyRegressionRecipe.CODEC.encode(new PropertyRegressionRecipe(blockLocation, property), JsonOps.INSTANCE, null).result();
        return DataProvider.saveStable(cache, jsonResult.get(), file);
    }

    @Override
    public String getName() {
        return Unstable.MOD_ID + ":reversing_hoe_gen";
    }
}
