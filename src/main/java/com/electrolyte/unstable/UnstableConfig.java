package com.electrolyte.unstable;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class UnstableConfig {

    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue CURSED_EARTH_PARTICLES;
    public static ForgeConfigSpec.IntValue ACTIVATED_DURABILITY;
    public static ForgeConfigSpec.IntValue NEEDED_MOBS;
    public static ForgeConfigSpec.IntValue MAX_MOBS;
    public static ForgeConfigSpec.ConfigValue<String> ACTIVATION_BLOCK;
    public static ForgeConfigSpec.IntValue MIN_SPAWN_DELAY;
    public static ForgeConfigSpec.IntValue MAX_SPAWN_DELAY;
    public static ForgeConfigSpec.IntValue MOB_SPAWN_RAGE_PIR;
    public static ForgeConfigSpec.BooleanValue SOUL_RESET_DEATH;

    public static ForgeConfigSpec.BooleanValue HEALING_AXE_OFFHAND;
    public static ForgeConfigSpec.LongValue HEALING_AXE_HEAL_RATE;

    static {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("Client Settings").push("client_settings");
        CURSED_EARTH_PARTICLES = CLIENT_BUILDER
                .comment("Should Cursed Earth emit particles?")
                .define("cursed_earth_particles", true);
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();

        SERVER_BUILDER.comment("Server Settings").push("server_settings");
            SERVER_BUILDER.comment("Activation Ritual").push("activation_ritual");
            ACTIVATION_BLOCK = SERVER_BUILDER
                .comment("Block that natural earth is changed to when the activation ritual is successful.")
                .define("activation_block", "unstable:cursed_earth");
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Cursed Earth").push("cursed_earth");
            MIN_SPAWN_DELAY = SERVER_BUILDER
                    .comment("Minimum amount of time to wait (in ticks) before cursed earth attempts to spawn a mob.")
                    .defineInRange("min_spawn_delay", 300, 0, Integer.MAX_VALUE);
            MAX_SPAWN_DELAY = SERVER_BUILDER
                .comment("Maximum amount of time to wait (in ticks) before cursed earth attempts to spawn a mob.")
                .defineInRange("max_spawn_delay", 1200, 1, Integer.MAX_VALUE);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Activated Division Sigil").push("activated_division_sigil");
            ACTIVATED_DURABILITY = SERVER_BUILDER
                .comment("Durability of the Activated Division Sigil")
                .defineInRange("activated_durability", 256, 1, Integer.MAX_VALUE);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Pseudo Inversion Ritual").push("pseudo_inversion_ritual");
            MAX_MOBS = SERVER_BUILDER
                .comment("What should the entity cap be during the Pseudo Inversion Ritual? (Once this limit is reached, no new mobs will spawn until current mobs are killed).")
                .defineInRange("maximum_mobs", 250, 1, Integer.MAX_VALUE);
            MOB_SPAWN_RAGE_PIR = SERVER_BUILDER
                .comment("Maximum range mobs can spawn from the center during the Pseudo Inversion Ritual. (The center is defined as where the beacon was before it was destroyed).")
                .defineInRange("mob_spawning_range_pir", 64, 1, 256);
            NEEDED_MOBS = SERVER_BUILDER
                .comment("During the Psudeo Inversion Ritual, how many mob kills are needed to obtain a stable sigil?")
                .defineInRange("needed_mobs", 100, 1, Integer.MAX_VALUE);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Soul Fragment").push("soul_fragment");
            SOUL_RESET_DEATH = SERVER_BUILDER
                .comment("Should the soul drain from the player's health reset when they die?")
                .define("soul_reset_death", false);
            SERVER_BUILDER.pop();

            SERVER_BUILDER.comment("Healing Axe").push("healing_axe");
            HEALING_AXE_OFFHAND = SERVER_BUILDER
                .comment("Should the Healing Axe be able to restore hunger when held in the offhand?")
                .define("healing_axe_offhand", false);
            HEALING_AXE_HEAL_RATE = SERVER_BUILDER
                 .comment("Rate (in ticks) at which 1 haunch is restored to the player")
                 .defineInRange("healing_axe_heal_rate", 40L, 1L, Long.MAX_VALUE);
            SERVER_BUILDER.pop();

        SERVER_BUILDER.pop();
        SERVER_CONFIG = SERVER_BUILDER.build();
    }
}
