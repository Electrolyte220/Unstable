package com.electrolyte.unstable.items;

import com.electrolyte.unstable.DivisionCheckEnd;
import com.electrolyte.unstable.UnstableConfig;
import com.electrolyte.unstable.UnstableEventHandler;
import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.end_siege.UnstableEntityDataStorage;
import com.electrolyte.unstable.end_siege.UnstableEntityDataStorageManager;
import com.electrolyte.unstable.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

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
       tooltip.add(new TranslatableComponent("ACTIVE: Number of uses remaining - " + (this.getMaxDamage(stack) - this.getDamage(stack))));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide) return InteractionResult.FAIL;
        ResourceLocation dim = context.getPlayer().level.dimension().location();
        if(dim.equals(DimensionType.OVERWORLD_LOCATION.location())) context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "This dimension has too much natural earth."), context.getPlayer().getUUID());
        if(dim.equals(DimensionType.NETHER_LOCATION.location())) context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "This dimension is too hot."), context.getPlayer().getUUID());
        if(dim.equals(DimensionType.END_LOCATION.location())) {
           if(DivisionCheckEnd.checkBeacon(context.getLevel(), context.getClickedPos())) {
               Unstable.LOGGER.info(UnstableConfig.ACTIVATED_DURABILITY.get().toString());
                if(DivisionCheckEnd.checkEndBlocks(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "Area is clear"), context.getPlayer().getUUID());
                } else if(!DivisionCheckEnd.checkEndBlocks(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "Area is not clear"), context.getPlayer().getUUID());
                } if(DivisionCheckEnd.checkChests(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "Chests in correct location"), context.getPlayer().getUUID());
                } else if(!DivisionCheckEnd.checkChests(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "Chests are not in correct location"), context.getPlayer().getUUID());
                } if(DivisionCheckEnd.checkNorthChestContents(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "North Chest Contents are correct"), context.getPlayer().getUUID());
                } else if(!DivisionCheckEnd.checkNorthChestContents(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "North Chest Contents are not correct"), context.getPlayer().getUUID());
                } if(DivisionCheckEnd.checkSouthChestContents(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "South Chest Contents are correct"), context.getPlayer().getUUID());
                } else if(!DivisionCheckEnd.checkSouthChestContents(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "South Chest Contents are not correct"), context.getPlayer().getUUID());
                } if(DivisionCheckEnd.checkEastChestContents(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "East Chest Contents are correct"), context.getPlayer().getUUID());
                } else if(!DivisionCheckEnd.checkEastChestContents(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "East Chest Contents are not correct"), context.getPlayer().getUUID());
                } if(DivisionCheckEnd.checkWestChestContents(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "West Chest Contents are correct"), context.getPlayer().getUUID());
                } else if(!DivisionCheckEnd.checkWestChestContents(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "West Chest Contents are not correct"), context.getPlayer().getUUID());
                } if(DivisionCheckEnd.checkRedstoneAndString(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "Redstone and String Placement is correct"), context.getPlayer().getUUID());
                } else if(!DivisionCheckEnd.checkRedstoneAndString(context.getLevel(), context.getClickedPos())) {
                    context.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "Redstone and String Placement is incorrect"), context.getPlayer().getUUID());
                }
            }
        }
        return InteractionResult.FAIL;
    }
}
