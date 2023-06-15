package electrolyte.unstable.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class UnstableIngot extends Item {

    public UnstableIngot(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("explodesIn", 200);
        stack.setTag(tag);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pStack.getTag() == null) {
            pTooltipComponents.add(Component.translatable("unstable.unstable_ingot.tooltip.unstable").withStyle(ChatFormatting.RED));
            pTooltipComponents.add(Component.translatable("unstable.unstable_ingot.tooltip.crafting").withStyle(ChatFormatting.GRAY));
        } else {
            CompoundTag tag = pStack.getTag();
            if (pStack.getTag().getBoolean("creativeSpawned")) {
                pTooltipComponents.add(Component.translatable("unstable.unstable_ingot.tooltip.creative").withStyle(ChatFormatting.GRAY));
            } else {
                pTooltipComponents.add(Component.translatable("unstable.unstable_ingot.tooltip.explode_timer", Component.translatable(new DecimalFormat("#.#").format(tag.getInt("explodesIn") / 20.0))).withStyle(ChatFormatting.GRAY));
            }
        }
    }
}