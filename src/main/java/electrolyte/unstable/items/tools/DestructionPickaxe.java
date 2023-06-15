package electrolyte.unstable.items.tools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class DestructionPickaxe extends PickaxeItem {

    public DestructionPickaxe(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }
}