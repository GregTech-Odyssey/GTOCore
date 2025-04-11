package com.gto.gtocore.api.playerskill.logic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class ExperienceSystemManager extends SavedData {

    private static final String DATA_NAME = "gto_experience_data";
    public static volatile ExperienceSystemManager INSTANCE;

    @Getter
    private final Object2LongMap<UUID> LastTimeRecordTable;
    private final Map<UUID, PlayerData> playerDataMap;
    private boolean isEnabled;

    private ExperienceSystemManager() {
        this.playerDataMap = new Object2ObjectOpenHashMap<>();
        this.isEnabled = false; // 默认关闭
        this.LastTimeRecordTable = new Object2LongOpenHashMap<>();
    }

    // 从 NBT 构造
    private ExperienceSystemManager(CompoundTag nbt, Map<UUID, PlayerData> playerDataMap, Object2LongMap<UUID> lastTimeRecordTable) {
        this.playerDataMap = playerDataMap;
        this.isEnabled = nbt.getBoolean("isEnabled");
        this.LastTimeRecordTable = lastTimeRecordTable;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        nbt.putBoolean("isEnabled", isEnabled);

        // 保存 LastTimeRecordTable
        CompoundTag timeRecordTag = new CompoundTag();
        for (Object2LongMap.Entry<UUID> entry : LastTimeRecordTable.object2LongEntrySet()) {
            timeRecordTag.putLong(entry.getKey().toString(), entry.getLongValue());
        }
        nbt.put("timeRecords", timeRecordTag);

        // 保存 playerDataMap
        CompoundTag playersTag = new CompoundTag();
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            entry.getValue().saveData(playerTag);
            playersTag.put(entry.getKey().toString(), playerTag);
        }
        nbt.put("players", playersTag);

        return nbt;
    }

    // 从 NBT 加载
    public static ExperienceSystemManager load(CompoundTag nbt) {
        Map<UUID, PlayerData> playerDataMap = new Object2ObjectOpenHashMap<>();
        Object2LongMap<UUID> lastTimeRecordTable = new Object2LongOpenHashMap<>();

        // 加载 LastTimeRecordTable
        if (nbt.contains("timeRecords")) {
            CompoundTag timeRecordTag = nbt.getCompound("timeRecords");
            for (String key : timeRecordTag.getAllKeys()) {
                UUID uuid = UUID.fromString(key);
                lastTimeRecordTable.put(uuid, timeRecordTag.getLong(key));
            }
        }

        // 加载 playerDataMap
        if (nbt.contains("players")) {
            CompoundTag playersTag = nbt.getCompound("players");
            for (String key : playersTag.getAllKeys()) {
                UUID playerId = UUID.fromString(key);
                PlayerData playerData = new PlayerData(playerId);
                playerData.loadData(playersTag.getCompound(key));
                playerDataMap.put(playerId, playerData);
            }
        }

        return new ExperienceSystemManager(nbt, playerDataMap, lastTimeRecordTable);
    }

    // 获取或创建数据
    public static ExperienceSystemManager getOrCreate(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        ExperienceSystemManager data = storage.computeIfAbsent(
                ExperienceSystemManager::load,
                ExperienceSystemManager::new,
                DATA_NAME);
        INSTANCE = data;
        return data;
    }

    public void enableSystem() {
        isEnabled = true;
        setDirty();
    }

    public void disableSystem() {
        isEnabled = false;
        setDirty();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void addPlayer(UUID playerId) {
        if (isEnabled) {
            playerDataMap.putIfAbsent(playerId, new PlayerData(playerId));
            setDirty();
        }
    }

    public void removePlayer(UUID playerId) {
        playerDataMap.remove(playerId);
        setDirty();
    }

    public PlayerData getPlayerData(UUID playerId) {
        return playerDataMap.get(playerId);
    }

    public void addHealthExperience(UUID playerId, int amount) {
        if (isEnabled && playerDataMap.containsKey(playerId)) {
            playerDataMap.get(playerId).addHealthExperience(amount);
            setDirty();
        }
    }

    public void addAttackExperience(UUID playerId, int amount) {
        if (isEnabled && playerDataMap.containsKey(playerId)) {
            playerDataMap.get(playerId).addAttackExperience(amount);
            setDirty();
        }
    }

    public void addBodyExperience(UUID playerId, int amount) {
        if (isEnabled && playerDataMap.containsKey(playerId)) {
            playerDataMap.get(playerId).addBodyExperience(amount);
            setDirty();
        }
    }

    // 记录时间数据
    public void recordTime(UUID playerId, long time) {
        LastTimeRecordTable.put(playerId, time);
        setDirty();
    }
}
