package net.blockboys.under_construction.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.blockboys.under_construction.UnderConstruction;
import net.blockboys.under_construction.command.TowerStructureCommands;
import net.blockboys.under_construction.structure.TowerStructureProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;
import java.util.logging.Level;

@Mod.EventBusSubscriber(modid = UnderConstruction.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesWorld(AttachCapabilitiesEvent<ServerLevel> event) {
        if (event.getObject() instanceof ServerLevel) {
            if (!event.getObject().getCapability(TowerStructureProvider.TOWER_STRUCTURE).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "properties"), new TowerStructureProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        event.getEntity().getCapability(TowerStructureProvider.TOWER_STRUCTURE).ifPresent(oldStore -> {
            event.getEntity().getCapability(TowerStructureProvider.TOWER_STRUCTURE).ifPresent(newStore -> {
                newStore.copyFrom(oldStore);
            });
        });
    }

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

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        TowerStructureCommands.register(event.getDispatcher());
    }
}
