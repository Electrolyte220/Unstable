package com.electrolyte.unstable.handler;

import com.electrolyte.unstable.Unstable;
import com.electrolyte.unstable.UnstableConfig;
import com.electrolyte.unstable.UnstableEnums;
import com.electrolyte.unstable.damagesource.DivideByDiamondDamageSource;
import com.electrolyte.unstable.endsiege.UnstableEntityDataStorage;
import com.electrolyte.unstable.helper.ActivationRitualHelper;
import com.electrolyte.unstable.helper.PseudoInversionRitualHelper;
import com.electrolyte.unstable.init.ModItems;
import com.electrolyte.unstable.init.ModSounds;
import com.electrolyte.unstable.listener.EndSiegeChestDataReloadListener;
import com.electrolyte.unstable.listener.EntityDataReloadListener;
import com.electrolyte.unstable.savedata.UnstableSavedData;
import com.google.gson.Gson;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.Slot;
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
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = Unstable.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class UnstableEventHandler {

    @SubscribeEvent
    public static void onDatapackReload(AddReloadListenerEvent event) {
        event.addListener(new EntityDataReloadListener(new Gson(), "end_siege_entity_data"));
        event.addListener(new EndSiegeChestDataReloadListener(new Gson(), "end_siege_chest_data"));
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
        } else {
            for (String tag : event.getEntity().getTags()) {
                if (tag.equals("{spawnedBySiege:1b}")) {
                    event.getEntity().remove(Entity.RemovalReason.DISCARDED);
                }
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
                        player.sendMessage(new TranslatableComponent("unstable.activation_ritual.active_sigil").withStyle(ChatFormatting.RED), player.getUUID());
                    } else if (stack.getItem() != ModItems.DIVISION_SIGIL_ACTIVATED.get()
                            && stack.getItem() != ModItems.DIVISION_SIGIL_STABLE.get() && stack.getItem() == ModItems.DIVISION_SIGIL.get() &&
                            ActivationRitualHelper.checkTime(event.getEntity().getLevel().getDayTime()) &&
                            ActivationRitualHelper.checkNatural(event.getEntity().getLevel(), pos2) &&
                            ActivationRitualHelper.checkRedstone(event.getEntity().getLevel(), pos2) &&
                            ActivationRitualHelper.checkLight(event.getEntity().getLevel(), pos2) &&
                            ActivationRitualHelper.checkSky(event.getEntity().getLevel(), pos2)) {
                        LightningBolt lightningBoltEntity = new LightningBolt(EntityType.LIGHTNING_BOLT, event.getEntity().getLevel());
                        ServerLevel level = (ServerLevel) event.getEntity().getLevel();
                        level.playSound(null, pos1.getX(), pos1.getY(), pos1.getZ(), ModSounds.ACTIVATION_RITUAL_SUCCESS.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
                        lightningBoltEntity.setPos(pos1.getX(), pos1.getY(), pos1.getZ());
                        level.addFreshEntity(lightningBoltEntity);
                        player.getInventory().removeItem(stack);
                        player.getInventory().add(new ItemStack(ModItems.DIVISION_SIGIL_ACTIVATED.get()));
                        ActivationRitualHelper.updateBlocks(level, pos2);
                        ActivationRitualHelper.updateRedstone(level, pos2);
                    }
                }
            }
            UnstableSavedData data = UnstableSavedData.get(player.level);
            if (beaconBlock == Blocks.BEACON && event.getEntity() instanceof IronGolem) {
                for(Slot slot : player.inventoryMenu.slots) {
                    ItemStack stack = slot.getItem();
                    if (stack.getItem() == ModItems.DIVISION_SIGIL.get()) {
                        event.setCanceled(true);
                        player.sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.not_activated").withStyle(ChatFormatting.RED), player.getUUID());
                    } else if (stack.getItem() == ModItems.DIVISION_SIGIL_STABLE.get()) {
                        event.setCanceled(true);
                        player.sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.already_stable").withStyle(ChatFormatting.RED), player.getUUID());
                    } else if (stack.getItem() == ModItems.DIVISION_SIGIL_ACTIVATED.get()) {
                        if (data.isEndSiegeOccurring()) {
                            event.setCanceled(true);
                            player.sendMessage(new TranslatableComponent( "unstable.pseudo_inversion_ritual.multiple_sigil").withStyle(ChatFormatting.RED), player.getUUID());
                        } else if (PseudoInversionRitualHelper.checkChests(event.getEntity().getLevel(), pos2.below()) &&
                                PseudoInversionRitualHelper.checkChestContents(event.getEntity().getLevel(), pos2.below().north(5), UnstableEnums.CHEST_LOCATION.NORTH) &&
                                PseudoInversionRitualHelper.checkChestContents(event.getEntity().getLevel(), pos2.below().south(5), UnstableEnums.CHEST_LOCATION.SOUTH) &&
                                PseudoInversionRitualHelper.checkChestContents(event.getEntity().getLevel(), pos2.below().east(5), UnstableEnums.CHEST_LOCATION.EAST) &&
                                PseudoInversionRitualHelper.checkChestContents(event.getEntity().getLevel(), pos2.below().west(5), UnstableEnums.CHEST_LOCATION.WEST) &&
                                PseudoInversionRitualHelper.checkRedstoneAndString(event.getEntity().getLevel(), pos2.below())) {
                            PseudoInversionRitualHelper.destroyBeaconAndChests(event.getEntity().getLevel(), pos2);
                            AtomicInteger players = new AtomicInteger();
                            player.level.players().forEach(playerIn -> {
                                if (playerIn.level.dimension().location().equals(DimensionType.END_LOCATION.location())) {
                                    playerIn.displayClientMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_started").withStyle(ChatFormatting.WHITE), true);
                                    players.getAndIncrement();
                                    ListTag listTag = new ListTag();
                                    if (playerIn.getInventory().contains(stack)) {
                                        CompoundTag tag = new CompoundTag();
                                        tag.putUUID("playerUUID", playerIn.getUUID());
                                        listTag.addAll(data.getPlayersWithActivationSigil());
                                        listTag.add(tag);
                                        data.setPlayersWithActivationSigil(listTag);
                                        playerIn.getInventory().removeItem(stack);
                                    }
                                }
                            });
                            data.setEndSiegeOccurring(true);
                            //data.setStartingLocation(new int[]{pos1.getX(), pos1.below(2).getY(), pos1.getZ()});
                            data.setPlayersParticipating(players.get());
                        }
                    }
                }
            }
            if (playerDim.equals(DimensionType.END_LOCATION.location())) {
                if (data.isEndSiegeOccurring() && data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get() && event.getEntity().getTags().contains("{spawnedBySiege:1b}")) {
                    data.setTotalKills(data.getTotalKills() + 1);
                    PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_kills", data.getTotalKills() + "/" + UnstableConfig.NEEDED_MOBS.get()).withStyle(ChatFormatting.WHITE), event.getEntity().getLevel(), data);
                }
                if (data.getTotalKills() >= UnstableConfig.NEEDED_MOBS.get()) {
                    PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().getLevel(), data);
                    player.level.players().forEach(playerIn -> {
                        for(Tag tag : data.getPlayersWithActivationSigil()) {
                            CompoundTag tag1 = (CompoundTag) tag;
                            if(tag1.getUUID("playerUUID").equals(playerIn.getUUID())) {
                                for (int i = 0; i < playerIn.getInventory().getContainerSize(); i++) {
                                    if (playerIn.getInventory().getItem(i).getItem() == ModItems.DIVISION_SIGIL_ACTIVATED.get()) {
                                        playerIn.getInventory().setItem(i, new ItemStack(ModItems.DIVISION_SIGIL_STABLE.get()));
                                   }
                                }
                            }
                        }
                    });
                    data.resetData();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCraftingMenuClose(PlayerContainerEvent.Close event) {
        if(event.getContainer() instanceof CraftingMenu) {
            if(event.getEntity() instanceof Player player) {
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
        }
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.world.dimension() == Level.END) {
            ServerLevel level = event.world.getServer().getLevel(Level.END);
            UnstableSavedData data = UnstableSavedData.get(level);
            if (data.isEndSiegeOccurring()) {
                int playersParticipating = level.players().size();
                data.setPlayersParticipating(playersParticipating);
                int maxSpawningRange = UnstableConfig.MOB_SPAWN_RAGE_PIR.get();
                if(level.getServer().getTickCount() % 10 == 0) {
                    AABB spawnableLocations = new AABB(-maxSpawningRange, 55, -maxSpawningRange, maxSpawningRange, 75, maxSpawningRange);
                    if (data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get()) {
                        int mobCount = level.getEntities(null, spawnableLocations).size();
                        if (mobCount < UnstableConfig.MAX_MOBS.get()) {
                            for (int i = 0; i < playersParticipating; i++) {
                                int spawnedMobInt = new Random().nextInt(UnstableEntityDataStorage.getMasterStorage().size());
                                UnstableEntityDataStorage entityData = UnstableEntityDataStorage.getMasterStorage().get(spawnedMobInt);
                                Optional<EntityType<?>> exists = EntityType.byString(entityData.entity().getRegistryName().toString());
                                while (exists.isEmpty()) {
                                    Unstable.LOGGER.error("Mob " + '\'' + entityData.entity().getRegistryName().toString() + '\'' + " cannot be spawned as it does not exist in the registry.");
                                    spawnedMobInt = new Random().nextInt(UnstableEntityDataStorage.getMasterStorage().size());
                                    entityData = UnstableEntityDataStorage.getMasterStorage().get(spawnedMobInt);
                                    exists = EntityType.byString(entityData.entity().getRegistryName().toString());
                                }
                                EntityType<?> entityType = exists.get();
                                Mob mob = (Mob) entityType.create(level);
                                CompoundTag tag = new CompoundTag();
                                tag.putBoolean("spawnedBySiege", true);
                                mob.addTag(tag.toString());
                                mob.setPos(genXOrZ(), genY(), genXOrZ());
                                while (!NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, level, mob.getOnPos(), entityType)) {
                                    mob.setPos(genXOrZ(), genY(), genXOrZ());
                                }
                                if (!entityData.effects().isEmpty()) {
                                    entityData.effects().forEach(mob::addEffect);
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
    }

    private static int genXOrZ(/*int relPos*/) {
        return /*relPos + */ (int) (Math.random()  * UnstableConfig.MOB_SPAWN_RAGE_PIR.get() * (Math.random() > 0.5 ? 1 : -1));
    }

    private static int genY() {
        return (int) (Math.random() * 10) + 55;
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