package com.electrolyte.unstable.tools;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.init.ModTools;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrecisionShears extends Item {

    public PrecisionShears(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player playerIn, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (entity instanceof IForgeShearable target) {
            if (entity.level.isClientSide) return InteractionResult.SUCCESS;
            BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
            if (target.isShearable(stack, entity.level, pos)) {
                List<ItemStack> drops = target.onSheared(playerIn, stack, entity.level, pos, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack));
                int slot = playerIn.getInventory().getFreeSlot();
                drops.forEach(d -> {
                    if(slot != -1) {
                        playerIn.getInventory().add(slot, d);
                    } else {
                        Random rand = new Random();
                        ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                        ent.setDeltaMovement(ent.getDeltaMovement().add((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F, (rand.nextFloat() - rand.nextFloat()) * 0.1F));
                    }
                });
                stack.hurtAndBreak(1, playerIn, e -> e.broadcastBreakEvent(hand));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SHEARS_ACTIONS.contains(toolAction);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
       Player player = context.getPlayer();
       Level level = context.getLevel();
       BlockPos pos = context.getClickedPos();
       BlockState state = level.getBlockState(pos);
       Block block = state.getBlock();
       if(block instanceof GrowingPlantHeadBlock) {
           GrowingPlantHeadBlock growingPlantHeadBlock = (GrowingPlantHeadBlock) block;
           if(!growingPlantHeadBlock.isMaxAge(state)) {
               if(player instanceof ServerPlayer) {
                   CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, context.getItemInHand());
               }
               level.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
               level.setBlockAndUpdate(pos, growingPlantHeadBlock.getMaxAgeState(state));
           }
           return InteractionResult.sidedSuccess(level.isClientSide);
       }
       else if(player.isCrouching() && this.checkBlock(state, level, pos, player)) {
           if(!level.isClientSide && !level.restoringBlockSnapshots && !player.getCooldowns().isOnCooldown(this)) {
               List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null);
               for (ItemStack drop : drops) {
                   player.addItem(drop);
               }
               level.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
               player.getCooldowns().addCooldown(this, 40);
               context.getItemInHand().hurtAndBreak(1, player, e -> e.broadcastBreakEvent(context.getHand()));
               return InteractionResult.SUCCESS;
           } level.addParticle(ParticleTypes.WITCH, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0, 0, 0);
       }
       return super.useOn(context);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack pStack, Level pLevel, @NotNull BlockState pState, @NotNull BlockPos pPos, @NotNull LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && !pState.is(BlockTags.FIRE)) {
            pStack.hurtAndBreak(1, pEntityLiving, (p_43076_) -> {
                p_43076_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return pState.is(BlockTags.LEAVES) || pState.is(Blocks.COBWEB) || pState.is(Blocks.GRASS) || pState.is(Blocks.FERN) || pState.is(Blocks.DEAD_BUSH) || pState.is(Blocks.HANGING_ROOTS) || pState.is(Blocks.VINE) || pState.is(Blocks.TRIPWIRE) || pState.is(BlockTags.WOOL) || super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack pStack, BlockState pState) {
        if (!pState.is(Blocks.COBWEB) && !pState.is(BlockTags.LEAVES)) {
            if (pState.is(BlockTags.WOOL)) {
                return 5.0F;
            } else {
                return !pState.is(Blocks.VINE) && !pState.is(Blocks.GLOW_LICHEN) ? super.getDestroySpeed(pStack, pState) : 2.0F;
            }
        } else {
            return 15.0F;
        }
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return state.is(Blocks.COBWEB) || state.is(Blocks.REDSTONE_WIRE) || state.is(Blocks.TRIPWIRE);
    }

    private boolean checkBlock(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            return TierSortingRegistry.isCorrectTierForDrops(Tiers.STONE, state) && state.getDestroySpeed(level, pos) != -1;
        }
        return false;
    }
}
