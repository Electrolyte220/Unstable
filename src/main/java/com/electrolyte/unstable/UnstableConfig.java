package com.electrolyte.unstable;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class UnstableConfig {

    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue CURSED_EARTH_PARTICLES;
    public static ForgeConfigSpec.BooleanValue REMOVE_ACTIVE_SIGIL;
    public static ForgeConfigSpec.IntValue ACTIVATED_DURABILITY;
    public static ForgeConfigSpec.IntValue NEEDED_MOBS;
    public static ForgeConfigSpec.IntValue MOB_SPAWN_RANGE;
    public static ForgeConfigSpec.IntValue MAX_MOBS;
    public static ForgeConfigSpec.ConfigValue<String> ACTIVATION_BLOCK;
    public static ForgeConfigSpec.IntValue MIN_SPAWN_DELAY;
    public static ForgeConfigSpec.IntValue MAX_SPAWN_DELAY;
    public static ForgeConfigSpec.BooleanValue SOUL_RESET_DEATH;

    static {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("Client Settings").push("client");
        CURSED_EARTH_PARTICLES = CLIENT_BUILDER
                .comment("Should Cursed Earth emit particles?")
                .define("cursedEarthParticles", true);
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();

        COMMON_BUILDER.comment("Misc Settings").push("main");

            COMMON_BUILDER.comment("test").push("misc");
            REMOVE_ACTIVE_SIGIL = COMMON_BUILDER
                    .comment("Should the active sigil be removed from the player's inventory after receiving the stable sigil?")
                    .define("removeActiveSigil", true);
            ACTIVATED_DURABILITY = COMMON_BUILDER
                    .comment("Durability of the Activated Division Sigil")
                    .defineInRange("activatedDurability", 256, 1, Integer.MAX_VALUE);
            NEEDED_MOBS = COMMON_BUILDER
                    .comment("During the Psudeo Inversion Ritual, how many mob kills are needed to obtain a stable sigil?")
                    .defineInRange("neededMobs", 100, 1, Integer.MAX_VALUE);
            MOB_SPAWN_RANGE = COMMON_BUILDER
                    .comment("During the Psudeo Inversion Ritual, how far away from the player should mobs be allowed to spawn?")
                    .defineInRange("mobSpawnRange", 25, 1, Integer.MAX_VALUE);
            MAX_MOBS = COMMON_BUILDER
                    .comment("Maximum number of mobs allowed during the Psudeo Inversion Ritual")
                    .defineInRange("maximumMobs", 250, 1, Integer.MAX_VALUE);
            COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();

        SERVER_BUILDER.comment("Server-side Settings").push("serverSideSettings");

            SERVER_BUILDER.comment("Activation Ritual").push("Settings for the Activation Ritual");
            ACTIVATION_BLOCK = SERVER_BUILDER
                .comment("Block that natural earth is changed to when the activation ritual is successful")
                .define("activationBlock", "unstable:cursed_earth");
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Cursed Earth").push("Settings for Cursed Earth");
            MIN_SPAWN_DELAY = SERVER_BUILDER
                    .comment("Minimum amount of time to wait before cursed earth attempts to spawn a mob")
                    .defineInRange("minSpawnDelay", 600, 0, Integer.MAX_VALUE);
            MAX_SPAWN_DELAY = SERVER_BUILDER
                .comment("Minimum amount of time to wait before cursed earth attempts to spawn a mob")
                .defineInRange("maxSpawnDelay", 1200, 0, Integer.MAX_VALUE);
            SOUL_RESET_DEATH = SERVER_BUILDER
                .comment("Should the soul drain from the player's health reset when they die?")
                .define("soulResetDeath", false);
            SERVER_BUILDER.pop();

        SERVER_BUILDER.pop();
        SERVER_CONFIG = SERVER_BUILDER.build();

    }
}
