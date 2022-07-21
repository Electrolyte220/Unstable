package com.electrolyte.unstable.items;

import com.electrolyte.unstable.damagesource.DivideByDiamondDamageSource;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class UnstableIngot extends Item {

    public UnstableIngot(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("explodesIn", 200);
        stack.setTag(tag);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        CompoundTag tag = pStack.getTag();
        if(tag != null) {
            int timeToExplode = tag.getInt("explodesIn");
            if(timeToExplode > 0) {
                timeToExplode--;
                tag.putInt("explodesIn", timeToExplode);
                pStack.setTag(tag);
            }
            if(timeToExplode == 0) {
                pStack.shrink(1);
                pLevel.explode(pEntity, pEntity.getX(), pEntity.getY(), pEntity.getZ(), 1, false, Explosion.BlockInteraction.NONE);
                if(pEntity instanceof LivingEntity livingEntity) {
                    pEntity.hurt(new DivideByDiamondDamageSource(), livingEntity.getMaxHealth());
                }
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        //TODO: fix this
            item.shrink(1);
            player.level.explode(player, player.getX(), player.getY(), player.getZ(), 1, false, Explosion.BlockInteraction.NONE);
            player.hurt(new DivideByDiamondDamageSource(), player.getMaxHealth());
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(new TranslatableComponent(ChatFormatting.WHITE + "Warning: Highly Unstable!"));
        CompoundTag tag = pStack.getTag();
        if(tag == null) {
            pTooltipComponents.add(new TranslatableComponent(ChatFormatting.WHITE + "Creative Spawned - Will not Explode"));
        } else {
            pTooltipComponents.add(new TranslatableComponent(ChatFormatting.WHITE + "Explodes In: " + new DecimalFormat("#.#").format(tag.getInt("explodesIn") / 20.0)));
        }
    }
}
