package dev.electrolyte.unstable.registry;

import com.mojang.serialization.Codec;
import dev.electrolyte.unstable.Unstable;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, Unstable.MOD_ID);

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }

    public static final Supplier<DataComponentType<Boolean>> CREATIVE_SPAWNED = COMPONENTS.register("creative_spawned",
            () -> new DataComponentType.Builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final Supplier<DataComponentType<Boolean>> EXPLODED = COMPONENTS.register("exploded",
            () -> new DataComponentType.Builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final Supplier<DataComponentType<Boolean>> WEAK_SOUL = COMPONENTS.register("weak_soul",
            () -> new DataComponentType.Builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final Supplier<DataComponentType<Integer>> EXPLODE_TIMER = COMPONENTS.register("explode_timer",
            () -> new DataComponentType.Builder<Integer>().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT).build());
}
