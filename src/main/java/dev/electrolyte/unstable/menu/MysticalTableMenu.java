package dev.electrolyte.unstable.menu;

import dev.electrolyte.unstable.recipe.MysticalTableRecipe;
import dev.electrolyte.unstable.registry.ModBlocks;
import dev.electrolyte.unstable.registry.ModMenus;
import dev.electrolyte.unstable.registry.ModRecipes;
import dev.electrolyte.unstable.slot.MysticalTableResultSlot;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

public class MysticalTableMenu extends AbstractContainerMenu {

    private final CraftingContainer craftSlots;
    private final ResultContainer resultSlots;
    private final ContainerLevelAccess access;
    private final Player player;

    public MysticalTableMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public MysticalTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenus.MYSTICAL_TABLE_MENU.get(), containerId);
        this.craftSlots = new TransientCraftingContainer(this, 3, 3);
        this.resultSlots = new ResultContainer();
        this.access = access;
        this.player = playerInventory.player;
        this.addSlot(new MysticalTableResultSlot(playerInventory.player, this.craftSlots, this.resultSlots, 0, 124, 35));

        int l;
        int i1;
        for(l = 0; l < 3; ++l) {
            for(i1 = 0; i1 < 3; ++i1) {
                this.addSlot(new Slot(this.craftSlots, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
            }
        }

        for(l = 0; l < 3; ++l) {
            for(i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftSlots, ResultContainer resultSlots, @Nullable RecipeHolder<MysticalTableRecipe> recipe) {
        if (!level.isClientSide) {
            CraftingInput craftingInput = craftSlots.asCraftInput();
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<RecipeHolder<MysticalTableRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(ModRecipes.MYSTICAL_TABLE_RECIPE_TYPE.get(), craftingInput, level, recipe);
            if (optional.isPresent()) {
                RecipeHolder<MysticalTableRecipe> holder = optional.get();
                MysticalTableRecipe tableRecipe = holder.value();
                if (resultSlots.setRecipeUsed(level, serverPlayer, holder)) {
                    itemstack = tableRecipe.assemble(craftingInput, level.registryAccess());
                }
            }
            resultSlots.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, itemstack));
        }
    }

    @Override
    public void slotsChanged(Container container) {
        this.access.execute((level, pos) -> slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots, null));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, pos) -> this.clearContainer(player, this.craftSlots));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                this.access.execute((p_39378_, p_39379_) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, p_39378_, player);
                });
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 10 && index < 46) {
                if (!this.moveItemStackTo(itemstack1, 1, 10, false)) {
                    if (index < 37) {
                        if (!this.moveItemStackTo(itemstack1, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 10, 46, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
            if (index == 0) {
                player.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, ModBlocks.MYSTICAL_TABLE.get());
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }
}
