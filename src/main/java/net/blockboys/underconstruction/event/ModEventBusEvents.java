package net.blockboys.underconstruction.event;

import net.blockboys.underconstruction.UnderConstruction;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            List<ItemStack> chestItems = event.getContainer().getItems();

            if (!chestItems.isEmpty()) {
                for (ItemStack item: chestItems) {
                    if (item.getItem() == Items.COBBLESTONE && item.getCount() == 32) {
                        System.out.println("TESTING WORKS");
                    }
                }
            }
        }
    }
}
