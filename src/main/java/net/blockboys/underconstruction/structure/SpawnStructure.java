package net.blockboys.underconstruction.structure;

import net.blockboys.underconstruction.UnderConstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID)
public class SpawnStructure {

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {
        ServerLevel level = event.getServer().overworld(); // Overworld only
        BlockPos spawn = level.getSharedSpawnPos();        // World spawn X/Z

        // Get surface height at spawn X/Z
        int surfaceY = level.getHeight(
                Heightmap.Types.WORLD_SURFACE,
                spawn.getX(),
                spawn.getZ()
        );

        // Position to place the structure
        BlockPos groundPos = new BlockPos(
                spawn.getX(),
                surfaceY,
                spawn.getZ()
        );

        StructureTemplateManager manager = level.getStructureManager();
        ResourceLocation id = new ResourceLocation("under_construction", "ruins");
        StructureTemplate template = manager.getOrCreate(id);

        if (template != null) {
            StructurePlaceSettings settings = new StructurePlaceSettings();
            template.placeInWorld(level, groundPos, groundPos, settings, level.random, 2);

            // Spawn a villager on top of the structure
            BlockPos villagerPos = groundPos.above(); // 1 block above ground
            Villager villager = new Villager(EntityType.VILLAGER, level);
            villager.setPosRaw(villagerPos.getX(), villagerPos.getY(), villagerPos.getZ());
            level.addFreshEntity(villager);
        }
    }
}
