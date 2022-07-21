package com.electrolyte.unstable.tools;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.init.ModTools;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

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
        if (entity instanceof net.minecraftforge.common.IForgeShearable target) {
            if (entity.level.isClientSide) return InteractionResult.SUCCESS;
            BlockPos pos = new BlockPos(entity.getX(), entity.getY(), entity.getZ());
            if (target.isShearable(stack, entity.level, pos)) {
                java.util.List<ItemStack> drops = target.onSheared(playerIn, stack, entity.level, pos,
                        EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack));
                Random rand = new java.util.Random();
                drops.forEach(d -> {
                    ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                    ent.setDeltaMovement(ent.getDeltaMovement().add((double)((rand.nextFloat() - rand.nextFloat()) * 0.1F), (double)(rand.nextFloat() * 0.05F), (double)((rand.nextFloat() - rand.nextFloat()) * 0.1F)));
                });
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SHEARS_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
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
       else if(player.isCrouching() && this.isCorrectToolForDrops(level.getBlockState(pos)) ){// && this.checkBlock(state, pos, level)) {
           if(!level.isClientSide && !level.restoringBlockSnapshots && !player.getCooldowns().isOnCooldown(this)) {
               state.getBlock();
               List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null);
               for (ItemStack drop : drops) {
                   player.addItem(drop);
               }
               level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
               player.getCooldowns().addCooldown(this, 40);
               return InteractionResult.SUCCESS;
           } level.addParticle(ParticleTypes.WITCH, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0, 0, 0);
       }
       return super.useOn(context);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity entityLiving) {
        if(level.isClientSide) {
            level.addParticle(ParticleTypes.WITCH, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0, 0, 0);
        }
        return state.is(BlockTags.LEAVES) || state.is(Blocks.COBWEB) || state.is(Blocks.GRASS) || state.is(Blocks.FERN) || state.is(Blocks.DEAD_BUSH) || state.is(Blocks.HANGING_ROOTS) || state.is(Blocks.VINE) || state.is(Blocks.TRIPWIRE) || state.is(BlockTags.WOOL) || super.mineBlock(stack, level, state, pos, entityLiving);
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, BlockState state) {
        Material material = state.getMaterial();
        boolean pickaxe = material == Material.METAL || material == Material.HEAVY_METAL || material == Material.STONE;
        boolean axe = material == Material.WOOD || material == Material.PLANT || material == Material.BAMBOO;
        boolean shovel = material == Material.DIRT || material == Material.SAND || material == Material.SNOW;
        boolean shears = material == Material.WEB || material == Material.LEAVES || material == Material.WOOL;
        return pickaxe || axe || shovel || shears ? 2.0F : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return state.is(Blocks.COBWEB) || state.is(Blocks.REDSTONE_WIRE) || state.is(Blocks.TRIPWIRE);
       /* Block block = blockIn.getBlock();
        int harvestLevel = 1;
        if (blockIn.getHarvestTool() == Tools.PICKAXE) {
            return harvestLevel >= blockIn.getHarvestLevel();
        }
        Material material = blockIn.getMaterial();
        return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL || block == Blocks.SNOW || block == Blocks.SNOW_BLOCK || material == Material.WOOD || material == Material.PLANT || material == Material.BAMBOO || material == Material.DIRT || material == Material.SAND || material == Material.SNOW || material == Material.WOOL || material == Material.WEB;
    */}

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

   /* public boolean checkBlock(BlockState blockIn, BlockPos pos, Level level) {
        if(blockIn.getBlockHardness(world, pos) == -1.0F) return !this.canHarvestBlock(blockIn);
        return true;
    }*/
}
