package com.electrolyte.unstable.end_siege;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

public record UnstableEntityDataStorage(
        EntityType<?> entity,
        int minCount,
        int maxCount,
        List<MobEffectInstance> effects,
        List<Map<InteractionHand, ItemStack>> equipment,
        List<Map<EquipmentSlot, ItemStack>> armor) {

}
