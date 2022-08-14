package com.electrolyte.unstable.tools;

import com.electrolyte.unstable.damagesource.HealingAxeDamageSource;
import com.electrolyte.unstable.Unstable;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class HealingAxe extends AxeItem {

    public HealingAxe(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public void onCraftedBy(ItemStack stack, @NotNull Level worldIn, @NotNull Player playerIn) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("Unbreakable", true);
        stack.setTag(nbt);
        stack.enchant(Enchantments.BLOCK_EFFICIENCY, 10);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if(isSelected && entityIn instanceof ServerPlayer player && worldIn.getGameTime() % 40L == 0L) {
            player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 1);
            player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + 0.1F);
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(player instanceof FakePlayer) return false;
        if(entity instanceof Mob mob) {
                if(mob.getMobType() != MobType.UNDEAD && mob.getHealth() < mob.getMaxHealth()) {
                    mob.setHealth(mob.getHealth() + 0.75F);
                    //TODO: Find a better particle for this.
                    mob.getLevel().addParticle(ParticleTypes.HEART, (mob.getX() - 0.5) + new Random().nextDouble(1), (mob.getY() + 0.25) + new Random().nextDouble(1), (mob.getZ() - 0.5) + new Random().nextDouble(1), 0, 0.5, 0);
                    player.hurt(HealingAxeDamageSource.INSTANCE, 1.5F);
                    //mob.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0, false, true));
                } else if (mob.getMobType() == MobType.UNDEAD) {
                    mob.setHealth(mob.getHealth() - 3);
                    //TODO: Find a better particle for this.
                    mob.getLevel().addParticle(ParticleTypes.HEART, (mob.getX() - 0.5) + new Random().nextDouble(1), (mob.getY() + 0.25) + new Random().nextDouble(1), (mob.getZ() - 0.5) + new Random().nextDouble(1), 0, 0.5, 0);
                    player.hurt(HealingAxeDamageSource.INSTANCE, 1.5F);
                    //mob.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 1, false, true));
                }
        }
        return false;
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab category, @NotNull NonNullList<ItemStack> list) {
        if (category == Unstable.UNSTABLE_TAB) {
            ItemStack stack = new ItemStack(this);
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("Unbreakable", true);
            stack.setTag(tag);
            stack.enchant(Enchantments.BLOCK_EFFICIENCY, 5);
            list.add(stack);
        }
    }
}
