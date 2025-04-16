package com.gto.gtocore.api.playerskill.utils;

import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

import java.util.UUID;

/**
 * 工具类，用于处理经验系统的数据保存和经验值添加
 * 提供了NBT数据存储和经验值操作的通用方法
 */
public final class UtilsData {

    /**
     * 私有构造函数，防止实例化
     */
    private UtilsData() {}

    /**
     * 消息发送冷却时间（毫秒）
     * 当前设置为0，意味着没有冷却
     */
    private static final long MESSAGE_COOLDOWN = 0;

    /**
     * 将经验等级数据保存到NBT中
     * 
     * @param key        保存的键名
     * @param experience 要保存的经验等级对象
     * @param nbt        目标NBT标签
     */
    public static void saveExperienceToNbt(String key, BasicExperienceLevel experience, CompoundTag nbt) {
        try {
            CompoundTag data = new CompoundTag();
            experience.saveData(data); // 给子类分配保存目标
            nbt.put(key, data); // 添加到主 CompoundTag 中
        } catch (Exception e) {
            System.err.println("Error saving " + key + " data: " + e.getMessage());
        }
    }

    /**
     * 为玩家添加经验值，并执行指定的回调
     * 包含冷却时间检查，避免频繁添加造成性能问题
     * 
     * @param player          玩家对象
     * @param experienceLevel 经验等级对象
     * @param amount          要添加的经验值数量
     * @param runnable        添加成功后执行的回调
     */
    public static void addExperience(Player player, BasicExperienceLevel experienceLevel, long amount, Runnable runnable) {
        if (!ExperienceSystemManager.INSTANCE.isEnabled()) return;
        UUID playerId = player.getUUID();
        Object2LongMap<UUID> lastTimeRecordTable = ExperienceSystemManager.INSTANCE.getLastTimeRecordTable();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastTimeRecordTable.getLong(playerId);
        if (lastTime == 0 || currentTime - lastTime >= MESSAGE_COOLDOWN) {
            lastTimeRecordTable.put(playerId, currentTime);
            experienceLevel.addExperience(amount);
            runnable.run();
            ExperienceSystemManager.INSTANCE.saveAll();
        }
    }

    /**
     * 为玩家添加经验值，并发送自定义消息
     * 
     * @param player          玩家对象
     * @param experienceLevel 经验等级对象
     * @param amount          要添加的经验值数量
     * @param message         要发送的自定义消息
     */
    public static void addExperienceAndSendMessage(Player player, BasicExperienceLevel experienceLevel, long amount, Component message) {
        addExperience(player, experienceLevel, amount, () -> player.sendSystemMessage(message));
    }

    /**
     * 为玩家添加经验值，并发送标准的获得经验消息
     * 
     * @param player          玩家对象
     * @param experienceLevel 经验等级对象
     * @param amount          要添加的经验值数量
     */
    public static void addExperienceAndSendMessage(Player player, BasicExperienceLevel experienceLevel, long amount) {
        Component message = Component.translatable("gtocore.player_exp_status.get_experience", amount, experienceLevel.getName()).withStyle(experienceLevel.getNameColor());
        addExperienceAndSendMessage(player, experienceLevel, amount, message);
    }
}
