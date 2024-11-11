package dev.electrolyte.unstable.items;

import dev.electrolyte.unstable.registry.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;

import java.util.List;

public class SoulFragment extends Item {

    public SoulFragment(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(stack.has(DataComponents.PROFILE)) {
            ResolvableProfile profile = stack.get(DataComponents.PROFILE);
            if(pPlayer.getUUID().equals(profile.id().get()) && pPlayer.getMaxHealth() <= 18) {
                if(!stack.has(ModComponents.WEAK_SOUL)) {
                    pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(pPlayer.getAttribute(Attributes.MAX_HEALTH).getValue() + 2);
                    stack.setCount(stack.getCount() - 1);
                    return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide);
                }
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(!pStack.has(DataComponents.PROFILE) && !pStack.has(ModComponents.WEAK_SOUL)) {
            pTooltipComponents.add(Component.translatable("unstable.soul_fragment.tooltip.empty").withStyle(ChatFormatting.RED));
        } if(pStack.has(DataComponents.PROFILE)) {
            ResolvableProfile profile = pStack.get(DataComponents.PROFILE);
            pTooltipComponents.add(Component.translatable("unstable.soul_fragment.tooltip.owner", Component.literal(profile.name().get()).withStyle(ChatFormatting.GRAY)));
        } if(pStack.has(ModComponents.WEAK_SOUL)) {
            pTooltipComponents.add(Component.translatable("unstable.soul_fragment.tooltip.weak_soul"));
        }
    }
}
