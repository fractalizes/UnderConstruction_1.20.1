package net.blockboys.underconstruction.structure;

import net.blockboys.underconstruction.UnderConstruction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID)
public class SpawnStructure {

    // TODO find way to do world generation event instead of server starting event
    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event) {

        ServerLevel level = event.getServer().overworld();

        // only generate when first creating world
        if (!TowerStructure.isGenerated()) {
            BlockPos spawn = level.getSharedSpawnPos(); // world x/z spawn

            // get surface height at spawn x/z
            int surfaceY = level.getHeight(
                    Heightmap.Types.WORLD_SURFACE,
                    spawn.getX(),
                    spawn.getZ()
            );

            // position to place the structure
            BlockPos groundPos = new BlockPos(
                    spawn.getX(),
                    surfaceY - 1,
                    spawn.getZ()
            );

            // lower y level if starting position has any of these blocks
            Block[] blocks = new Block[]{
                    Blocks.SNOW, Blocks.GRASS, Blocks.SUNFLOWER
            };
            if (Arrays.asList(blocks).contains(level.getBlockState(groundPos).getBlock())) {
                groundPos.atY(groundPos.getY() - 1);
            }

            // spawn initial villager
            BlockPos villagerPos = groundPos.above(); // 1 block above ground
            Villager villager = new Villager(EntityType.VILLAGER, level);
            villager.setPosRaw(villagerPos.getX(), villagerPos.getY(), villagerPos.getZ());
            level.addFreshEntity(villager);

            // create class to store data across files
            TowerStructure.constructor(level, groundPos, villager);
            TowerStructure.generateStructurePiece();
            TowerStructure.setGenerated();
        }
    }
}
