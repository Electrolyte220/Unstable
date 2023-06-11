package electrolyte.unstable.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import electrolyte.unstable.Unstable;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Path;

public record ReversingHoeDataGenerator(DataGenerator gen) implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public void run(CachedOutput pCache) {
        buildTransmutationBlock(pCache, Blocks.BLACK_CONCRETE, Blocks.BLACK_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.BLUE_CONCRETE, Blocks.BLUE_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.BROWN_CONCRETE, Blocks.BROWN_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE);
        buildTransmutationBlock(pCache, Blocks.COBBLESTONE, Blocks.STONE);
        buildTransmutationBlock(pCache, Blocks.CYAN_CONCRETE, Blocks.CYAN_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.DEAD_BRAIN_CORAL, Blocks.BRAIN_CORAL);
        buildTransmutationBlock(pCache, Blocks.DEAD_BRAIN_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK);
        buildTransmutationBlock(pCache, Blocks.DEAD_BRAIN_CORAL_FAN, Blocks.BRAIN_CORAL_FAN);
        buildTransmutationBlock(pCache, Blocks.DEAD_BUBBLE_CORAL, Blocks.BUBBLE_CORAL);
        buildTransmutationBlock(pCache, Blocks.DEAD_BUBBLE_CORAL_BLOCK, Blocks.BUBBLE_CORAL_BLOCK);
        buildTransmutationBlock(pCache, Blocks.DEAD_BUBBLE_CORAL_FAN, Blocks.BUBBLE_CORAL_FAN);
        buildTransmutationBlock(pCache, Blocks.DEAD_FIRE_CORAL, Blocks.FIRE_CORAL);
        buildTransmutationBlock(pCache, Blocks.DEAD_FIRE_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK);
        buildTransmutationBlock(pCache, Blocks.DEAD_FIRE_CORAL_FAN, Blocks.FIRE_CORAL_FAN);
        buildTransmutationBlock(pCache, Blocks.DEAD_HORN_CORAL, Blocks.HORN_CORAL);
        buildTransmutationBlock(pCache, Blocks.DEAD_HORN_CORAL_BLOCK, Blocks.HORN_CORAL_BLOCK);
        buildTransmutationBlock(pCache, Blocks.DEAD_HORN_CORAL_FAN, Blocks.HORN_CORAL_FAN);
        buildTransmutationBlock(pCache, Blocks.DEAD_TUBE_CORAL, Blocks.TUBE_CORAL);
        buildTransmutationBlock(pCache, Blocks.DEAD_TUBE_CORAL_BLOCK, Blocks.TUBE_CORAL_BLOCK);
        buildTransmutationBlock(pCache, Blocks.DEAD_TUBE_CORAL_FAN, Blocks.TUBE_CORAL_FAN);
        buildTransmutationBlock(pCache, Blocks.DIRT, Blocks.GRASS_BLOCK);
        buildTransmutationBlock(pCache, Blocks.GRAY_CONCRETE, Blocks.GRAY_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.GREEN_CONCRETE, Blocks.GREEN_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.LIGHT_BLUE_CONCRETE, Blocks.LIGHT_BLUE_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.LIGHT_GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.LIME_CONCRETE, Blocks.LIME_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.MAGENTA_CONCRETE, Blocks.MAGENTA_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.ORANGE_CONCRETE, Blocks.ORANGE_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.PINK_CONCRETE, Blocks.PINK_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.PURPLE_CONCRETE, Blocks.PURPLE_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.RED_CONCRETE, Blocks.RED_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_LOG);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_WOOD);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_LOG);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_WOOD);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_HYPHAE);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_STEM);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_LOG);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_WOOD);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_LOG);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_WOOD);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_LOG);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_WOOD);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_LOG);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_WOOD);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_HYPHAE);
        buildTransmutationBlock(pCache, Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_STEM);
        buildTransmutationBlock(pCache, Blocks.WHITE_CONCRETE, Blocks.WHITE_CONCRETE_POWDER);
        buildTransmutationBlock(pCache, Blocks.YELLOW_CONCRETE, Blocks.YELLOW_CONCRETE_POWDER);

        buildPropertyRegression(pCache, Blocks.BEETROOTS, "age");
        buildPropertyRegression(pCache, Blocks.CARROTS, "age");
        buildPropertyRegression(pCache, Blocks.COCOA, "age");
        buildPropertyRegression(pCache, Blocks.MELON_STEM, "age");
        buildPropertyRegression(pCache, Blocks.NETHER_WART, "age");
        buildPropertyRegression(pCache, Blocks.POTATOES, "age");
        buildPropertyRegression(pCache, Blocks.PUMPKIN_STEM, "age");
        buildPropertyRegression(pCache, Blocks.RESPAWN_ANCHOR, "charges");
        buildPropertyRegression(pCache, Blocks.SEA_PICKLE, "pickles");
        buildPropertyRegression(pCache, Blocks.SWEET_BERRY_BUSH, "age");
        buildPropertyRegression(pCache, Blocks.TURTLE_EGG, "eggs");
        buildPropertyRegression(pCache, Blocks.WATER_CAULDRON, "level");
        buildPropertyRegression(pCache, Blocks.WHEAT, "age");
    }

    private void buildTransmutationBlock(CachedOutput cache, Block input, Block output) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "unstable:transmutation");
        JsonObject blockObject = new JsonObject();
        blockObject.addProperty("block", ForgeRegistries.BLOCKS.getKey(input).toString());
        jsonObject.add("input", blockObject);
        jsonObject.addProperty("output", ForgeRegistries.BLOCKS.getKey(output).toString());
        buildTransmutationFile(cache, ForgeRegistries.BLOCKS.getKey(output).toString().substring(10), jsonObject);
    }

    private void buildTransmutationTag(CachedOutput cache, ResourceLocation input, Block output) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "unstable:transmutation");
        JsonObject tagObject = new JsonObject();
        tagObject.addProperty("tag", input.toString());
        jsonObject.add("input", tagObject);
        jsonObject.addProperty("output", ForgeRegistries.BLOCKS.getKey(output).toString());
        buildTransmutationFile(cache, output.getDescriptionId().toString().substring(10), jsonObject);
    }

    private void buildTransmutationFile(CachedOutput cache, String fileName, JsonObject obj) {
        Path file = this.gen.getOutputFolder().resolve("data/unstable/reversing_hoe/transmutation/" + fileName + ".json");
        try {
            DataProvider.saveStable(cache, GSON.toJsonTree(obj), file);
        } catch(IOException e) {
            Unstable.LOGGER.error("Error adding reversing hoe recipe {}", file, e);
        }
    }

    private void buildPropertyRegression(CachedOutput cache, Block block, String property) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "unstable:property_regression");
        jsonObject.addProperty("block", ForgeRegistries.BLOCKS.getKey(block).toString());
        jsonObject.addProperty("property", property);
        buildPropertyRegressionFile(cache, ForgeRegistries.BLOCKS.getKey(block).toString().substring(10), jsonObject);
    }

    private void buildPropertyRegressionFile(CachedOutput cache, String fileName, JsonObject obj) {
        Path file = this.gen.getOutputFolder().resolve("data/unstable/reversing_hoe/property_regression/" + fileName + ".json");
        try {
            DataProvider.saveStable(cache, GSON.toJsonTree(obj), file);
        } catch(IOException e) {
            Unstable.LOGGER.error("Error adding reversing hoe recipe {}", file, e);
        }
    }

    @Override
    public String getName() {
        return Unstable.MOD_ID + ":reversing_hoe_gen";
    }
}
