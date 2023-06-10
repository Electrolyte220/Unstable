package electrolyte.unstable.items.tools;

import electrolyte.unstable.Unstable;
import electrolyte.unstable.init.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;

public class EthericSword extends SwordItem {

    public EthericSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return false;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> list) {
        if(category == Unstable.UNSTABLE_TAB) {
            ItemStack stack = new ItemStack(this);
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("Unbreakable", true);
            stack.setTag(tag);
            stack.enchant(Enchantments.SHARPNESS, 5);
            list.add(stack);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack ethericSwordStack = pPlayer.getItemInHand(pUsedHand);
        ItemStack soulFragmentStack = new ItemStack(ModItems.SOUL_FRAGMENT.get());
        if(pPlayer instanceof FakePlayer || !pPlayer.isCrouching() || pLevel.isClientSide || ethericSwordStack.getDamageValue() != 0) return InteractionResultHolder.pass(ethericSwordStack);
        soulFragmentStack.getOrCreateTag().putUUID("playerUUID", pPlayer.getUUID());
        if (pPlayer.getHealth() > 2) {
            pPlayer.hurt(DamageSource.GENERIC, 2.0F);
            if(pPlayer.getMaxHealth() > 6) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(pPlayer.getAttribute(Attributes.MAX_HEALTH).getValue() - 2);
            } else {
                soulFragmentStack.getOrCreateTag().putBoolean("weakSoul", true);
            }
            pPlayer.setItemInHand(pUsedHand, soulFragmentStack);
            return InteractionResultHolder.success(soulFragmentStack);
        }
        return InteractionResultHolder.pass(ethericSwordStack);
    }
}
