package electrolyte.unstable;

import com.mojang.logging.LogUtils;
import electrolyte.unstable.init.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.List;

@Mod("unstable")
public class Unstable {

    public static final String MOD_ID = "unstable";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Unstable.MOD_ID);

    public Unstable() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, UnstableConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, UnstableConfig.SERVER_CONFIG);

        //TODO: check cursed earth block
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        ModBlocks.init();
        ModItems.init();
        ModTools.init();
        ModRecipes.init();
        ModSounds.init();

        CREATIVE_MODE_TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static final RegistryObject<CreativeModeTab> UNSTABLE_TAB = CREATIVE_MODE_TABS.register("unstable_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.unstable"))
            .icon(() -> new ItemStack(ModItems.DIVISION_SIGIL.get()))
            .displayItems((displayItemsGen, output) -> {
                ItemStack unstableIngot = new ItemStack(ModItems.UNSTABLE_INGOT.get());
                unstableIngot.getOrCreateTag().putBoolean("creativeSpawned", true);
                ItemStack ethericSword = new ItemStack(ModTools.ETHERIC_SWORD.get());
                ethericSword.enchant(Enchantments.SHARPNESS, 5);
                ethericSword.getOrCreateTag().putBoolean("Unbreakable", true);
                ItemStack destructionPickaxe = new ItemStack(ModTools.DESTRUCTION_PICKAXE.get());
                destructionPickaxe.enchant(Enchantments.BLOCK_EFFICIENCY, 5);
                destructionPickaxe.getOrCreateTag().putBoolean("Unbreakable", true);
                ItemStack erosionShovel = new ItemStack(ModTools.EROSION_SHOVEL.get());
                erosionShovel.enchant(Enchantments.BLOCK_EFFICIENCY, 5);
                erosionShovel.getOrCreateTag().putBoolean("Unbreakable", true);
                ItemStack healingAxe = new ItemStack(ModTools.HEALING_AXE.get());
                healingAxe.enchant(Enchantments.BLOCK_EFFICIENCY, 5);
                healingAxe.getOrCreateTag().putBoolean("Unbreakable", true);
                ItemStack reversingHoe = new ItemStack(ModTools.REVERSING_HOE.get());
                reversingHoe.enchant(Enchantments.BLOCK_EFFICIENCY, 5);
                reversingHoe.getOrCreateTag().putBoolean("Unbreakable", true);
                ItemStack precisionShears = new ItemStack(ModTools.PRECISION_SHEARS.get());
                precisionShears.enchant(Enchantments.BLOCK_EFFICIENCY, 5);
                precisionShears.getOrCreateTag().putBoolean("Unbreakable", true);

                output.acceptAll(List.of(
                        ModBlocks.CURSED_EARTH_ITEM.get().getDefaultInstance(),
                        ModItems.DIVISION_SIGIL.get().getDefaultInstance(),
                        ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance(),
                        ModItems.DIVISION_SIGIL_STABLE.get().getDefaultInstance(),
                        unstableIngot,
                        ModItems.UNSTABLE_STABLE_INGOT.get().getDefaultInstance(),
                        ModItems.STABLE_INGOT.get().getDefaultInstance(),
                        ModItems.SOUL_FRAGMENT.get().getDefaultInstance(),
                        ethericSword,
                        destructionPickaxe,
                        erosionShovel,
                        healingAxe,
                        reversingHoe,
                        precisionShears
                ));
            }).build());

    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemBlockRenderTypes.setRenderLayer(ModBlocks.CURSED_EARTH.get(), RenderType.cutoutMipped()));
    }
}
