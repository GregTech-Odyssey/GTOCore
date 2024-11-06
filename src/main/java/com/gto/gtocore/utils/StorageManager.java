package com.gto.gtocore.utils;

import com.gto.gtocore.integration.ae2.storage.InfinityCellDataStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StorageManager extends SavedData {

    private final Map<UUID, InfinityCellDataStorage> disks;

    public StorageManager() {
        disks = new HashMap<>();
        this.setDirty();
    }

    private StorageManager(Map<UUID, InfinityCellDataStorage> disks) {
        this.disks = disks;
        this.setDirty();
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        ListTag diskList = new ListTag();
        for (Map.Entry<UUID, InfinityCellDataStorage> entry : disks.entrySet()) {
            CompoundTag disk = new CompoundTag();

            disk.putUUID("diskuuid", entry.getKey());
            disk.put("diskdata", entry.getValue().toNbt());
            diskList.add(disk);
        }

        nbt.put("disklist", diskList);
        return nbt;
    }

    public static StorageManager readNbt(CompoundTag nbt) {
        Map<UUID, InfinityCellDataStorage> disks = new HashMap<>();
        ListTag diskList = nbt.getList("disklist", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < diskList.size(); i++) {
            CompoundTag disk = diskList.getCompound(i);
            disks.put(disk.getUUID("diskuuid"), InfinityCellDataStorage.fromNbt(disk.getCompound("diskdata")));
        }
        return new StorageManager(disks);
    }

    public void updateDisk(UUID uuid, InfinityCellDataStorage infinityCellDataStorage) {
        disks.put(uuid, infinityCellDataStorage);
        setDirty();
    }

    public void removeDisk(UUID uuid) {
        disks.remove(uuid);
        setDirty();
    }

    public InfinityCellDataStorage getOrCreateDisk(UUID uuid) {
        if (!disks.containsKey(uuid)) {
            updateDisk(uuid, new InfinityCellDataStorage());
        }
        return disks.get(uuid);
    }

    public void modifyDisk(UUID diskID, ListTag stackKeys, long[] stackAmounts) {
        InfinityCellDataStorage diskToModify = getOrCreateDisk(diskID);
        if (stackKeys != null && stackAmounts != null) {
            diskToModify.stackKeys = stackKeys;
            diskToModify.stackAmounts = stackAmounts;
        }
        updateDisk(diskID, diskToModify);
    }

    public static StorageManager getInstance(MinecraftServer server) {
        ServerLevel world = server.getLevel(ServerLevel.OVERWORLD);
        return world.getDataStorage().computeIfAbsent(StorageManager::readNbt, StorageManager::new,
                "disk_manager");
    }
}
