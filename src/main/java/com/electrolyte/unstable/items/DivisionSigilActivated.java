package com.electrolyte.unstable.items;

import com.electrolyte.unstable.helper.PseudoInversionRitualHelper;
import com.electrolyte.unstable.UnstableConfig;
import com.electrolyte.unstable.listener.EndSiegeChestDataReloadListener;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

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
    public boolean isRepairable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return UnstableConfig.ACTIVATED_DURABILITY.get();
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if(!stack.hasTag()) return;
       tooltip.add(new TranslatableComponent("unstable.division_sigil.tooltip.active", new TranslatableComponent(String.valueOf(this.getMaxDamage(stack) - this.getDamage(stack)))).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide) return InteractionResult.FAIL;
        ResourceLocation dim = context.getPlayer().level.dimension().location();
        boolean everythingCorrect = true;
        if(PseudoInversionRitualHelper.checkDimension(dim)) {
           if(PseudoInversionRitualHelper.checkBeacon(context.getLevel(), context.getClickedPos())) {
                if(PseudoInversionRitualHelper.checkEndBlocks(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.area").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
                } else if(!PseudoInversionRitualHelper.checkEndBlocks(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.area_fail"), context.getPlayer().getUUID());
                    everythingCorrect = false;
                } if(PseudoInversionRitualHelper.checkChests(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
                } else if(!PseudoInversionRitualHelper.checkChests(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                    everythingCorrect = false;
                } if(PseudoInversionRitualHelper.checkChestContents(context.getLevel(), context.getClickedPos().north(5), EndSiegeChestDataReloadListener.CHEST_LOCATION.NORTH)) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.north_chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
                } else if(!PseudoInversionRitualHelper.checkChestContents(context.getLevel(), context.getClickedPos().north(5), EndSiegeChestDataReloadListener.CHEST_LOCATION.NORTH)) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.north_chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                    everythingCorrect = false;
                } if(PseudoInversionRitualHelper.checkChestContents(context.getLevel(), context.getClickedPos().south(5), EndSiegeChestDataReloadListener.CHEST_LOCATION.SOUTH)) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.south_chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
                } else if(!PseudoInversionRitualHelper.checkChestContents(context.getLevel(), context.getClickedPos().south(5), EndSiegeChestDataReloadListener.CHEST_LOCATION.SOUTH)) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.south_chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                    everythingCorrect = false;
                } if(PseudoInversionRitualHelper.checkChestContents(context.getLevel(), context.getClickedPos().east(5), EndSiegeChestDataReloadListener.CHEST_LOCATION.EAST)) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.east_chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
                } else if(!PseudoInversionRitualHelper.checkChestContents(context.getLevel(), context.getClickedPos().east(5), EndSiegeChestDataReloadListener.CHEST_LOCATION.EAST)) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.east_chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                    everythingCorrect = false;
                } if(PseudoInversionRitualHelper.checkChestContents(context.getLevel(), context.getClickedPos().west(5), EndSiegeChestDataReloadListener.CHEST_LOCATION.WEST)) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.west_chest").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
                } else if(!PseudoInversionRitualHelper.checkChestContents(context.getLevel(), context.getClickedPos().west(5), EndSiegeChestDataReloadListener.CHEST_LOCATION.WEST)) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.west_chest_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                    everythingCorrect = false;
                } if(PseudoInversionRitualHelper.checkRedstoneAndString(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.redstone_string").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
                } else if(!PseudoInversionRitualHelper.checkRedstoneAndString(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.redstone_string_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                    everythingCorrect = false;
                } if(everythingCorrect) {
                   context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.correct_setup").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
               }
            }
        } else if(dim.equals(DimensionType.OVERWORLD_LOCATION.location())) {
            context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.dimension.overworld").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
        } else if(dim.equals(DimensionType.NETHER_LOCATION.location())) {
            context.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.dimension.nether").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
        }
        return InteractionResult.FAIL;
    }
}
