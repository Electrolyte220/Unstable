package dev.electrolyte.unstable.slot;

import dev.electrolyte.unstable.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public class MysticalTableResultSlot extends ResultSlot {
    public MysticalTableResultSlot(Player player, CraftingContainer craftSlots, Container container, int slot, int xPosition, int yPosition) {
        super(player, craftSlots, container, slot, xPosition, yPosition);
    }

    @Override
    public void onTake(Player player, ItemStack stack) {
        CraftingInput.Positioned inputPositioned = this.craftSlots.asPositionedCraftInput();
        CraftingInput input = inputPositioned.input();
        int i = inputPositioned.left();
        int j = inputPositioned.top();
        NonNullList<ItemStack> list = player.level().getRecipeManager().getRemainingItemsFor(ModRecipes.MYSTICAL_TABLE_RECIPE_TYPE.get(), input, player.level());
        for(int k = 0; k < input.height(); k++) {
            for(int l = 0; l < input.width(); l++) {
                int i1 = l + i + (k + j) * this.craftSlots.getWidth();
                ItemStack itemStack = this.craftSlots.getItem(i1);
                ItemStack itemStack1 = list.get(l + k * input.width());
                if(!itemStack.isEmpty()) {
                    this.craftSlots.removeItem(i1, 1);
                    itemStack = this.craftSlots.getItem(i1);
                }

                if(!itemStack1.isEmpty()) {
                    if(itemStack.isEmpty()) {
                        this.craftSlots.setItem(i1, itemStack1);
                    } else if(ItemStack.isSameItemSameComponents(itemStack, itemStack1)) {
                        itemStack1.grow(itemStack.getCount());
                        this.craftSlots.setItem(i1, itemStack1);
                    } else if(!this.player.getInventory().add(itemStack1)) {
                        this.player.drop(itemStack1, false);
                    }
                }
            }
        }
    }
}
