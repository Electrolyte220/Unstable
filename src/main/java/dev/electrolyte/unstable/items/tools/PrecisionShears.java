package dev.electrolyte.unstable.items.tools;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.IShearable;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;

import static net.minecraft.world.entity.LivingEntity.getSlotForHand;

public class PrecisionShears extends Item {

    public PrecisionShears(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity entity, InteractionHand hand) {
        if(entity instanceof IShearable target) {
            if(entity.level().isClientSide) return InteractionResult.CONSUME;
            BlockPos pos = entity.blockPosition();
            if(target.isShearable(playerIn, stack, entity.level(), pos)) {
                target.onSheared(playerIn, stack, entity.level(), pos).forEach(d -> target.spawnShearedDrop(entity.level(), pos, d));
                entity.gameEvent(GameEvent.SHEAR, playerIn);
                stack.hurtAndBreak(1, playerIn, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_SHEARS_ACTIONS.contains(toolAction);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
       Level level = context.getLevel();
       BlockPos pos = context.getClickedPos();
       BlockState state = level.getBlockState(pos);
       Player player = context.getPlayer();
       if(state.getBlock() instanceof GrowingPlantHeadBlock growingPlantHeadBlock) {
           if(!growingPlantHeadBlock.isMaxAge(state)) {
            ItemStack stack = context.getItemInHand();
            if(player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, stack);
            }
            level.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
            BlockState state1 = growingPlantHeadBlock.getMaxAgeState(state);
            level.setBlockAndUpdate(pos, state1);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(context.getPlayer(), state1));
            if(player != null) {
                stack.hurtAndBreak(1, player, getSlotForHand(context.getHand()));
            }
               return InteractionResult.sidedSuccess(level.isClientSide);
           }
       }
       else if(player.isCrouching() && this.checkBlock(state, level, pos)) {
           if(!level.isClientSide && !level.restoringBlockSnapshots && !player.getCooldowns().isOnCooldown(this)) {
               List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null, player, this.getDefaultInstance());
               for (ItemStack drop : drops) {
                   player.addItem(drop);
               }
               level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
               player.getCooldowns().addCooldown(this, 40);
               context.getItemInHand().hurtAndBreak(1, player, getSlotForHand(context.getHand()));
               ((ServerLevel) level).sendParticles(ParticleTypes.WITCH, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0, 0, 0,0 ,0);
               return InteractionResult.SUCCESS;
           }
       }
       return super.useOn(context);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && !pState.is(BlockTags.FIRE)) {
            pStack.hurtAndBreak(1, pEntityLiving, EquipmentSlot.MAINHAND);
        }
        return pState.is(BlockTags.LEAVES) || pState.is(Blocks.COBWEB) || pState.is(Blocks.SHORT_GRASS) || pState.is(Blocks.FERN) || pState.is(Blocks.DEAD_BUSH) || pState.is(Blocks.HANGING_ROOTS) || pState.is(Blocks.VINE) || pState.is(Blocks.TRIPWIRE) || pState.is(BlockTags.WOOL) || super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(
                Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F),
                Tool.Rule.overrideSpeed(BlockTags.LEAVES, 15.0F),
                Tool.Rule.overrideSpeed(BlockTags.WOOL, 5.0F),
                Tool.Rule.overrideSpeed(List.of(Blocks.VINE, Blocks.GLOW_LICHEN), 2.0F)
        ), 1.0F, 1);
    }

    private boolean checkBlock(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            return level.getBlockState(pos).getTags().noneMatch(tag -> tag.equals(BlockTags.INCORRECT_FOR_STONE_TOOL)) && state.getDestroySpeed(level, pos) != -1;
        }
        return false;
    }
}