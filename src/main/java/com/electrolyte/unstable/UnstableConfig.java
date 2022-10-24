package com.electrolyte.unstable;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class UnstableConfig {

    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue CURSED_EARTH_PARTICLES;
    public static ForgeConfigSpec.IntValue ACTIVATED_DURABILITY;
    public static ForgeConfigSpec.IntValue NEEDED_MOBS;
    public static ForgeConfigSpec.IntValue MAX_MOBS;
    public static ForgeConfigSpec.ConfigValue<String> ACTIVATION_BLOCK;
    public static ForgeConfigSpec.IntValue MIN_SPAWN_DELAY;
    public static ForgeConfigSpec.IntValue MAX_SPAWN_DELAY;
    public static ForgeConfigSpec.IntValue MIN_MOBS_PIR;
    public static ForgeConfigSpec.IntValue MAX_MOBS_PIR;
    public static ForgeConfigSpec.IntValue MOB_SPAWN_RAGE_PIR;
    public static ForgeConfigSpec.BooleanValue SOUL_RESET_DEATH;

    static {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("Client Settings").push("clientSettings");
        CURSED_EARTH_PARTICLES = CLIENT_BUILDER
                .comment("Should Cursed Earth emit particles?")
                .define("cursedEarthParticles", true);
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();

        SERVER_BUILDER.comment("Server-side Settings").push("serverSettings");
            SERVER_BUILDER.comment("Activation Ritual").push("Settings for the Activation Ritual");
            ACTIVATION_BLOCK = SERVER_BUILDER
                .comment("Block that natural earth is changed to when the activation ritual is successful.")
                .define("activationBlock", "unstable:cursed_earth");
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Cursed Earth").push("Settings for Cursed Earth");
            MIN_SPAWN_DELAY = SERVER_BUILDER
                    .comment("Minimum amount of time to wait before cursed earth attempts to spawn a mob.")
                    .defineInRange("minSpawnDelay", 600, 0, Integer.MAX_VALUE);
            MAX_SPAWN_DELAY = SERVER_BUILDER
                .comment("Minimum amount of time to wait before cursed earth attempts to spawn a mob.")
                .defineInRange("maxSpawnDelay", 1200, 0, Integer.MAX_VALUE);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Misc Settings").push("Settings that do not belong to a specific category");
            ACTIVATED_DURABILITY = SERVER_BUILDER
                .comment("Durability of the Activated Division Sigil")
                .defineInRange("activatedDurability", 256, 1, Integer.MAX_VALUE);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Pseudo Inversion Ritual").push("Settings related to the Pseudo-Inversion Ritual");
            MIN_MOBS_PIR = SERVER_BUILDER
                    .comment("Minimum amount of mobs that will spawn in a group during the ritual.")
                    .defineInRange("minMobsPir", 1, 1, Integer.MAX_VALUE);
            MAX_MOBS_PIR = SERVER_BUILDER
                    .comment("Maximum amount of mobs that will spawn in a group during the ritual.")
                    .defineInRange("maxMobsPir", 4, 1, Integer.MAX_VALUE);
            MAX_MOBS = SERVER_BUILDER
                .comment("Maximum number of mobs allowed during the Psudeo Inversion Ritual.")
                .defineInRange("maximumMobs", 250, 1, Integer.MAX_VALUE);
            MOB_SPAWN_RAGE_PIR = SERVER_BUILDER
                .comment("Maximum range mobs can spawn from the player during the pseudo inversion ritual.")
                .defineInRange("mobSpawnRagePir", 75, 1, 128);
            NEEDED_MOBS = SERVER_BUILDER
                .comment("During the Psudeo Inversion Ritual, how many mob kills are needed to obtain a stable sigil?")
                .defineInRange("neededMobs", 100, 1, Integer.MAX_VALUE);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Soul Fragment").push("Settings related to Soul Fragment");
            SOUL_RESET_DEATH = SERVER_BUILDER
                .comment("Should the soul drain from the player's health reset when they die?")
                .define("soulResetDeath", false);
            SERVER_BUILDER.pop();

        SERVER_BUILDER.pop();
        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
