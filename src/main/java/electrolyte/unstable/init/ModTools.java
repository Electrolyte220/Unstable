package electrolyte.unstable.init;

import electrolyte.unstable.Unstable;
import electrolyte.unstable.UnstableToolMaterial;
import electrolyte.unstable.items.tools.*;
import net.minecraft.world.item.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTools {

    public static final DeferredRegister<Item> TOOLS = DeferredRegister.create(ForgeRegistries.ITEMS, Unstable.MOD_ID);

    public static void init() {
        TOOLS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SwordItem> ETHERIC_SWORD = TOOLS.register("etheric_sword",
            () -> new EthericSword(UnstableToolMaterial.UNSTABLE, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<PickaxeItem> DESTRUCTION_PICKAXE = TOOLS.register("destruction_pickaxe",
            () -> new DestructionPickaxe(UnstableToolMaterial.UNSTABLE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<TieredItem> EROSION_SHOVEL = TOOLS.register("erosion_shovel",
            () -> new ErosionShovel(UnstableToolMaterial.UNSTABLE,1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<AxeItem> HEALING_AXE = TOOLS.register("healing_axe",
            () -> new HealingAxe(UnstableToolMaterial.UNSTABLE, -5, -3.0F, new Item.Properties()));
    public static final RegistryObject<TieredItem> REVERSING_HOE = TOOLS.register("reversing_hoe",
            () -> new ReversingHoe(UnstableToolMaterial.UNSTABLE, -4, 0.0F, new Item.Properties()));
    public static final RegistryObject<Item> PRECISION_SHEARS = TOOLS.register("precision_shears",
            () -> new PrecisionShears(new Item.Properties().tab(Unstable.UNSTABLE_TAB).durability(2501)));
}
