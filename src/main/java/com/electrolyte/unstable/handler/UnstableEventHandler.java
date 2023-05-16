package com.electrolyte.unstable.handler;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.UnstableConfig;
import com.electrolyte.unstable.damagesource.DivideByDiamondDamageSource;
import com.electrolyte.unstable.datastorage.endsiege.EntityDataStorage;
import com.electrolyte.unstable.helper.ActivationRitualHelper;
import com.electrolyte.unstable.helper.PseudoInversionRitualHelper;
import com.electrolyte.unstable.init.ModItems;
import com.electrolyte.unstable.init.ModSounds;
import com.electrolyte.unstable.listener.EndSiegeChestDataReloadListener;
import com.electrolyte.unstable.listener.EntityDataReloadListener;
import com.electrolyte.unstable.listener.PropertyRegressionReloadListener;
import com.electrolyte.unstable.listener.TransmutationReloadListener;
import com.electrolyte.unstable.savedata.UnstableSavedData;
import com.google.gson.Gson;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = Unstable.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class UnstableEventHandler {

    @SubscribeEvent
    public static void onDatapackReload(AddReloadListenerEvent event) {
        event.addListener(new EntityDataReloadListener(new Gson(), "end_siege_entity_data"));
        event.addListener(new EndSiegeChestDataReloadListener(new Gson(), "end_siege_chest_data"));
        event.addListener(new TransmutationReloadListener(new Gson(), "reversing_hoe/recipes/transmutation"));
        event.addListener(new PropertyRegressionReloadListener(new Gson(), "reversing_hoe/recipes/property_regression"));
    }

    @SubscribeEvent
    public static void onBossKilled(LivingDropsEvent event) {
        ServerLevel level = event.getEntity().level.getServer().getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(level);
        if (event.getEntity() instanceof ElderGuardian || event.getEntity() instanceof EnderDragon || event.getEntity() instanceof WitherBoss) {
            event.getDrops().add(new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ModItems.DIVISION_SIGIL.get(), 1)));
        } else if (data.isEndSiegeOccurring() && event.getEntity().getLevel().dimension().location().equals(DimensionType.END_LOCATION.location())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEndSiegeMobKilled(LivingExperienceDropEvent event) {
        ServerLevel level = event.getEntity().level.getServer().getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(level);
        if(!data.isEndSiegeOccurring()) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void tagVex(LivingSpawnEvent event) {
        if(event.getEntity().level.isClientSide) return;
        ServerLevel level = event.getEntity().level.getServer().getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(level);
        if(data.isEndSiegeOccurring() && event.getEntity() instanceof Vex vex) {
            if(!vex.getTags().contains("{spawnedBySiege:1b}")) {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("spawnedBySiege", true);
                vex.addTag(tag.toString());
            }
        }
    }

    @SubscribeEvent
    public static void disableEndermanSpawn(LivingSpawnEvent.CheckSpawn event) {
        if(event.getEntity().level.isClientSide) return;
        ServerLevel serverLevel = event.getEntity().level.getServer().getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(serverLevel);
        if(data.isEndSiegeOccurring()) {
            if (event.getEntity() instanceof EnderMan) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void updateEntity(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity().level.isClientSide) return;
        ServerLevel serverLevel = event.getEntity().level.getServer().getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(serverLevel);
        if (data.isEndSiegeOccurring()) {
            ResourceLocation entityDim = event.getEntity().level.dimension().location();
            if (event.getEntity() instanceof EnderMan && entityDim.equals(DimensionType.END_LOCATION.location())) {
                event.getEntity().remove(Entity.RemovalReason.DISCARDED);
            }
        } else if (event.getEntity().getTags().contains("{spawnedBySiege:1b}")) {
            event.getEntity().remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @SubscribeEvent
    public static void onCreeperExplode(ExplosionEvent event) {
        if(event.getExplosion().getSourceMob() instanceof Creeper creeper) {
            if(creeper.getTags().contains("noLingeringEffects")) {
                creeper.removeAllEffects();
            }
        }
    }

    @SubscribeEvent
    public static void onActivationMobKilled(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player)) return;
        if(!(event.getEntity() instanceof Mob)) return;
        BlockPos pos1 = new BlockPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
        BlockPos pos2 = new BlockPos(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
        Block enchantBlock = event.getEntity().getLevel().getBlockState(pos1.below()).getBlock();
        ServerPlayer player = (ServerPlayer) event.getSource().getEntity();
        if(enchantBlock != Blocks.ENCHANTING_TABLE) return;
        if(player.getInventory().contains(ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance()) || player.getInventory().contains(ModItems.DIVISION_SIGIL_STABLE.get().getDefaultInstance())) {
            event.setCanceled(true);
            player.sendMessage(new TranslatableComponent("unstable.activation_ritual.active_sigil").withStyle(ChatFormatting.RED), player.getUUID());
        } else if (player.getInventory().contains(ModItems.DIVISION_SIGIL.get().getDefaultInstance()) && ActivationRitualHelper.checkConditions(event.getEntity().getLevel(), pos2)) {
            LightningBolt lightningBoltEntity = new LightningBolt(EntityType.LIGHTNING_BOLT, event.getEntity().getLevel());
            ServerLevel level = (ServerLevel) event.getEntity().getLevel();
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
        BlockPos pos = new BlockPos(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ()).below();
        Block beaconBlock = event.getEntity().getLevel().getBlockState(pos).getBlock();
        if(!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        if(beaconBlock != Blocks.BEACON) return;
        if (!(event.getSource().getDirectEntity() instanceof Player)) return;
        UnstableSavedData data = UnstableSavedData.get(player.level);
        if(data.isEndSiegeOccurring()) {
            event.setCanceled(true);
            player.sendMessage(new TranslatableComponent( "unstable.pseudo_inversion_ritual.multiple_sigil").withStyle(ChatFormatting.RED), player.getUUID());
        }
        if(player.getInventory().contains(ModItems.DIVISION_SIGIL.get().getDefaultInstance())) {
            event.setCanceled(true);
            player.sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.not_activated").withStyle(ChatFormatting.RED), player.getUUID());
        } else if(player.getInventory().contains(ModItems.DIVISION_SIGIL_STABLE.get().getDefaultInstance())) {
            event.setCanceled(true);
            player.sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.already_stable").withStyle(ChatFormatting.RED), player.getUUID());
        } else if(player.getInventory().contains(ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance())) {
            if (PseudoInversionRitualHelper.checkChestsPos(event.getEntity().getLevel(), pos) &&
                    PseudoInversionRitualHelper.checkAllChestContents(event.getEntity().getLevel(), pos) &&
                    PseudoInversionRitualHelper.checkRedstoneAndString(event.getEntity().getLevel(), pos)) {
                PseudoInversionRitualHelper.destroyBeaconAndChests(event.getEntity().getLevel(), pos);
                AtomicInteger players = new AtomicInteger();
                player.level.players().forEach(playerIn -> {
                    if (playerIn.level.dimension().location().equals(DimensionType.END_LOCATION.location())) {
                        playerIn.displayClientMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_started").withStyle(ChatFormatting.WHITE), true);
                        players.getAndIncrement();
                    }
                });
                data.setEndSiegeOccurring(true);
                data.setStartingLocation(new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance()), 1);
            }
        }
    }

    @SubscribeEvent
    public static void onEndSiegeMobKilled(LivingDeathEvent event) {
        if(!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        UnstableSavedData data = UnstableSavedData.get(player.level);
        if(!data.isEndSiegeOccurring()) return;
        ResourceLocation playerDim = player.level.dimension().location();
        if(!(playerDim.equals(DimensionType.END_LOCATION.location()))) return;
        if (data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get() && event.getEntity().getTags().contains("{spawnedBySiege:1b}")) {
            data.setTotalKills(data.getTotalKills() + 1);
            PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_kills", data.getTotalKills() + "/" + UnstableConfig.NEEDED_MOBS.get()).withStyle(ChatFormatting.WHITE), event.getEntity().getLevel(), data);
        }
        if (data.getTotalKills() >= UnstableConfig.NEEDED_MOBS.get()) {
            PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().getLevel(), data);
            player.getInventory().add(player.getInventory().getFreeSlot(), new ItemStack(ModItems.DIVISION_SIGIL_STABLE.get()));
            data.resetData();
        }
    }

    @SubscribeEvent
    public static void onCraftingMenuClose(PlayerContainerEvent.Close event) {
        if(!(event.getEntity() instanceof Player player)) return;
        if(!(event.getContainer() instanceof CraftingMenu)) return;
        for(ItemStack stack : player.inventoryMenu.getItems()) {
            if(stack.is(ModItems.UNSTABLE_INGOT.get()) && stack.getTag() != null) {
                if (stack.getTag().contains("explodesIn")) {
                    player.level.explode(player, player.getX(), player.getY(), player.getZ(), 1, false, Explosion.BlockInteraction.NONE);
                    player.hurt(DivideByDiamondDamageSource.INSTANCE, Float.MAX_VALUE);
                    player.getInventory().removeItem(stack);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.world.dimension() == Level.END) {
            ServerLevel level = event.world.getServer().getLevel(Level.END);
            UnstableSavedData data = UnstableSavedData.get(level);
            if (!data.isEndSiegeOccurring()) return;
            AABB noFlightZone = new AABB(new BlockPos(data.getStartingLocation()[0], data.getStartingLocation()[1], data.getStartingLocation()[2])).inflate(256);
            List<ServerPlayer> playersParticipating = level.getPlayers(p -> p.getBoundingBox().intersects(noFlightZone));
            ListTag playersParticipatingTag = data.getPlayersParticipating();
            for(ServerPlayer player : playersParticipating) {
                if(!playersParticipatingTag.contains(StringTag.valueOf(player.getStringUUID()))) {
                    playersParticipatingTag.add(StringTag.valueOf(player.getStringUUID()));
                }
                if(player.getAbilities().flying) {
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                    player.hurt(DamageSource.OUT_OF_WORLD, 0.5f);
                }
            }
            data.setPlayersParticipating(playersParticipatingTag);
            if(level.getServer().getTickCount() % 10 == 0) {
                AABB spawnableLocations = new AABB(new BlockPos(data.getStartingLocation()[0], data.getStartingLocation()[1], data.getStartingLocation()[2])).inflate(UnstableConfig.MOB_SPAWN_RAGE_PIR.get());
                if (data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get()) {
                    int mobCount = level.getEntities(null, spawnableLocations).size();
                    if (mobCount < UnstableConfig.MAX_MOBS.get()) {
                        for (int i = 0; i < data.getPlayersParticipating().size(); i++) {
                            int spawnedMobInt = new Random().nextInt(EntityDataStorage.getMasterStorage().size());
                            EntityDataStorage entityData = EntityDataStorage.getMasterStorage().get(spawnedMobInt);
                            Optional<EntityType<?>> exists = EntityType.byString(entityData.entity().getRegistryName().toString());
                            while (exists.isEmpty()) {
                                Unstable.LOGGER.error("Mob " + '\'' + entityData.entity().getRegistryName().toString() + '\'' + " cannot be spawned as it does not exist in the registry.");
                                spawnedMobInt = new Random().nextInt(EntityDataStorage.getMasterStorage().size());
                                entityData = EntityDataStorage.getMasterStorage().get(spawnedMobInt);
                                exists = EntityType.byString(entityData.entity().getRegistryName().toString());
                            }
                            EntityType<?> entityType = exists.get();
                            Mob mob = (Mob) entityType.create(level);
                            CompoundTag tag = new CompoundTag();
                            tag.putBoolean("spawnedBySiege", true);
                            mob.addTag(tag.toString());
                            if(mob instanceof Creeper creeper) {
                                creeper.addTag("noLingeringEffects");
                            }
                            mob.setPos(genXOrZ(data.getStartingLocation()[0]), genY(data.getStartingLocation()[1]), genXOrZ(data.getStartingLocation()[2]));
                            while (!NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, mob.getOnPos(), entityType)) {
                                mob.setPos(genXOrZ(data.getStartingLocation()[0]), genY(data.getStartingLocation()[1]), genXOrZ(data.getStartingLocation()[2]));
                            }
                            if (!entityData.effects().isEmpty()) {
                                entityData.effects().forEach(effect -> mob.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.isVisible())));
                            }
                            if (!entityData.equipment().isEmpty()) {
                                entityData.equipment().forEach(equipmentList -> equipmentList.forEach((interactionHand, stack) ->
                                        //Apparently we have to copy the stack because totems are dumb
                                        mob.setItemInHand(interactionHand, stack.copy())));
                            }
                            if (!entityData.armor().isEmpty()) {
                                entityData.armor().forEach(armorList -> armorList.forEach(mob::setItemSlot));
                            }
                            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(new BlockPos(mob.getX(), mob.getY(), mob.getZ())), MobSpawnType.NATURAL, null, null);
                            level.addFreshEntity(mob);
                        }
                    }
                }
            }
        }
    }

    private static int genXOrZ(int relPos) {
        return relPos + (int) (Math.random()  * UnstableConfig.MOB_SPAWN_RAGE_PIR.get() * (Math.random() > 0.5 ? 1 : -1));
    }

    private static int genY(int relY) {
        return relY + (int) (Math.random() * 10);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.player instanceof ServerPlayer) {
            if (event.player.containerMenu instanceof CraftingMenu menu) {
                if (menu.getCarried().is(ModItems.UNSTABLE_INGOT.get())) {
                    ItemStack stack = menu.getCarried();
                    if (stack.getTag() != null && !stack.getTag().getBoolean("creativeSpawned")) {
                        if (stack.getTag().getInt("explodesIn") > 0) {
                            stack.getTag().putInt("explodesIn", stack.getTag().getInt("explodesIn") - 1);
                        } else {
                            stack.shrink(1);
                            event.player.level.explode(null, event.player.getX(), event.player.getY(), event.player.getZ(), 1, false, Explosion.BlockInteraction.NONE);
                            event.player.hurt(DivideByDiamondDamageSource.INSTANCE, Float.MAX_VALUE);
                        }
                    }
                }
                for (ItemStack stack : menu.getItems()) {
                    if(stack.is(ModItems.UNSTABLE_INGOT.get())) {
                        if(stack.getTag() != null && !stack.getTag().getBoolean("creativeSpawned")) {
                            if(stack.getTag().getInt("explodesIn") > 0) {
                                stack.getTag().putInt("explodesIn", stack.getTag().getInt("explodesIn") - 1);
                            } else {
                                event.player.level.explode(null, event.player.getX(), event.player.getY(), event.player.getZ(), 1, false, Explosion.BlockInteraction.NONE);
                                event.player.hurt(DivideByDiamondDamageSource.INSTANCE, Float.MAX_VALUE);
                                menu.getItems().forEach(item -> {
                                    if(item.is(ModItems.UNSTABLE_INGOT.get())) item.shrink(1);
                                });
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void cancelSiege(PlayerEvent.Clone event) {
        Player player = event.getPlayer();
        ServerLevel serverLevel = player.level.getServer().getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(serverLevel);
        if (data.isEndSiegeOccurring()) {
            if(event.isWasDeath()) {
                player.sendMessage(new TranslatableComponent( "unstable.pseudo_inversion_ritual.death").withStyle(ChatFormatting.RED), player.getUUID());
            } else {
                player.sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.changed_dimension").withStyle(ChatFormatting.RED), player.getUUID());
            }
            player.sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), player.getUUID());
            data.resetData();
        }
        if(!UnstableConfig.SOUL_RESET_DEATH.get()) {
            if (event.getPlayer().getAttribute(Attributes.MAX_HEALTH).getValue() != (event.getOriginal().getAttributeBaseValue(Attributes.MAX_HEALTH))) {
                event.getPlayer().getAttribute(Attributes.MAX_HEALTH).setBaseValue(event.getOriginal().getAttributeBaseValue(Attributes.MAX_HEALTH));
            }
        }
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.getEntity().level.isClientSide) return;
        ServerLevel serverLevel = event.getEntity().level.getServer().getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(serverLevel);
        if(data.isEndSiegeOccurring() && !event.getTo().location().equals(Level.END.location())) {
            event.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.changed_dimension").withStyle(ChatFormatting.RED), event.getPlayer().getUUID());
            event.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getPlayer().getUUID());
            data.resetData();
        }
    }

    @SubscribeEvent
    public static void tossUnstableIngot(ItemTossEvent event) {
        ItemStack stack = event.getEntityItem().getItem();
        if(stack.is(ModItems.UNSTABLE_INGOT.get())) {
            if(stack.getTag() != null && !stack.getTag().getBoolean("creativeSpawned")) {
                event.getEntityItem().level.explode(event.getEntityItem(), event.getEntityItem().getX(), event.getEntityItem().getY(), event.getEntityItem().getZ(), 0, false, Explosion.BlockInteraction.NONE);
                event.getPlayer().hurt(DivideByDiamondDamageSource.INSTANCE, Float.MAX_VALUE);
                event.getEntityItem().remove(Entity.RemovalReason.DISCARDED);
                event.setCanceled(true);
            }
        }
    }
}