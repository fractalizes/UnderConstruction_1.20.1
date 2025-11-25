package net.blockboys.under_construction.structure;

import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class TowerStructureGenerator {

    public static void generateStructurePiece(ServerLevel level, int structureLevel, TowerStructure tower) {
        String[] STRUCTURE_PATHS = {
                "ruins", "tier1", "tier2", "tier3"
        };
        StructureTemplateManager manager = level.getStructureManager();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                UnderConstruction.MOD_ID, STRUCTURE_PATHS[structureLevel - 1]);
        StructureTemplate template = manager.getOrCreate(id);
        StructurePlaceSettings settings = new StructurePlaceSettings();

        // replace old structure with new one
        template.placeInWorld(level,
                tower.getGroundPos(),
                tower.getGroundPos(),
                settings,
                level.random,
                2
        );
    }

}
