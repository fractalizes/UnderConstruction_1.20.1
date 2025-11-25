package net.blockboys.under_construction.client;

import net.blockboys.under_construction.UnderConstruction;
import net.blockboys.under_construction.structure.TowerStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientLevelLoad(LoggingIn event) {
        assert Minecraft.getInstance().getSingleplayerServer() != null;
        ServerLevel level = Minecraft.getInstance().getSingleplayerServer().overworld();

        // Only in singleplayer or connected to server
        TowerStructure tower = level.getDataStorage().computeIfAbsent(
                TowerStructure::loadTower,
                TowerStructure::new,
                "TOWER_STRUCTURE_DATA"
        );

        // Copy data to client-safe storage
        ClientTowerData.setGenerated(tower.isGenerated());
        ClientTowerData.setStructureLevel(tower.getStructureLevel());

        // Copy upgrade progress for each level
        for (int structureLevel = 1; structureLevel <= 3; structureLevel++) {
            ClientTowerData.setUpgradeAmounts(structureLevel, tower.getAmountsList(structureLevel));
        }
    }
}