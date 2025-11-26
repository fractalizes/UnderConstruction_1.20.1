package net.blockboys.under_construction.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.blockboys.under_construction.client.ClientTowerData;
import net.blockboys.under_construction.structure.TowerStructure;
import net.blockboys.under_construction.structure.TowerStructureGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TowerStructureCommands {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("tower")
                .then(Commands.literal("level")
                    // general commands
                    .then(Commands.literal("get")
                            .executes(TowerStructureCommands::towerGetLevel))
                    // operator commands
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                            .then(Commands.literal("set")
                            .then(Commands.argument("level", IntegerArgumentType.integer(0, TowerStructure.getMaxStructureLevel()))
                                    .executes(TowerStructureCommands::towerSetLevel)))
                            .then(Commands.literal("reset")
                                    .executes(TowerStructureCommands::towerResetLevel))
                            .then(Commands.literal("max")
                                    .executes(TowerStructureCommands::towerMaxLevel))
                ).then(Commands.literal("materials")
                        .executes(TowerStructureCommands::towerMaterials))
        );
    }

    private static int towerGetLevel(CommandContext<CommandSourceStack> context) {
        ServerLevel level = getOverworld(context);
        TowerStructure tower = getTowerStructure(level);
        int structureLevel = tower.getStructureLevel();

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure is currently Level " + structureLevel + "."));
        return Command.SINGLE_SUCCESS;
    }

    private static int towerSetLevel(CommandContext<CommandSourceStack> context) {
        int structureLevel = IntegerArgumentType.getInteger(context, "level");
        ServerLevel level = getOverworld(context);

        TowerStructure tower = getTowerStructure(level);
        tower.setStructureLevel(structureLevel);
        ClientTowerData.setStructureLevel(tower.getStructureLevel()); // update client data
        TowerStructureGenerator.generateStructurePiece(level, tower);

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure is now Level " + structureLevel + "!"));
        return Command.SINGLE_SUCCESS;
    }

    private static int towerResetLevel(CommandContext<CommandSourceStack> context) {
        ServerLevel level = getOverworld(context);
        TowerStructure tower = getTowerStructure(level);

        tower.setStructureLevel(0);
        ClientTowerData.setStructureLevel(tower.getStructureLevel()); // update client data
        TowerStructureGenerator.generateStructurePiece(level, tower);

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure Level has been reset!"));
        return Command.SINGLE_SUCCESS;
    }

    private static int towerMaxLevel(CommandContext<CommandSourceStack> context) {
        ServerLevel level = getOverworld(context);
        TowerStructure tower = getTowerStructure(level);

        int maxLevel = TowerStructure.getMaxStructureLevel();
        tower.setStructureLevel(maxLevel);
        ClientTowerData.setStructureLevel(tower.getStructureLevel()); // update client data
        TowerStructureGenerator.generateStructurePiece(level, tower);

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure has been maxed out to Level " + maxLevel + "!"));
        return Command.SINGLE_SUCCESS;
    }

    private static int towerMaterials(CommandContext<CommandSourceStack> context) {
        ServerLevel level = getOverworld(context);
        TowerStructure tower = getTowerStructure(level);

        if (!tower.checkStructureMaxed()) {

            int structureLevel = tower.getStructureLevel();
            List<Item> upgradeItems = TowerStructure.getItemsList(structureLevel);
            List<Integer> upgradeAmounts = tower.getAmountsList(structureLevel);

            Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                    "These are the materials needed for your Tower Upgrade:"));
            for (int i = 0; i < upgradeItems.size(); i++) {
                int required = upgradeAmounts.get(i);
                if (required > 0) {
                    Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                            "| x" + required + " " + upgradeItems.get(i)));
                }
            }
        } else {
            Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                    "There are no more materials you need for the Tower, you have maxed out its Level!"));
        } return Command.SINGLE_SUCCESS;
    }

    //////////////////////////////////////
    //////     HELPER FUNCTIONS     //////
    //////////////////////////////////////

    private static ServerLevel getOverworld(@NotNull CommandContext<CommandSourceStack> context) {
        return context.getSource().getLevel().getServer().overworld();
    }

    private static TowerStructure getTowerStructure(@NotNull ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                TowerStructure::loadTower,
                TowerStructure::new,
                "TOWER_STRUCTURE_DATA"
        );
    }
}