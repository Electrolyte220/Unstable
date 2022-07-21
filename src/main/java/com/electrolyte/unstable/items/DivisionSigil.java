package com.electrolyte.unstable.items;

import com.electrolyte.unstable.DivisionCheck;
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
        if(DivisionCheck.checkEnchantTable(context.getLevel(), context.getClickedPos())) {
            context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "Activation Ritual"), context.getPlayer().getUUID());
            if(DivisionCheck.checkNatural(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "- Altar has sufficient natural earth"), context.getPlayer().getUUID());
            } else if(!DivisionCheck.checkNatural(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "! Altar lacks sufficient natural earth"), context.getPlayer().getUUID());
            } if(DivisionCheck.checkLight(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "- Altar is in darkness"), context.getPlayer().getUUID());
            } else if(!DivisionCheck.checkLight(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "! Altar must not be lit by outside sources"), context.getPlayer().getUUID());
            } if(DivisionCheck.checkSky(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "- Altar can see the moon"), context.getPlayer().getUUID());
            } else if (!DivisionCheck.checkSky(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "! Altar cannot see the moon"), context.getPlayer().getUUID());
            }if(DivisionCheck.checkRedstone(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "- Altar has a redstone circle"), context.getPlayer().getUUID());
            } else if(!DivisionCheck.checkRedstone(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "! Altar does not have a redstone circle"), context.getPlayer().getUUID());
            } if(DivisionCheck.checkTime(context.getLevel().getDayTime())) {
                context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "- Time is right"), context.getPlayer().getUUID());
            } if(!DivisionCheck.checkTime(context.getLevel().getDayTime())) {
                if(context.getLevel().getDayTime() <= 15000) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "! Too early, sacrifice must be made at midnight"), context.getPlayer().getUUID());
                } else if (context.getLevel().getDayTime() >= 20000) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "! Too late, sacrifice must be made at midnight"), context.getPlayer().getUUID());
                }
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack stack) {
        return DivisionCheck.checkTime(Minecraft.getInstance().level.getDayTime());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag isAdvanced) {
            tooltip.add(new TranslatableComponent("unstable.chat.inactive_1"));
            tooltip.add(new TranslatableComponent("unstable.chat.inactive_2"));
            tooltip.add(new TranslatableComponent("unstable.chat.inactive_3"));
    }
}
