package com.electrolyte.unstable.listener;

import com.electrolyte.unstable.Unstable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EndSiegeChestDataReloadListener extends SimpleJsonResourceReloadListener {

    public enum CHEST_LOCATION {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        NONE
    }

    public static final Map<CHEST_LOCATION, List<Ingredient>> CHEST_CONTENTS = new HashMap<>();

    public EndSiegeChestDataReloadListener(Gson gson, String folder) {
        super(gson, folder);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        pObject.forEach(((resourceLocation, jsonElement) -> {
            String jsonLocation = jsonElement.getAsJsonObject().get("location").getAsString();
            JsonArray jsonContents = jsonElement.getAsJsonObject().get("contents").getAsJsonArray();
            CHEST_LOCATION location = validateLocation(jsonLocation);
            if(!location.equals(CHEST_LOCATION.NONE)) {
                List<Ingredient> contents = validateContents(jsonContents);
                CHEST_CONTENTS.put(location, contents);
            } else {
                Unstable.LOGGER.error("Location '{}' is not a valid location. Valid locations are: 'NORTH', 'SOUTH', 'EAST', or 'WEST'", location);
            }
        }));
    }

    private CHEST_LOCATION validateLocation(String location) {
        if(Arrays.asList(Arrays.stream(CHEST_LOCATION.values()).toArray()).toString().contains(location.toUpperCase())) {
            return CHEST_LOCATION.valueOf(location.toUpperCase());
        } else {
            return CHEST_LOCATION.NONE;
        }
    }

    private List<Ingredient> validateContents(JsonArray array) {
        List<Ingredient> contents = new ArrayList<>();
        array.forEach(element -> {
            if(contents.size() < 27) {
                if (element.getAsJsonObject().get("item") != null) {
                    if (element.getAsJsonObject().get("nbt") != null) {
                        Unstable.LOGGER.info(String.valueOf(element.getAsJsonObject()));
                        contents.add(PartialNBTIngredient.Serializer.INSTANCE.parse(element.getAsJsonObject()));
                    } else {
                        contents.add(Ingredient.fromJson(element.getAsJsonObject()));
                    }
                } else if (element.getAsJsonObject().get("tag") != null) {
                        contents.add(Ingredient.fromJson(element.getAsJsonObject()));
                }
            } else {
                Unstable.LOGGER.warn("More than 27 itemstacks have been added to a chest! Only the first 27 items in the list will count!");
            }
        });

        contents.forEach(content -> Unstable.LOGGER.warn(String.valueOf(content.toJson())));
        return contents;
    }
}
