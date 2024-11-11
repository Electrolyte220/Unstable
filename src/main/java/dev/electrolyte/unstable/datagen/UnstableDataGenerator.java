package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.Unstable;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Unstable.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class UnstableDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(event.includeServer(), new EndSiegeDataGenerator(gen));
        gen.addProvider(event.includeServer(), new ReversingHoeDataGenerator(gen));
        UnstableBlockTagsProvider blockTags = new UnstableBlockTagsProvider(gen.getPackOutput(), event.getLookupProvider(), Unstable.MOD_ID, event.getExistingFileHelper());
        gen.addProvider(event.includeServer(), blockTags);
        gen.addProvider(event.includeServer(), new UnstableItemTagsProvider(gen.getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter(), Unstable.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeServer(), new UnstableRecipesProvider(gen.getPackOutput(), event.getLookupProvider()));
        gen.addProvider(event.includeServer(), new UnstableDamageTypeTagsProvider(gen.getPackOutput(), Registries.DAMAGE_TYPE, event.getLookupProvider(), Unstable.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeServer(), UnstableLootTableProvider.create(event.getGenerator().getPackOutput(), event.getLookupProvider()));

        gen.addProvider(event.includeClient(), new UnstableSoundsProvider(gen.getPackOutput(), Unstable.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new UnstableLangProvider(gen.getPackOutput(), Unstable.MOD_ID, "en_us"));
        gen.addProvider(event.includeClient(), new UnstableBlockStateProvider(gen.getPackOutput(), Unstable.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new UnstableBlockModelProvider(gen.getPackOutput(), Unstable.MOD_ID, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new UnstableItemModelProvider(gen.getPackOutput(), Unstable.MOD_ID, event.getExistingFileHelper()));
    }
}