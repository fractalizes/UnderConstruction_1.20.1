package net.blockboys.under_construction.registry;

import net.blockboys.under_construction.UnderConstruction;
import net.blockboys.under_construction.block.UpgradeChestBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;


public class ModBlocks {

    // Deferred register for blocks
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UnderConstruction.MOD_ID);

    // Deferred register for items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UnderConstruction.MOD_ID);

    // Register the block
    public static final RegistryObject<Block> UPGRADE_CHEST =
            BLOCKS.register("upgrade_chest",
                    () -> new UpgradeChestBlock(BlockBehaviour.Properties
                            .of()
                            .strength(3.0f, 6.0f)   // whatever you want
                            .sound(SoundType.STONE)
                    ));

    // Register the block's item
    public static final RegistryObject<Item> UPGRADE_CHEST_ITEM = ITEMS.register("upgrade_chest",
            () -> new BlockItem(UPGRADE_CHEST.get(), new Item.Properties()));

    // Call this in your main mod class
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}
