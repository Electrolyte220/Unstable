package com.electrolyte.unstable;

import com.electrolyte.unstable.saveddata.*;
import com.electrolyte.unstable.end_siege.UnstableEntityDataStorage;
import com.electrolyte.unstable.end_siege.UnstableEntityDataStorageManager;
import com.electrolyte.unstable.init.ModItems;
import com.electrolyte.unstable.init.ModTools;
import com.google.gson.Gson;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = Unstable.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class UnstableEventHandler {

    @SubscribeEvent
    public static void onDatapackReload(AddReloadListenerEvent event) {
        event.addListener(new EntityDataReloadListener(new Gson(), "end_siege_entity_data"));
    }

    @SubscribeEvent
    public static void onDestructionPickaxeCraft(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().is(ModTools.DESTRUCTION_PICKAXE.get())) {
            //TODO: Check if there is a better way to do this.
            int ingots = 0;
            for(int i = 0; i < event.getInventory().getContainerSize(); i++) {
                ItemStack item = event.getInventory().getItem(i);
                if (item.is(ModItems.UNSTABLE_STABLE_INGOT.get()) && ingots < 3) {
                    ingots++;
                }
            }
            if (ingots == 3) {
                event.getCrafting().enchant(Enchantments.BLOCK_EFFICIENCY, 10);
            }
        }
    }

    @SubscribeEvent
    public static void onBossKilled(LivingDropsEvent event) {
        if (event.getEntity() instanceof WitherBoss || event.getEntity() instanceof EnderDragon) {
            event.getDrops().add(new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ModItems.DIVISION_SIGIL.get(), 1)));
        }
    }

    @SubscribeEvent
    public static void disableEndermanSpawn(LivingSpawnEvent.CheckSpawn event) {
        if(event.getEntity().level.isClientSide) return;
        ServerLevel serverLevel = Objects.requireNonNull(event.getEntity().level.getServer()).getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(serverLevel);
        if(data.isEndSiegeOccurring()) {
            if (event.getEntity() instanceof EnderMan) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void removeEnderman(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity().level.isClientSide) return;
        ServerLevel serverLevel = Objects.requireNonNull(event.getEntity().level.getServer()).getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(serverLevel);
        if (data.isEndSiegeOccurring()) {
            ResourceLocation entityDim = event.getEntity().level.dimension().location();
            if (event.getEntity() instanceof EnderMan && entityDim.equals(DimensionType.END_LOCATION.location())) {
                event.getEntity().remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @SubscribeEvent
    public static void onMobKilled(LivingDeathEvent event) {
        if (event.getEntity() instanceof Mob) {
            BlockPos pos1 = new BlockPos(event.getEntity().getX(), event.getEntity().getY() + 1, event.getEntity().getZ());
            BlockPos pos2 = new BlockPos(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
            Block enchantBlock = event.getEntity().getLevel().getBlockState(pos1.below()).getBlock();
            Block beaconBlock = event.getEntity().getLevel().getBlockState(pos1.below(2)).getBlock();
            if (!(event.getSource().getDirectEntity() instanceof Player))
                return;
            ServerPlayer player = (ServerPlayer) event.getSource().getDirectEntity();
            ResourceLocation playerDim = player.level.dimension().location();
            if (enchantBlock == Blocks.ENCHANTING_TABLE) {
                for (Slot slot : player.inventoryMenu.slots) {
                    ItemStack stack = slot.getItem();
                    if (stack.getItem() == ModItems.DIVISION_SIGIL_ACTIVATED.get() || stack.getItem() == ModItems.DIVISION_SIGIL_STABLE.get()) {
                        event.setCanceled(true);
                        player.sendMessage(new TranslatableComponent(ChatFormatting.RED + "Sigil(s) have already been activated!"), player.getUUID());
                    } else if (stack.getItem() == ModItems.DIVISION_SIGIL.get() &&
                            DivisionCheck.checkTime(event.getEntity().getLevel().getDayTime()) &&
                            DivisionCheck.checkNatural(event.getEntity().getLevel(), pos2) &&
                            DivisionCheck.checkRedstone(event.getEntity().getLevel(), pos2) &&
                            DivisionCheck.checkLight(event.getEntity().getLevel(), pos2) &&
                            DivisionCheck.checkSky(event.getEntity().getLevel(), pos2)) {
                        LightningBolt lightningBoltEntity = new LightningBolt(EntityType.LIGHTNING_BOLT, event.getEntity().getLevel());
                        ServerLevel world = (ServerLevel) event.getEntity().getLevel();
                        lightningBoltEntity.setPos(pos1.getX(), pos1.getY(), pos1.getZ());
                        world.addFreshEntity(lightningBoltEntity);
                        player.getInventory().removeItem(stack);
                        player.getInventory().add(new ItemStack(ModItems.DIVISION_SIGIL_ACTIVATED.get()));
                        DivisionCheck.updateBlocks(event.getEntity().getLevel(), pos2);
                        DivisionCheck.updateRedstone(event.getEntity().getLevel(), pos2);
                    }
                }
            }
            UnstableSavedData data = UnstableSavedData.get(player.level);
            if (beaconBlock == Blocks.BEACON && event.getEntity() instanceof IronGolem) {
                for(Slot slot : player.inventoryMenu.slots) {
                    ItemStack stack = slot.getItem();
                    if (stack.getItem() == ModItems.DIVISION_SIGIL.get()) {
                        event.setCanceled(true);
                        player.sendMessage(new TranslatableComponent(ChatFormatting.RED + "You must first perform the Activation Ritual before performing the Pseudo-Inversion Ritual"), player.getUUID());
                    } else if (stack.getItem() == ModItems.DIVISION_SIGIL_STABLE.get()) {
                        event.setCanceled(true);
                        player.sendMessage(new TranslatableComponent(ChatFormatting.RED + "Sigil is already Stable"), player.getUUID());
                    } else if (stack.getItem() == ModItems.DIVISION_SIGIL_ACTIVATED.get()) {
                        if (data.isEndSiegeOccurring()) {
                            event.setCanceled(true);
                            player.sendMessage(new TranslatableComponent(ChatFormatting.RED + "There can only be one Pseudo-Inversion Ritual active at any given time."), player.getUUID());
                        } else if (DivisionCheckEnd.checkChests(event.getEntity().getLevel(), pos2.below()) &&
                                DivisionCheckEnd.checkNorthChestContents(event.getEntity().getLevel(), pos2.below()) &&
                                DivisionCheckEnd.checkSouthChestContents(event.getEntity().getLevel(), pos2.below()) &&
                                DivisionCheckEnd.checkEastChestContents(event.getEntity().getLevel(), pos2.below()) &&
                                DivisionCheckEnd.checkWestChestContents(event.getEntity().getLevel(), pos2.below()) &&
                                DivisionCheckEnd.checkRedstoneAndString(event.getEntity().getLevel(), pos2.below())) {
                            DivisionCheckEnd.destroyBeaconAndChests(event.getEntity().getLevel(), pos2);
                            player.sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "The Siege in 'The End' has begun"), player.getUUID());
                            AtomicInteger players = new AtomicInteger();
                            player.level.players().forEach(playerIn -> {
                                if (playerIn.level.dimension().location().equals(DimensionType.END_LOCATION.getRegistryName())) {
                                    players.getAndIncrement();
                                }
                            });
                            data.setEndSiegeOccurring(true);
                            data.setPlayersParticipating(players.get());
                        }
                    }
                }
            }
            if (playerDim.equals(DimensionType.END_LOCATION.location())) {
                if (data.isEndSiegeOccurring() && data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get() && event.getEntity().getTags().contains("{spawnedBySiege:1b}")) {
                    data.setTotalKills(data.getTotalKills() + 1);
                    player.sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "Kills: " + data.getTotalKills()), player.getUUID());
                }
                if (data.getTotalKills() >= UnstableConfig.NEEDED_MOBS.get()) {
                    player.sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "The Siege in 'The End' has ended"), player.getUUID());
                    data.resetData();
                    player.getInventory().add(new ItemStack(ModItems.DIVISION_SIGIL_STABLE.get()));
                    if (UnstableConfig.REMOVE_ACTIVE_SIGIL.get()) {
                        AtomicBoolean foundSigil = new AtomicBoolean(false);
                        player.inventoryMenu.slots.forEach(slot -> {
                            if (!foundSigil.get() && slot.getItem().getItem() == ModItems.DIVISION_SIGIL_ACTIVATED.get()) {
                                slot.remove(1);
                                foundSigil.set(true);
                            }
                        });
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.getEntity().level.isClientSide) return;
        ServerLevel serverLevel = Objects.requireNonNull(event.getEntity().level.getServer()).getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(serverLevel);
        if(data.isEndSiegeOccurring() && !event.getTo().location().equals(Level.END)) {
            event.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.RED + "You must be in The End whilst performing this ritual!"), event.getPlayer().getUUID());
            event.getPlayer().sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "The Siege in 'The End' has ended."), event.getPlayer().getUUID());
            data.resetData();
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.world.dimension() == Level.END) {
            ServerLevel level = Objects.requireNonNull(event.world.getServer()).getLevel(Level.END);
            UnstableSavedData data = UnstableSavedData.get(level);
            int posX = -10;
            int posY = 70;
            int posZ = -10;
            int distanceAway = new Random().nextInt(UnstableConfig.MOB_SPAWN_RANGE.get()) - UnstableConfig.MOB_SPAWN_RANGE.get();
            if (data.isEndSiegeOccurring()) {
                AtomicInteger playersParticipating = new AtomicInteger();
                if(level.getServer().getTickCount() % 20 == 0) { level.players().forEach(player -> {
                    if(player.level.dimension().location().equals(DimensionType.END_LOCATION.location())) playersParticipating.getAndIncrement();
                });
                    data.setPlayersParticipating(playersParticipating.get());
                }
                if(level.getServer().getTickCount() % 100 == 0) {
                    if (data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get()) {
                        Iterator<Entity> mobs = level.getEntities().getAll().iterator();
                        int mobCount = 0;
                        while(mobs.hasNext()) {
                            Entity entity = mobs.next();
                            if(entity instanceof Mob) mobCount++;
                        }
                        if (mobCount < UnstableConfig.MAX_MOBS.get()) {
                            for (int i = 0; i < playersParticipating.get(); i++) {
                                int spawnedMobInt = new Random().nextInt(UnstableEntityDataStorageManager.getMasterStorage().size());
                                UnstableEntityDataStorage entityData = UnstableEntityDataStorageManager.getMasterStorage().get(spawnedMobInt);
                                Optional<EntityType<?>> exists = EntityType.byString(entityData.entity().getRegistryName().toString());
                                while (exists.isEmpty()) {
                                    Unstable.LOGGER.error("Mob " + '\'' + entityData.entity().getRegistryName().toString() + '\'' + " cannot be spawned as it does not exist in the registry.");
                                    spawnedMobInt = new Random().nextInt(UnstableEntityDataStorageManager.getMasterStorage().size());
                                    entityData = UnstableEntityDataStorageManager.getMasterStorage().get(spawnedMobInt);
                                    EntityType.byString(entityData.entity().getRegistryName().toString());
                                }
                                EntityType<?> entityType = exists.get();
                                Mob mob = (Mob) entityType.create(level);
                                CompoundTag tag = new CompoundTag();
                                tag.putBoolean("spawnedBySiege", true);
                                mob.addTag(tag.toString());
                                mob.setPos(posX, posY, posZ + 3);
                                if (!entityData.effects().isEmpty()) {
                                    entityData.effects().forEach(mob::addEffect);
                                }
                                if (!entityData.equipment().isEmpty()) {
                                    entityData.equipment().forEach(equipmentList -> equipmentList.forEach((interactionHand, stack) ->
                                            //Apparently we have to copy the stack because totems are dumb
                                            mob.setItemInHand(interactionHand, stack.copy())));
                                    mob.setDropChance(EquipmentSlot.MAINHAND, 0);
                                    mob.setDropChance(EquipmentSlot.OFFHAND, 0);
                                }
                                if (!entityData.armor().isEmpty()) {
                                    entityData.armor().forEach(armorList -> armorList.forEach(mob::setItemSlot));
                                    mob.setDropChance(EquipmentSlot.HEAD, 0);
                                    mob.setDropChance(EquipmentSlot.CHEST, 0);
                                    mob.setDropChance(EquipmentSlot.LEGS, 0);
                                    mob.setDropChance(EquipmentSlot.FEET, 0);
                                }
                                level.addFreshEntity(mob);
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
        ServerLevel serverLevel = Objects.requireNonNull(player.level.getServer()).getLevel(Level.END);
        UnstableSavedData data = UnstableSavedData.get(serverLevel);
        if (data.isEndSiegeOccurring()) {
            if(event.isWasDeath()) {
                player.sendMessage(new TranslatableComponent(ChatFormatting.RED + "You cannot die whilst performing this ritual!"), player.getUUID());
            } else {
                player.sendMessage(new TranslatableComponent(ChatFormatting.RED + "You must be in The End whilst performing this ritual!"), player.getUUID());
            }
            player.sendMessage(new TranslatableComponent(ChatFormatting.WHITE + "The Siege in 'The End' has ended."), player.getUUID());
            data.resetData();
        }
    }


    /*@SubscribeEvent
    public static void alterBlockDrops(PlayerEvent.HarvestCheck event) {
        LootContext.Builder builder = event.getTargetBlock().
        List<ItemStack> drops = event.getTargetBlock().getDrops(builder);
        if(event.getPlayer() == null) return;
        if(event.getPlayer().getHeldItemMainhand().getItem() == ModTools.destructionPickaxe.get() || event.getPlayer().getHeldItemMainhand().getItem() == ModTools.erosionShovel.get()) {
            event.getDrops().clear();
        } else if (event.getPlayer().getHeldItemMainhand().getItem() == ModTools.precisionShears.get() && !event.getPlayer().isCrouching()) {
            for (ItemStack drop : drops) {
                event.getPlayer().addItemStackToInventory(drop);
            }
        }
    }*/
}