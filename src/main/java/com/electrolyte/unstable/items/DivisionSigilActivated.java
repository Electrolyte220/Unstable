package com.electrolyte.unstable.items;

import com.electrolyte.unstable.UnstableConfig;
import com.electrolyte.unstable.UnstableEnums;
import com.electrolyte.unstable.helper.PseudoInversionRitualHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import javax.annotation.Nullable;
import java.util.List;

public class DivisionSigilActivated extends Item {
    public DivisionSigilActivated(Item.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack item = itemStack.copy();
        item.setDamageValue(item.getDamageValue() + 1);
        if(item.getDamageValue() >= getMaxDamage(item)) {
            item.shrink(1);
        }
        return item;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return UnstableConfig.ACTIVATED_DURABILITY.get();
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if(!stack.hasTag()) return;
       tooltip.add(new TranslatableComponent("unstable.division_sigil.tooltip.active", new TranslatableComponent(String.valueOf(this.getMaxDamage(stack) - this.getDamage(stack)))).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide) return InteractionResult.FAIL;
        ResourceLocation dim = context.getLevel().dimension().location();
        boolean everythingCorrect = true;
        if(dim.equals(DimensionType.OVERWORLD_LOCATION.location())) {
            context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.dimension.overworld").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
            return InteractionResult.SUCCESS;
        } else if(dim.equals(DimensionType.NETHER_LOCATION.location())) {
            context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.dimension.nether").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
            return InteractionResult.SUCCESS;
        }
        if(PseudoInversionRitualHelper.checkBeacon(context.getLevel(), context.getClickedPos())) {
            if (PseudoInversionRitualHelper.checkChestsPos(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkIndividualChestContents(context.getLevel(), context.getClickedPos().north(5), UnstableEnums.CHEST_LOCATION.NORTH)) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.north_chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.north_chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkIndividualChestContents(context.getLevel(), context.getClickedPos().south(5), UnstableEnums.CHEST_LOCATION.SOUTH)) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.south_chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.south_chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkIndividualChestContents(context.getLevel(), context.getClickedPos().east(5), UnstableEnums.CHEST_LOCATION.EAST)) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.east_chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.east_chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkIndividualChestContents(context.getLevel(), context.getClickedPos().west(5), UnstableEnums.CHEST_LOCATION.WEST)) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.west_chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.west_chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkRedstoneAndString(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.redstone_string").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.redstone_string_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                everythingCorrect = false;
            }
            if (everythingCorrect) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.correct_setup").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            }
             return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
