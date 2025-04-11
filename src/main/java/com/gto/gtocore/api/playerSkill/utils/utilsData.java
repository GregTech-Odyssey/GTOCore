package com.gto.gtocore.api.playerSkill.utils;

import com.google.gson.JsonObject;
import com.gto.gtocore.api.playerSkill.experienceSub.BasicExperienceLevel;
import com.gto.gtocore.api.playerSkill.logic.ExperienceSystemManager;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;


public class utilsData {
    private static final long MESSAGE_COOLDOWN = 20;
    public static void saveExperienceToNbt(String key, BasicExperienceLevel experience, CompoundTag nbt) {
        try {
            CompoundTag data = new CompoundTag();
            experience.saveData(data); // 给子类分配保存目标
            nbt.put(key, data); // 添加到主 CompoundTag 中
        } catch (Exception e) {
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
