package net.blockboys.under_construction.structure;

import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class TowerStructure extends SavedData {

    private ServerLevel WORLD_LEVEL;
    private BlockPos GROUND_POS;
    private Villager VILLAGER_ENTITY;

    private boolean GENERATED = false;
    private int STRUCTURE_LEVEL = 1;
    private static int MAX_STRUCTURE_LEVEL = 3;

    // construct materials list
    private static final Map<Integer, List<Item>> UPGRADE_ITEMS_LIST = new HashMap<>();
    static {
        UPGRADE_ITEMS_LIST.put(1, List.of(Items.COBBLESTONE, Items.STONE));
        UPGRADE_ITEMS_LIST.put(2, List.of(Items.STONE_BRICKS, Items.COBBLESTONE_STAIRS));
    };

    // construct number of materials needed list
    private final Map<Integer, List<Integer>> UPGRADE_AMOUNT_LIST = new HashMap<>();

    // construct material assets list
    private static final Map<Item, ResourceLocation> UPGRADE_ASSET_LIST = new HashMap<>();
    static {
        UPGRADE_ASSET_LIST.put(Items.COBBLESTONE, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/cobblestone_32.png"));
        UPGRADE_ASSET_LIST.put(Items.STONE, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/stone_32.png"));
        UPGRADE_ASSET_LIST.put(Items.STONE_BRICKS, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/stone_bricks_32.png"));
        UPGRADE_ASSET_LIST.put(Items.COBBLESTONE_STAIRS, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/cobblestone_stairs_32.png"));
    }

    public TowerStructure() {
        // construct upgrade amounts
        for (int level : UPGRADE_ITEMS_LIST.keySet()) {
            List<Integer> defaults = switch (level) {
                case 1 -> new ArrayList<>(List.of(32, 32));
                case 2 -> new ArrayList<>(List.of(32, 16));
                default -> new ArrayList<>();
            };
            UPGRADE_AMOUNT_LIST.put(level, defaults);
        }
    }

    public void constructor(ServerLevel levelConst, BlockPos groundPosConst, Villager villagerConst) {
        WORLD_LEVEL = levelConst;
        GROUND_POS = groundPosConst;
        VILLAGER_ENTITY = villagerConst;
    }

    public boolean isGenerated() {
        return GENERATED;
    }

    public void setGenerated() {
        GENERATED = true;
        setDirty();
    }

    public int getStructureLevel() {
        return STRUCTURE_LEVEL;
    }

    public void setStructureLevel(int level) {
        STRUCTURE_LEVEL = level;
        setDirty();
    }

    public void incrementStructureLevel() {
        STRUCTURE_LEVEL++;
        setDirty();
        Minecraft.getInstance().gui.getChat().addMessage(Component.literal(
                "The Tower Structure is now Level " + STRUCTURE_LEVEL + "!"));
    }

    public boolean checkUpgradeCompletion() {
        List<Integer> amounts = getAmountsList(this.STRUCTURE_LEVEL);
        return amounts.stream().allMatch(count -> count <= 0);
    }

    public static int getMaxStructureLevel() {
        return MAX_STRUCTURE_LEVEL;
    }

    public boolean checkStructureMaxed() {
        if (STRUCTURE_LEVEL < MAX_STRUCTURE_LEVEL) return false;
        return true;
    }

    public ServerLevel getLevel() {
        return WORLD_LEVEL;
    }

    public void setLevel(ServerLevel levelConst) {
        WORLD_LEVEL = levelConst;
    }

    public BlockPos getGroundPos() {
        return GROUND_POS;
    }

    public void setGroundPos(BlockPos groundPosConst) {
        GROUND_POS = groundPosConst;
    }

    public Villager getVillager() {
        return VILLAGER_ENTITY;
    }

    private void setVillager(Villager villagerConst) {
        VILLAGER_ENTITY = villagerConst;
    }

    public static List<Item> getItemsList(int level) {
        return UPGRADE_ITEMS_LIST.getOrDefault(level, List.of());
    }

    public List<Integer> getAmountsList(int level) {
        return UPGRADE_AMOUNT_LIST.getOrDefault(level, List.of());
    }

    public void setAmountsList(int level, int index, int amount) {
        List<Integer> amounts = UPGRADE_AMOUNT_LIST.get(level);
        if (amounts != null && index < amounts.size()) {
            amounts.set(index, amount);
            setDirty();
        }
    }

    public static ResourceLocation getMaterialAsset(Item item) {
        return UPGRADE_ASSET_LIST.get(item);
    }

    public void copyFrom(TowerStructure tower) {
        this.GENERATED = tower.GENERATED;
        this.STRUCTURE_LEVEL = tower.STRUCTURE_LEVEL;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("GENERATED", GENERATED);
        tag.putInt("STRUCTURE_LEVEL", STRUCTURE_LEVEL);

        for (int level = 1; level <= 3; level++) {
            List<Integer> amounts = UPGRADE_AMOUNT_LIST.get(level);
            if (amounts != null) {
                tag.putIntArray("UPGRADE_AMOUNT_LIST" + level, amounts);
            }
        }

        return tag;
    }

    public static TowerStructure loadTower(CompoundTag tag) {
        TowerStructure tower = new TowerStructure();
        tower.load(tag);
        return tower;
    }

    public void load(CompoundTag tag) {
        this.GENERATED = tag.getBoolean("GENERATED");
        this.STRUCTURE_LEVEL = tag.getInt("STRUCTURE_LEVEL");

        // Load upgrade amounts
        for (int level = 1; level <= 3; level++) {
            if (tag.contains("UPGRADE_AMOUNT_LIST" + level)) {
                List<Integer> loaded = Arrays.stream(tag.getIntArray("UPGRADE_AMOUNT_LIST" + level)).boxed().toList();
                List<Integer> current = UPGRADE_AMOUNT_LIST.get(level);
                if (current != null && loaded.size() == current.size()) {
                    for (int i = 0; i < loaded.size(); i++) {
                        current.set(i, loaded.get(i));
                    }
                }
            }
        }
    }
}