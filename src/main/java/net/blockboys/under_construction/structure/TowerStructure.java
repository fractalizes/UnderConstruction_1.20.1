package net.blockboys.under_construction.structure;

import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.*;

public class TowerStructure {

    private static ServerLevel WORLD_LEVEL;
    private static BlockPos GROUND_POS;
    private static Villager VILLAGER_ENTITY;

    private static boolean GENERATED = false;
    private static int STRUCTURE_LEVEL = 1;
    private static int MAX_STRUCTURE_LEVEL = 3;
    private static String[] STRUCTURE_PATHS = {
        "ruins", "tier1", "tier2", "tier3"
    };

    // construct materials list
    private static final Map<Integer, List<Item>> UPGRADE_ITEMS_LIST = new HashMap<>();
    static {
        UPGRADE_ITEMS_LIST.put(1, List.of(Items.COBBLESTONE, Items.STONE));
        UPGRADE_ITEMS_LIST.put(2, List.of(Items.STONE_BRICKS, Items.COBBLESTONE_STAIRS));
    };

    // construct number of materials needed list
    private static final Map<Integer, List<Integer>> UPGRADE_AMOUNT_LIST = new HashMap<>();
    static {
        UPGRADE_AMOUNT_LIST.put(1, new ArrayList<>(List.of(32, 32)));
        UPGRADE_AMOUNT_LIST.put(2, new ArrayList<>(List.of(32, 16)));
    }

    public static void constructor(ServerLevel levelConst, BlockPos groundPosConst, Villager villagerConst) {
        WORLD_LEVEL = levelConst;
        GROUND_POS = groundPosConst;
        VILLAGER_ENTITY = villagerConst;
    }

    public static boolean isGenerated() {
        return GENERATED;
    }

    public static void setGenerated() {
        GENERATED = true;
    }

    public static int getStructureLevel() {
        return STRUCTURE_LEVEL;
    }

    public static void setStructureLevel(int level) {
        STRUCTURE_LEVEL = level;
    }

    public static void incrementStructureLevel() {
        STRUCTURE_LEVEL++;
        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure is now Level " + STRUCTURE_LEVEL + "!"));
    }

    public static boolean checkUpgradeCompletion() {
        List<Integer> upgradeNumbers = TowerStructure.getAmountsList(STRUCTURE_LEVEL);
        for (int upgradeNumber: upgradeNumbers) {
            if (upgradeNumber > 0) {
                return false;
            }
        } return true;
    }

    public static int getMaxStructureLevel() {
        return MAX_STRUCTURE_LEVEL;
    }

    public static boolean checkStructureMaxed() {
        if (STRUCTURE_LEVEL < MAX_STRUCTURE_LEVEL) return false;
        System.out.println("max level of " + STRUCTURE_LEVEL + " achieved!");
        return true;
    }

    public static void generateStructurePiece() {
        StructureTemplateManager manager = WORLD_LEVEL.getStructureManager();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                UnderConstruction.MOD_ID, STRUCTURE_PATHS[STRUCTURE_LEVEL - 1]);
        StructureTemplate template = manager.getOrCreate(id);
        StructurePlaceSettings settings = new StructurePlaceSettings();

        // replace old structure with new one
        template.placeInWorld(WORLD_LEVEL,
                GROUND_POS,
                GROUND_POS,
                settings,
                WORLD_LEVEL.random,
                2
        );
    }

    public static ServerLevel getLevel() {
        return WORLD_LEVEL;
    }

    public static void setLevel(ServerLevel levelConst) {
        WORLD_LEVEL = levelConst;
    }

    public static BlockPos getGroundPos() {
        return GROUND_POS;
    }

    public static void setGroundPos(BlockPos groundPosConst) {
        GROUND_POS = groundPosConst;
    }

    public static Villager getVillager() {
        return VILLAGER_ENTITY;
    }

    private void setVillager(Villager villagerConst) {
        VILLAGER_ENTITY = villagerConst;
    }

    public static List<Item> getItemsList(int i) {
        return UPGRADE_ITEMS_LIST.get(i);
    }

    public static List<Integer> getAmountsList(int i) {
        return UPGRADE_AMOUNT_LIST.get(i);
    }

    public static void setAmountsList(int index, int newCount) {
        List<Integer> list = UPGRADE_AMOUNT_LIST.get(STRUCTURE_LEVEL);
        list.set(index, newCount);
    }
}