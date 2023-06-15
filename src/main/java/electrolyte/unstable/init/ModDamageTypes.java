package electrolyte.unstable.init;

import electrolyte.unstable.Unstable;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> DIVIDE_BY_DIAMOND = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Unstable.MOD_ID, "divide_by_diamond"));
    public static final ResourceKey<DamageType> HEALING_AXE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Unstable.MOD_ID, "healing_axe"));

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> resourceKey) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(resourceKey));
    }
}
