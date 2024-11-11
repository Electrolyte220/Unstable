package dev.electrolyte.unstable.registry;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.items.DivisionSigil;
import dev.electrolyte.unstable.items.DivisionSigilActivated;
import dev.electrolyte.unstable.items.DivisionSigilStable;
import dev.electrolyte.unstable.items.SoulFragment;
import dev.electrolyte.unstable.items.UnstableIngot;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Unstable.MOD_ID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }

    public static final Supplier<Item> DIVISION_SIGIL = ITEMS.register("division_sigil",
            () -> new DivisionSigil(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> DIVISION_SIGIL_ACTIVATED = ITEMS.register("division_sigil_activated",
            () -> new DivisionSigilActivated(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> DIVISION_SIGIL_STABLE = ITEMS.register("division_sigil_stable",
            () -> new DivisionSigilStable(new Item.Properties().stacksTo(1).fireResistant()));
    public static final Supplier<Item> UNSTABLE_INGOT = ITEMS.register("unstable_ingot",
            () -> new UnstableIngot(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> UNSTABLE_STABLE_INGOT = ITEMS.registerSimpleItem("unstable_stable_ingot");
    public static final Supplier<Item> STABLE_INGOT = ITEMS.registerSimpleItem("stable_ingot");
    public static final Supplier<Item> SOUL_FRAGMENT = ITEMS.register("soul_fragment",
            () -> new SoulFragment(new Item.Properties()));
}
