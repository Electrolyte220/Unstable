package com.electrolyte.unstable.items;

import com.electrolyte.unstable.helper.ActivationRitualHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class DivisionSigil extends Item {

    public DivisionSigil(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide) return InteractionResult.FAIL;
        if(ActivationRitualHelper.checkEnchantTable(context.getLevel(), context.getClickedPos())) {
            context.getPlayer().sendMessage(new TranslatableComponent("unstable.activation_ritual.title").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            if(ActivationRitualHelper.checkNatural(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.activation_ritual.natural_earth").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else if(!ActivationRitualHelper.checkNatural(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.activation_ritual.natural_earth_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
            } if(ActivationRitualHelper.checkLight(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.activation_ritual.darkness").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else if(!ActivationRitualHelper.checkLight(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent( "unstable.activation_ritual.darkness_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
            } if(ActivationRitualHelper.checkSky(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent( "unstable.activation_ritual.sky").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else if (!ActivationRitualHelper.checkSky(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent( "unstable.activation_ritual.sky_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
            }if(ActivationRitualHelper.checkRedstone(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent( "unstable.activation_ritual.redstone").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } else if(!ActivationRitualHelper.checkRedstone(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.activation_ritual.redstone_fail").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
            } if(ActivationRitualHelper.checkTime(context.getLevel().getDayTime())) {
                context.getPlayer().sendMessage(new TranslatableComponent("unstable.activation_ritual.time").withStyle(ChatFormatting.WHITE), context.getPlayer().getUUID());
            } if(!ActivationRitualHelper.checkTime(context.getLevel().getDayTime())) {
                if(context.getLevel().getDayTime() <= 15000) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.activation_ritual.time_early").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                } else if (context.getLevel().getDayTime() >= 20000) {
                    context.getPlayer().sendMessage(new TranslatableComponent("unstable.activation_ritual.time_late").withStyle(ChatFormatting.RED), context.getPlayer().getUUID());
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack stack) {
        return ActivationRitualHelper.checkTime(Minecraft.getInstance().level.getDayTime());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag isAdvanced) {
        tooltip.add(new TranslatableComponent("unstable.division_sigil.tooltip_inactive_1").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("unstable.division_sigil.tooltip_inactive_2").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("unstable.division_sigil.tooltip_inactive_3").withStyle(ChatFormatting.GRAY));
    }
}
