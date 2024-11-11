package dev.electrolyte.unstable.handler;

import com.google.gson.Gson;
import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.UnstableConfig;
import dev.electrolyte.unstable.datastorage.endsiege.EntityDataStorage;
import dev.electrolyte.unstable.helper.ActivationRitualHelper;
import dev.electrolyte.unstable.helper.PseudoInversionRitualHelper;
import dev.electrolyte.unstable.items.UnstableIngot;
import dev.electrolyte.unstable.listener.EntityDataReloadListener;
import dev.electrolyte.unstable.menu.MysticalTableMenu;
import dev.electrolyte.unstable.registry.ModComponents;
import dev.electrolyte.unstable.registry.ModDamageTypes;
import dev.electrolyte.unstable.registry.ModItems;
import dev.electrolyte.unstable.registry.ModSounds;
import dev.electrolyte.unstable.registry.ModTools;
import dev.electrolyte.unstable.savedata.UnstableSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;
import java.util.Random;

import static net.minecraft.world.entity.LivingEntity.getSlotForHand;

@EventBusSubscriber(modid = Unstable.MOD_ID, bus = Bus.GAME)
public class UnstableNeoEventHandler {

    @SubscribeEvent
    public static void onDatapackReload(AddReloadListenerEvent event) {
        event.addListener(new EntityDataReloadListener(new Gson(), "end_siege/entity_data"));
    }

