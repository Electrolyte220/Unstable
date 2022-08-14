package com.electrolyte.unstable.datagen;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.init.ModTags;
import com.google.gson.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.NBTIngredient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
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

        buildChestData(cache, "north", List.of(Ingredient.of(Tags.Items.STONE), Ingredient.of(Items.BRICK), Ingredient.of(Tags.Items.GLASS), Ingredient.of(ModTags.COOKED_FISH), Ingredient.of(Items.TERRACOTTA),
                Ingredient.of(Tags.Items.DYES_GREEN), Ingredient.of(ItemTags.COALS), Ingredient.of(ModTags.COOKED_MEAT), Ingredient.of(Tags.Items.INGOTS), Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Items.QUARTZ), Ingredient.of(ItemTags.FOX_FOOD), Ingredient.of(Items.NETHER_BRICK)));
        buildChestData(cache, "south", List.of(Ingredient.of(Blocks.GRASS_BLOCK), Ingredient.of(Tags.Items.ORES_LAPIS), Ingredient.of(Blocks.DIRT), Ingredient.of(Blocks.OBSIDIAN), Ingredient.of(Tags.Items.SAND), Ingredient.of(Tags.Items.ORES_DIAMOND),
                Ingredient.of(Tags.Items.GRAVEL), Ingredient.of(Tags.Items.ORES_REDSTONE), Ingredient.of(Tags.Items.ORES_GOLD), Ingredient.of(Blocks.CLAY), Ingredient.of(Tags.Items.ORES_IRON), Ingredient.of(Tags.Items.ORES_EMERALD), Ingredient.of(Tags.Items.ORES_COAL), Ingredient.of(Tags.Items.ORES_COPPER)));
        buildChestData(cache, "east", List.of(NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_INVISIBILITY)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_LEAPING)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_FIRE_RESISTANCE)),
                NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SWIFTNESS)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOWNESS)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_TURTLE_MASTER)),
                NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_WATER_BREATHING)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HARMING)),
                NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_POISON)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_REGENERATION)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_STRENGTH)),
                NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_WEAKNESS)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOW_FALLING))));
        buildChestData(cache, "west", List.of(Ingredient.of(Items.MUSIC_DISC_11), Ingredient.of(Items.MUSIC_DISC_13), Ingredient.of(Items.MUSIC_DISC_BLOCKS), Ingredient.of(Items.MUSIC_DISC_CAT), Ingredient.of(Items.MUSIC_DISC_CHIRP),
                Ingredient.of(Items.MUSIC_DISC_FAR), Ingredient.of(Items.MUSIC_DISC_MALL), Ingredient.of(Items.MUSIC_DISC_MELLOHI), Ingredient.of(Items.MUSIC_DISC_OTHERSIDE), Ingredient.of(Items.MUSIC_DISC_PIGSTEP), Ingredient.of(Items.MUSIC_DISC_STAL), Ingredient.of(Items.MUSIC_DISC_STRAD), Ingredient.of(Items.MUSIC_DISC_WAIT), Ingredient.of(Items.MUSIC_DISC_WARD)));
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

    private void buildChestData(HashCache cache, String chestLocation, List<Ingredient> chestContents) {
        Path file = this.gen.getOutputFolder().resolve("data/unstable/end_siege_chest_data/" + chestLocation + ".json");
        JsonObject jsonObject = new JsonObject();
        JsonArray chestContentsArray = new JsonArray();
        jsonObject.addProperty("location", chestLocation.toUpperCase());
        chestContents.forEach(ingredient -> {
            JsonElement ingredientElement = ingredient.toJson();
            //Only copy 'item' and 'nbt', since 'type' and 'count' is not needed
            JsonObject newIngredient = new JsonObject();
            if(ingredientElement.getAsJsonObject().get("item") != null) {
                newIngredient.add("item", ingredientElement.getAsJsonObject().get("item"));
                if(ingredientElement.getAsJsonObject().get("nbt") != null) {
                    newIngredient.add("nbt", ingredientElement.getAsJsonObject().get("nbt"));
                }
                chestContentsArray.add(newIngredient);
            } else {
                chestContentsArray.add(ingredientElement);
            }
        });
        jsonObject.add("contents", chestContentsArray);
        try {
            DataProvider.save(GSON, cache, GSON.toJsonTree(jsonObject), file);
        } catch (IOException e) {
            Unstable.LOGGER.error("Error adding chest data for {}", file, e);
        }
    }

    @Override
    public @NotNull String getName() {
        return Unstable.MOD_ID;
    }
}
