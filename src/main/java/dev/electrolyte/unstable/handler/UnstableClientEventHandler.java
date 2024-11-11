package dev.electrolyte.unstable.handler;


import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.registry.ModBlocks;
import dev.electrolyte.unstable.registry.ModComponents;
import dev.electrolyte.unstable.registry.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.awt.*;

@EventBusSubscriber(modid = Unstable.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class UnstableClientEventHandler {

    @SubscribeEvent
    public static void onBlockColor(RegisterColorHandlersEvent.Block event) {
        event.register((pState, pLevel, pPos, pTintIndex) -> Color.HSBtoRGB(2.8f,1,0.5f), ModBlocks.CURSED_EARTH.get());
    }
    @SubscribeEvent
    public static void onItemColor(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tint) -> {
            if(stack.has(ModComponents.EXPLODE_TIMER)) {
                int ticksTillExplosion = stack.get(ModComponents.EXPLODE_TIMER) / 2;
                return Color.HSBtoRGB(0, 1 - (ticksTillExplosion * 0.01f), 1);
            } else {
                return Color.HSBtoRGB(0, 0, 1);
            }
        }, ModItems.UNSTABLE_INGOT.get());
        event.register((stack, tint) -> Color.HSBtoRGB(2.8f, 1, 0.5f), ModBlocks.CURSED_EARTH_ITEM.get());
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemBlockRenderTypes.setRenderLayer(ModBlocks.CURSED_EARTH.get(), RenderType.cutoutMipped()));
    }
}
