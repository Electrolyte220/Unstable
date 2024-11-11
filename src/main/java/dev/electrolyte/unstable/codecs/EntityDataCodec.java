package dev.electrolyte.unstable.codecs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;
import java.util.Optional;

public record EntityDataCodec(
        List<ResourceLocation> entities,
        Optional<List<MobEffectInstance>> effects,
        Optional<EntityEquipmentCodec> equipment) {
}
