package net.blockboys.under_construction.registry;

import net.blockboys.under_construction.UnderConstruction;
import net.blockboys.under_construction.block.entity.UpgradeChestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.minecraftforge.eventbus.api.IEventBus;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, UnderConstruction.MOD_ID);

    public static final RegistryObject<BlockEntityType<UpgradeChestBlockEntity>> UPGRADE_CHEST =
            BLOCK_ENTITIES.register("upgrade_chest",
                    () -> BlockEntityType.Builder.of(UpgradeChestBlockEntity::new, ModBlocks.UPGRADE_CHEST.get()).build(null));

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITIES.register(modEventBus);
    }
}
