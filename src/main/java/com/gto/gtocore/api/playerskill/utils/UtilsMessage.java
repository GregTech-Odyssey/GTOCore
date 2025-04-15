package com.gto.gtocore.api.playerskill.utils;

import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public final class UtilsMessage {

    private UtilsMessage() {}

    // 通用框框QaQ
    public static Component buildPlayerExpStatusMessage(Player player, List<BasicExperienceLevel> basicExperienceLevel) {
        MutableComponent message = Component.translatable("gtocore.player_exp_status.header")
                .withStyle(ChatFormatting.GOLD);

        // 添加玩家名称
        message = message.append(Component.translatable("gtocore.player_exp_status.player")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(player.getName().getString())
                        .withStyle(ChatFormatting.AQUA)));

        // 添加每个技能的信息List
        for (BasicExperienceLevel skill : basicExperienceLevel) {
            message = message.append(formatSkillInfo(skill));
        }

        // 添加页脚
        message = message.append(Component.translatable("gtocore.player_exp_status.footer")
                .withStyle(ChatFormatting.GOLD));

        return message;
    }

    // Part 内容
    private static MutableComponent formatSkillInfo(BasicExperienceLevel basicExperienceLevel) {
        MutableComponent skillInfo = Component.literal("\n\n")
                .append(Component.literal(basicExperienceLevel.getName() + ":")
                        .withStyle(basicExperienceLevel.getNameColor()));

        // 添加等级信息
        skillInfo = skillInfo.append(Component.translatable("gtocore.player_exp_status.level")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(basicExperienceLevel.getLevel() + " (" + GTValues.VN[(int) basicExperienceLevel.getVoltage()] + ")" + " / " +
                        basicExperienceLevel.getMaxLevel() + " (" + GTValues.VN[(int) basicExperienceLevel.getMaxVoltage()] + ")" +
                        Component.translatable("gtocore.player_exp_status.level_max").getString())
                        .withStyle(ChatFormatting.YELLOW)));

        // 添加经验信息
        skillInfo = skillInfo.append(Component.translatable("gtocore.player_exp_status.experience")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(basicExperienceLevel.getExperience() + " / " +
                        basicExperienceLevel.getExperienceForNextLevel() +
                        Component.translatable("gtocore.player_exp_status.experience_next").getString())
                        .withStyle(ChatFormatting.YELLOW)));

        // 添加进度信息
        skillInfo = skillInfo.append(Component.translatable("gtocore.player_exp_status.progress")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(calculateProgressPercentage(basicExperienceLevel) + " % ")
                        .withStyle(ChatFormatting.YELLOW)));
        if (calculateProgressPercentage(basicExperienceLevel) >= 100) {
            skillInfo.append(Component.literal("\n"));
            skillInfo.append(
                    Component.translatable("gtocore.player_exp_status.upgrade_institution")
                            .withStyle(ChatFormatting.RED));
        }
        return skillInfo;
    }

    // 一键
    public static void sendPlayerExpStatusMessage(Player player, List<BasicExperienceLevel> basicExperienceLevel) {
        Component component = buildPlayerExpStatusMessage(player, basicExperienceLevel);
        player.sendSystemMessage(component);
    }

    public static int calculateProgressPercentage(BasicExperienceLevel experienceLevel) {
        return (int) ((float) experienceLevel.getExperience() / experienceLevel.getExperienceForNextLevel() * 100);
    }
}
