package com.electrolyte.unstable.init;

import com.electrolyte.unstable.Unstable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Unstable.MOD_ID);

    public static void init() {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SoundEvent> ACTIVATION_RITUAL_SUCCESS = SOUNDS.register("activation_ritual_success",
            () -> new SoundEvent(new ResourceLocation(Unstable.MOD_ID, "activation_ritual_success")));
}
