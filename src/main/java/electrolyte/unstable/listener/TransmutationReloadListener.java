package electrolyte.unstable.listener;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import electrolyte.unstable.Unstable;
import electrolyte.unstable.datastorage.reversinghoe.TransmutationDataStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
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
                if(!inputObj.has("block") && !inputObj.has("tag")) {
                    Unstable.LOGGER.warn("Unable to find block or tag json object for transmutation recipe {}. This recipe will be ignored.", resourceLocation);
                    return;
                }
                if(!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(obj.get("output").getAsString()))) {
                    Unstable.LOGGER.warn("Unable to find block {} for transmutation recipe {}. This recipe will be ignored.", obj.get("output").getAsString(), resourceLocation);
                    return;
                }
                if(inputObj.has("block")) {
                    if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(inputObj.get("block").getAsString()))) {
                        Unstable.LOGGER.warn("Unable to find block {} for transmutation recipe {}. This recipe will be ignored.", inputObj.get("block").getAsString(), resourceLocation);
                        return;
                    } else {
                        TransmutationDataStorage.getMasterStorage().add(new TransmutationDataStorage(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(inputObj.get("block").getAsString())), ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.get("output").getAsString()))));
                    }
                }
                if(inputObj.has("tag")) {
                    TransmutationDataStorage.getMasterStorage().add(new TransmutationDataStorage(new ResourceLocation(inputObj.get("tag").getAsString()), ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.get("output").getAsString()))));
                }
            }
        });
        Unstable.LOGGER.info("Finished setting up Reversing Hoe Transmutation Recipes.");
    }
}
