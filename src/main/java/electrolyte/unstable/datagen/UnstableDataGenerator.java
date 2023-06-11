package electrolyte.unstable.datagen;

import electrolyte.unstable.Unstable;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
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
        gen.addProvider(event.includeServer(), new UnstableTagDataGenerator(gen, new BlockTagsProvider(gen, Unstable.MOD_ID, event.getExistingFileHelper()), Unstable.MOD_ID, event.getExistingFileHelper()));
    }
}
