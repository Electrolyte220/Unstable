package dev.electrolyte.unstable.registry;

import dev.electrolyte.unstable.Unstable;
import dev.electrolyte.unstable.be.CursedEarthBlockEntity;
import dev.electrolyte.unstable.blocks.CursedEarth;
import dev.electrolyte.unstable.blocks.MysticalTable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Unstable.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Unstable.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Unstable.MOD_ID);


    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
        ITEMS.register(bus);
    }

    public static final Supplier<Block> CURSED_EARTH = BLOCKS.register("cursed_earth",
            () -> new CursedEarth(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().explosionResistance(200).strength(0.6F).sound(SoundType.GRASS)));
    public static final Supplier<BlockItem> CURSED_EARTH_ITEM = ITEMS.registerSimpleBlockItem("cursed_earth", CURSED_EARTH);
    public static final Supplier<BlockEntityType<CursedEarthBlockEntity>> CURSED_EARTH_BE = BLOCK_ENTITIES.register("cursed_earth",
            () -> BlockEntityType.Builder.of(CursedEarthBlockEntity::new, CURSED_EARTH.get()).build(null));

    public static final Supplier<Block> MYSTICAL_TABLE = BLOCKS.register("mystical_table",
            () -> new MysticalTable(BlockBehaviour.Properties.ofFullCopy(Blocks.ENCHANTING_TABLE)));
    public static final Supplier<BlockItem> MYSTICAL_TABLE_ITEM = ITEMS.registerSimpleBlockItem("mystical_table", MYSTICAL_TABLE);
}
