package net.blockboys.underconstruction.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.blockboys.underconstruction.UnderConstruction;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {

        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // clear every trade level from villager
            // guarantees our custom trade!
            int firstLevel = 1, lastLevel = 5;
            for (int level = firstLevel; level <= lastLevel; level++) {
                trades.get(level).clear();
            }

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(Items.CARROT, 10),
                    10, 8, 0.02f
            ));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    new ItemStack(Items.PUMPKIN_SEEDS, 3),
                    4, 15, 0.03f
            ));

        }
    }
}
