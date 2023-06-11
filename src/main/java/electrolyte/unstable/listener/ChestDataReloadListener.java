package electrolyte.unstable.listener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import electrolyte.unstable.Unstable;
import electrolyte.unstable.UnstableEnums;
import electrolyte.unstable.datastorage.endsiege.ChestDataStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ChestDataReloadListener extends SimpleJsonResourceReloadListener {

    public ChestDataReloadListener(Gson gson, String folder) {
        super(gson, folder);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ChestDataStorage.getMasterStorage().clear();
        Unstable.LOGGER.info("Setting up End Siege Chest Data...");
        pObject.forEach(((resourceLocation, jsonElement) -> {
            String type = jsonElement.getAsJsonObject().get("type").getAsString();
            if(type.equals("unstable:chest_data")) {
                String jsonLocation = jsonElement.getAsJsonObject().get("location").getAsString();
                JsonArray jsonContents = jsonElement.getAsJsonObject().get("contents").getAsJsonArray();
                UnstableEnums.CHEST_LOCATION location = validateLocation(jsonLocation);
                if (!location.equals(UnstableEnums.CHEST_LOCATION.NONE)) {
                    List<Ingredient> contents = validateContents(jsonContents);
                    ChestDataStorage.getMasterStorage().add(new ChestDataStorage(location, contents));
                } else {
                    Unstable.LOGGER.error("Location '{}' is not a valid location. Valid locations are: 'NORTH', 'SOUTH', 'EAST', or 'WEST'", location);
                }
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

    private List<Ingredient> validateContents(JsonArray array) {
        List<Ingredient> contents = new ArrayList<>();
        array.forEach(element -> {
            if(contents.size() > 27) {
                Unstable.LOGGER.warn("More than 27 itemstacks have been added to a chest! Only the first 27 items in the list will count!");
            }
            if (element.getAsJsonObject().get("item") != null) {
                if(element.getAsJsonObject().get("nbt") != null) {
                    contents.add(StrictNBTIngredient.Serializer.INSTANCE.parse(element.getAsJsonObject()));
                } else {
                    contents.add(Ingredient.fromJson(element.getAsJsonObject()));
                }
            } else if (element.getAsJsonObject().get("tag") != null) {
                if(element.getAsJsonObject().get("nbt") != null) {
                    Unstable.LOGGER.warn("Due to a vanilla minecraft limitation, NBT on tags is ignored. This applies to the following: {} on tag {}", element.getAsJsonObject().get("nbt"), element.getAsJsonObject().get("tag"));
                } else {
                    contents.add(Ingredient.fromJson(element.getAsJsonObject()));
                }
            }
        });
        return contents;
    }
}
