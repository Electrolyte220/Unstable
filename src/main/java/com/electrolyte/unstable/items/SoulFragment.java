package com.electrolyte.unstable.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulFragment extends Item {

    public SoulFragment(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        if(!pLevel.isClientSide) {
            pStack.getOrCreateTag().putUUID("playerUUID", pPlayer.getUUID());
            if (pPlayer.getMaxHealth() > 6) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(pPlayer.getAttribute(Attributes.MAX_HEALTH).getValue() - 2);
                pPlayer.hurt(DamageSource.GENERIC, 2.0F);
            } else {
                pStack.getOrCreateTag().putBoolean("weakSoul", true);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pPlayer.getItemInHand(pUsedHand).getTag() != null && pPlayer.getItemInHand(pUsedHand).getTag().contains("playerUUID") &&
                pPlayer.getUUID().equals(pPlayer.getItemInHand(pUsedHand).getTag().getUUID("playerUUID")) && pPlayer.getMaxHealth() <= 18) {
            if(!pPlayer.getItemInHand(pUsedHand).getTag().getBoolean("weakSoul")) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(pPlayer.getAttribute(Attributes.MAX_HEALTH).getValue() + 2);
                pPlayer.getInventory().removeFromSelected(true);
            }
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if(pStack.getTag() == null) {
            pTooltipComponents.add(new TranslatableComponent("unstable.soul_fragment.tooltip_empty").withStyle(ChatFormatting.RED));
        }  else {
            pTooltipComponents.add(new TranslatableComponent("unstable.soul_fragment.tooltip_owner", new TranslatableComponent(pLevel.getPlayerByUUID(pStack.getTag().getUUID("playerUUID")).getName().getContents())).withStyle(ChatFormatting.GRAY));
            if(pStack.getTag().getBoolean("weakSoul")) {
                pTooltipComponents.add(new TranslatableComponent("unstable.soul_fragment.tooltip_weak_soul"));
            }
        }
    }
}
