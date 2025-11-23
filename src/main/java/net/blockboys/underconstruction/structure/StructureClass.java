package net.blockboys.underconstruction.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.*;

public class StructureClass {
    private static boolean generated = false;
    private static int structureLevel = 1;
    private static ServerLevel level;
    private static BlockPos groundPos;
    private static Villager villager;

    // construct materials list
    private static final Map<Integer, List<Item>> upgradeItemsList = new HashMap<>();
    static {
        upgradeItemsList.put(1, List.of(Items.COBBLESTONE, Items.STONE));
        upgradeItemsList.put(2, List.of(Items.STONE_BRICKS, Items.COBBLESTONE_STAIRS));
    };

    // construct number of materials needed list
    private static final Map<Integer, List<Integer>> upgradeNumbersList = new HashMap<>();
    static {
        upgradeNumbersList.put(1, new ArrayList<>(List.of(32, 32)));
        upgradeNumbersList.put(2, new ArrayList<>(List.of(32, 16)));
    }

    public static void constructor(ServerLevel levelConst, BlockPos groundPosConst, Villager villagerConst) {
        level = levelConst;
        groundPos = groundPosConst;
        villager = villagerConst;
    }

    public static boolean isGenerated() {
        return generated;
    }

    public static void setGenerated() {
        generated = true;
    }

    public static int getStructureLevel() {
        return structureLevel;
    }

    public static void incrementStructureLevel() {
        structureLevel++;
        System.out.println("level increased to " + structureLevel + "!!!!");
    }

    public static boolean checkUpgrade() {
        List<Integer> upgradeNumbers = StructureClass.getNumbersList(structureLevel);
        for (int upgradeNumber: upgradeNumbers) {
            if (upgradeNumber > 0) {
                return false;
            }
        }
        return true;
    }

    public static ServerLevel getLevel() {
        return level;
    }

    public static void setLevel(ServerLevel levelConst) {
        level = levelConst;
    }

    public static BlockPos getGroundPos() {
        return groundPos;
    }

    public static void setGroundPos(BlockPos groundPosConst) {
        groundPos = groundPosConst;
    }

    public static Villager getVillager() {
        return villager;
    }

    private void setVillager(Villager villagerConst) {
        villager = villagerConst;
    }

    public static List<Item> getItemsList(int i) {
        return upgradeItemsList.get(i);
    }

    public static List<Integer> getNumbersList(int i) {
        return upgradeNumbersList.get(i);
    }

    public static void setNumbersList(int index, int newCount) {
        List<Integer> list = upgradeNumbersList.get(structureLevel);
        list.set(index, newCount);
    }
}
