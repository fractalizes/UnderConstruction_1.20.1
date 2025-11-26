package net.blockboys.under_construction.client;

import net.blockboys.under_construction.UnderConstruction;
import net.blockboys.under_construction.structure.TowerData;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void onClientLevelLoad(LoggingIn event) {
        assert Minecraft.getInstance().getSingleplayerServer() != null;
        ServerLevel level = Minecraft.getInstance().getSingleplayerServer().overworld();

        // only in singleplayer or connected to server
        TowerData tower = level.getDataStorage().computeIfAbsent(
                TowerData::loadTower,
                TowerData::new,
                "TOWER_STRUCTURE_DATA"
        );

        // copy data to client-safe storage
        ClientTowerData.setGenerated(tower.isGenerated());
        ClientTowerData.setStructureLevel(tower.getStructureLevel());
        for (int structureLevel = 0; structureLevel <= 3; structureLevel++) {
            ClientTowerData.setUpgradeAmounts(structureLevel, tower.getAmountsList(structureLevel));
        }
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("materials", TowerMaterialsOverlay.HUD_MATERIALS_NEEDED);
    }
}