package dev.electrolyte.unstable.registry;

import dev.electrolyte.unstable.Unstable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Unstable.MOD_ID);

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

    public static final Supplier<SoundEvent> ACTIVATION_RITUAL_SUCCESS = SOUNDS.register("activation_ritual_success",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "activation_ritual_success")));
}
