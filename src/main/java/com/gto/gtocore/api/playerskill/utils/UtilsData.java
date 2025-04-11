package com.gto.gtocore.api.playerskill.utils;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.logic.ExperienceSystemManager;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;

public class UtilsData {

    private static final long MESSAGE_COOLDOWN = 0;

    public static void saveExperienceToNbt(String key, BasicExperienceLevel experience, CompoundTag nbt) {
        try {
            CompoundTag data = new CompoundTag();
            experience.saveData(data); // 给子类分配保存目标
            nbt.put(key, data); // 添加到主 CompoundTag 中
        } catch (Exception e) {
            System.err.println("Error saving " + key + " data: " + e.getMessage());
        }
    }

    public static void addExperience(Player player, BasicExperienceLevel experienceLevel, int amount, Runnable runnable) {
        if (!ExperienceSystemManager.INSTANCE.isEnabled()) return;
        UUID playerId = player.getUUID();
        Map<UUID, Long> lastTimeRecordTable = ExperienceSystemManager.INSTANCE.getLastTimeRecordTable();
        long currentTime = System.currentTimeMillis();
        if (!lastTimeRecordTable.containsKey(playerId) || currentTime - lastTimeRecordTable.get(playerId) >= MESSAGE_COOLDOWN) {
            lastTimeRecordTable.put(playerId, currentTime);
            experienceLevel.addExperience(amount);
            runnable.run();
        }
    }
    public static void addExperienceAndSendMessage(Player player, BasicExperienceLevel experienceLevel, int amount, Component message) {
        addExperience(player, experienceLevel, amount, () -> {player.sendSystemMessage(message);});
    }

    public static void addExperienceAndSendMessage(Player player, BasicExperienceLevel experienceLevel, int amount) {
        Component message = Component.translatable("gtocore.player_exp_status.get_experience", amount,experienceLevel.getName()).withStyle(experienceLevel.getNameColor());
        addExperienceAndSendMessage(
                player,
                experienceLevel,
                amount,
                message);
    }
}
