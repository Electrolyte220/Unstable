package com.electrolyte.unstable.listener;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.UnstableEnums;
import com.electrolyte.unstable.endsiege.UnstableChestDataStorage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EndSiegeChestDataReloadListener extends SimpleJsonResourceReloadListener {

    public EndSiegeChestDataReloadListener(Gson gson, String folder) {
        super(gson, folder);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        if(!UnstableChestDataStorage.getMasterStorage().isEmpty()) {
            UnstableChestDataStorage.getMasterStorage().clear();
        }
        Unstable.LOGGER.info("Setting up End Siege Chest Data...");
        pObject.forEach(((resourceLocation, jsonElement) -> {
            String jsonLocation = jsonElement.getAsJsonObject().get("location").getAsString();
            JsonArray jsonContents = jsonElement.getAsJsonObject().get("contents").getAsJsonArray();
            UnstableEnums.CHEST_LOCATION location = validateLocation(jsonLocation);
            if(!location.equals(UnstableEnums.CHEST_LOCATION.NONE)) {
                List<Map<UnstableEnums.NBT_TYPE, Ingredient>> contents = validateContents(jsonContents);
                UnstableChestDataStorage.addEntries(new UnstableChestDataStorage(location, contents));
            } else {
                Unstable.LOGGER.error("Location '{}' is not a valid location. Valid locations are: 'NORTH', 'SOUTH', 'EAST', or 'WEST'", location);
            }
        }));
        Unstable.LOGGER.info("Finished Setting up End Siege Chest Data.");
    }

    private UnstableEnums.CHEST_LOCATION validateLocation(String location) {
        if(Arrays.asList(Arrays.stream(UnstableEnums.CHEST_LOCATION.values()).toArray()).toString().contains(location.toUpperCase())) {
            return UnstableEnums.CHEST_LOCATION.valueOf(location.toUpperCase());
        } else {
            return UnstableEnums.CHEST_LOCATION.NONE;
        }
    }

    private UnstableEnums.NBT_TYPE validateType(String type) {
        if(Arrays.asList(Arrays.stream(UnstableEnums.NBT_TYPE.values()).toArray()).toString().contains(type)) {
            return UnstableEnums.NBT_TYPE.valueOf(type);
        } else {
            return UnstableEnums.NBT_TYPE.IGNORE_NBT;
        }
    }

    private List<Map<UnstableEnums.NBT_TYPE, Ingredient>> validateContents(JsonArray array) {
        List<Map<UnstableEnums.NBT_TYPE, Ingredient>> contents = new ArrayList<>();
        array.forEach(element -> {
            if(contents.size() < 27) {
                if (element.getAsJsonObject().get("item") != null) {
                    if(element.getAsJsonObject().get("nbt") != null &&
                            element.getAsJsonObject().get("nbtType") != null) {
                        UnstableEnums.NBT_TYPE nbtType = validateType(element.getAsJsonObject().get("nbtType").getAsString());
                        if (nbtType == UnstableEnums.NBT_TYPE.ALL_NBT) {
                            contents.add(Map.of(UnstableEnums.NBT_TYPE.ALL_NBT, NBTIngredient.Serializer.INSTANCE.parse(element.getAsJsonObject())));
                        } else if (nbtType == UnstableEnums.NBT_TYPE.PARTIAL_NBT) {
                            //Needed for a case of wanting to check an enchanted item for soley the enchantment, while ignoring the durability
                            contents.add(Map.of(UnstableEnums.NBT_TYPE.PARTIAL_NBT, PartialNBTIngredient.Serializer.INSTANCE.parse(element.getAsJsonObject())));
                        } else {
                            contents.add(Map.of(UnstableEnums.NBT_TYPE.IGNORE_NBT, Ingredient.fromJson(element.getAsJsonObject())));
                        }
                    } else {
                        contents.add(Map.of(UnstableEnums.NBT_TYPE.IGNORE_NBT, Ingredient.fromJson(element.getAsJsonObject())));
                    }
                } else if (element.getAsJsonObject().get("tag") != null) {
                    contents.add(Map.of(UnstableEnums.NBT_TYPE.IGNORE_NBT, Ingredient.fromJson(element.getAsJsonObject())));
                }
            } else {
                Unstable.LOGGER.warn("More than 27 itemstacks have been added to a chest! Only the first 27 items in the list will count!");
            }
        });
        return contents;
    }
}
