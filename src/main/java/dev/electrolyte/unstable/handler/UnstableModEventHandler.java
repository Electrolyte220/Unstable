package dev.electrolyte.unstable.handler;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.client.screen.MysticalTableScreen;
import dev.electrolyte.unstable.registry.ModMenus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = Unstable.MOD_ID, bus = Bus.MOD)
public class UnstableModEventHandler {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MYSTICAL_TABLE_MENU.get(), MysticalTableScreen::new);
    }
}
