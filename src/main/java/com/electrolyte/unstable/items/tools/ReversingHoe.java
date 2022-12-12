package com.electrolyte.unstable.items.tools;

import com.electrolyte.unstable.Unstable;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ReversingHoe extends DiggerItem {

    private static final Map<Block, Block> REVERSABLE_BLOCKS = new ImmutableMap.Builder<Block, Block>().put(Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_WOOD).put(Blocks.STRIPPED_OAK_LOG, Blocks.OAK_LOG).put(Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_WOOD).put(Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_LOG).put(Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_WOOD).put(Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_LOG).put(Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_WOOD).put(Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_LOG).put(Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_WOOD).put(Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_LOG).put(Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_WOOD).put(Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_LOG).put(Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_STEM).put(Blocks.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_HYPHAE).put(Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_STEM).put(Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_HYPHAE).put(Blocks.COBBLESTONE, Blocks.STONE).put(Blocks.COBBLED_DEEPSLATE, Blocks.DEEPSLATE).put(Blocks.DIRT, Blocks.GRASS_BLOCK).build();

    public ReversingHoe(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(attackDamage, attackSpeed, tier, BlockTags.MINEABLE_WITH_HOE, properties);
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
        if (player.isCrouching()) {
            if(REVERSABLE_BLOCKS.containsKey(state.getBlock())) {
                if (!state.getProperties().isEmpty()) {
                    level.setBlock(pos, REVERSABLE_BLOCKS.get(state.getBlock()).withPropertiesOf(state), 11);
                } else {
                    level.setBlock(pos, REVERSABLE_BLOCKS.get(state.getBlock()).defaultBlockState(), 11);
                }
                level.addParticle(ParticleTypes.EXPLOSION, pos.getX(), pos.getY() + 0.5, pos.getZ(), 0, 0, 0);
                level.playSound(player, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.25F, 0.85F);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if ((state.hasProperty(BlockStateProperties.AGE_1) || state.hasProperty(BlockStateProperties.AGE_2) || state.hasProperty(BlockStateProperties.AGE_3) || state.hasProperty(BlockStateProperties.AGE_5) || state.hasProperty(BlockStateProperties.AGE_7) || state.hasProperty(BlockStateProperties.AGE_15) || state.hasProperty(BlockStateProperties.AGE_25))) {
                ImmutableMap<Property<?>, Comparable<?>> age = state.getValues();
                for(Property<?> property : age.keySet()) {
                    int currentAge = Integer.parseInt(String.valueOf(age.values().iterator().next()));
                    if(currentAge > 0) {
                        if(!level.isClientSide) {
                            level.setBlock(pos, state.setValue((Property<Integer>) property, currentAge - 1), 2);
                            return InteractionResult.SUCCESS;
                        }
                    } else return InteractionResult.PASS;
                }
            }
        }
        //Copied from HoeItem
        else {
            BlockState toolModifiedState = level.getBlockState(pos).getToolModifiedState(context, ToolActions.HOE_TILL, false);
            Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> pair = toolModifiedState == null ? null : Pair.of((ctx) -> true, changeIntoState(toolModifiedState));
            if (pair == null) {
                return InteractionResult.PASS;
            } else {
                Predicate<UseOnContext> predicate = pair.getFirst();
                Consumer<UseOnContext> consumer = pair.getSecond();
                if (predicate.test(context)) {
                    level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!level.isClientSide) {
                        consumer.accept(context);
                        context.getItemInHand().hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(context.getHand()));
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

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction);
    }
}
