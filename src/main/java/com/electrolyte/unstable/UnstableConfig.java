package com.electrolyte.unstable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class UnstableConfig {

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue REMOVE_ACTIVE_SIGIL;
    public static ForgeConfigSpec.IntValue ACTIVATED_DURABILITY;
    public static ForgeConfigSpec.IntValue NEEDED_MOBS;
    public static ForgeConfigSpec.IntValue MOB_SPAWN_RANGE;
    public static ForgeConfigSpec.IntValue MAX_MOBS;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("Misc Settings").push("main");

            COMMON_BUILDER.comment("test").push("misc");
            REMOVE_ACTIVE_SIGIL = COMMON_BUILDER.comment("Should the active sigil be removed from the player's inventory after recieving the stable sigil?").define("removeActiveSigil", true);
            ACTIVATED_DURABILITY = COMMON_BUILDER.comment("Durability of the Activated Division Sigil").defineInRange("activatedDurability", 256, 1, Integer.MAX_VALUE);
            NEEDED_MOBS = COMMON_BUILDER.comment("During the Psudeo Inversion Ritual, how many mob kills are needed to obtain a stable sigil?").defineInRange("neededMobs", 100, 1, Integer.MAX_VALUE);
            MOB_SPAWN_RANGE = COMMON_BUILDER.comment("During the Psudeo Inversion Ritual, how far away from the player should mobs be allowed to spawn?").defineInRange("mobSpawnRange", 25, 1, Integer.MAX_VALUE);
            MAX_MOBS = COMMON_BUILDER.comment("Maximum number of mobs allowed during the Psudeo Inversion Ritual").defineInRange("maximumMobs", 250, 1, Integer.MAX_VALUE);
            COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
