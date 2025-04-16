package com.gto.gtocore.api.playerskill.utils;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.List;

/**
 * 工具类，用于构建和发送与玩家经验相关的消息
 * 负责格式化和显示玩家技能状态信息
 */
public final class UtilsMessage {

    /**
     * 私有构造函数，防止实例化
     */
    private UtilsMessage() {}

    /**
     * 构建玩家经验状态消息的完整组件
     * 包括标题、玩家名称、各技能信息和页脚
     * 
     * @param player               玩家对象
     * @param basicExperienceLevel 要显示的经验等级列表
     * @return 格式化好的消息组件
     */
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
            if (!skill.skillType.isVisible) continue;
            message = message.append(formatSkillInfo(skill));
        }

        // 添加页脚
        message = message.append(Component.translatable("gtocore.player_exp_status.footer")
                .withStyle(ChatFormatting.GOLD));

        return message;
    }

    /**
     * 格式化单个技能的信息为可显示的组件
     * 包括技能名称、等级信息、经验值和进度
     * 
     * @param basicExperienceLevel 要格式化的经验等级对象
     * @return 格式化好的技能信息组件
     */
    private static MutableComponent formatSkillInfo(BasicExperienceLevel basicExperienceLevel) {
        MutableComponent skillInfo = Component.literal("\n\n")
                .append(Component.literal(basicExperienceLevel.getName() + ":")
                        .withStyle(basicExperienceLevel.getNameColor()));

        // 添加等级信息
        skillInfo = skillInfo.append(Component.translatable("gtocore.player_exp_status.level")
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(basicExperienceLevel.getLevel() + " (" + GTOValues.VNFR[(int) basicExperienceLevel.getVoltage()] + ")" + " / " +
                        basicExperienceLevel.getMaxLevel() + " (" + GTOValues.VNFR[(int) basicExperienceLevel.getMaxVoltage()] + ")" +
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
                    Component.literal("  ").append(Component.translatable("gtocore.player_exp_status.upgrade_institution"))
                            .withStyle(ChatFormatting.RED));
        }
        return skillInfo;
    }

    /**
     * 向玩家发送经验状态消息
     * 构建并发送完整的技能状态信息
     * 
     * @param player               目标玩家
     * @param basicExperienceLevel 要显示的经验等级列表
     */
    public static void sendPlayerExpStatusMessage(Player player, List<BasicExperienceLevel> basicExperienceLevel) {
        Component component = buildPlayerExpStatusMessage(player, basicExperienceLevel);
        player.sendSystemMessage(component);
    }

    /**
     * 计算技能升级进度的百分比
     * 
     * @param experienceLevel 经验等级对象
     * @return 升级进度百分比（0-100）
     */
    public static int calculateProgressPercentage(BasicExperienceLevel experienceLevel) {
        return (int) ((float) experienceLevel.getExperience() / experienceLevel.getExperienceForNextLevel() * 100);
    }
}
