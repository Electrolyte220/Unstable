package com.electrolyte.unstable.listener;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.datastorage.reversinghoe.PropertyRegressionDataStorage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class PropertyRegressionReloadListener extends SimpleJsonResourceReloadListener {

    public PropertyRegressionReloadListener(Gson gson, String folder) {
        super(gson, folder);
    }
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        PropertyRegressionDataStorage.getMasterStorage().clear();
        Unstable.LOGGER.info("Setting up Reversing Hoe Property Regression Recipes...");
        pObject.forEach((resourceLocation, jsonElement) -> {
            JsonObject obj = jsonElement.getAsJsonObject();
            if(!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(obj.get("block").getAsString()))) {
                Unstable.LOGGER.error("Unable to find block {} in the block registry. This property regression recipe will be ignored.", obj.get("block"));
            } else {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.get("block").getAsString()));
            if(!block.defaultBlockState().getProperties().contains(block.getStateDefinition().getProperty(obj.get("property").getAsString()))) {
                Unstable.LOGGER.error("Unable to find property {} for block {}. This property regression recipe will be ignored.", obj.get("property"), obj.get("block"));
            } else {
                PropertyRegressionDataStorage.getMasterStorage().add(new PropertyRegressionDataStorage(block, obj.get("property").getAsString()));
            }
            }
        });
        Unstable.LOGGER.info("Finished setting up Reversing Hoe Property Regression Recipes.");
    }
}
