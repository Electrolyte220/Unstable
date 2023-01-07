package com.electrolyte.unstable.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class HealingAxeDamageSource extends DamageSource {

    public static final HealingAxeDamageSource INSTANCE = new HealingAxeDamageSource();

    public HealingAxeDamageSource() {
        super("unstable:healing_axe");
        this.bypassArmor();
        this.bypassMagic();
        this.setScalesWithDifficulty();
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        return new TranslatableComponent(entityLivingBaseIn.getDisplayName().getString() + " did not watch their health!");
    }
}
