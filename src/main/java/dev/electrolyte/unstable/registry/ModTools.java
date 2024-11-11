package dev.electrolyte.unstable.registry;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.UnstableToolMaterial;
import dev.electrolyte.unstable.items.tools.DestructionPickaxe;
import dev.electrolyte.unstable.items.tools.ErosionShovel;
import dev.electrolyte.unstable.items.tools.EthericSword;
import dev.electrolyte.unstable.items.tools.HealingAxe;
import dev.electrolyte.unstable.items.tools.PrecisionShears;
import dev.electrolyte.unstable.items.tools.ReversingHoe;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModTools {

    public static final DeferredRegister.Items TOOLS = DeferredRegister.createItems(Unstable.MOD_ID);

    public static void register(IEventBus bus) {
        TOOLS.register(bus);
    }

    public static final Supplier<EthericSword> ETHERIC_SWORD = TOOLS.register("etheric_sword",
            () -> new EthericSword(UnstableToolMaterial.UNSTABLE, new Properties()));
    public static final Supplier<DestructionPickaxe> DESTRUCTION_PICKAXE = TOOLS.register("destruction_pickaxe",
            () -> new DestructionPickaxe(UnstableToolMaterial.UNSTABLE, new Item.Properties()));
    public static final Supplier<ErosionShovel> EROSION_SHOVEL = TOOLS.register("erosion_shovel",
            () -> new ErosionShovel(UnstableToolMaterial.UNSTABLE, new Item.Properties()));
    public static final Supplier<HealingAxe> HEALING_AXE = TOOLS.register("healing_axe",
            () -> new HealingAxe(UnstableToolMaterial.UNSTABLE, new Item.Properties()));
    public static final Supplier<ReversingHoe> REVERSING_HOE = TOOLS.register("reversing_hoe",
            () -> new ReversingHoe(UnstableToolMaterial.UNSTABLE, new Item.Properties()));
    public static final Supplier<PrecisionShears> PRECISION_SHEARS = TOOLS.register("precision_shears",
            () -> new PrecisionShears(new Item.Properties().durability(2501).component(DataComponents.TOOL, PrecisionShears.createToolProperties())));
}
