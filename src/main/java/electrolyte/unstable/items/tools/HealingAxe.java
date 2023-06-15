package electrolyte.unstable.items.tools;

import electrolyte.unstable.UnstableConfig;
import electrolyte.unstable.init.ModDamageTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Random;

public class HealingAxe extends AxeItem {

    public HealingAxe(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(entityIn instanceof ServerPlayer player && worldIn.getGameTime() % UnstableConfig.HEALING_AXE_HEAL_RATE.get() == 0L) {
            if(player.getMainHandItem().equals(stack) || (player.getOffhandItem().equals(stack) && UnstableConfig.HEALING_AXE_OFFHAND.get())) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() + 1);
                player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + 0.1F);
            }
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(player instanceof FakePlayer) return false;
        if(entity instanceof Mob mob && entity.level() instanceof ServerLevel serverLevel) {
            if(mob.getType().getCategory() == MobCategory.CREATURE) {
                if(mob.getHealth() < mob.getMaxHealth()) {
                    mob.setHealth(mob.getHealth() + 0.75F);
                    serverLevel.sendParticles(ParticleTypes.HEART, (mob.getX() - 0.5) + new Random().nextDouble(1), (mob.getY() + 0.5) + new Random().nextDouble(1), (mob.getZ() - 0.5) + new Random().nextDouble(1),1, 0, 0.25, 0, 0.25);
                    player.hurt(ModDamageTypes.getDamageSource(entity.level(), ModDamageTypes.HEALING_AXE), 1.5F);
                    stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
                }
                return true;
            }
        }
        return false;
    }
}