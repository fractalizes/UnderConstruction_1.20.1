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

public class SetStructureLevel {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(
                Commands.literal("tower")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(3))
                        .then(Commands.literal("set")
                        .then(Commands.argument("level", IntegerArgumentType.integer(0, TowerStructure.getMaxStructureLevel()))
                                .executes(SetStructureLevel::towerSetLevel)))
                        .then(Commands.literal("reset")
                                .executes(SetStructureLevel::towerReset))
        );
    }

    private static int towerSetLevel(CommandContext<CommandSourceStack> context) {
        int structureLevel = IntegerArgumentType.getInteger(context, "level");
        TowerStructure.setStructureLevel(structureLevel);
        TowerStructure.generateStructurePiece();

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal("Tower Structure is now Level " + structureLevel + "!"));
        return Command.SINGLE_SUCCESS;
    }

    private static int towerReset(CommandContext<CommandSourceStack> context) {
        TowerStructure.setStructureLevel(1);
        TowerStructure.generateStructurePiece();

        Minecraft.getInstance().gui.getChat().addMessage(Component.literal("Tower Structure Level has been reset!"));
        return Command.SINGLE_SUCCESS;
    }
}