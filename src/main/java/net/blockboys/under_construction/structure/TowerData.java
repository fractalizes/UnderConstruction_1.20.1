package net.blockboys.under_construction.structure;

import net.blockboys.under_construction.UnderConstruction;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TowerData extends SavedData {

    private BlockPos GROUND_POS;
    private UUID VILLAGER_ID;

    private boolean GENERATED = false;
    private int STRUCTURE_LEVEL = 0;
    private static int MAX_STRUCTURE_LEVEL = 3;

    // construct materials list
    private static final Map<Integer, List<Item>> UPGRADE_ITEMS_LIST = new HashMap<>();
    static {
        UPGRADE_ITEMS_LIST.put(0, List.of(Items.COBBLESTONE, Items.STONE));
        UPGRADE_ITEMS_LIST.put(1, List.of(Items.STONE_BRICKS, Items.COBBLESTONE_STAIRS));
        UPGRADE_ITEMS_LIST.put(2, List.of(Items.SMOOTH_STONE_SLAB, Items.STONE_BRICK_STAIRS, Items.SPRUCE_FENCE));
    };

    // construct number of materials needed list
    private final Map<Integer, List<Integer>> UPGRADE_AMOUNT_LIST = new HashMap<>();

    // construct material assets list
    private static final Map<Item, ResourceLocation> UPGRADE_ASSET_LIST = new HashMap<>();
    static {
        UPGRADE_ASSET_LIST.put(Items.COBBLESTONE, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/cobblestone_32.png"));
        UPGRADE_ASSET_LIST.put(Items.COBBLESTONE_STAIRS, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/cobblestone_stairs_32.png"));
        UPGRADE_ASSET_LIST.put(Items.SMOOTH_STONE_SLAB, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/smooth_stone_slab_32.png"));
        UPGRADE_ASSET_LIST.put(Items.SPRUCE_FENCE, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/spruce_fence_32.png"));
        UPGRADE_ASSET_LIST.put(Items.STONE, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/stone_32.png"));
        UPGRADE_ASSET_LIST.put(Items.STONE_BRICK_STAIRS, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/stone_brick_stairs_32.png"));
        UPGRADE_ASSET_LIST.put(Items.STONE_BRICKS, ResourceLocation.fromNamespaceAndPath(UnderConstruction.MOD_ID, "textures/icons/stone_bricks_32.png"));
    }

    public TowerData() {
        // construct upgrade amounts
        for (int level : UPGRADE_ITEMS_LIST.keySet()) {
            List<Integer> defaults = switch (level) {
                case 0 -> new ArrayList<>(List.of(32, 32));
                case 1 -> new ArrayList<>(List.of(32, 16));
                case 2 -> new ArrayList<>(List.of(16, 16, 4));
                default -> new ArrayList<>();
            };
            UPGRADE_AMOUNT_LIST.put(level, defaults);
        }
    }

    public void constructor(BlockPos groundPosConst, UUID villagerIdConst) {
        GROUND_POS = groundPosConst;
        VILLAGER_ID = villagerIdConst;
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

    public BlockPos getGroundPos() {
        return GROUND_POS;
    }

    public void setGroundPos(BlockPos groundPosConst) {
        GROUND_POS = groundPosConst;
    }

    public UUID getVillager() {
        return VILLAGER_ID;
    }

    private void setVillager(UUID villagerConst) {
        VILLAGER_ID = villagerConst;
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

    public void copyFrom(@NotNull TowerData tower) {
        this.GENERATED = tower.GENERATED;
        this.STRUCTURE_LEVEL = tower.STRUCTURE_LEVEL;
    }

    @Override
    public CompoundTag save(@NotNull CompoundTag tag) {
        tag.putBoolean("GENERATED", GENERATED);
        tag.putInt("STRUCTURE_LEVEL", STRUCTURE_LEVEL);
        tag.putLong("GROUND_POS", GROUND_POS.asLong()); // save groundpos as long

        for (int level = 1; level <= 3; level++) {
            List<Integer> amounts = UPGRADE_AMOUNT_LIST.get(level);
            if (amounts != null) {
                tag.putIntArray("UPGRADE_AMOUNT_LIST" + level, amounts);
            }
        }

        return tag;
    }

    @NotNull
    public static TowerData loadTower(CompoundTag tag) {
        TowerData tower = new TowerData();
        tower.load(tag);
        return tower;
    }

    public void load(@NotNull CompoundTag tag) {
        this.GENERATED = tag.getBoolean("GENERATED");
        this.STRUCTURE_LEVEL = tag.getInt("STRUCTURE_LEVEL");

        if (tag.contains("GROUND_POS")) {
            this.GROUND_POS = BlockPos.of(tag.getLong("GROUND_POS"));
        }

        for (int level = 1; level <= 3; level++) {
            if (tag.contains("UPGRADE_AMOUNT_LIST" + level)) {
                int[] raw = tag.getIntArray("UPGRADE_AMOUNT_LIST" + level);
                List<Integer> loaded = Arrays.stream(raw).boxed().toList();
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