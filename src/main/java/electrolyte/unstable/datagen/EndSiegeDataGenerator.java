package electrolyte.unstable.datagen;

import com.google.gson.*;
import electrolyte.unstable.Unstable;
import electrolyte.unstable.init.ModTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.NBTIngredient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record EndSiegeDataGenerator(DataGenerator gen) implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Override
    public void run(HashCache cache) {
        buildEntityData(cache, "default", List.of(EntityType.ZOMBIE, EntityType.WITCH, EntityType.SILVERFISH, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.CREEPER, EntityType.BLAZE, EntityType.ZOMBIFIED_PIGLIN,
                        EntityType.PHANTOM, EntityType.EVOKER, EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.VEX),
                Optional.of(List.of(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0, true, true),
                        new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 1, true, true),
                        new MobEffectInstance(MobEffects.REGENERATION, 6000, 0, true, true),
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0, true, true))),
                Optional.empty());

        buildChestData(cache, "north", List.of(Ingredient.of(Tags.Items.STONE), Ingredient.of(ItemTags.DIRT), Ingredient.of(Tags.Items.GRAVEL), Ingredient.of(Tags.Items.SAND), Ingredient.of(Tags.Items.GLASS), Ingredient.of(Items.CLAY),
                Ingredient.of(ItemTags.TERRACOTTA), Ingredient.of(Tags.Items.DYES), Ingredient.of(ModTags.COOKED_MEAT), Ingredient.of(ItemTags.FOX_FOOD), Ingredient.of(ModTags.COOKED_FISH), Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Tags.Items.ORES_COAL),
                Ingredient.of(Tags.Items.ORES_IRON), Ingredient.of(Tags.Items.ORES_COPPER), Ingredient.of(Tags.Items.ORES_LAPIS), Ingredient.of(Tags.Items.ORES_GOLD), Ingredient.of(Tags.Items.ORES_REDSTONE), Ingredient.of(Tags.Items.ORES_DIAMOND),
                Ingredient.of(Tags.Items.ORES_EMERALD), Ingredient.of(Tags.Items.ORES_QUARTZ), Ingredient.of(Tags.Items.OBSIDIAN), Ingredient.of(Tags.Items.STORAGE_BLOCKS_AMETHYST)));
        buildChestData(cache, "south", List.of(Ingredient.of(Items.TURTLE_EGG), Ingredient.of(Items.SCUTE), Ingredient.of(Tags.Items.HEADS), Ingredient.of(Items.END_CRYSTAL), Ingredient.of(Items.RABBIT_FOOT), Ingredient.of(Items.NAME_TAG),
                Ingredient.of(Items.DRAGON_BREATH), Ingredient.of(Items.TOTEM_OF_UNDYING), Ingredient.of(Items.SHULKER_SHELL), Ingredient.of(Items.PHANTOM_MEMBRANE), Ingredient.of(Items.NAUTILUS_SHELL), Ingredient.of(Items.HEART_OF_THE_SEA),
                Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE)));
        buildChestData(cache, "east", List.of(NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_INVISIBILITY)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_LEAPING)),
                NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_FIRE_RESISTANCE)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SWIFTNESS)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOWNESS)),
                NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_TURTLE_MASTER)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_WATER_BREATHING)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING)),
                NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HARMING)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_POISON)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_REGENERATION)),
                NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_STRENGTH)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_WEAKNESS)), NBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LONG_SLOW_FALLING))));
        buildChestData(cache, "west", List.of(Ingredient.of(Items.MUSIC_DISC_11), Ingredient.of(Items.MUSIC_DISC_13), Ingredient.of(Items.MUSIC_DISC_BLOCKS), Ingredient.of(Items.MUSIC_DISC_CAT), Ingredient.of(Items.MUSIC_DISC_CHIRP),
                Ingredient.of(Items.MUSIC_DISC_FAR), Ingredient.of(Items.MUSIC_DISC_MALL), Ingredient.of(Items.MUSIC_DISC_MELLOHI),   Ingredient.of(Items.MUSIC_DISC_STAL), Ingredient.of(Items.MUSIC_DISC_STRAD), Ingredient.of(Items.MUSIC_DISC_WAIT),
                Ingredient.of(Items.MUSIC_DISC_WARD), Ingredient.of(Items.MUSIC_DISC_PIGSTEP), Ingredient.of(Items.MUSIC_DISC_OTHERSIDE)));
    }

    private void buildEntityData(HashCache cache, String fileName, List<EntityType<?>> entities, Optional<List<MobEffectInstance>> effects, Optional<List<Map<EquipmentSlot, ItemStack>>> equipment) {
        Path file = this.gen.getOutputFolder().resolve("data/unstable/end_siege/entity_data/" + fileName + ".json");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "unstable:entity_data");
        JsonArray entityArray = new JsonArray();
        for (EntityType<?> entity : entities) {
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
        equipment.ifPresent(equipmentList -> equipmentList.forEach(equipmentItem -> equipmentItem.forEach((equipmentSlot, stack) -> {
            JsonObject equipmentObject = new JsonObject();
            equipmentObject.addProperty("slot", equipmentSlot.toString());
            equipmentObject.addProperty("item", stack.getItem().getRegistryName().toString());
            if (stack.getTag() != null) {
                equipmentObject.addProperty("nbt", stack.getTag().toString());
            }
            equipmentArray.add(equipmentObject);
        })));
        jsonObject.add("equipment", equipmentArray);
        try {
            DataProvider.save(GSON, cache, GSON.toJsonTree(jsonObject), file);
        } catch (
                IOException e) {
            Unstable.LOGGER.error("Error adding entity data for {}", file, e);
        }
    }

    private void buildChestData(HashCache cache, String chestLocation, List<Ingredient> chestContents) {
        Path file = this.gen.getOutputFolder().resolve("data/unstable/end_siege/chest_data/" + chestLocation + ".json");
        JsonObject jsonObject = new JsonObject();
        JsonArray chestContentsArray = new JsonArray();
        jsonObject.addProperty("type", "unstable:chest_data");
        jsonObject.addProperty("location", chestLocation.toUpperCase());
        chestContents.forEach(ingredient -> {
            JsonElement ingredientElement = ingredient.toJson();
            JsonObject newIngredient = new JsonObject();
            if (ingredientElement.getAsJsonObject().get("item") != null) {
                newIngredient.add("item", ingredientElement.getAsJsonObject().get("item"));
                if (ingredientElement.getAsJsonObject().get("nbt") != null) {
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
        } catch (
                IOException e) {
            Unstable.LOGGER.error("Error adding chest data for {}", file, e);
        }
    }

    @Override
    public String getName() {
        return Unstable.MOD_ID + ":end_siege_gen";
    }
}
