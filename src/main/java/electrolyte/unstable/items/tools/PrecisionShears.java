package electrolyte.unstable.items.tools;

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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.List;
import java.util.Random;

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
        if (entity instanceof IForgeShearable target) {
            if (entity.level().isClientSide) return InteractionResult.SUCCESS;
            BlockPos pos = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
            if (target.isShearable(stack, entity.level(), pos)) {
                List<ItemStack> drops = target.onSheared(playerIn, stack, entity.level(), pos, stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE));
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
    public InteractionResult useOn(UseOnContext context) {
       Player player = context.getPlayer();
       Level level = context.getLevel();
       BlockPos pos = context.getClickedPos();
       BlockState state = level.getBlockState(pos);
       Block block = state.getBlock();
       if(block instanceof GrowingPlantHeadBlock growingPlantHeadBlock) {
           if(!growingPlantHeadBlock.isMaxAge(state)) {
               if(player instanceof ServerPlayer) {
                   CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, pos, context.getItemInHand());
               }
               level.playSound(player, pos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
               level.setBlockAndUpdate(pos, growingPlantHeadBlock.getMaxAgeState(state));
           }
           return InteractionResult.sidedSuccess(level.isClientSide);
       }
       else if(player.isCrouching() && this.checkBlock(state, level, pos)) {
           if(!level.isClientSide && !level.restoringBlockSnapshots && !player.getCooldowns().isOnCooldown(this)) {
               List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null, player, this.getDefaultInstance());
               for (ItemStack drop : drops) {
                   player.addItem(drop);
               }
               level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
               player.getCooldowns().addCooldown(this, 40);
               context.getItemInHand().hurtAndBreak(1, player, e -> e.broadcastBreakEvent(context.getHand()));
               ((ServerLevel) level).sendParticles(ParticleTypes.WITCH, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0, 0, 0,0 ,0);
               return InteractionResult.SUCCESS;
           }
       }
       return super.useOn(context);
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide && !pState.is(BlockTags.FIRE)) {
            pStack.hurtAndBreak(1, pEntityLiving, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return pState.is(BlockTags.LEAVES) || pState.is(Blocks.COBWEB) || pState.is(Blocks.GRASS) || pState.is(Blocks.FERN) || pState.is(Blocks.DEAD_BUSH) || pState.is(Blocks.HANGING_ROOTS) || pState.is(Blocks.VINE) || pState.is(Blocks.TRIPWIRE) || pState.is(BlockTags.WOOL) || super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
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

    private boolean checkBlock(BlockState state, Level level, BlockPos pos) {
        if (!level.isClientSide) {
            return TierSortingRegistry.isCorrectTierForDrops(Tiers.STONE, state) && state.getDestroySpeed(level, pos) != -1;
        }
        return false;
    }
}