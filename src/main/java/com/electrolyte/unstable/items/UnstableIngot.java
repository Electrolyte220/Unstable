package com.electrolyte.unstable.items;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.damagesource.DivideByDiamondDamageSource;
import com.electrolyte.unstable.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    //TODO: Double check and make sure there are no ways to cheese the ingots.
    /*@Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pStack.hasTag()) {
            CompoundTag tag = pStack.getOrCreateTag();
            if (!tag.getBoolean("creativeSpawned")) {
                int timeToExplode = tag.getInt("explodesIn");
                if (timeToExplode > 0) {
                    timeToExplode--;
                    tag.putInt("explodesIn", timeToExplode);
                    pStack.setTag(tag);
                }
                if (timeToExplode == 0) {
                    pStack.shrink(1);
                    pLevel.explode(pEntity, pEntity.getX(), pEntity.getY(), pEntity.getZ(), 1, false, Explosion.BlockInteraction.NONE);
                    if (pEntity instanceof LivingEntity) {
                        pEntity.hurt(DivideByDiamondDamageSource.INSTANCE, Float.MAX_VALUE);
                    }
                }
            }
        }
    }*/

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if(pStack.getTag() == null) {
            pTooltipComponents.add(new TranslatableComponent("unstable.unstable_ingot.tooltip.unstable").withStyle(ChatFormatting.RED));
            pTooltipComponents.add(new TranslatableComponent("unstable.unstable_ingot.tooltip.crafting").withStyle(ChatFormatting.GRAY));
        } else {
            CompoundTag tag = pStack.getTag();
            if (pStack.getTag().getBoolean("creativeSpawned")) {
                pTooltipComponents.add(new TranslatableComponent("unstable.unstable_ingot.tooltip.creative").withStyle(ChatFormatting.GRAY));
            } else {
                pTooltipComponents.add(new TranslatableComponent("unstable.unstable_ingot.tooltip.explode_timer", new TranslatableComponent(new DecimalFormat("#.#").format(tag.getInt("explodesIn") / 20.0))).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab pCategory, @NotNull NonNullList<ItemStack> pItems) {
        if(pCategory == Unstable.UNSTABLE_TAB) {
            ItemStack unstableIngot = new ItemStack(ModItems.UNSTABLE_INGOT.get());
            unstableIngot.getOrCreateTag().putBoolean("creativeSpawned", true);
            pItems.add(unstableIngot);
        }
    }
}
