package electrolyte.unstable.items.tools;

import com.mojang.datafixers.util.Pair;
import electrolyte.unstable.Unstable;
import electrolyte.unstable.datastorage.reversinghoe.PropertyRegressionDataStorage;
import electrolyte.unstable.datastorage.reversinghoe.TransmutationDataStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ReversingHoe extends DiggerItem {

    public ReversingHoe(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(attackDamage, attackSpeed, tier, BlockTags.MINEABLE_WITH_HOE, properties);
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
            Optional<TransmutationDataStorage> transmutationBlockOptional = TransmutationDataStorage.getMasterStorage().stream().filter(p -> Arrays.stream(p.getInput().getItems()).anyMatch(s -> s.is(state.getBlock().asItem()))).findFirst();
            Optional<PropertyRegressionDataStorage> propertyRegressionOptional = PropertyRegressionDataStorage.getMasterStorage().stream().filter(p -> p.block().equals(state.getBlock())).findFirst();
            if(transmutationBlockOptional.isPresent()) {
                Block newBlock = transmutationBlockOptional.get().getOutput();
                if (!state.getProperties().isEmpty()) {
                    level.setBlock(pos, newBlock.withPropertiesOf(state), 11);
                } else {
                    level.setBlock(pos, newBlock.defaultBlockState(), 11);
                }
                level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.25F, 0.85F);
                if(!level.isClientSide) {
                    ((ServerLevel) level).sendParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0, 0, 0);
                    context.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (propertyRegressionOptional.isPresent()) {
                if(state.getBlock().getStateDefinition().getProperty(propertyRegressionOptional.get().property()) != null) {
                    Property<?> property = state.getBlock().getStateDefinition().getProperty(propertyRegressionOptional.get().property());
                    int currentVal = Integer.parseInt(state.getValue(property).toString());
                    if(currentVal > Integer.parseInt(property.getPossibleValues().stream().toList().get(0).toString())) {
                        if (!level.isClientSide) {
                            level.setBlock(pos, state.setValue((Property<Integer>) property, currentVal - 1), 2);
                            context.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
                            ((ServerLevel) level).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0, 0, 0);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
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
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> list) {
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
