package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.registry.ModBlocks;
import dev.electrolyte.unstable.registry.ModItems;
import dev.electrolyte.unstable.registry.ModTools;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class UnstableLangProvider extends LanguageProvider {
    public UnstableLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        this.addBlock(ModBlocks.CURSED_EARTH, "Cursed Earth");
        this.addBlock(ModBlocks.MYSTICAL_TABLE, "Mystical Table");
        this.add("unstable.container.mystical_table", "Mystical Table");

        this.addItem(ModItems.DIVISION_SIGIL, "Division Sigil");
        this.addItem(ModItems.DIVISION_SIGIL_ACTIVATED, "Division Sigil");
        this.addItem(ModItems.DIVISION_SIGIL_STABLE, "Division Sigil");
        this.addItem(ModItems.UNSTABLE_INGOT, "Unstable Ingot");
        this.addItem(ModItems.UNSTABLE_STABLE_INGOT, "Unstable/Stable Ingot");
        this.addItem(ModItems.STABLE_INGOT, "Stable Ingot");
        this.addItem(ModItems.SOUL_FRAGMENT, "Soul Fragment");
        this.addItem(ModTools.DESTRUCTION_PICKAXE, "Destruction Pickaxe");
        this.addItem(ModTools.ETHERIC_SWORD, "Etheric Sword");
        this.addItem(ModTools.HEALING_AXE, "Healing Axe");
        this.addItem(ModTools.REVERSING_HOE, "Reversing Hoe");
        this.addItem(ModTools.EROSION_SHOVEL, "Erosion Shovel");
        this.addItem(ModTools.PRECISION_SHEARS, "Precision Shears");

        this.add("itemGroup.unstable", "Unstable");

        this.add("death.attack.divide_by_diamond", "%1$s suffered a fatal 'java.lang.ArithmeticException: \\ by diamond");
        this.add("death.attack.healing_axe", "%1$s forgot to heal themselves before healing others");

        this.add("unstable.division_sigil.tooltip_inactive_1", "INACTIVE: You must perform the Activation Ritual.");
        this.add("unstable.division_sigil.tooltip_inactive_2", "Sneak right-click on an");
        this.add("unstable.division_sigil.tooltip_inactive_3", "enchanting table for more details.");
        this.add("unstable.division_sigil.tooltip.active", "ACTIVE: Uses remaining: %d");
        this.add("unstable.division_sigil.tooltip.stable", "Stable");

        this.add("unstable.unstable_ingot.tooltip.unstable", "Warning: Highly Explosive!");
        this.add("unstable.unstable_ingot.tooltip.creative", "Creative Spawned - Will Not Explode");
        this.add("unstable.unstable_ingot.tooltip.crafting", "Mystical forces block this ingot from being crafted in anything other than the mystical table.");
        this.add("unstable.unstable_ingot.tooltip.explode_timer", "Explodes In: %d");

        this.add("unstable.soul_fragment.tooltip.empty", "Requires 1 Heart of Health (2HP) to Craft.");
        this.add("unstable.soul_fragment.tooltip.owner", "Soul Owner: %s");
        this.add("unstable.soul_fragment.tooltip.weak_soul", "Soul has been spread too thin.");

        this.add("unstable.activation_ritual.title", "Activation Ritual");
        this.add("unstable.activation_ritual.subtitle", "Activation Ritual Activates");
        this.add("unstable.activation_ritual.natural_earth", "- Altar has sufficient natural earth");
        this.add("unstable.activation_ritual.natural_earth_fail", "! Altar lacks sufficient natural earth");
        this.add("unstable.activation_ritual.darkness", "- Altar is in darkness");
        this.add("unstable.activation_ritual.darkness_fail", "! Altar must not be lit by outside sources");
        this.add("unstable.activation_ritual.sky", "- Altar can see the moon");
        this.add("unstable.activation_ritual.sky_fail", "! Altar cannot see the moon");
        this.add("unstable.activation_ritual.redstone", "- Altar has a redstone circle");
        this.add("unstable.activation_ritual.redstone_fail", "! Altar does not have a redstone circle");
        this.add("unstable.activation_ritual.time", "- Time is right");
        this.add("unstable.activation_ritual.time_early", "! Too early, sacrifice must be made at midnight");
        this.add("unstable.activation_ritual.time_late", "! Too late, sacrifice must be made at midnight");
        this.add("unstable.activation_ritual.active_sigil", "Sigil(s) have already been activated!");

        this.add("unstable.pseudo_inversion_ritual.dimension.overworld", "This dimension has too much natural earth.");
        this.add("unstable.pseudo_inversion_ritual.dimension.nether", "This dimension is too hot.");
        this.add("unstable.pseudo_inversion_ritual.chest", "Chests in correct location");
        this.add("unstable.pseudo_inversion_ritual.chest_fail", "Chests are not in the correct location");
        this.add("unstable.pseudo_inversion_ritual.north_chest", "North Chest Contents are correct");
        this.add("unstable.pseudo_inversion_ritual.north_chest_fail", "North Chest Contents are not correct");
        this.add("unstable.pseudo_inversion_ritual.south_chest", "South Chest Contents are correct");
        this.add("unstable.pseudo_inversion_ritual.south_chest_fail", "South Chest Contents are not correct");
        this.add("unstable.pseudo_inversion_ritual.east_chest", "East Chest Contents are correct");
        this.add("unstable.pseudo_inversion_ritual.east_chest_fail", "East Chest Contents are not correct");
        this.add("unstable.pseudo_inversion_ritual.west_chest", "West Chest Contents are correct");
        this.add("unstable.pseudo_inversion_ritual.west_chest_fail", "West Chest Contents are not correct");
        this.add("unstable.pseudo_inversion_ritual.redstone_string", "Redstone and String Placement is correct");
        this.add("unstable.pseudo_inversion_ritual.redstone_string_fail", "Redstone and String Placement is not correct");
        this.add("unstable.pseudo_inversion_ritual.death", "You cannot die whilst performing this ritual!");
        this.add("unstable.pseudo_inversion_ritual.changed_dimension", "You must be in The End whilst performing this ritual!");
        this.add("unstable.pseudo_inversion_ritual.siege_ended", "The Siege in 'The End' has ended.");
        this.add("unstable.pseudo_inversion_ritual.correct_setup", "Everything is correct, now sacrifice the one who guards them all.");
        this.add("unstable.pseudo_inversion_ritual.not_activated", "You must first perform the Activation Ritual before performing the Pseudo-Inversion Ritual");
        this.add("unstable.pseudo_inversion_ritual.already_stable", "Sigil is already Stable");
        this.add("unstable.pseudo_inversion_ritual.multiple_sigil", "There can only be one Pseudo-Inversion Ritual active at any given time");
        this.add("unstable.pseudo_inversion_ritual.siege_started", "The Siege in 'The End' has begun");
        this.add("unstable.pseudo_inversion_ritual.siege_kills", "Kills: %s");


        this.add("unstable.jei.transmutation.title", "Block Transmutation");
        this.add("unstable.jei.property_regression.title", "Property Regression");
        this.add("unstable.jei.end_siege.title", "Required Items for End Siege");
        this.add("unstable.jei.end_siege.chest_north", "North Chest");
        this.add("unstable.jei.end_siege.chest_south", "South Chest");
        this.add("unstable.jei.end_siege.chest_east", "East Chest");
        this.add("unstable.jei.end_siege.chest_west", "West Chest");
        this.add("unstable.jei.end_siege.tooltip.chest_hover", "The order of items in chests does not matter, as long as all items are in their respective chests.");

        this.add("tag.item.unstable.unstable_ingots", "Unstable Ingots");

    }
}

