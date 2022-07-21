package com.electrolyte.unstable.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class DivideByDiamondDamageSource extends DamageSource {

    public static final DivideByDiamondDamageSource INSTANCE = new DivideByDiamondDamageSource();

    public DivideByDiamondDamageSource() {
        super("com/electrolyte/unstable/damagesource/divide_by_diamond");
        this.bypassArmor();
        this.bypassMagic();
    }

    @Override
    public @NotNull Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        return new TranslatableComponent(pLivingEntity.getDisplayName().getString() + " suffered a fatal 'java.lang.ArithmeticException: \\ by diamond'");
    }


}
