package net.blockboys.under_construction.event;

import net.blockboys.under_construction.UnderConstruction;
import net.blockboys.under_construction.client.ClientTowerData;
import net.blockboys.under_construction.structure.TowerData;
import net.blockboys.under_construction.structure.TowerStructureGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventBusEvents {

    // do check after player closes menu
    @SubscribeEvent
    public static void detectChestItems(final PlayerContainerEvent.@NotNull Close event) {
        ServerLevel level = (ServerLevel) event.getEntity().getCommandSenderWorld();
        TowerData tower = level.getDataStorage().computeIfAbsent(
                TowerData::loadTower,
                TowerData::new,
                "TOWER_STRUCTURE_DATA"
        );
        int structureLevel = tower.getStructureLevel();

        // check if max upgrade has been achieved
        // ensures null will not be called
        if (!tower.checkStructureMaxed()) {

            AbstractContainerMenu container = event.getContainer();
            if (!event.isCanceled() && container instanceof ChestMenu chestMenu) {

                // only check chest slots!
                Container chestInv = chestMenu.getContainer();
                List<ItemStack> chestItems = new ArrayList<>();
                for (int i = 0; i < chestInv.getContainerSize(); i++) {
                    ItemStack item = chestInv.getItem(i);
                    if (item.getItem() != Items.AIR) {
                        chestItems.add(item);
                    }
                }

                // if chest does not have only "air" items, perform actual check
                if (!chestItems.isEmpty()) {

                    // get current necessary items and item amounts for upgrades
                    List<Item> upgradeItems = tower.getItemsList(structureLevel);
                    List<Integer> upgradeAmounts = tower.getAmountsList(structureLevel);

                    boolean changed = false;

                    for (int i = 0; i < chestInv.getContainerSize(); i++) {
                        ItemStack stack = chestInv.getItem(i);
                        if (stack.isEmpty()) continue;

                        for (int j = 0; j < upgradeItems.size(); j++) {
                            if (stack.getItem() == upgradeItems.get(j)) {
                                int required = upgradeAmounts.get(j);
                                if (required > 0) {
                                    int take = Math.min(stack.getCount(), required);
                                    stack.shrink(take);
                                    upgradeAmounts.set(j, required - take);
                                    changed = true;
                                }
                            }
                        }
                    }

                    if (changed) {
                        for (int j = 0; j < upgradeAmounts.size(); j++) {
                            tower.setAmountsList(structureLevel, j, upgradeAmounts.get(j));
                        }

                        if (tower.checkUpgradeCompletion()) {
                            tower.incrementStructureLevel();
                            ClientTowerData.setStructureLevel(tower.getStructureLevel()); // update client data
                            TowerStructureGenerator.generateStructurePiece(level, tower);
                        }
                    }
                }
            }
        }
    }
}