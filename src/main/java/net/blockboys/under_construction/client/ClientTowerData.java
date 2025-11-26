package net.blockboys.under_construction.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientTowerData {
    public static boolean GENERATED = false;
    public static int STRUCTURE_LEVEL = 1;
    private static final Map<Integer, List<Integer>> UPGRADE_AMOUNT_LIST = new HashMap<>();

    public static void setGenerated(boolean gen) {
        GENERATED = gen;
    }

    public static int getStructureLevel() {
        return STRUCTURE_LEVEL;
    }

    public static void setStructureLevel(int level) {
        STRUCTURE_LEVEL = level;
    }

    public static void setUpgradeAmounts(int level, List<Integer> amounts) {
        UPGRADE_AMOUNT_LIST.put(level, amounts);
    }

    public static List<Integer> getUpgradeAmounts(int level) {
        return UPGRADE_AMOUNT_LIST.getOrDefault(level, List.of());
    }

    public static boolean checkStructureMaxed() {
        return STRUCTURE_LEVEL >= 4;
    }
}