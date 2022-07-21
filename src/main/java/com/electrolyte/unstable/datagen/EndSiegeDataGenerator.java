package com.electrolyte.unstable.datagen;

import com.electrolyte.unstable.Unstable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record EndSiegeDataGenerator(DataGenerator gen) implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public void run(@NotNull HashCache cache) {
        buildEntityData(cache, "default", 2, 5, List.of(EntityType.ZOMBIE, EntityType.WITCH, EntityType.SILVERFISH, EntityType.SPIDER, EntityType.CREEPER, EntityType.BLAZE, EntityType.PIGLIN, EntityType.PHANTOM),
                Optional.of(List.of(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 3, true, true), new MobEffectInstance(MobEffects.DIG_SPEED, 6000, 3, true, true))),
                Optional.empty(), Optional.empty());
        buildEntityData(cache, "skeleton", 1, 3, List.of(EntityType.SKELETON),
                Optional.of(List.of(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 3, true, true), new MobEffectInstance(MobEffects.DIG_SPEED, 6000, 3, true, true))),
                Optional.of(List.of(Map.of(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW)))), Optional.empty());
        buildEntityData(cache, "wither_skeleton", 1, 3, List.of(EntityType.WITHER_SKELETON),
                Optional.of(List.of(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 3, true, true), new MobEffectInstance(MobEffects.DIG_SPEED, 6000, 3, true, true))),
                Optional.of(List.of(Map.of(InteractionHand.MAIN_HAND, new ItemStack(Items.STONE_SWORD)))), Optional.empty());
        /*ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        sword.enchant(Enchantments.SHARPNESS, 3);
        ItemStack diamond_leggings = new ItemStack(Items.DIAMOND_LEGGINGS);
        diamond_leggings.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 2);
        buildEntityData(cache, "undead", 2, 5, Arrays.asList(EntityType.SKELETON, EntityType.ZOMBIE),
                Optional.of(Arrays.asList(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 10, 10, true, true),
                        new MobEffectInstance(MobEffects.ABSORPTION, 10, 10, true, true))),
                Optional.of(Arrays.asList(Map.of(InteractionHand.MAIN_HAND, sword), Map.of(InteractionHand.OFF_HAND, new ItemStack(Items.TOTEM_OF_UNDYING)))),
                Optional.of(Arrays.asList(Map.of(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET)), Map.of(EquipmentSlot.LEGS, diamond_leggings))));*/
    }

    private void buildEntityData(HashCache cache, String fileName, int minCount, int maxCount, List<EntityType<?>> entities, Optional<List<MobEffectInstance>> effects, Optional<List<Map<InteractionHand, ItemStack>>> equipment, Optional<List<Map<EquipmentSlot, ItemStack>>> armor) {
        Path file = this.gen.getOutputFolder().resolve("data/unstable/end_siege_entity_data/" + fileName + ".json");
        JsonObject jsonObject = new JsonObject();
        JsonArray spawnCountArray = new JsonArray();
        JsonObject spawnCount = new JsonObject();
        spawnCount.addProperty("min", minCount);
        spawnCount.addProperty("max", maxCount);
        spawnCountArray.add(spawnCount);
        jsonObject.add("spawnCount", spawnCountArray);
        JsonArray entityArray = new JsonArray();
        for(EntityType<?> entity : entities) {
            entityArray.add(entity.getRegistryName().toString());
        }
        jsonObject.add("entities", entityArray);

        JsonArray mobEffectDetails = new JsonArray();
        effects.ifPresent(mobEffectInstances -> mobEffectInstances.forEach(effect -> {
            JsonObject effectObject = new JsonObject();
            effectObject.addProperty("mobEffect", effect.getEffect().getRegistryName().toString());
            effectObject.addProperty("amplifier", effect.getAmplifier());
            effectObject.addProperty("duration", effect.getDuration());
            effectObject.addProperty("ambient", effect.isAmbient());
            effectObject.addProperty("visible", effect.isVisible());
            mobEffectDetails.add(effectObject);
        }));
        jsonObject.add("effects", mobEffectDetails);

        JsonArray equipmentArray = new JsonArray();
        equipment.ifPresent(equipmentList -> equipmentList.forEach(equipmentItem -> equipmentItem.forEach((interactionHand, stack) -> {
            JsonObject equipmentObject = new JsonObject();
            equipmentObject.addProperty("hand", interactionHand.toString());
            equipmentObject.addProperty("item", stack.getItem().getRegistryName().toString());
            if(stack.getTag() != null) {
                equipmentObject.addProperty("nbt", stack.getTag().toString());
            }
        equipmentArray.add(equipmentObject);
        })));
        jsonObject.add("equipment", equipmentArray);

        JsonArray armorArray = new JsonArray();
        armor.ifPresent(armorList -> armorList.forEach(armorItem -> armorItem.forEach((equipmentSlot, stack) -> {
            JsonObject armorObject = new JsonObject();
            armorObject.addProperty("armor", equipmentSlot.toString());
            armorObject.addProperty("item", stack.getItem().getRegistryName().toString());
            if(stack.getTag() != null) {
                armorObject.addProperty("nbt", stack.getTag().toString());
            }
            armorArray.add(armorObject);
        })));
        jsonObject.add("armor", armorArray);
        try {
            DataProvider.save(GSON, cache, GSON.toJsonTree(jsonObject), file);
        } catch (IOException e) {
            Unstable.LOGGER.error("Error adding entity data for {}", file, e);
        }
    }

    @Override
    public @NotNull String getName() {
        return Unstable.MOD_ID;
    }
}
