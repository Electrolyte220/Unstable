package com.electrolyte.unstable.listener;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.endsiege.UnstableEntityDataStorage;
import com.electrolyte.unstable.endsiege.UnstableEntityDataStorageManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityDataReloadListener extends SimpleJsonResourceReloadListener {

    public EntityDataReloadListener(Gson gson, String folder) {
        super(gson, folder);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        if(!UnstableEntityDataStorageManager.getMasterStorage().isEmpty()) {
            UnstableEntityDataStorageManager.getMasterStorage().clear();
        }
        Unstable.LOGGER.info("Setting up End Siege Entity Data...");
        object.forEach((location, jsonElement) -> {
            JsonArray spawnCount = jsonElement.getAsJsonObject().get("spawnCount").getAsJsonArray();
            JsonArray entities = jsonElement.getAsJsonObject().get("entities").getAsJsonArray();
            JsonArray effects = jsonElement.getAsJsonObject().get("effects").getAsJsonArray();
            JsonArray equipment = jsonElement.getAsJsonObject().get("equipment").getAsJsonArray();
            JsonArray armor = jsonElement.getAsJsonObject().get("armor").getAsJsonArray();
            int[] spawnCountArray = validateSpawnCount(spawnCount);
            List<EntityType<?>> entityList = validateAndConvertEntities(entities);
            List<MobEffectInstance> effectList = validateAndConvertEffects(effects);
            List<Map<InteractionHand, ItemStack>> equipmentList = validateAndConvertEquipment(equipment);
            List<Map<EquipmentSlot, ItemStack>> armorList = validateAndConvertArmor(armor);
            entityList.forEach(entity -> UnstableEntityDataStorageManager.addEntries(new UnstableEntityDataStorage(entity, spawnCountArray[0], spawnCountArray[1], effectList, equipmentList, armorList)));
        });
        Unstable.LOGGER.info("Finished Setting up End Siege Entity Data.");
    }

    private int[] validateSpawnCount(JsonArray input) {
        int[] spawnCount = new int[2];
        if(input.getAsJsonArray().get(0).getAsJsonObject().get("min") == null) spawnCount[0] = 1;
        else spawnCount[0] = input.getAsJsonArray().get(0).getAsJsonObject().get("min").getAsInt();
        if(input.getAsJsonArray().get(0).getAsJsonObject().get("max") == null) spawnCount[1] = 5;
        else spawnCount[1] = input.getAsJsonArray().get(0).getAsJsonObject().get("max").getAsInt();
        return spawnCount;
    }

    private List<EntityType<?>> validateAndConvertEntities(JsonArray input) {
        List<EntityType<?>> entities = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            if (!ForgeRegistries.ENTITIES.containsKey(new ResourceLocation(input.get(i).getAsString()))) {
                Unstable.LOGGER.error("Error adding end siege data for entity {}", input.get(i).getAsString());
            } else {
                entities.add(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(input.get(i).getAsString())));
            }
        }
        return entities;
    }

    private List<MobEffectInstance> validateAndConvertEffects(JsonArray input) {
        List<MobEffectInstance> mobEffects = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            JsonObject jsonObject = input.get(i).getAsJsonObject();
            if (!ForgeRegistries.MOB_EFFECTS.containsKey(new ResourceLocation(jsonObject.get("mobEffect").getAsString()))) {
                Unstable.LOGGER.error("Error adding end siege data for mob effect {}", jsonObject.get("mobEffect").getAsString());
            } else {
                MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(jsonObject.get("mobEffect").getAsString()));
                int amplifier = jsonObject.has("amplifier") ? jsonObject.get("amplifier").getAsInt() : 0;
                int duration = jsonObject.has("duration") ? jsonObject.get("duration").getAsInt() : 100;
                boolean ambient = jsonObject.has("ambient") ? jsonObject.get("ambient").getAsBoolean() : true;
                boolean visible = jsonObject.has("visible") ? jsonObject.get("visible").getAsBoolean() : true;
                mobEffects.add(new MobEffectInstance(effect, amplifier, duration, ambient, visible));
            }
        }
        return mobEffects;
    }

    private List<Map<InteractionHand, ItemStack>> validateAndConvertEquipment(JsonArray input) {
        List<Map<InteractionHand, ItemStack>> equipment = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            JsonObject jsonObject = input.get(i).getAsJsonObject();
            if (!ForgeRegistries.ITEMS.containsKey(new ResourceLocation(jsonObject.get("item").getAsString()))) {
                Unstable.LOGGER.error("Error adding end siege data for equipment item {}", input.get(i).getAsString());
            } else {
                InteractionHand hand = InteractionHand.valueOf(jsonObject.get("hand").getAsString());
                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(jsonObject.get("item").getAsString())));
                if(jsonObject.get("nbt") != null) {
                    try {
                        CompoundTag tag = NbtUtils.snbtToStructure(jsonObject.get("nbt").getAsString());
                        stack.setTag(tag);
                    } catch (CommandSyntaxException e) { Unstable.LOGGER.error("Unable to get nbt for item {}.", jsonObject.get("item").getAsString(), e); }
                }
                equipment.add(Map.of(hand, stack));
            }
        }
        return equipment;
    }

    private List<Map<EquipmentSlot, ItemStack>> validateAndConvertArmor(JsonArray input) {
        List<Map<EquipmentSlot, ItemStack>> armor = new ArrayList<>();
        for(int i = 0; i < input.size(); i++) {
            JsonObject jsonObject = input.get(i).getAsJsonObject();
            if (!ForgeRegistries.ITEMS.containsKey(new ResourceLocation(jsonObject.get("item").getAsString()))) {
                Unstable.LOGGER.error("Error adding end siege data for armor item {}", jsonObject.get("item").getAsString());
            } else {
                EquipmentSlot slot = EquipmentSlot.valueOf(jsonObject.get("armor").getAsString());
                ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(jsonObject.get("item").getAsString())));
                if(jsonObject.get("nbt") != null) {
                    try {
                        CompoundTag tag = NbtUtils.snbtToStructure(jsonObject.get("nbt").getAsString());
                        stack.setTag(tag);
                    } catch (CommandSyntaxException e) {Unstable.LOGGER.error("Unable to get nbt for armor item {}.", jsonObject.get("item").getAsString(), e);}
                }
                armor.add(Map.of(slot, stack));
            }
        }
        return armor;
    }
}
