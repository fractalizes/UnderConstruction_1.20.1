package net.blockboys.underconstruction.event;

import net.blockboys.underconstruction.UnderConstruction;
import net.blockboys.underconstruction.structure.StructureClass;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventBusEvents {

    // do check after player closes menu
    @SubscribeEvent
    public static void detectChestItems(final PlayerContainerEvent.Close event) {

        AbstractContainerMenu container = event.getContainer();
        if (!event.isCanceled() && container instanceof ChestMenu chestMenu) {

            // only check chest slots!
            Container chestInv = chestMenu.getContainer();
            List<ItemStack> chestItems = new ArrayList<>();
            for (int i = 0; i < chestInv.getContainerSize(); i++) {
                chestItems.add(chestInv.getItem(i));
            }

            if (!chestItems.isEmpty()) {

                // get current necessary items and item amounts for upgrades
                int structureLevel = StructureClass.getStructureLevel();
                List<Item> upgradeItems = StructureClass.getItemsList(structureLevel);
                List<Integer> upgradeNumbers = StructureClass.getNumbersList(structureLevel);

                for (ItemStack chestItem: chestItems) {
                    for (int i = 0; i < upgradeItems.size(); i++) {

                        if (chestItem.getItem() == upgradeItems.get(i)) {

                            int required = upgradeNumbers.get(i);
                            int count = chestItem.getCount();

                            // update chest container items
                            if (chestItem.getCount() >= upgradeNumbers.get(i)) {

                                StructureClass.setNumbersList(i, 0);
                                chestItem.shrink(required);

                            } else {

                                StructureClass.setNumbersList(i, required - count);
                                chestItem.setCount(0);

                            }
                        }
                    }
                }
                if (StructureClass.checkUpgradeCompletion()) {

                    StructureClass.incrementStructureLevel();
                    StructureClass.generateStructurePiece();

                }
            }
        }
    }
}
