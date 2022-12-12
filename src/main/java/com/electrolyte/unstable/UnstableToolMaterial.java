package com.electrolyte.unstable;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public enum UnstableToolMaterial implements Tier {

    UNSTABLE(4.5F, 9.5F, 2501, 4, 0);

    private final float attackDamage, speed;
    private final int uses, harvestLevel, enchantability;

    UnstableToolMaterial(float attackDamage, float speed, int uses, int harvestLevel, int enchantability) {
        this.attackDamage = attackDamage;
        this.speed = speed;
        this.uses = uses;
        this.harvestLevel = harvestLevel;
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
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

}
