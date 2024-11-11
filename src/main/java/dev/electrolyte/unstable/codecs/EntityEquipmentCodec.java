package dev.electrolyte.unstable.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public record EntityEquipmentCodec(
        Optional<ItemStack> head,
        Optional<ItemStack> chest,
        Optional<ItemStack> legs,
        Optional<ItemStack> feet,
        Optional<ItemStack> body,
        Optional<ItemStack> mainhand,
        Optional<ItemStack> offhand) {

    public static final Codec<EntityEquipmentCodec> CODEC = RecordCodecBuilder.create(i ->
        i.group(
                ItemStack.CODEC.optionalFieldOf("head").forGetter(EntityEquipmentCodec::head),
                ItemStack.CODEC.optionalFieldOf("chest").forGetter(EntityEquipmentCodec::chest),
                ItemStack.CODEC.optionalFieldOf("legs").forGetter(EntityEquipmentCodec::legs),
                ItemStack.CODEC.optionalFieldOf("feet").forGetter(EntityEquipmentCodec::feet),
                ItemStack.CODEC.optionalFieldOf("body").forGetter(EntityEquipmentCodec::body),
                ItemStack.CODEC.optionalFieldOf("mainhand").forGetter(EntityEquipmentCodec::mainhand),
                ItemStack.CODEC.optionalFieldOf("offhand").forGetter(EntityEquipmentCodec::offhand)
        ).apply(i, EntityEquipmentCodec::new));
}
