package com.electrolyte.unstable.listener;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.datastorage.reversinghoe.TransmutationDataStorage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class TransmutationReloadListener extends SimpleJsonResourceReloadListener {

    public TransmutationReloadListener(Gson gson, String folder) {
        super(gson, folder);
    }
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        TransmutationDataStorage.getMasterStorage().clear();
        Unstable.LOGGER.info("Setting up Reversing Hoe Transmutation Recipes...");
        pObject.forEach((resourceLocation, jsonElement) -> {
            String type = jsonElement.getAsJsonObject().get("type").getAsString();
            if(type.equals("unstable:transmutation")) {
            JsonObject obj = jsonElement.getAsJsonObject();
                JsonObject inputObj = obj.get("input").getAsJsonObject();
                ResourceLocation output = ResourceLocation.tryParse(obj.get("output").getAsString());
                TransmutationDataStorage.getMasterStorage().add(new TransmutationDataStorage(Ingredient.fromJson(inputObj), new ItemStack(ForgeRegistries.BLOCKS.getValue(output))));
            }
        });
        Unstable.LOGGER.info("Finished setting up Reversing Hoe Transmutation Recipes.");
    }
}
