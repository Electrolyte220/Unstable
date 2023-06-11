package electrolyte.unstable.items;

import electrolyte.unstable.UnstableConfig;
import electrolyte.unstable.UnstableEnums;
import electrolyte.unstable.helper.PseudoInversionRitualHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

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
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
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
       tooltip.add(Component.translatable("unstable.division_sigil.tooltip.active", Component.translatable(String.valueOf(this.getMaxDamage(stack) - this.getDamage(stack)))).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getLevel().isClientSide) return InteractionResult.FAIL;
        ResourceLocation dim = context.getLevel().dimension().location();
        boolean everythingCorrect = true;
        if(dim.equals(BuiltinDimensionTypes.OVERWORLD.location())) {
            context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.dimension.overworld").withStyle(ChatFormatting.RED));
            return InteractionResult.SUCCESS;
        } else if(dim.equals(BuiltinDimensionTypes.NETHER.location())) {
            context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.dimension.nether").withStyle(ChatFormatting.RED));
            return InteractionResult.SUCCESS;
        }
        if(PseudoInversionRitualHelper.checkBeacon(context.getLevel(), context.getClickedPos())) {
            if (PseudoInversionRitualHelper.checkChestsPos(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.chest").withStyle(ChatFormatting.WHITE));
            } else {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.chest_fail").withStyle(ChatFormatting.RED));
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkIndividualChestContents(context.getLevel(), context.getClickedPos().north(5), UnstableEnums.CHEST_LOCATION.NORTH)) {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.north_chest").withStyle(ChatFormatting.WHITE));
            } else {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.north_chest_fail").withStyle(ChatFormatting.RED));
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkIndividualChestContents(context.getLevel(), context.getClickedPos().south(5), UnstableEnums.CHEST_LOCATION.SOUTH)) {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.south_chest").withStyle(ChatFormatting.WHITE));
            } else {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.south_chest_fail").withStyle(ChatFormatting.RED));
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkIndividualChestContents(context.getLevel(), context.getClickedPos().east(5), UnstableEnums.CHEST_LOCATION.EAST)) {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.east_chest").withStyle(ChatFormatting.WHITE));
            } else {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.east_chest_fail").withStyle(ChatFormatting.RED));
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkIndividualChestContents(context.getLevel(), context.getClickedPos().west(5), UnstableEnums.CHEST_LOCATION.WEST)) {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.west_chest").withStyle(ChatFormatting.WHITE));
            } else {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.west_chest_fail").withStyle(ChatFormatting.RED));
                everythingCorrect = false;
            }
            if (PseudoInversionRitualHelper.checkRedstoneAndString(context.getLevel(), context.getClickedPos())) {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.redstone_string").withStyle(ChatFormatting.WHITE));
            } else {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.redstone_string_fail").withStyle(ChatFormatting.RED));
                everythingCorrect = false;
            }
            if (everythingCorrect) {
                context.getPlayer().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.correct_setup").withStyle(ChatFormatting.WHITE));
            }
             return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
