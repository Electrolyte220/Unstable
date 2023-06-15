package electrolyte.unstable.init;

import electrolyte.unstable.Unstable;
import electrolyte.unstable.be.CursedEarthBlockEntity;
import electrolyte.unstable.blocks.CursedEarth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Unstable.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Unstable.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Unstable.MOD_ID);


    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Block> CURSED_EARTH = BLOCKS.register("cursed_earth",
            () -> new CursedEarth(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).randomTicks().explosionResistance(200).strength(0.6F).sound(SoundType.GRASS)));
    public static final RegistryObject<BlockItem> CURSED_EARTH_ITEM = ITEMS.register("cursed_earth",
            () -> new BlockItem(CURSED_EARTH.get(), new Item.Properties()));
    public static final RegistryObject<BlockEntityType<CursedEarthBlockEntity>> CURSED_EARTH_BE = BLOCK_ENTITIES.register("cursed_earth",
            () -> BlockEntityType.Builder.of(CursedEarthBlockEntity::new, CURSED_EARTH.get()).build(null));
}
