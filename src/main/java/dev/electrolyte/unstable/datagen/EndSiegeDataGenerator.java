package dev.electrolyte.unstable.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.UnstableEnums.ChestLocation;
import dev.electrolyte.unstable.codecs.EntityDataCodec;
import dev.electrolyte.unstable.codecs.EntityEquipmentCodec;
import dev.electrolyte.unstable.listener.EntityDataReloadListener;
import dev.electrolyte.unstable.recipe.endsiege.EndSiegeChestRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public record EndSiegeDataGenerator(DataGenerator gen) implements DataProvider {

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> list = new ArrayList<>();
        list.add(buildEntityData(cache, "default", List.of(EntityType.ZOMBIE, EntityType.WITCH, EntityType.SILVERFISH, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.CREEPER, EntityType.BLAZE, EntityType.ZOMBIFIED_PIGLIN,
                        EntityType.PHANTOM, EntityType.EVOKER, EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.ILLUSIONER, EntityType.VEX),
                Optional.of(List.of(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 0, true, true),
                        new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 1, true, true),
                        new MobEffectInstance(MobEffects.REGENERATION, 6000, 0, true, true),
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0, true, true))),
                //todo: remove once wiki is updated
                List.of(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty())));

        list.add(buildChestData(cache, "north", List.of(Ingredient.of(Tags.Items.STONES), Ingredient.of(ItemTags.DIRT), Ingredient.of(Tags.Items.GRAVELS), Ingredient.of(Tags.Items.SANDS), Ingredient.of(Tags.Items.GLASS_BLOCKS), Ingredient.of(Items.CLAY), Ingredient.of(ItemTags.TERRACOTTA), Ingredient.of(Tags.Items.DYES),
                Ingredient.of(Tags.Items.FOODS_COOKED_MEAT), Ingredient.of(ItemTags.FOX_FOOD), Ingredient.of(Tags.Items.FOODS_COOKED_FISH), Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Tags.Items.ORES_COAL), Ingredient.of(Tags.Items.ORES_IRON), Ingredient.of(Tags.Items.ORES_COPPER), Ingredient.of(Tags.Items.ORES_LAPIS),
                Ingredient.of(Tags.Items.ORES_GOLD), Ingredient.of(Tags.Items.ORES_REDSTONE), Ingredient.of(Tags.Items.ORES_DIAMOND), Ingredient.of(Tags.Items.ORES_EMERALD), Ingredient.of(Tags.Items.ORES_QUARTZ), Ingredient.of(Blocks.OBSIDIAN), Ingredient.of(Blocks.AMETHYST_BLOCK),
                Ingredient.of(Blocks.SCULK_SHRIEKER))));
        list.add(buildChestData(cache, "south", List.of(Ingredient.of(Items.TURTLE_EGG), Ingredient.of(Items.TURTLE_SCUTE, Items.ARMADILLO_SCUTE), Ingredient.of(ItemTags.SKULLS), Ingredient.of(Items.END_CRYSTAL), Ingredient.of(Items.RABBIT_FOOT), Ingredient.of(Items.NAME_TAG), Ingredient.of(Items.DRAGON_BREATH), Ingredient.of(Items.TOTEM_OF_UNDYING),
                    Ingredient.of(Items.SHULKER_SHELL), Ingredient.of(Items.PHANTOM_MEMBRANE), Ingredient.of(Items.NAUTILUS_SHELL), Ingredient.of(Items.HEART_OF_THE_SEA), Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE), Ingredient.of(Items.GOAT_HORN), Ingredient.of(ItemTags.TRIM_TEMPLATES), Ingredient.of(ItemTags.DECORATED_POT_SHERDS), Ingredient.of(Items.OMINOUS_TRIAL_KEY),
                    Ingredient.of(Items.OMINOUS_BOTTLE), Ingredient.of(Items.HEAVY_CORE))));
        list.add(buildChestData(cache, "east", buildPotionData(List.of(Potions.LONG_INVISIBILITY, Potions.LONG_LEAPING, Potions.LONG_FIRE_RESISTANCE, Potions.LONG_SWIFTNESS, Potions.LONG_SLOWNESS, Potions.LONG_TURTLE_MASTER, Potions.LONG_WATER_BREATHING, Potions.STRONG_HEALING,
                Potions.STRONG_HARMING, Potions.LONG_POISON, Potions.LONG_REGENERATION, Potions.LONG_STRENGTH, Potions.LONG_WEAKNESS, Potions.LONG_SLOW_FALLING, Potions.WEAVING, Potions.OOZING, Potions.INFESTED))));
        list.add(buildChestData(cache, "west", List.of(Ingredient.of(Items.MUSIC_DISC_11), Ingredient.of(Items.MUSIC_DISC_13), Ingredient.of(Items.MUSIC_DISC_BLOCKS), Ingredient.of(Items.MUSIC_DISC_CAT), Ingredient.of(Items.MUSIC_DISC_CHIRP), Ingredient.of(Items.MUSIC_DISC_FAR),
                Ingredient.of(Items.MUSIC_DISC_MALL), Ingredient.of(Items.MUSIC_DISC_MELLOHI), Ingredient.of(Items.MUSIC_DISC_STAL), Ingredient.of(Items.MUSIC_DISC_STRAD), Ingredient.of(Items.MUSIC_DISC_WAIT), Ingredient.of(Items.MUSIC_DISC_WARD), Ingredient.of(Items.MUSIC_DISC_PIGSTEP),
                Ingredient.of(Items.MUSIC_DISC_OTHERSIDE), Ingredient.of(Items.MUSIC_DISC_5), Ingredient.of(Items.MUSIC_DISC_RELIC), Ingredient.of(Items.MUSIC_DISC_CREATOR), Ingredient.of(Items.MUSIC_DISC_CREATOR_MUSIC_BOX), Ingredient.of(Items.MUSIC_DISC_PRECIPICE))));

        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> buildEntityData(CachedOutput cache, String fileName, List<EntityType<?>> entityTypes, Optional<List<MobEffectInstance>> effects, List<Optional<ItemStack>> equipment) {
        List<ResourceLocation> entities = new ArrayList<>();
        entityTypes.forEach(entity -> entities.add(ResourceLocation.parse(entity.toShortString())));
        Path file = this.gen.getPackOutput().getOutputFolder().resolve("data/unstable/end_siege/entity_data/" + fileName + ".json");
        Optional<JsonElement> jsonResult = EntityDataReloadListener.ENTITY_DATA_CODEC.encode(new EntityDataCodec(entities, effects, Optional.of(new EntityEquipmentCodec(equipment.get(0), equipment.get(1), equipment.get(2), equipment.get(3), equipment.get(4), equipment.get(5), equipment.get(6)))), JsonOps.INSTANCE, null).result();
        return DataProvider.saveStable(cache, jsonResult.get(), file);
    }

    private List<Ingredient> buildPotionData(List<Holder<Potion>> potions) {
        List<Ingredient> contents = new ArrayList<>();
        for(Holder<Potion> potion : potions) {
            contents.add(DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, new PotionContents(potion), Items.POTION));
        }
        return contents;
    }

    private CompletableFuture<?> buildChestData(CachedOutput cache, String chestLocation, List<Ingredient> chestContents) {
        Path file = this.gen.getPackOutput().getOutputFolder().resolve("data/unstable/recipe/end_siege/chest_data/" + chestLocation + ".json");
        DataResult<JsonElement> jsonResult = EndSiegeChestRecipe.CODEC.encode(new EndSiegeChestRecipe(ChestLocation.valueOf(chestLocation.toUpperCase()), NonNullList.copyOf(chestContents)), JsonOps.INSTANCE, null);
        return DataProvider.saveStable(cache, jsonResult.getOrThrow(), file);
    }

    @Override
    public String getName() {
        return Unstable.MOD_ID + ":end_siege_gen";
    }
}
