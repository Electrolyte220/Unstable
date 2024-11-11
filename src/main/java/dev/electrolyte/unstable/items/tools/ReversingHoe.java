package dev.electrolyte.unstable.items.tools;

import com.mojang.datafixers.util.Pair;
import dev.electrolyte.unstable.recipe.reversinghoe.PropertyRegressionRecipe;
import dev.electrolyte.unstable.recipe.reversinghoe.TransmutationRecipe;
import dev.electrolyte.unstable.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraft.world.entity.LivingEntity.getSlotForHand;

public class ReversingHoe extends DiggerItem {

    public ReversingHoe(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_HOE, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState state = level.getBlockState(pos);
        if (player.isCrouching()) {
            Optional<RecipeHolder<TransmutationRecipe>> transmutationBlockOptional = level.getRecipeManager().getAllRecipesFor(ModRecipes.TRANSMUTATION_RECIPE_TYPE.get()).stream().filter(p -> BuiltInRegistries.BLOCK.get(p.value().input()) == state.getBlock()).findFirst();
            Optional<RecipeHolder<PropertyRegressionRecipe>> propertyRegressionOptional = level.getRecipeManager().getAllRecipesFor(ModRecipes.PROPERTY_REGRESSION_RECIPE_TYPE.get()).stream().filter(p -> BuiltInRegistries.BLOCK.get(p.value().block()) == state.getBlock()).findFirst();
            if(transmutationBlockOptional.isPresent()) {
                Block newBlock = BuiltInRegistries.BLOCK.get(transmutationBlockOptional.get().value().output());
                if (!state.getProperties().isEmpty()) {
                    level.setBlock(pos, newBlock.withPropertiesOf(state), 11);
                } else {
                    level.setBlock(pos, newBlock.defaultBlockState(), 11);
                }
                level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 0.25F, 0.85F);
                if(!level.isClientSide) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0, 0, 0);
                    context.getItemInHand().hurtAndBreak(1, player, getSlotForHand(context.getHand()));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (propertyRegressionOptional.isPresent()) {
                if(state.getBlock().getStateDefinition().getProperty(propertyRegressionOptional.get().value().property()) != null) {
                    Property<?> property = state.getBlock().getStateDefinition().getProperty(propertyRegressionOptional.get().value().property());
                    int currentVal = Integer.parseInt(state.getValue(property).toString());
                    if(currentVal > Integer.parseInt(property.getPossibleValues().stream().toList().get(0).toString())) {
                        if (!level.isClientSide) {
                            level.setBlock(pos, state.setValue((Property<Integer>) property, currentVal - 1), 2);
                            context.getItemInHand().hurtAndBreak(1, player, getSlotForHand(context.getHand()));
                            ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0, 0, 0);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }
        //Copied from HoeItem
        else {
            BlockState toolModifiedState = level.getBlockState(pos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false);
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
                        context.getItemInHand().hurtAndBreak(1, player, getSlotForHand(context.getHand()));
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
    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_HOE_ACTIONS.contains(toolAction);
    }
}