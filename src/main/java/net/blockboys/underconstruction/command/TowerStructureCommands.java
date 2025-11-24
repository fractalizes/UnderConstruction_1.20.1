package net.blockboys.underconstruction.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.blockboys.underconstruction.structure.TowerStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.List;

public class TowerStructureCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("tower")
                .then(Commands.literal("level")
                    // general commands
                    .then(Commands.literal("get")
                            .executes(TowerStructureCommands::towerGetLevel))
                    // operator commands
                    .requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                            .then(Commands.literal("set")
                            .then(Commands.argument("level", IntegerArgumentType.integer(1, TowerStructure.getMaxStructureLevel()))
                                    .executes(TowerStructureCommands::towerSetLevel)))
                            .then(Commands.literal("reset")
                                    .executes(TowerStructureCommands::towerResetLevel)))
                .then(Commands.literal("materials")
                        .executes(TowerStructureCommands::towerMaterials))
        );
    }

    private static int towerGetLevel(CommandContext<CommandSourceStack> context) {
        int structureLevel = TowerStructure.getStructureLevel();

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure is currently Level " + structureLevel + "."));
        return Command.SINGLE_SUCCESS;
    }

    private static int towerSetLevel(CommandContext<CommandSourceStack> context) {
        int structureLevel = IntegerArgumentType.getInteger(context, "level");
        TowerStructure.setStructureLevel(structureLevel);
        TowerStructure.generateStructurePiece();

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure is now Level " + structureLevel + "!"));
        return Command.SINGLE_SUCCESS;
    }

    private static int towerResetLevel(CommandContext<CommandSourceStack> context) {
        TowerStructure.setStructureLevel(1);
        TowerStructure.generateStructurePiece();

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure Level has been reset!"));
        return Command.SINGLE_SUCCESS;
    }

    private static int towerMaterials(CommandContext<CommandSourceStack> context) {
        int structureLevel = TowerStructure.getStructureLevel();
        List<Item> upgradeItems = TowerStructure.getItemsList(structureLevel);
        List<Integer> upgradeNumbers = TowerStructure.getNumbersList(structureLevel);

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "These are the materials needed for your Tower Upgrade:"));
        for (int i = 0; i < upgradeItems.size(); i++) {
            int required = upgradeNumbers.get(i);
            if (required > 0) {
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                        "| " + required + " " + upgradeItems.get(i)));
            }
        }
        return Command.SINGLE_SUCCESS;
    }
}