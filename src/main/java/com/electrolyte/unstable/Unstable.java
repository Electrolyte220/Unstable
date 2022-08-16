package com.electrolyte.unstable;

import com.electrolyte.unstable.init.ModBlocks;
import com.electrolyte.unstable.init.ModItems;
import com.electrolyte.unstable.init.ModRecipes;
import com.mojang.logging.LogUtils;
import com.electrolyte.unstable.init.ModTools;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod("unstable")
public class Unstable {

    public static final String MOD_ID = "unstable";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Unstable() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, UnstableConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnstableConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, UnstableConfig.SERVER_CONFIG);

        ModBlocks.init();
        ModItems.init();
        ModTools.init();
        ModRecipes.init();
    }

    public static final CreativeModeTab UNSTABLE_TAB = new CreativeModeTab("unstable") {

        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.DIVISION_SIGIL.get());
        }
    };
}
