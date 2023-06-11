package electrolyte.unstable.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class DivideByDiamondDamageSource extends DamageSource {

    public static final DivideByDiamondDamageSource INSTANCE = new DivideByDiamondDamageSource();

    public DivideByDiamondDamageSource() {
        super("unstable:divide_by_diamond");
        this.bypassArmor();
        this.bypassMagic();
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        return Component.translatable(pLivingEntity.getDisplayName().getString() + " suffered a fatal 'java.lang.ArithmeticException: \\ by diamond'");
    }
}
