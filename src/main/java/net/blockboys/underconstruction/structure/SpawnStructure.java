package net.blockboys.underconstruction.structure;

import net.blockboys.underconstruction.UnderConstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
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
        BlockPos spawn = level.getSharedSpawnPos();        // World spawn

        StructureTemplateManager manager = level.getStructureManager();
        ResourceLocation id = new ResourceLocation("under_construction:ruins");
        StructureTemplate template = manager.getOrCreate(id);

        if (template != null) {
            StructurePlaceSettings settings = new StructurePlaceSettings();
            template.placeInWorld(level, spawn, spawn, settings, level.random, 2);

            // spawn villager near structure
            // TODO find way to spawn villager INSIDE structure and store inside location
            BlockPos location = level.getSharedSpawnPos();
            Villager villager = new Villager(EntityType.VILLAGER, level);
            villager.setPosRaw(location.getX(), location.getY(), location.getZ());
            level.addFreshEntity(villager);
        }
    }
}
