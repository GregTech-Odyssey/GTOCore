package com.gto.gtocore.api.playerSkill.utils;

import com.gto.gtocore.api.playerSkill.experienceSub.BasicExperienceLevel;
import com.gto.gtocore.api.playerSkill.logic.ExperienceSystemManager;
import com.gto.gtocore.api.playerSkill.logic.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent; // 添加此导入
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class utilsMessage {
    // 通用框框
    public static Component buildPlayerExpStatusMessage(Player player, List<BasicExperienceLevel> basicExperienceLevel) {
        // 创建标题 - 使用 MutableComponent 而不是 Component
        MutableComponent message = Component.literal("========== 玩家经验状态 ==========")
                .withStyle(ChatFormatting.GOLD);

        // 添加玩家名称
        message = message.append(Component.literal("\n玩家: ")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(player.getName().getString())
                        .withStyle(ChatFormatting.AQUA)));

        // 添加每个技能的信息
        for (BasicExperienceLevel skill : basicExperienceLevel) {
            message = message.append(formatSkillInfo(skill));
        }

        // 添加页脚
        message = message.append(Component.literal("\n================================")
                .withStyle(ChatFormatting.GOLD));

        return message;
    }

    // Part 内容
    private static MutableComponent formatSkillInfo(BasicExperienceLevel basicExperienceLevel) {
        MutableComponent skillInfo = Component.literal("\n\n" + basicExperienceLevel.getName() + ":")
                .withStyle(basicExperienceLevel.getNameColor());

        // 添加等级信息
        skillInfo = skillInfo.append(Component.literal("\n  等级: ")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(String.valueOf(basicExperienceLevel.getLevel()) + " / " + basicExperienceLevel.getMaxLevel() + " 上限")
                        .withStyle(ChatFormatting.YELLOW)));

        // 添加经验信息
        skillInfo = skillInfo.append(Component.literal("\n  经验: ")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(basicExperienceLevel.getExperience() + " / " + basicExperienceLevel.getExperienceForNextLevel() + "升级")
                        .withStyle(ChatFormatting.YELLOW)));

        // 添加进度信息
        skillInfo = skillInfo.append(Component.literal("\n  升级进度: ")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(calculateProgressPercentage(basicExperienceLevel) + " % ")
                        .withStyle(ChatFormatting.YELLOW)));

        return skillInfo;
    }

    // 一键
    public static void sendPlayerExpStatusMessage(Player player, List<BasicExperienceLevel> basicExperienceLevel) {
        Component component = buildPlayerExpStatusMessage(player, basicExperienceLevel);
        player.sendSystemMessage(component);
    }

    public static int calculateProgressPercentage(BasicExperienceLevel experienceLevel) {
        if (experienceLevel.getExperienceForNextLevel() <= 0) {
            return 100; // 防止除以零
        }
        return (int) ((float) experienceLevel.getExperience() / experienceLevel.getExperienceForNextLevel() * 100);
    }
}