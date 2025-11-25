package net.blockboys.under_construction.structure;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TowerStructureProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<TowerStructure> TOWER_STRUCTURE = CapabilityManager.get(new CapabilityToken<>() {});

    public TowerStructure tower = null;
    private final LazyOptional <TowerStructure> optional = LazyOptional.of(this::createTowerStructure);

    private TowerStructure createTowerStructure() {
        if (this.tower == null) {
            this.tower = new TowerStructure();
        } return this.tower;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == TOWER_STRUCTURE) {
            return optional.cast();
        } return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createTowerStructure().save(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createTowerStructure().load(nbt);
    }
}
