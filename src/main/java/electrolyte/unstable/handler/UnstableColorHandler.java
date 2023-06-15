package electrolyte.unstable.handler;

import electrolyte.unstable.Unstable;
import electrolyte.unstable.init.ModBlocks;
import electrolyte.unstable.init.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = Unstable.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnstableColorHandler {

    @SubscribeEvent
    public static void onBlockColor(RegisterColorHandlersEvent.Block event) {
        event.register((pState, pLevel, pPos, pTintIndex) -> Color.HSBtoRGB(2.8f,1,0.5f), ModBlocks.CURSED_EARTH.get());
    }
    @SubscribeEvent
    public static void onItemColor(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> {
            if(stack.getTag() != null && !stack.getTag().getBoolean("creativeSpawned")) {
                int ticksTillExplosion = stack.getTag().getInt("explodesIn") / 2;
                return Color.HSBtoRGB(0, 1 - (ticksTillExplosion * 0.01f), 1);
            } else {
                return Color.HSBtoRGB(0, 0, 1);
            }
        }, ModItems.UNSTABLE_INGOT.get());
        event.register((stack, tint) -> Color.HSBtoRGB(2.8f, 1, 0.5f), ModBlocks.CURSED_EARTH_ITEM.get());
    }
}
