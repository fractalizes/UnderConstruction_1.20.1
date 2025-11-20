package net.blockboys.underconstruction.block.entity;

import net.blockboys.underconstruction.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class UpgradeChestBlockEntity extends BlockEntity {

    private String customData = ""; // example string, can store progress or state

    public UpgradeChestBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.UPGRADE_CHEST.get(), pos, state);
    }

    // Getter & setter
    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String data) {
        this.customData = data;
        setChanged(); // marks the block entity as dirty so it saves
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("CustomData")) {
            customData = tag.getString("CustomData");
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("CustomData", customData);
    }
}
