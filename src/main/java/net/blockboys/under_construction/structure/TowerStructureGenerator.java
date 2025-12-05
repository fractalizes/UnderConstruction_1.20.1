package net.blockboys.under_construction.structure;

import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

public class TowerStructureGenerator {
    private static final String[] STRUCTURE_PATHS = {
            "ruins", "tier1", "tier2", "tier3" };

    public static void generateStructurePiece(ServerLevel level, @NotNull TowerData tower) {
        int structureLevel = tower.getStructureLevel();
        if (structureLevel < 0 || structureLevel > 3) return; // failsafe

        BlockPos pos = tower.getGroundPos();
        if (pos == null) return;

        Rotation rotation = Rotation.NONE;

        StructureTemplateManager manager = level.getStructureManager();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                UnderConstruction.MOD_ID, STRUCTURE_PATHS[structureLevel]);
        StructureTemplate template = manager.getOrCreate(id);

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(rotation)
                .setMirror(Mirror.NONE);
        template.placeInWorld(level, pos, pos, settings, level.getRandom(), 2);
    }
}
