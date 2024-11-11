package dev.electrolyte.unstable;

import com.mojang.logging.LogUtils;
import dev.electrolyte.unstable.registry.ModBlocks;
import dev.electrolyte.unstable.registry.ModComponents;
import dev.electrolyte.unstable.registry.ModItems;
import dev.electrolyte.unstable.registry.ModMenus;
import dev.electrolyte.unstable.registry.ModRecipes;
import dev.electrolyte.unstable.registry.ModSounds;
import dev.electrolyte.unstable.registry.ModTools;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

@Mod(Unstable.MOD_ID)
public class Unstable {

    public static final String MOD_ID = "unstable";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Unstable.MOD_ID);

    public Unstable(IEventBus bus, ModContainer container, Dist dist) {
        container.registerConfig(ModConfig.Type.CLIENT, UnstableConfig.CLIENT_CONFIG);
        container.registerConfig(ModConfig.Type.SERVER, UnstableConfig.SERVER_CONFIG);

        if(dist == Dist.CLIENT) {
            container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        ModComponents.register(bus);
        ModMenus.register(bus);
        ModBlocks.register(bus);
        ModItems.register(bus);
        ModTools.register(bus);
        ModRecipes.register(bus);
        ModSounds.register(bus);

        CREATIVE_MODE_TABS.register(bus);
    }

    public static final Supplier<CreativeModeTab> UNSTABLE_TAB = CREATIVE_MODE_TABS.register("unstable_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.unstable"))
            .icon(() -> new ItemStack(ModItems.DIVISION_SIGIL.get()))
            .displayItems((displayItemsGen, output) -> {
                ItemStack unstableIngot = new ItemStack(ModItems.UNSTABLE_INGOT.get());
                unstableIngot.set(ModComponents.CREATIVE_SPAWNED, true);
                ItemStack ethericSword = new ItemStack(ModTools.ETHERIC_SWORD.get());
                ItemStack destructionPickaxe = new ItemStack(ModTools.DESTRUCTION_PICKAXE.get());
                ItemStack erosionShovel = new ItemStack(ModTools.EROSION_SHOVEL.get());
                ItemStack healingAxe = new ItemStack(ModTools.HEALING_AXE.get());
                ItemStack reversingHoe = new ItemStack(ModTools.REVERSING_HOE.get());
                ItemStack precisionShears = new ItemStack(ModTools.PRECISION_SHEARS.get());
                displayItemsGen.holders().lookup(Registries.ENCHANTMENT).ifPresent(e -> {
                    ethericSword.enchant(Holder.direct(e.get(Enchantments.SHARPNESS).get().value()), 5);
                    ethericSword.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
                    destructionPickaxe.enchant(Holder.direct(e.get(Enchantments.EFFICIENCY).get().value()), 5);
                    destructionPickaxe.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
                    erosionShovel.enchant(Holder.direct(e.get(Enchantments.EFFICIENCY).get().value()), 5);
                    erosionShovel.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
                    healingAxe.enchant(Holder.direct(e.get(Enchantments.EFFICIENCY).get().value()), 5);
                    healingAxe.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
                    reversingHoe.enchant(Holder.direct(e.get(Enchantments.EFFICIENCY).get().value()), 5);
                    reversingHoe.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
                    precisionShears.enchant(Holder.direct(e.get(Enchantments.EFFICIENCY).get().value()), 5);
                    precisionShears.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
                });

                output.acceptAll(List.of(
                        unstableIngot,
                        ethericSword,
                        destructionPickaxe,
                        erosionShovel,
                        healingAxe,
                        reversingHoe,
                        precisionShears,

                        ModBlocks.CURSED_EARTH_ITEM.get().getDefaultInstance(),
                        ModBlocks.MYSTICAL_TABLE_ITEM.get().getDefaultInstance(),
                        ModItems.DIVISION_SIGIL.get().getDefaultInstance(),
                        ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance(),
                        ModItems.DIVISION_SIGIL_STABLE.get().getDefaultInstance(),

                        ModItems.UNSTABLE_STABLE_INGOT.get().getDefaultInstance(),
                        ModItems.STABLE_INGOT.get().getDefaultInstance(),
                        ModItems.SOUL_FRAGMENT.get().getDefaultInstance()
                ));
            }).build());
}
