package electrolyte.unstable.listener;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import electrolyte.unstable.Unstable;
import electrolyte.unstable.datastorage.endsiege.EntityDataStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityDataReloadListener extends SimpleJsonResourceReloadListener {

    public EntityDataReloadListener(Gson gson, String folder) {
        super(gson, folder);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        EntityDataStorage.getMasterStorage().clear();
        Unstable.LOGGER.info("Setting up End Siege Entity Data...");
        object.forEach((resourceLocation, jsonElement) -> {
            String type = jsonElement.getAsJsonObject().get("type").getAsString();
            if(type.equals("unstable:entity_data")) {
                JsonArray entities = jsonElement.getAsJsonObject().get("entities").getAsJsonArray();
                JsonArray effects = jsonElement.getAsJsonObject().get("effects").getAsJsonArray();
                JsonArray equipment = jsonElement.getAsJsonObject().get("equipment").getAsJsonArray();
                List<EntityType<?>> entityList = validateAndConvertEntities(entities);
                List<MobEffectInstance> effectList = validateAndConvertEffects(effects);
                List<Map<EquipmentSlot, ItemStack>> equipmentList = validateAndConvertEquipment(equipment);
                entityList.forEach(entity -> EntityDataStorage.getMasterStorage().add(new EntityDataStorage(entity, effectList, equipmentList)));
            }
        });
        Unstable.LOGGER.info("Finished Setting up End Siege Entity Data.");
    }

    private List<EntityType<?>> validateAndConvertEntities(JsonArray input) {
        List<EntityType<?>> entities = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            if (!ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(input.get(i).getAsString()))) {
                Unstable.LOGGER.warn("Unable to find entity {} as it does not exist in the registry. This entity will not be added to the spawn list.", input.get(i).getAsString());
            } else {
                entities.add(ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(input.get(i).getAsString())));
            }
        }
        return entities;
    }

    private List<MobEffectInstance> validateAndConvertEffects(JsonArray input) {
        List<MobEffectInstance> mobEffects = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            JsonObject jsonObject = input.get(i).getAsJsonObject();
            if (!ForgeRegistries.MOB_EFFECTS.containsKey(new ResourceLocation(jsonObject.get("mobEffect").getAsString()))) {
                Unstable.LOGGER.warn("Unable to find mob effect {} as it does not exist in the registry. This effect will not be applied to entities.", jsonObject.get("mobEffect").getAsString());
            } else {
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(jsonObject.get("mobEffect").getAsString()));
                int amplifier = jsonObject.has("amplifier") ? jsonObject.get("amplifier").getAsInt() : 0;
                int duration = jsonObject.has("duration") ? jsonObject.get("duration").getAsInt() : 100;
                boolean ambient = !jsonObject.has("ambient") || jsonObject.get("ambient").getAsBoolean();
                boolean visible = !jsonObject.has("visible") || jsonObject.get("visible").getAsBoolean();
                mobEffects.add(new MobEffectInstance(effect, duration, amplifier, ambient, visible));
            }
        }
        return mobEffects;
    }

    private List<Map<EquipmentSlot, ItemStack>> validateAndConvertEquipment(JsonArray input) {
        List<Map<EquipmentSlot, ItemStack>> equipment = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            JsonObject jsonObject = input.get(i).getAsJsonObject();
            if (!ForgeRegistries.ITEMS.containsKey(new ResourceLocation(jsonObject.get("item").getAsString()))) {
                Unstable.LOGGER.warn("Unable to find item {} as it does not exist in the registry. This item will not be applied to entities.", input.get(i).getAsString());
            } else {
                EquipmentSlot slot = EquipmentSlot.valueOf(jsonObject.get("slot").getAsString());
                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(jsonObject.get("item").getAsString())));
                if(jsonObject.get("nbt") != null) {
                    try {
                        CompoundTag tag = NbtUtils.snbtToStructure(jsonObject.get("nbt").getAsString());
                        stack.setTag(tag);
                    } catch (CommandSyntaxException e) { Unstable.LOGGER.error("Unable to get nbt for item {}.", jsonObject.get("item").getAsString()); }
                }
                equipment.add(Map.of(slot, stack));
            }
        }
        return equipment;
    }
}
