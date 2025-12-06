package net.blockboys.under_construction.structure;

import com.mojang.datafixers.util.Pair;
import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Quaterniond;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnToTowerOnce {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ServerLevel level = event.getServer().overworld();

        TowerData tower = level.getDataStorage().computeIfAbsent(
                TowerData::loadTower, TowerData::new, "TOWER_STRUCTURE_DATA");
        if (tower.isGenerated() && tower.getGroundPos() != null) return;

        // Get structure registry and key
        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        ResourceKey<Structure> key = ResourceKey.create(
                Registries.STRUCTURE,
                ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "tower_structure"));

        Optional<Structure> structOpt = structureRegistry.getOptional(key);
        if (structOpt.isEmpty()) return;
        Structure structure = structOpt.get();

        // Use findNearestMapStructure to get chunk position
        ChunkGenerator gen = (ChunkGenerator) level.getChunkSource().getGenerator();
        BlockPos searchFrom = level.getSharedSpawnPos();
        Pair<BlockPos, Holder<Structure>> nearest = gen.findNearestMapStructure(
                level,
                HolderSet.direct(Holder.direct(structure)),
                searchFrom,
                64,
                false);
        if (nearest == null) return;

        BlockPos center = nearest.getFirst();

        // get the actual start from spawn chunk
        ChunkAccess chunk = level.getChunk(
                center.getX() >> 4, center.getZ() >> 4, ChunkStatus.STRUCTURE_STARTS, false);
        if (chunk == null) return;

        StructureStart start = chunk.getAllStarts().get(structure);
        if (start == null || !start.isValid() || start.getPieces().isEmpty()) return;

        StructurePiece mainPiece = start.getPieces().get(0);
        BoundingBox box = mainPiece.getBoundingBox();

        int pieceX = box.minX();
        int pieceZ = box.minZ();

        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pieceX, pieceZ);
        BlockPos groundPos = new BlockPos(pieceX, Math.max(y, 1), pieceZ);

        float yaw = 0.0f; // default rotation
        if (mainPiece instanceof TemplateStructurePiece template) {
            Rotation rotation = template.placeSettings().getRotation();
            yaw = switch (rotation) {
                case NONE -> 0f;
                case CLOCKWISE_90 -> 90f;
                case CLOCKWISE_180 -> 180f;
                case COUNTERCLOCKWISE_90 -> -90f;
            };
        }

        // set tower data as dirty
        level.setDefaultSpawnPos(groundPos, yaw);
        tower.setGroundPos(groundPos.below());
        tower.setStructureLevel(0);
        tower.setGenerated();
    }
}