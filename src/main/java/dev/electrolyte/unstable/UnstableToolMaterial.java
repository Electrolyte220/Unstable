package dev.electrolyte.unstable;

import dev.electrolyte.unstable.registry.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public enum UnstableToolMaterial implements Tier {

    UNSTABLE(ModTags.INCORRECT_FOR_UNSTABLE_TOOL, 4.5F, 9.5F, 2501, 4);

    private final TagKey<Block> incorrectBlocksForDrops;
    private final float attackDamage, speed;
    private final int uses, enchantability;

    UnstableToolMaterial(TagKey<Block> tag, float attackDamage, float speed, int uses, int enchantability) {
        this.incorrectBlocksForDrops = tag;
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.uses = uses;
        this.enchantability = enchantability;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

}
