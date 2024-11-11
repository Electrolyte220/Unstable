package dev.electrolyte.unstable.datagen;

import dev.electrolyte.unstable.registry.ModDamageTypes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class UnstableDamageTypeTagsProvider extends TagsProvider<DamageType> {
    protected UnstableDamageTypeTagsProvider(PackOutput packOutput, ResourceKey<? extends Registry<DamageType>> resourceKey, CompletableFuture<Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, resourceKey, provider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(Provider pProvider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ModDamageTypes.DIVIDE_BY_DIAMOND).add(ModDamageTypes.HEALING_AXE);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ModDamageTypes.DIVIDE_BY_DIAMOND).add(ModDamageTypes.HEALING_AXE);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(ModDamageTypes.DIVIDE_BY_DIAMOND).add(ModDamageTypes.HEALING_AXE);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(ModDamageTypes.DIVIDE_BY_DIAMOND).add(ModDamageTypes.HEALING_AXE);
    }
}
