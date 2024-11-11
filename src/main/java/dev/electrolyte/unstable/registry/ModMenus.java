package dev.electrolyte.unstable.registry;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.menu.MysticalTableMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Unstable.MOD_ID);

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }

    public static final Supplier<MenuType<MysticalTableMenu>> MYSTICAL_TABLE_MENU = MENUS.register("mystical_table_menu",
            () -> new MenuType<>(MysticalTableMenu::new, FeatureFlags.DEFAULT_FLAGS));
}
