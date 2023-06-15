package electrolyte.unstable.datagen;

import electrolyte.unstable.Unstable;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnstableDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(event.includeServer(), new EndSiegeDataGenerator(gen));
        gen.addProvider(event.includeServer(), new ReversingHoeDataGenerator(gen));
        UnstableBlockTagsProvider blockTags = new UnstableBlockTagsProvider(gen.getPackOutput(), event.getLookupProvider(), Unstable.MOD_ID, event.getExistingFileHelper());
        gen.addProvider(event.includeServer(), new UnstableItemTagsProvider(gen.getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter(), event.getExistingFileHelper()));
    }
}