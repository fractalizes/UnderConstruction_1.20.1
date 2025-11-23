package net.blockboys.underconstruction.event;

import net.blockboys.underconstruction.UnderConstruction;
import net.blockboys.underconstruction.structure.StructureClass;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void detectChestItems(final PlayerContainerEvent.Open event) {

        AbstractContainerMenu container = event.getContainer();
        if (!event.isCanceled() && container.getClass() == ChestMenu.class) {
            // TODO it is also checking player inventory, make sure it doesnt do that!
            List<ItemStack> chestItems = event.getContainer().getItems();

            if (!chestItems.isEmpty()) {

                int structureLevel = StructureClass.getStructureLevel();
                List<Item> upgradeItems = StructureClass.getItemsList(structureLevel);
                List<Integer> upgradeNumbers = StructureClass.getNumbersList(structureLevel);

                for (ItemStack chestItem: chestItems) {
                    for (int i = 0; i < upgradeItems.size(); i++) {

                        if (
                                chestItem.getItem() == upgradeItems.get(i)
                                && chestItem.getCount() >= upgradeNumbers.get(i)
                        ) {
                            int newCount = chestItem.getCount() - upgradeNumbers.get(i);
                            StructureClass.setNumbersList(i, newCount);
                            chestItem.setCount(newCount);
                        }
                    }
                }
                if (StructureClass.checkUpgrade()) {
                    StructureClass.incrementStructureLevel();
                }
            }
        }
    }
}
