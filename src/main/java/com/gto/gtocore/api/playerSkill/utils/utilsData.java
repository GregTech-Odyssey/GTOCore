package com.gto.gtocore.api.playerSkill.utils;

import com.google.gson.JsonObject;
import com.gto.gtocore.api.playerSkill.experienceSub.BasicExperienceLevel;
import com.gto.gtocore.api.playerSkill.logic.ExperienceSystemManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;


public class utilsData {
    private static final long MESSAGE_COOLDOWN = 20;
    public static void saveExperienceToJson(String key, BasicExperienceLevel experience, JsonObject jsonObject) {
        try {
            JsonObject data = new JsonObject();
            experience.saveData(data); // 调用 saveData 方法
            jsonObject.add(key, data); // 将数据添加到主 JsonObject 中
        } catch (Exception e) {
            // 捕获异常并记录日志，避免程序崩溃
            System.err.println("Error saving " + key + " data: " + e.getMessage());

        }
    }
    public static void addExperienceAndSendMessage(Player player , BasicExperienceLevel experienceLevel, int amount, String message, ChatFormatting color) {
        if (!ExperienceSystemManager.INSTANCE.isEnabled()) return;
        UUID playerId = player.getUUID();
        Map<UUID, Long> lastTimeRecordTable = ExperienceSystemManager.INSTANCE.getLastTimeRecordTable();
        long currentTime = System.currentTimeMillis();
        if (!lastTimeRecordTable.containsKey(playerId) || currentTime - lastTimeRecordTable.get(playerId) > MESSAGE_COOLDOWN) {
            lastTimeRecordTable.put(playerId, currentTime);
            experienceLevel.addExperience(amount);
            player.sendSystemMessage(Component.literal(message).withStyle(color));
        }
    }
}
