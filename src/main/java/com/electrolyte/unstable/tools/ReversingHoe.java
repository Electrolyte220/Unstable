package com.electrolyte.unstable.tools;

import com.electrolyte.unstable.Unstable;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ReversingHoe extends DiggerItem {

    public ReversingHoe(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(attackDamage, attackSpeed, tier, BlockTags.MINEABLE_WITH_HOE, properties);
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
        if (player.isCrouching()) {
            if (block == Blocks.COBBLESTONE) {
                BlockState newState = Blocks.STONE.defaultBlockState();
                if (!level.isClientSide)
                    level.setBlock(pos, newState, 11);
                return InteractionResult.SUCCESS;
            } else if (block == Blocks.DIRT) {
                BlockState newState = Blocks.GRASS_BLOCK.defaultBlockState();
                if (!level.isClientSide)
                    level.setBlock(pos, newState, 11);
                return InteractionResult.SUCCESS;
            }
            if (state.hasProperty(BlockStateProperties.AGE_7) && state.getValue(BlockStateProperties.AGE_7) > 0) {
                if (!level.isClientSide)
                    level.setBlock(pos, state.setValue(BlockStateProperties.AGE_7, state.getValue(BlockStateProperties.AGE_7) - 1), 2);
                return InteractionResult.SUCCESS;
            } else if (state.hasProperty(BlockStateProperties.AGE_3) && state.getValue(BlockStateProperties.AGE_3) > 0) {
                if (!level.isClientSide)
                    level.setBlock(pos, state.setValue(BlockStateProperties.AGE_3, state.getValue(BlockStateProperties.AGE_3) - 1), 2);
                return InteractionResult.SUCCESS;
            }
        }
        //Copied from HoeItem
        else if (!player.isCrouching()) {
            BlockState toolModifiedState = level.getBlockState(pos).getToolModifiedState(context, ToolActions.HOE_TILL, false);
            Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of((ctx) -> {
                return true;
            }, changeIntoState(toolModifiedState));
            if (pair == null) {
                return InteractionResult.PASS;
            } else {
                Predicate<UseOnContext> predicate = pair.getFirst();
                Consumer<UseOnContext> consumer = pair.getSecond();
                if (predicate.test(context)) {
                    level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!level.isClientSide) {
                        consumer.accept(context);
                        context.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
                            p_150845_.broadcastBreakEvent(context.getHand());
                        });
                    }

                    return InteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    return InteractionResult.PASS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    private static Consumer<UseOnContext> changeIntoState(BlockState pState) {
        return context -> context.getLevel().setBlock(context.getClickedPos(), pState, 11);
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
