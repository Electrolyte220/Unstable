package com.electrolyte.unstable.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulFragment extends Item {

    public SoulFragment(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(stack.getTag() != null && stack.getTag().contains("playerUUID") &&
                pPlayer.getUUID().equals(stack.getTag().getUUID("playerUUID")) && pPlayer.getMaxHealth() <= 18) {
            if(!stack.getTag().getBoolean("weakSoul")) {
                pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(pPlayer.getAttribute(Attributes.MAX_HEALTH).getValue() + 2);
                stack.setCount(stack.getCount() - 1);
                return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
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
