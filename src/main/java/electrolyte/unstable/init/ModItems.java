package electrolyte.unstable.init;

import electrolyte.unstable.Unstable;
import electrolyte.unstable.items.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Unstable.MOD_ID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Item> DIVISION_SIGIL = ITEMS.register("division_sigil",
            () -> new DivisionSigil(new Item.Properties().stacksTo(1).tab(Unstable.UNSTABLE_TAB)));
    public static final RegistryObject<Item> DIVISION_SIGIL_ACTIVATED = ITEMS.register("division_sigil_activated",
            () -> new DivisionSigilActivated(new Item.Properties().stacksTo(1).tab(Unstable.UNSTABLE_TAB)));
    public static final RegistryObject<Item> DIVISION_SIGIL_STABLE = ITEMS.register("division_sigil_stable",
            () -> new DivisionSigilStable(new Item.Properties().stacksTo(1).fireResistant().tab(Unstable.UNSTABLE_TAB)));
    public static final RegistryObject<Item> UNSTABLE_INGOT = ITEMS.register("unstable_ingot",
            () -> new UnstableIngot(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> UNSTABLE_STABLE_INGOT = ITEMS.register("unstable_stable_ingot",
            () -> new Item(new Item.Properties().tab(Unstable.UNSTABLE_TAB)));
    public static final RegistryObject<Item> STABLE_INGOT = ITEMS.register("stable_ingot",
            () -> new Item(new Item.Properties().tab(Unstable.UNSTABLE_TAB)));
    public static final RegistryObject<Item> SOUL_FRAGMENT = ITEMS.register("soul_fragment",
            () -> new SoulFragment(new Item.Properties().tab(Unstable.UNSTABLE_TAB)));
}
