package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.recipe.builder.MysticalTableRecipeBuilder;
import dev.electrolyte.unstable.registry.ModComponents;
import dev.electrolyte.unstable.registry.ModItems;
import dev.electrolyte.unstable.registry.ModTags;
import dev.electrolyte.unstable.registry.ModTools;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.concurrent.CompletableFuture;

public class UnstableRecipesProvider extends RecipeProvider {

    private Provider registries;

    public UnstableRecipesProvider(PackOutput output, CompletableFuture<Provider> provider) {
        super(output, provider);
    }

    @Override
    protected CompletableFuture<?> run(CachedOutput pOutput, Provider pRegistries) {
        this.registries = pRegistries;
        return super.run(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        MysticalTableRecipeBuilder.shaped(ModTools.DESTRUCTION_PICKAXE.get().getDefaultInstance())
                .pattern("III")
                .pattern(" O ")
                .pattern(" O ")
                .define('I', ModTags.UNSTABLE_INGOTS)
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.UNSTABLE_INGOT.get()), has(ModItems.UNSTABLE_INGOT.get()))
                .save(pRecipeOutput);

        ItemStack destructionPickaxe = new ItemStack(ModTools.DESTRUCTION_PICKAXE.get());
        destructionPickaxe.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        destructionPickaxe.enchant(registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.EFFICIENCY).get(), 5);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, destructionPickaxe)
                .pattern("III")
                .pattern(" O ")
                .pattern(" O ")
                .define('I', ModItems.STABLE_INGOT.get())
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.STABLE_INGOT.get()), has(ModItems.STABLE_INGOT.get()))
                .save(pRecipeOutput, "unstable:destruction_pickaxe_stable");

        MysticalTableRecipeBuilder.shaped(ModTools.EROSION_SHOVEL.get().getDefaultInstance())
                .pattern("I")
                .pattern("O")
                .pattern("O")
                .define('I', ModTags.UNSTABLE_INGOTS)
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.UNSTABLE_INGOT.get()), has(ModItems.UNSTABLE_INGOT.get()))
                .save(pRecipeOutput);

        ItemStack erosionShovel = new ItemStack(ModTools.EROSION_SHOVEL.get());
        erosionShovel.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        erosionShovel.enchant(registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.EFFICIENCY).get(), 5);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, erosionShovel)
                .pattern("I")
                .pattern("O")
                .pattern("O")
                .define('I', ModItems.STABLE_INGOT.get())
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.STABLE_INGOT.get()), has(ModItems.STABLE_INGOT.get()))
                .save(pRecipeOutput, "unstable:erosion_shovel_stable");

        MysticalTableRecipeBuilder.shaped(ModTools.ETHERIC_SWORD.get().getDefaultInstance())
                .pattern("I")
                .pattern("I")
                .pattern("O")
                .define('I', ModTags.UNSTABLE_INGOTS)
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.UNSTABLE_INGOT.get()), has(ModItems.UNSTABLE_INGOT.get()))
                .save(pRecipeOutput);

        ItemStack ethericSword = new ItemStack(ModTools.ETHERIC_SWORD.get());
        ethericSword.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        ethericSword.enchant(registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.EFFICIENCY).get(), 5);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ethericSword)
                .pattern("I")
                .pattern("I")
                .pattern("O")
                .define('I', ModItems.STABLE_INGOT.get())
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.STABLE_INGOT.get()), has(ModItems.STABLE_INGOT.get()))
                .save(pRecipeOutput, "unstable:etheric_sword_stable");

        MysticalTableRecipeBuilder.shaped(ModTools.HEALING_AXE.get().getDefaultInstance())
                .pattern("II")
                .pattern("IO")
                .pattern(" O")
                .define('I', ModTags.UNSTABLE_INGOTS)
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.UNSTABLE_INGOT.get()), has(ModItems.UNSTABLE_INGOT.get()))
                .save(pRecipeOutput);

        ItemStack healingAxe = new ItemStack(ModTools.HEALING_AXE.get());
        healingAxe.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        healingAxe.enchant(registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.EFFICIENCY).get(), 5);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, healingAxe)
                .pattern("II")
                .pattern("IO")
                .pattern(" O")
                .define('I', ModItems.STABLE_INGOT.get())
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.STABLE_INGOT.get()), has(ModItems.STABLE_INGOT.get()))
                .save(pRecipeOutput, "unstable:healing_axe_stable");

        MysticalTableRecipeBuilder.shaped(ModTools.PRECISION_SHEARS.get().getDefaultInstance())
                .pattern("OI")
                .pattern("IO")
                .define('I', ModTags.UNSTABLE_INGOTS)
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.UNSTABLE_INGOT.get()), has(ModItems.UNSTABLE_INGOT.get()))
                .save(pRecipeOutput);

        ItemStack precisionShears = new ItemStack(ModTools.PRECISION_SHEARS.get());
        precisionShears.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        precisionShears.enchant(registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.EFFICIENCY).get(), 5);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, precisionShears)
                .pattern("OI")
                .pattern("IO")
                .define('I', ModItems.STABLE_INGOT.get())
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.STABLE_INGOT.get()), has(ModItems.STABLE_INGOT.get()))
                .save(pRecipeOutput, "unstable:precision_shears_stable");

        MysticalTableRecipeBuilder.shaped(ModTools.REVERSING_HOE.get().getDefaultInstance())
                .pattern("II")
                .pattern(" O")
                .pattern(" O")
                .define('I', ModTags.UNSTABLE_INGOTS)
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.UNSTABLE_INGOT.get()), has(ModItems.UNSTABLE_INGOT.get()))
                .save(pRecipeOutput);

        ItemStack reversingHoe = new ItemStack(ModTools.REVERSING_HOE.get());
        reversingHoe.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
        reversingHoe.enchant(registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.EFFICIENCY).get(), 5);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, reversingHoe)
                .pattern("II")
                .pattern(" O")
                .pattern(" O")
                .define('I', ModItems.STABLE_INGOT.get())
                .define('O', Blocks.OBSIDIAN)
                .unlockedBy(getHasName(ModItems.STABLE_INGOT.get()), has(ModItems.STABLE_INGOT.get()))
                .save(pRecipeOutput, "unstable:reversing_hoe_stable");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UNSTABLE_STABLE_INGOT.get())
                .pattern("I")
                .pattern("S")
                .pattern("N")
                .define('I', Items.IRON_INGOT)
                .define('S', ModItems.DIVISION_SIGIL_ACTIVATED.get())
                .define('N', Items.NETHERITE_SCRAP)
                .unlockedBy(getHasName(ModItems.DIVISION_SIGIL_ACTIVATED.get()), has(ModItems.DIVISION_SIGIL_ACTIVATED.get()))
                .save(pRecipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STABLE_INGOT.get())
                .pattern("I")
                .pattern("S")
                .pattern("D")
                .define('I', Items.IRON_INGOT)
                .define('S', ModItems.DIVISION_SIGIL_STABLE.get())
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ModItems.DIVISION_SIGIL_STABLE.get()), has(ModItems.DIVISION_SIGIL_STABLE.get()))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PatchouliAPI.get().getBookStack(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "guide")))
                .requires(Items.BOOK)
                .requires(ModItems.DIVISION_SIGIL.get())
                .unlockedBy(getHasName(ModItems.DIVISION_SIGIL.get()), has(ModItems.DIVISION_SIGIL.get()))
                .save(pRecipeOutput.withConditions(new ModLoadedCondition("patchouli")), "unstable:guide_book");

        ItemStack unstableIngot = new ItemStack(ModItems.UNSTABLE_INGOT.get());
        unstableIngot.set(ModComponents.EXPLODE_TIMER, 200);
        unstableIngot.set(ModComponents.EXPLODED, false);
        MysticalTableRecipeBuilder.shaped(unstableIngot)
                .pattern("I")
                .pattern("S")
                .pattern("D")
                .define('I', Items.IRON_INGOT)
                .define('S', ModItems.DIVISION_SIGIL_ACTIVATED.get())
                .define('D', Items.DIAMOND)
                .unlockedBy(getHasName(ModItems.DIVISION_SIGIL_ACTIVATED.get()), has(ModItems.DIVISION_SIGIL_ACTIVATED.get()))
                .save(pRecipeOutput, ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "mystical_table/unstable_ingot"));
    }
}
