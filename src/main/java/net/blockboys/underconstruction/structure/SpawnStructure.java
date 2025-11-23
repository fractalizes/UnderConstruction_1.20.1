package net.blockboys.underconstruction.structure;

import net.blockboys.underconstruction.UnderConstruction;
import net.blockboys.underconstruction.event.ModEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID)
public class SpawnStructure {

    // TODO find way to do world generation event instead of server starting event
    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {

        // only generate when first creating world
        if (!StructureClass.isGenerated()) {

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
                    surfaceY - 1,
                    spawn.getZ()
            );

            System.out.println(spawn.getX() + " " + spawn.getY() + " " + spawn.getZ() + " ");

            // lower y level if starting position has any of these blocks
            Block[] blocks = new Block[]{
                    Blocks.SNOW, Blocks.GRASS, Blocks.SUNFLOWER
            };
            if (Arrays.asList(blocks).contains(level.getBlockState(groundPos).getBlock())) {
                groundPos.atY(groundPos.getY() - 1);
            }

            StructureTemplateManager manager = level.getStructureManager();
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath("under_construction", "ruins");
            StructureTemplate template = manager.getOrCreate(id);

            // generate structure and villager
            StructurePlaceSettings settings = new StructurePlaceSettings();
            template.placeInWorld(level, groundPos, groundPos, settings, level.random, 2);

            BlockPos villagerPos = groundPos.above(); // 1 block above ground
            Villager villager = new Villager(EntityType.VILLAGER, level);
            villager.setPosRaw(villagerPos.getX(), villagerPos.getY(), villagerPos.getZ());
            level.addFreshEntity(villager);

            // create class to store data across files
            StructureClass.constructor(level, groundPos, villager);
            StructureClass.setGenerated();
        }

        // TODO maybe instead of jigsaws, you can just spawn another piece on top of already generated structure or replace it?
    }
}
