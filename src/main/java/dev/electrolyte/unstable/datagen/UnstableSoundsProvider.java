package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.registry.ModSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinition.Sound;
import net.neoforged.neoforge.common.data.SoundDefinition.SoundType;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class UnstableSoundsProvider extends SoundDefinitionsProvider {

    protected UnstableSoundsProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    @Override
    public void registerSounds() {
        this.add(ModSounds.ACTIVATION_RITUAL_SUCCESS.get(), SoundDefinition.definition().with(Sound.sound(ResourceLocation.fromNamespaceAndPath(Unstable.MOD_ID, "activation_ritual_success"), SoundType.SOUND)).subtitle("unstable.activation_ritual.subtitle"));
    }
}
