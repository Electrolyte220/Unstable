package com.electrolyte.unstable.handler;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.init.ModItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(modid = Unstable.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnstableColorHandler {

    @SubscribeEvent
    public static void onItemColor(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, tint) -> {
            if(stack.getTag() != null && !stack.getTag().getBoolean("creativeSpawned")) {
                int ticksTillExplosion = stack.getTag().getInt("explodesIn") / 2;
                return Color.HSBtoRGB(0, 1 - (ticksTillExplosion * 0.01f), 1);
            } else {
                return Color.HSBtoRGB(0, 0, 1);
            }
        }, ModItems.UNSTABLE_INGOT.get());
    }
}
