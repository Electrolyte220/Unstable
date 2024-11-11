package dev.electrolyte.unstable.listener;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.codecs.EntityDataCodec;
import dev.electrolyte.unstable.codecs.EntityEquipmentCodec;
import dev.electrolyte.unstable.datastorage.endsiege.EntityDataStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;

import java.util.Map;
import java.util.Optional;

public class EntityDataReloadListener extends SimpleJsonResourceReloadListener {

    public static final Codec<EntityDataCodec> ENTITY_DATA_CODEC = RecordCodecBuilder.create(i -> i.group(
            ResourceLocation.CODEC.listOf().fieldOf("entities").forGetter(EntityDataCodec::entities),
            MobEffectInstance.CODEC.listOf().optionalFieldOf("effects").forGetter(EntityDataCodec::effects),
            EntityEquipmentCodec.CODEC.optionalFieldOf("equipment").forGetter(EntityDataCodec::equipment)
    ).apply(i, EntityDataCodec::new));

    public EntityDataReloadListener(Gson gson, String folder) {
        super(gson, folder);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        EntityDataStorage.getMasterStorage().clear();
        Unstable.LOGGER.info("Setting up End Siege Entity Data...");
        object.forEach((resourceLocation, jsonElement) -> {
            Optional<EntityDataCodec> result = ENTITY_DATA_CODEC.parse(JsonOps.INSTANCE, jsonElement).result();
            result.ifPresentOrElse(r ->
                    r.entities().forEach(entity -> EntityDataStorage.getMasterStorage().add(new EntityDataStorage(EntityType.byString(entity.toString()).get(), r.effects(), r.equipment()))),
                    () -> Unstable.LOGGER.error("Unable to parse entity data for {}.", resourceLocation));
        });
        Unstable.LOGGER.info("Finished Setting up End Siege Entity Data.");
    }
}
