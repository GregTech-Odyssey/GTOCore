package com.gto.gtocore.api.playerskill.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

/**
 * 玩家技能经验系统的中央管理器
 * 负责存储和管理所有玩家的技能数据
 * 继承自Minecraft的SavedData类，使数据可持久化
 */
public class ExperienceSystemManager extends SavedData {

    /** 单例实例，使用volatile确保多线程可见性 */
    public static volatile ExperienceSystemManager INSTANCE;

    /**
     * 记录玩家最后一次操作的时间戳
     * 用于实现各种基于时间的功能，如冷却时间等
     */
    @Getter
    private final Object2LongMap<UUID> LastTimeRecordTable;

    /** 存储所有玩家数据的映射表，键为玩家UUID */
    private final Map<UUID, PlayerData> playerDataMap;

    /** 系统启用状态标志 */
    private boolean isEnabled;

    /**
     * 默认构造函数
     * 初始化数据结构并默认禁用系统
     */
    public ExperienceSystemManager() {
        this.playerDataMap = new Object2ObjectOpenHashMap<>();
        this.isEnabled = false; // 默认关闭
        this.LastTimeRecordTable = new Object2LongOpenHashMap<>();
    }

    /**
     * 从NBT数据构造实例
     * 用于从保存的游戏数据中加载经验系统
     * 
     * @param nbt                 NBT数据标签
     * @param playerDataMap       玩家数据映射
     * @param lastTimeRecordTable 时间记录表
     */
    private ExperienceSystemManager(CompoundTag nbt, Map<UUID, PlayerData> playerDataMap, Object2LongMap<UUID> lastTimeRecordTable) {
        this.playerDataMap = playerDataMap;
        this.isEnabled = nbt.getBoolean("isEnabled");
        this.LastTimeRecordTable = lastTimeRecordTable;
    }

    /**
     * 将管理器的所有数据保存到NBT
     * 
     * @param nbt 目标NBT标签
     * @return 填充了数据的NBT标签
     */
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

    /**
     * 从NBT数据加载管理器实例
     * 
     * @param nbt 包含系统数据的NBT标签
     * @return 加载了数据的经验系统管理器实例
     */
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

    /**
     * 启用经验系统
     * 并标记数据为已更改，需要保存
     */
    public void enableSystem() {
        isEnabled = true;
        setDirty();
    }

    /**
     * 禁用经验系统
     * 并标记数据为已更改，需要保存
     */
    public void disableSystem() {
        isEnabled = false;
        setDirty();
    }

    /**
     * 获取系统是否启用的状态
     * 
     * @return 系统启用状态
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * 获取指定玩家的数据
     * 如果玩家数据不存在，则创建一个新的
     * 
     * @param playerId 玩家UUID
     * @return 玩家数据对象
     */
    public @NotNull PlayerData getPlayerData(UUID playerId) {
        return playerDataMap.computeIfAbsent(playerId, k -> {
            setDirty();
            return new PlayerData(playerId);
        });
    }

    /**
     * 保存所有数据
     * 将当前状态标记为需要保存
     */
    public void saveAll() {
        setDirty();
    }
}
