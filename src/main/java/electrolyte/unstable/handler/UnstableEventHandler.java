package electrolyte.unstable.handler;

import com.google.gson.Gson;
import electrolyte.unstable.Unstable;
import electrolyte.unstable.UnstableConfig;
import electrolyte.unstable.UnstableEnums;
import electrolyte.unstable.damagesource.DivideByDiamondDamageSource;
import electrolyte.unstable.damagesource.HealingAxeDamageSource;
import electrolyte.unstable.datastorage.endsiege.EntityDataStorage;
import electrolyte.unstable.datastorage.reversinghoe.TransmutationDataStorage;
import electrolyte.unstable.helper.ActivationRitualHelper;
import electrolyte.unstable.helper.PseudoInversionRitualHelper;
import electrolyte.unstable.init.ModItems;
import electrolyte.unstable.init.ModSounds;
import electrolyte.unstable.init.ModTools;
import electrolyte.unstable.listener.ChestDataReloadListener;
import electrolyte.unstable.listener.EntityDataReloadListener;
import electrolyte.unstable.listener.PropertyRegressionReloadListener;
import electrolyte.unstable.listener.TransmutationReloadListener;
import electrolyte.unstable.savedata.UnstableSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Unstable.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class UnstableEventHandler {

    @SubscribeEvent
    public static void onDatapackReload(AddReloadListenerEvent event) {
        event.addListener(new EntityDataReloadListener(new Gson(), "end_siege/entity_data"));
        event.addListener(new ChestDataReloadListener(new Gson(), "end_siege/chest_data"));
        event.addListener(new TransmutationReloadListener(new Gson(), "reversing_hoe/transmutation"));
        event.addListener(new PropertyRegressionReloadListener(new Gson(), "reversing_hoe/property_regression"));
    }

    @SubscribeEvent
    public static void alterDrops(LivingDropsEvent event) {
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level);
        if (event.getEntity() instanceof ElderGuardian || event.getEntity() instanceof EnderDragon || event.getEntity() instanceof WitherBoss) {
            event.getDrops().add(new ItemEntity(event.getEntity().level, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ModItems.DIVISION_SIGIL.get(), 1)));
        } else if (data.isEndSiegeOccurring() && event.getEntity().getTags().contains("spawnedBySiege")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMonsterHurt(LivingHurtEvent event) {
        if(event.getEntity().getType().getCategory() != MobCategory.MONSTER) return;
        if(!(event.getSource().getEntity() instanceof Player player)) return;
        if(!(player.getMainHandItem().is(ModTools.HEALING_AXE.get()))) return;
        event.setAmount(event.getAmount() + 6);
        ((ServerLevel) event.getEntity().getLevel()).sendParticles(ParticleTypes.WITCH, (event.getEntity().getX() - 0.5) + new Random().nextDouble(1), (event.getEntity().getY() + 0.75) + new Random().nextDouble(1), (event.getEntity().getZ() - 0.5) + new Random().nextDouble(1), 5, 0, 0.5, 0, 0.25);
        player.hurt(HealingAxeDamageSource.INSTANCE, 1.5F);
        player.getMainHandItem().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
    }

    @SubscribeEvent
    public static void onEndSiegeMobKilled(LivingExperienceDropEvent event) {
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level);
        if(!data.isEndSiegeOccurring()) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void tagVex(LivingSpawnEvent event) {
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level);
        if(data.isEndSiegeOccurring() && event.getEntity() instanceof Vex vex) {
            if(!vex.getTags().contains("spawnedBySiege")) {
                vex.addTag("spawnedBySiege");
            }
        }
    }

    @SubscribeEvent
    public static void disableEndermanSpawn(LivingSpawnEvent.CheckSpawn event) {
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level);
        if(data.isEndSiegeOccurring()) {
            if (event.getEntity() instanceof EnderMan) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void updateEntity(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity().level.isClientSide) return;
        UnstableSavedData data = UnstableSavedData.get(event.getEntity().level);
        if (data.isEndSiegeOccurring()) {
            ResourceLocation entityDim = event.getEntity().level.dimension().location();
            if (event.getEntity() instanceof EnderMan && entityDim.equals(DimensionType.END_LOCATION.location())) {
                event.getEntity().discard();
            }
        } else if (event.getEntity().getTags().contains("spawnedBySiege")) {
            event.getEntity().discard();
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
                data.setEndSiegeOccurring(true);
                data.setStartingLocation(new int[]{pos.getX(), pos.getY(), pos.getZ()});
                player.getInventory().removeItem(player.getInventory().findSlotMatchingItem(ModItems.DIVISION_SIGIL_ACTIVATED.get().getDefaultInstance()), 1);
                PseudoInversionRitualHelper.destroyBeaconAndChests(event.getEntity().getLevel(), pos);
                ServerLevel serverLevel = event.getEntity().level.getServer().getLevel(Level.END);
                AABB spawnableLocations = new AABB(new BlockPos(data.getStartingLocation()[0], data.getStartingLocation()[1], data.getStartingLocation()[2])).inflate(UnstableConfig.MOB_SPAWN_RAGE_PIR.get());
                List<ServerPlayer> playersParticipating = serverLevel.getPlayers(p -> p.getBoundingBox().intersects(spawnableLocations.inflate(1)));
                ListTag playersParticipatingTag = data.getPlayersParticipating();
                for(ServerPlayer playerIn : playersParticipating) {
                    if (!playersParticipatingTag.contains(StringTag.valueOf(playerIn.getStringUUID()))) {
                        playersParticipatingTag.add(StringTag.valueOf(playerIn.getStringUUID()));
                    }
                }
                PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_started").withStyle(ChatFormatting.WHITE), event.getEntity().getLevel().getServer().getPlayerList(), data);
            }
        }
    }

    @SubscribeEvent
    public static void onEndSiegeMobKilled(LivingDeathEvent event) {
        if(!(event.getSource().getEntity() instanceof ServerPlayer player)) return;
        UnstableSavedData data = UnstableSavedData.get(player.level);
        if(!data.isEndSiegeOccurring()) return;
        AABB spawnableLocations = new AABB(new BlockPos(data.getStartingLocation()[0], data.getStartingLocation()[1], data.getStartingLocation()[2])).inflate(UnstableConfig.MOB_SPAWN_RAGE_PIR.get());
        if(!player.getBoundingBox().intersects(spawnableLocations)) return;
        ResourceLocation playerDim = player.level.dimension().location();
        if(!(playerDim.equals(DimensionType.END_LOCATION.location()))) return;
        if (data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get() && event.getEntity().getTags().contains("spawnedBySiege")) {
            data.setTotalKills(data.getTotalKills() + 1);
            PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_kills", data.getTotalKills() + "/" + UnstableConfig.NEEDED_MOBS.get()).withStyle(ChatFormatting.WHITE), event.getEntity().getLevel().getServer().getPlayerList(), data);
        }
        if (data.getTotalKills() >= UnstableConfig.NEEDED_MOBS.get()) {
            PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().getLevel().getServer().getPlayerList(), data);
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
            UnstableSavedData data = UnstableSavedData.get(event.world);
            if (!data.isEndSiegeOccurring()) return;
            AABB spawnableLocations = new AABB(new BlockPos(data.getStartingLocation()[0], data.getStartingLocation()[1], data.getStartingLocation()[2])).inflate(UnstableConfig.MOB_SPAWN_RAGE_PIR.get());
            List<ServerPlayer> playersParticipating = level.getPlayers(p -> p.getBoundingBox().intersects(spawnableLocations.inflate(1)));
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
                if (data.getTotalKills() < UnstableConfig.NEEDED_MOBS.get()) {
                    int mobCount = level.getEntities(null, spawnableLocations).size();
                    if (mobCount < UnstableConfig.MAX_MOBS.get()) {
                        for (int i = 0; i < data.getPlayersParticipating().size(); i++) {
                            int spawnedMobInt = new Random().nextInt(EntityDataStorage.getMasterStorage().size());
                            EntityDataStorage entityData = EntityDataStorage.getMasterStorage().get(spawnedMobInt);
                            Optional<EntityType<?>> exists = EntityType.byString(entityData.entity().getRegistryName().toString());
                            while (exists.isEmpty()) {
                                Unstable.LOGGER.error("Mob {} cannot be spawned as it does not exist in the registry.", entityData.entity().getRegistryName());
                                spawnedMobInt = new Random().nextInt(EntityDataStorage.getMasterStorage().size());
                                entityData = EntityDataStorage.getMasterStorage().get(spawnedMobInt);
                                exists = EntityType.byString(entityData.entity().getRegistryName().toString());
                            }
                            EntityType<?> entityType = exists.get();
                            Mob mob = (Mob) entityType.create(level);
                            mob.addTag("spawnedBySiege");
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
                                entityData.equipment().forEach(equipmentList -> equipmentList.forEach((equipmentSlot, stack) -> mob.setItemSlot(equipmentSlot, stack.copy())));
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
    public static void playerClone(PlayerEvent.Clone event) {
        if(event.getPlayer().getLevel().isClientSide) return;
        Player player = event.getPlayer();
        UnstableSavedData data = UnstableSavedData.get(event.getPlayer().getLevel());
        if (data.isEndSiegeOccurring() && !event.isWasDeath()) {
            player.sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.changed_dimension").withStyle(ChatFormatting.RED), player.getUUID());
            PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().getLevel().getServer().getPlayerList(), data);
            data.resetData();
        }
        if(!UnstableConfig.SOUL_RESET_DEATH.get()) {
            if (event.getPlayer().getAttribute(Attributes.MAX_HEALTH).getValue() != (event.getOriginal().getAttributeBaseValue(Attributes.MAX_HEALTH))) {
                event.getPlayer().getAttribute(Attributes.MAX_HEALTH).setBaseValue(event.getOriginal().getAttributeBaseValue(Attributes.MAX_HEALTH));
            }
        }
    }

    @SubscribeEvent
    public static void playerDeath(LivingDeathEvent event) {
        if(!(event.getEntity() instanceof Player player)) return;
        UnstableSavedData data = UnstableSavedData.get(player.getLevel());
        if(data.isEndSiegeOccurring() && data.getPlayersParticipating().contains(StringTag.valueOf(player.getStringUUID()))) {
            player.sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.death").withStyle(ChatFormatting.RED), player.getUUID());
            PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().getLevel().getServer().getPlayerList(), data);
            data.resetData();
        }
    }
    
    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(event.getPlayer().getLevel().isClientSide) return;
        UnstableSavedData data = UnstableSavedData.get(event.getPlayer().getLevel());
        if(data.isEndSiegeOccurring() && data.getPlayersParticipating().contains(StringTag.valueOf(event.getPlayer().getStringUUID())) && !event.getTo().location().equals(Level.END.location())) {
            event.getPlayer().sendMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.changed_dimension").withStyle(ChatFormatting.RED), event.getPlayer().getUUID());
            System.out.println(event.getEntity().getLevel().getServer().getPlayerList());
            PseudoInversionRitualHelper.sendSiegeMessage(new TranslatableComponent("unstable.pseudo_inversion_ritual.siege_ended").withStyle(ChatFormatting.WHITE), event.getEntity().getLevel().getServer().getPlayerList(), data);
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
                event.getEntityItem().discard();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void tagUpdate(TagsUpdatedEvent event) {
        if(event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
            TransmutationDataStorage.getMasterStorage().forEach(dataStorage -> {
                if(dataStorage.getInputType() == UnstableEnums.TRANSMUTATION_INPUT.TAG) {
                    Optional<TagKey<Block>> tagOptional = ForgeRegistries.BLOCKS.tags().getTagNames().filter(p -> p.location().equals(dataStorage.getLocation())).findFirst();
                    if (tagOptional.isEmpty()) {
                        dataStorage.setInput(Ingredient.of(new ItemStack(Blocks.BARRIER).setHoverName(new TextComponent("Empty Tag: " + dataStorage.getLocation()))));
                    } else {
                        List<ItemStack> list = new ArrayList<>();
                        ForgeRegistries.BLOCKS.tags().getTag(tagOptional.get()).forEach(block -> list.add(new ItemStack(block)));
                        dataStorage.setInput(Ingredient.of(list.stream()));
                    }
                }
            });
        }
    }
}