package electrolyte.unstable.items.tools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class ErosionShovel extends ShovelItem {


    public ErosionShovel(Tier tier, float attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}