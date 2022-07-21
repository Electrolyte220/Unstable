package com.electrolyte.unstable.tools;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.init.ModTools;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ReversingHoe extends TieredItem {

    protected static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT_PATH, Blocks.FARMLAND.defaultBlockState(), Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(), Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState()));

    public ReversingHoe(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, @NotNull Level level, @NotNull Player playerIn) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("Unbreakable", true);
        stack.setTag(nbt);
    }
    
    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if(player.isCrouching()) {
            if (block == Blocks.COBBLESTONE) {
                BlockState newState = Blocks.STONE.defaultBlockState();
                if(!level.isClientSide) level.setBlock(pos, newState, 11);
                return InteractionResult.SUCCESS;
            } else if (block == Blocks.DIRT) {
                BlockState newState = Blocks.GRASS_BLOCK.defaultBlockState();
                if(!level.isClientSide) level.setBlock(pos, newState, 11);
                return InteractionResult.SUCCESS;
            }
            if (state.hasProperty(BlockStateProperties.AGE_7) && state.getValue(BlockStateProperties.AGE_7) > 0) {
                if(!level.isClientSide) level.setBlock(pos, state.setValue(BlockStateProperties.AGE_7, state.getValue(BlockStateProperties.AGE_7) - 1), 2);
                return InteractionResult.SUCCESS;
            } else if(state.hasProperty(BlockStateProperties.AGE_3) && state.getValue(BlockStateProperties.AGE_3) > 0) {
                if(!level.isClientSide) level.setBlock(pos, state.setValue(BlockStateProperties.AGE_3, state.getValue(BlockStateProperties.AGE_3) - 1), 2);
                return InteractionResult.SUCCESS;
            }
        }
        //Copied from HoeItem
        else if (!player.isCrouching()) {
            int hook = ForgeEventFactory.onHoeUse(context);
            if (hook != 0) return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
            if (context.getClickedFace() != Direction.DOWN && level.isEmptyBlock(pos.above())) {
                BlockState blockstate = HOE_LOOKUP.get(level.getBlockState(pos).getBlock());
                if (blockstate != null) {
                    level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!level.isClientSide) {
                        level.setBlock(pos, blockstate, 11);
                        context.getItemInHand().hurtAndBreak(1, player, i -> i.broadcastBreakEvent(player.getUsedItemHand()));
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        return InteractionResult.PASS;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab category, @NotNull NonNullList<ItemStack> list) {
        if (category == Unstable.UNSTABLE_TAB) {
            ItemStack stack = new ItemStack(this);
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("Unbreakable", true);
            stack.setTag(tag);
            list.add(stack);
        }
    }
}
