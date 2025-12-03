package net.blockboys.under_construction.structure;

import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnToTowerOnce {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ServerLevel level = event.getServer().overworld();
        if (level == null) return;

        // Load/create SavedData for a one-time guard and to store tower position
        TowerData tower = level.getDataStorage().computeIfAbsent(
                TowerData::loadTower, TowerData::new, "TOWER_STRUCTURE_DATA");

        // If we’ve already processed this world, do nothing
        if (tower.isGenerated() && tower.getGroundPos() != null) {
            return;
        }

        // Prepare a HolderSet<Structure> for findNearestMapStructure
        var structReg = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        ResourceKey<Structure> key = ResourceKey.create(
                Registries.STRUCTURE,
                ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "tower_structure")
        );
        Optional<Holder.Reference<Structure>> holderOpt = structReg.getHolder(key);
        if (holderOpt.isEmpty()) {
            // Structure not registered/available (e.g., JSON parse error). Abort gracefully.
            return;
        }
        HolderSet<Structure> targets = HolderSet.direct(holderOpt.get());

        // Search from the current world spawn; radius in chunks (64 is plenty)
        var gen = level.getChunkSource().getGenerator();
        BlockPos searchFrom = level.getSharedSpawnPos();
        var nearest = gen.findNearestMapStructure(level, targets, searchFrom, 64, false);
        if (nearest == null) return; // none found

        BlockPos start = nearest.getFirst(); // structure start X/Z

        // Find a safe surface Y at the structure X/Z
        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, start.getX(), start.getZ());
        BlockPos newSpawn = new BlockPos(start.getX(), Math.max(y + 1, 1), start.getZ());

        // Move world spawn there (angle 0f)
        level.setDefaultSpawnPos(newSpawn, 0.0F);

        // Save tower info so your upgrade system knows where it is
        tower.setGroundPos(newSpawn.below()); // your code expects ground block
        tower.setStructureLevel(0);
        tower.setGenerated(); // marks SavedData dirty so it persists
    }
}