    @SubscribeEvent
    public static void alterDrops(LivingDropsEvent event) {
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level());
        if (event.getEntity() instanceof ElderGuardian || event.getEntity() instanceof EnderDragon || event.getEntity() instanceof WitherBoss || event.getEntity() instanceof Warden) {
            event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ModItems.DIVISION_SIGIL.get(), 1)));
        } else if (data.isEndSiegeOccurring() && event.getEntity().getTags().contains("spawnedBySiege")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEndSiegeMobKilled(LivingExperienceDropEvent event) {
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level());
        if(data.isEndSiegeOccurring() && event.getEntity().getTags().contains("spawnedBySiege")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMonsterHurt(LivingDamageEvent.Pre event) {
        if(event.getEntity().getType().getCategory() != MobCategory.MONSTER) return;
        if(!(event.getEntity().getLastAttacker() instanceof Player player)) return;
        if(!(player.getMainHandItem().is(ModTools.HEALING_AXE.get()))) return;
        event.getEntity().setHealth(event.getEntity().getHealth() - 6);
        ((ServerLevel) event.getEntity().level()).sendParticles(ParticleTypes.WITCH, (event.getEntity().getX() - 0.5) + new Random().nextDouble(1), (event.getEntity().getY() + 0.75) + new Random().nextDouble(1), (event.getEntity().getZ() - 0.5) + new Random().nextDouble(1), 5, 0, 0.5, 0, 0.25);
        player.hurt(ModDamageTypes.getDamageSource(player.level(), ModDamageTypes.HEALING_AXE), 1.5F);
        player.getMainHandItem().hurtAndBreak(1, player, getSlotForHand(event.getEntity().getUsedItemHand()));
    }

    @SubscribeEvent
    public static void tagVex(FinalizeSpawnEvent event) {
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level());
        if(data.isEndSiegeOccurring() && event.getEntity() instanceof Vex vex) {
            if(!vex.getTags().contains("spawnedBySiege")) {
                vex.addTag("spawnedBySiege");
            }
        }
    }

    @SubscribeEvent
    public static void disableEntitySpawn(FinalizeSpawnEvent event) {
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level());
        if(data.isEndSiegeOccurring()) {
            if((event.getEntity() instanceof EnderMan && event.getEntity().level().dimension() == Level.END) || !event.getEntity().getTags().contains("spawnedBySiege")) {
                event.setSpawnCancelled(true);
            }
        }
    }

    @SubscribeEvent
    public static void updateEntity(EntityTickEvent.Pre event) {
        if(event.getEntity().level().isClientSide) return;
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level());
        if (data.isEndSiegeOccurring()) {
            ResourceLocation entityDim = event.getEntity().level().dimension().location();
            if (event.getEntity() instanceof EnderMan && entityDim.equals(BuiltinDimensionTypes.END.location())) {
                event.getEntity().discard();
            }
        } else if (event.getEntity().getTags().contains("spawnedBySiege")) {
            event.getEntity().discard();
        }
    }

    @SubscribeEvent
    public static void onCreeperExplode(ExplosionEvent.Detonate event) {
        if(event.getExplosion().getDirectSourceEntity() instanceof Creeper creeper) {
            if(creeper.getTags().contains("noLingeringEffects")) {
                creeper.removeAllEffects();
            }
        }
    }

    @SubscribeEvent
    public static void onActivationMobKilled(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player)) return;
        if(!(event.getEntity() instanceof Mob)) return;
        BlockPos pos1 = new BlockPos(event.getEntity().getBlockX(), event.getEntity().getBlockY() + 1, event.getEntity().getBlockZ());
        BlockPos pos2 = new BlockPos(event.getEntity().getBlockX(), event.getEntity().getBlockY(), event.getEntity().getBlockZ());
        Block enchantBlock = event.getEntity().level().getBlockState(pos1.below()).getBlock();
        ServerPlayer player = (ServerPlayer) event.getSource().getEntity();
        if(enchantBlock != Blocks.ENCHANTING_TABLE) return;
        if(player.getInventory().contains(ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance()) || player.getInventory().contains(ModItems.DIVISION_SIGIL_STABLE.get().getDefaultInstance())) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.translatable("unstable.activation_ritual.active_sigil").withStyle(ChatFormatting.RED));
        } else if (player.getInventory().contains(ModItems.DIVISION_SIGIL.get().getDefaultInstance()) && ActivationRitualHelper.checkConditions(event.getEntity().level(), pos2)) {
            LightningBolt lightningBoltEntity = new LightningBolt(EntityType.LIGHTNING_BOLT, event.getEntity().level());
            ServerLevel level = (ServerLevel) event.getEntity().level();
            level.playSound(null, pos1.getX(), pos1.getY(), pos1.getZ(), ModSounds.ACTIVATION_RITUAL_SUCCESS.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
            lightningBoltEntity.setPos(pos1.getX(), pos1.getY(), pos1.getZ());
            level.addFreshEntity(lightningBoltEntity);
            player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(ModItems.DIVISION_SIGIL.get().getDefaultInstance()), 1);
            player.getInventory().add(new ItemStack(ModItems.DIVISION_SIGIL_ACTIVATED.get()));
            ActivationRitualHelper.updateBlocks(level, pos2);
            ActivationRitualHelper.updateRedstone(level, pos2);
        }
    }

    @SubscribeEvent
    public static void onIronGolemKilled(LivingDeathEvent event) {
        if(!(event.getEntity() instanceof IronGolem)) return;
        BlockPos pos = new BlockPos(event.getEntity().getBlockX(), event.getEntity().getBlockY(), event.getEntity().getBlockZ()).below();
        Block beaconBlock = event.getEntity().level().getBlockState(pos).getBlock();
        if(!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if(beaconBlock != Blocks.BEACON) return;
        UnstableSavedData data = UnstableSavedData.get(player.level());
        if(data.isEndSiegeOccurring()) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.translatable( "unstable.pseudo_inversion_ritual.multiple_sigil").withStyle(ChatFormatting.RED));
        }
        if(player.getInventory().contains(ModItems.DIVISION_SIGIL.get().getDefaultInstance())) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.not_activated").withStyle(ChatFormatting.RED));
        } else if(player.getInventory().contains(ModItems.DIVISION_SIGIL_STABLE.get().getDefaultInstance())) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.already_stable").withStyle(ChatFormatting.RED));
        } else if(player.getInventory().contains(ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance())) {
            if (PseudoInversionRitualHelper.checkChestsPos(event.getEntity().level(), pos) &&
                    PseudoInversionRitualHelper.checkAllChestContents(event.getEntity().level(), pos) &&
                    PseudoInversionRitualHelper.checkRedstoneAndString(event.getEntity().level(), pos)) {
                data.setEndSiegeOccurring(true);
                data.setStartingLocation(new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance()), 1);
                PseudoInversionRitualHelper.destroyBeaconAndChests(event.getEntity().level(), pos);
                ServerLevel serverLevel = event.getEntity().getServer().getLevel(Level.END);
                AABB spawnableLocations = new AABB(new BlockPos(data.getStartingLocation()[0], data.getStartingLocation()[1], data.getStartingLocation()[2])).inflate(UnstableConfig.MOB_SPAWN_RAGE_PIR.get());
                List<ServerPlayer> playersParticipating = serverLevel.getPlayers(p -> p.getBoundingBox().intersects(spawnableLocations.inflate(1)));
                ListTag playersParticipatingTag = data.getPlayersParticipating();
                if(playersParticipatingTag == null) {
                    playersParticipatingTag = new ListTag();
                }
                for(ServerPlayer playerIn : playersParticipating) {
                    if (!playersParticipatingTag.contains(StringTag.valueOf(playerIn.getStringUUID()))) {
                        playersParticipatingTag.add(StringTag.valueOf(playerIn.getStringUUID()));
                    }
                }
                data.setPlayersParticipating(playersParticipatingTag);
                PseudoInversionRitualHelper.sendSiegeMessage(Component.translatable("unstable.pseudo_inversion_ritual.siege_started").withStyle(ChatFormatting.WHITE), event.getEntity().level().getServer().getPlayerList(), data);
            }
        }
    }

    @SubscribeEvent
    public static void onEndSiegeMobKilled(LivingDeathEvent event) {
        if(!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        UnstableSavedData data = UnstableSavedData.get(player.level());
        if(!data.isEndSiegeOccurring()) return;
        ResourceLocation playerDim = player.level().dimension().location();
        if(!(playerDim.equals(BuiltinDimensionTypes.END.location()))) return;
        if (data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get() && event.getEntity().getTags().contains("spawnedBySiege")) {
            data.setTotalKills(data.getTotalKills() + 1);
            PseudoInversionRitualHelper.sendSiegeMessage(Component.translatable("unstable.pseudo_inversion_ritual.siege_kills", data.getTotalKills() + "/" + UnstableConfig.NEEDED_MOBS.get()).withStyle(ChatFormatting.WHITE), event.getEntity().level().getServer().getPlayerList(), data);
        }
        if (data.getTotalKills() >= UnstableConfig.NEEDED_MOBS.get()) {
            PseudoInversionRitualHelper.sendSiegeMessage(Component.translatable("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().level().getServer().getPlayerList(), data);
            if(player.getInventory().getFreeSlot() == -1) {
                player.level().addFreshEntity(new ItemEntity(player.level(), player.getBlockX(), player.getBlockY() + 1, player.getBlockZ(), new ItemStack(ModItems.DIVISION_SIGIL_STABLE.get())));
            } else {
                player.getInventory().add(player.getInventory().getFreeSlot(), new ItemStack(ModItems.DIVISION_SIGIL_STABLE.get()));
            }
            data.resetData();
        }
    }

    @SubscribeEvent
    public static void onMysticalTableClose(PlayerContainerEvent.Close event) {
        if(!(event.getContainer() instanceof MysticalTableMenu)) return;
        if(event.getEntity().getInventory().contains(p -> p.is(ModItems.UNSTABLE_INGOT.get()) && (!p.has(ModComponents.CREATIVE_SPAWNED) || !p.get(ModComponents.CREATIVE_SPAWNED)))) {
            UnstableIngot.unstableIngotFail(event.getEntity());
        }
        ItemStack offhandItem = event.getEntity().getOffhandItem();
        if(offhandItem.is(ModItems.UNSTABLE_INGOT.get())) {
            if(!offhandItem.has(ModComponents.CREATIVE_SPAWNED) || !offhandItem.get(ModComponents.CREATIVE_SPAWNED)) {
                offhandItem.set(ModComponents.EXPLODED, true);
                offhandItem.shrink(1);
                UnstableIngot.unstableIngotFail(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void worldTick(ServerTickEvent.Pre event) {
        ServerLevel level = event.getServer().getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(level);
        if (!data.isEndSiegeOccurring()) return;
        AABB spawnableLocations = new AABB(new BlockPos(data.getStartingLocation()[0], data.getStartingLocation()[1], data.getStartingLocation()[2])).inflate(UnstableConfig.MOB_SPAWN_RAGE_PIR.get(), 10, UnstableConfig.MOB_SPAWN_RAGE_PIR.get());
        List<ServerPlayer> playersParticipating = level.getPlayers(p -> p.getBoundingBox().intersects(spawnableLocations.inflate(1)));
        ListTag playersParticipatingTag = data.getPlayersParticipating();
        if(playersParticipatingTag == null) {
            playersParticipatingTag = new ListTag();
        }
        for(ServerPlayer player : playersParticipating) {
            if(!playersParticipatingTag.contains(StringTag.valueOf(player.getStringUUID()))) {
                playersParticipatingTag.add(StringTag.valueOf(player.getStringUUID()));
            }
            if(player.getAbilities().flying && player.gameMode.isSurvival()) {
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
                player.hurt(player.damageSources().fellOutOfWorld(), 0.5f);
            }
        }
        data.setPlayersParticipating(playersParticipatingTag);
        if(level.getServer().getTickCount() % 10 == 0 &&
                data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get() &&
                level.getEntities(null, spawnableLocations).size() < UnstableConfig.MAX_MOBS.get()) {
            for (int i = 0; i < data.getPlayersParticipating().size(); i++) {
                int spawnedMobInt = new Random().nextInt(EntityDataStorage.getMasterStorage().size());
                EntityDataStorage entityData = EntityDataStorage.getMasterStorage().get(spawnedMobInt);
                Mob mob = (Mob) entityData.entity().create(level);
                mob.addTag("spawnedBySiege");
                if(mob instanceof Creeper creeper) {
                    creeper.addTag("noLingeringEffects");
                }
                mob.setPos(genXOrZ(data.getStartingLocation()[0]), genY(data.getStartingLocation()[1]), genXOrZ(data.getStartingLocation()[2]));
                BlockPos mobPos = new BlockPos(mob.getBlockX(), mob.getBlockY(), mob.getBlockZ());
                while (!(level.noCollision(new AABB(mobPos).inflate(1, 0, 1)) && NaturalSpawner.isValidEmptySpawnBlock(level, mobPos, level.getBlockState(mobPos), level.getFluidState(mobPos), entityData.entity()))) {
                    mob.setPos(genXOrZ(data.getStartingLocation()[0]), genY(data.getStartingLocation()[1]), genXOrZ(data.getStartingLocation()[2]));
                    mobPos = new BlockPos(mob.getBlockX(), mob.getBlockY(), mob.getBlockZ());
                }
                if (entityData.effects().isPresent()) {
                    entityData.effects().get().forEach(effect -> mob.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible())));
                }
                if (entityData.equipment().isPresent()) {
                    entityData.equipment().get().head().ifPresent(stack -> mob.setItemSlot(EquipmentSlot.HEAD, stack.copy()));
                    entityData.equipment().get().chest().ifPresent(stack -> mob.setItemSlot(EquipmentSlot.CHEST, stack.copy()));
                    entityData.equipment().get().legs().ifPresent(stack -> mob.setItemSlot(EquipmentSlot.LEGS, stack.copy()));
                    entityData.equipment().get().feet().ifPresent(stack -> mob.setItemSlot(EquipmentSlot.FEET, stack.copy()));
                    entityData.equipment().get().body().ifPresent(stack -> mob.setItemSlot(EquipmentSlot.BODY, stack.copy()));
                    entityData.equipment().get().mainhand().ifPresent(stack -> mob.setItemSlot(EquipmentSlot.MAINHAND, stack.copy()));
                    entityData.equipment().get().offhand().ifPresent(stack -> mob.setItemSlot(EquipmentSlot.OFFHAND, stack.copy()));
                }
                EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(new BlockPos(mob.getBlockX(), mob.getBlockY(), mob.getBlockZ())), MobSpawnType.NATURAL, null);
                level.addFreshEntity(mob);
            }
        }
    }

    private static int genXOrZ(int relPos) {
        return relPos + (int) (Math.random() * UnstableConfig.MOB_SPAWN_RAGE_PIR.get() * (Math.random() > 0.5 ? 1 : -1));
    }

    private static int genY(int relY) {
        return relY + (int) (Math.random() * 10 * (Math.random() > 0.5 ? 1 : -1));
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        boolean failed = false;
        if(event.getEntity() instanceof ServerPlayer) {
            if(event.getEntity().containerMenu instanceof MysticalTableMenu menu) {
                if(menu.getCarried().is(ModItems.UNSTABLE_INGOT.get())) {
                    ItemStack stack = menu.getCarried();
                    if(stack.getItem() instanceof UnstableIngot ingot) {
                        if(ingot.checkItem(stack)) {
                            failed = true;
                        }
                    }
                }
                for(ItemStack stack : menu.getItems()) {
                    if(menu.getSlot(0).getItem().is(ModItems.UNSTABLE_INGOT.get())) continue;
                    if(stack.getItem() instanceof UnstableIngot ingot) {
                        if(ingot.checkItem(stack)) {
                            failed = true;
                            break;
                        }
                    }
                }
            }
            if(failed) {
                for(ItemStack stack : event.getEntity().containerMenu.getItems()) {
                    if(stack.is(ModItems.UNSTABLE_INGOT.get())) stack.shrink(1);
                }
                UnstableIngot.unstableIngotFail(event.getEntity());
            }
        }
    }

    @SubscribeEvent
    public static void playerClone(PlayerEvent.Clone event) {
        if(event.getEntity().level().isClientSide) return;
        Player player = event.getEntity();
        UnstableSavedData data = UnstableSavedData.get(event.getOriginal().level());
        if (data.isEndSiegeOccurring() && !event.isWasDeath()) {
            player.sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.changed_dimension").withStyle(ChatFormatting.RED));
            PseudoInversionRitualHelper.sendSiegeMessage(Component.translatable("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().level().getServer().getPlayerList(), data);
            data.resetData();
        }
        if(!UnstableConfig.SOUL_RESET_DEATH.get()) {
            if (event.getEntity().getAttribute(Attributes.MAX_HEALTH).getValue() != (event.getOriginal().getAttributeBaseValue(Attributes.MAX_HEALTH))) {
                event.getEntity().getAttribute(Attributes.MAX_HEALTH).setBaseValue(event.getOriginal().getAttributeBaseValue(Attributes.MAX_HEALTH));
            }
        }
    }

    @SubscribeEvent
    public static void playerDeath(LivingDeathEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;
        UnstableSavedData data = UnstableSavedData.get(player.level());
        if(data.isEndSiegeOccurring() && data.getPlayersParticipating().contains(StringTag.valueOf(player.getStringUUID()))) {
            player.sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.death").withStyle(ChatFormatting.RED));
            PseudoInversionRitualHelper.sendSiegeMessage(Component.translatable("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().level().getServer().getPlayerList(), data);
            data.resetData();
        }
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.getEntity().level().isClientSide) return;
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level().getServer().getLevel(Level.END));
        if(data.isEndSiegeOccurring() && data.getPlayersParticipating().contains(StringTag.valueOf(event.getEntity().getStringUUID())) && !event.getTo().location().equals(Level.END.location())) {
            event.getEntity().sendSystemMessage(Component.translatable("unstable.pseudo_inversion_ritual.changed_dimension").withStyle(ChatFormatting.RED));
            System.out.println(event.getEntity().level().getServer().getPlayerList());
            PseudoInversionRitualHelper.sendSiegeMessage(Component.translatable("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().level().getServer().getPlayerList(), data);
            data.resetData();
        }
    }

    @SubscribeEvent
    public static void tossUnstableIngot(ItemTossEvent event) {
        ItemStack stack = event.getEntity().getItem();
        if(stack.is(ModItems.UNSTABLE_INGOT.get())) {
            if(!stack.has(ModComponents.CREATIVE_SPAWNED)) {
                event.getEntity().discard();
                event.getPlayer().getInventory().clearOrCountMatchingItems(s ->
                        s.is(ModItems.UNSTABLE_INGOT.get()), -1, event.getPlayer().getInventory());
                event.getEntity().level().explode(event.getEntity(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), 0.25f, false, Level.ExplosionInteraction.NONE);
                event.getPlayer().hurt(ModDamageTypes.getDamageSource(event.getEntity().level(), ModDamageTypes.DIVIDE_BY_DIAMOND), Float.MAX_VALUE);
            }
        }
    }

    @SubscribeEvent
    public static void moveIngotToOffhand(LivingEquipmentChangeEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;
        if(event.getSlot() != EquipmentSlot.OFFHAND) return;
        if(event.getFrom().is(ModItems.UNSTABLE_INGOT.get())) {
            player.getOffhandItem().shrink(1);
            UnstableIngot.unstableIngotFail(player);
        }
    }
}