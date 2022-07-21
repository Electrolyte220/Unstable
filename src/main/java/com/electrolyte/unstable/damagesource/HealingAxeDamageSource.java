package com.electrolyte.unstable.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class HealingAxeDamageSource extends DamageSource {

    public static final HealingAxeDamageSource INSTANCE = new HealingAxeDamageSource();

    public HealingAxeDamageSource() {
        super("com/electrolyte/unstable/damagesource/healing_axe");

        setScalesWithDifficulty();
    }

    @Override
    public @NotNull Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        return new TranslatableComponent(entityLivingBaseIn.getDisplayName().getString() + " did not watch their health!");
    }
}
