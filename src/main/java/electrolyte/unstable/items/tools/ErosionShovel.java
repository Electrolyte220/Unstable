package electrolyte.unstable.items.tools;

import electrolyte.unstable.Unstable;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;

public class ErosionShovel extends ShovelItem {


    public ErosionShovel(Tier tier, float attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> list) {
        if (category == Unstable.UNSTABLE_TAB) {
            ItemStack stack = new ItemStack(this);
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("Unbreakable", true);
            stack.setTag(tag);
            stack.enchant(Enchantments.BLOCK_EFFICIENCY, 5);
            list.add(stack);
        }
    }
}
