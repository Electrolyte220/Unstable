package dev.electrolyte.unstable.items;

import dev.electrolyte.unstable.registry.ModComponents;
import dev.electrolyte.unstable.registry.ModDamageTypes;
import dev.electrolyte.unstable.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.text.DecimalFormat;
import java.util.List;

public class UnstableIngot extends Item {

    public UnstableIngot(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.has(ModComponents.CREATIVE_SPAWNED)) {
            pTooltipComponents.add(Component.translatable("unstable.unstable_ingot.tooltip.creative").withStyle(ChatFormatting.GRAY));
        } else {
            pTooltipComponents.add(Component.translatable("unstable.unstable_ingot.tooltip.unstable").withStyle(ChatFormatting.RED));
            pTooltipComponents.add(Component.translatable("unstable.unstable_ingot.tooltip.crafting").withStyle(ChatFormatting.GRAY));
            if(pStack.has(ModComponents.EXPLODE_TIMER)) {
                pTooltipComponents.add(Component.translatable("unstable.unstable_ingot.tooltip.explode_timer", Component.translatable(new DecimalFormat("#.#").format(pStack.get(ModComponents.EXPLODE_TIMER) / 20.0))).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if(!(entity instanceof Player player)) {
            super.inventoryTick(stack, level, entity, slotId, isSelected);
            return;
        }
        if(checkItem(stack)) unstableIngotFail(player);
    }

    public boolean checkItem(ItemStack stack) {
        if(!stack.has(ModComponents.CREATIVE_SPAWNED)) {
            if(stack.has(ModComponents.EXPLODE_TIMER) && stack.get(ModComponents.EXPLODE_TIMER) > 0) {
                stack.update(ModComponents.EXPLODE_TIMER, 100, c -> c - 1);
            }
            if (stack.has(ModComponents.EXPLODE_TIMER) && stack.get(ModComponents.EXPLODE_TIMER) <= 0) {
                stack.update(ModComponents.EXPLODED, false, c -> true);
                return true;
            }
        }
        return false;
    }

    public static void unstableIngotFail(Player player) {
        player.getInventory().clearOrCountMatchingItems(s ->
                s.is(ModItems.UNSTABLE_INGOT.get()) &&
                        !s.has(ModComponents.CREATIVE_SPAWNED), -1, player.getInventory());
        player.level().explode(null, player.getBlockX(), player.getBlockY(), player.getBlockZ(), 0.25f, false, Level.ExplosionInteraction.NONE);
        player.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.DIVIDE_BY_DIAMOND), Float.MAX_VALUE);
    }
}