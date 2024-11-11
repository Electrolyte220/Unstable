package dev.electrolyte.unstable.items.tools;

import com.mojang.authlib.GameProfile;
import dev.electrolyte.unstable.registry.ModComponents;
import dev.electrolyte.unstable.registry.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;

public class EthericSword extends SwordItem {

    public EthericSword(Tier pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack ethericSwordStack = pPlayer.getItemInHand(pUsedHand);
        ItemStack soulFragmentStack = new ItemStack(ModItems.SOUL_FRAGMENT.get());
        if(pPlayer instanceof FakePlayer || !pPlayer.isCrouching() || pLevel.isClientSide || ethericSwordStack.getDamageValue() != 0) return InteractionResultHolder.pass(ethericSwordStack);
        soulFragmentStack.set(DataComponents.PROFILE, new ResolvableProfile(new GameProfile(pPlayer.getUUID(), pPlayer.getName().getString())));
        if (pPlayer.getHealth() > 2) {
            pPlayer.hurt(pPlayer.damageSources().generic(), 2.0F);
            if(pPlayer.getMaxHealth() > 6) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(pPlayer.getAttribute(Attributes.MAX_HEALTH).getValue() - 2);
            } else {
                soulFragmentStack.set(ModComponents.WEAK_SOUL, true);
            }
            pPlayer.setItemInHand(pUsedHand, soulFragmentStack);
            return InteractionResultHolder.success(soulFragmentStack);
        }
        return InteractionResultHolder.pass(ethericSwordStack);
    }
}