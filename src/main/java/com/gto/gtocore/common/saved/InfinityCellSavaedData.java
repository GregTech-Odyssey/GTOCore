package com.gto.gtocore.common.saved;

import com.gto.gtocore.integration.ae2.storage.InfinityCellDataStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

import static com.gto.gtocore.api.GTOValues.*;

public final class InfinityCellSavaedData extends SavedData {

    public static InfinityCellSavaedData INSTANCE = new InfinityCellSavaedData();

    private final Map<UUID, InfinityCellDataStorage> cells;

    public InfinityCellSavaedData() {
        cells = new Object2ObjectOpenHashMap<>();
    }

    private InfinityCellSavaedData(Map<UUID, InfinityCellDataStorage> cells) {
        this.cells = cells;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        ListTag cellList = new ListTag();
        for (Map.Entry<UUID, InfinityCellDataStorage> entry : cells.entrySet()) {
            CompoundTag cell = new CompoundTag();
            cell.putUUID(INFINITY_CELL_UUID, entry.getKey());
            cell.put(INFINITY_CELL_DATA, entry.getValue().toNbt());
            cellList.add(cell);
        }
        nbt.put(INFINITY_CELL_LIST, cellList);
        return nbt;
    }

    public static InfinityCellSavaedData readNbt(CompoundTag nbt) {
        Map<UUID, InfinityCellDataStorage> cells = new Object2ObjectOpenHashMap<>();
        ListTag cellList = nbt.getList(INFINITY_CELL_LIST, CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < cellList.size(); i++) {
            CompoundTag cell = cellList.getCompound(i);
            cells.put(cell.getUUID(INFINITY_CELL_UUID), InfinityCellDataStorage.fromNbt(cell.getCompound(INFINITY_CELL_DATA)));
        }
        return new InfinityCellSavaedData(cells);
    }

    public void updateCell(UUID uuid, InfinityCellDataStorage infinityCellDataStorage) {
        cells.put(uuid, infinityCellDataStorage);
        setDirty();
    }

    public void removeCell(UUID uuid) {
        cells.remove(uuid);
        setDirty();
    }

    public InfinityCellDataStorage getOrCreateCell(UUID uuid) {
        if (!cells.containsKey(uuid)) {
            updateCell(uuid, new InfinityCellDataStorage());
        }
        return cells.get(uuid);
    }

    public void modifyCell(UUID cellID, ListTag keys, long[] amounts) {
        InfinityCellDataStorage cellToModify = getOrCreateCell(cellID);
        if (keys != null && amounts != null) {
            cellToModify.keys = keys;
            cellToModify.amounts = amounts;
        }
        updateCell(cellID, cellToModify);
    }
}
