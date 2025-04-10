package com.gto.gtocore.api.playerSkill.command;

import com.gto.gtocore.api.playerSkill.logic.ExperienceSystemManager;
import com.gto.gtocore.api.playerSkill.logic.PlayerData;
import com.gto.gtocore.api.playerSkill.utils.utilsMessage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;

public class Administration {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("experienceStart")
                .requires(source -> source.hasPermission(2)) // 仅OP可用
                .executes(context -> {
                    ExperienceSystemManager.INSTANCE.enableSystem();
                    context.getSource().sendSuccess(
                            () -> Component.literal("经验系统已开启！").withStyle(ChatFormatting.GREEN), true);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(Commands.literal("experienceStop")
                .requires(source -> source.hasPermission(2)) // 仅OP可用
                .executes(context -> {
                    ExperienceSystemManager.INSTANCE.disableSystem();
                    context.getSource().sendSuccess(
                            () -> Component.literal("经验系统已关闭！").withStyle(ChatFormatting.RED), true);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(Commands.literal("experienceStatus")
                .executes(context -> {
                    for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
                        PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                        if (playerData != null) {
                            // 计算升级进度百分比
                            int healthExp = playerData.getHealthExperienceLevel().getExperience();
                            int healthNextLevelExp = playerData.getHealthExperienceLevel().getExperienceForNextLevel();
                            int healthLevel = playerData.getHealthExperienceLevel().getLevel();
                            int healthProgress = (int) ((float) healthExp / healthNextLevelExp * 100);

                            int attackExp = playerData.getAttackExperienceLevel().getExperience();
                            int attackNextLevelExp = playerData.getAttackExperienceLevel().getExperienceForNextLevel();
                            int attackLevel = playerData.getAttackExperienceLevel().getLevel();
                            int attackProgress = (int) ((float) attackExp / attackNextLevelExp * 100);
//
//                            // 创建格式化的消息
//                            Component message = Component.literal("========== 玩家经验状态 ==========")
//                                    .withStyle(ChatFormatting.GOLD)
//                                    .append(Component.literal("\n玩家: ")
//                                            .withStyle(ChatFormatting.WHITE)
//                                            .append(Component.literal(player.getName().getString())
//                                                    .withStyle(ChatFormatting.AQUA)))
//                                    .append(Component.literal("\n\n生命值经验:")
//                                            .withStyle(ChatFormatting.GREEN))
//                                    .append(Component.literal("\n  等级: ")
//                                            .withStyle(ChatFormatting.WHITE)
//                                            .append(Component.literal(String.valueOf(healthLevel))
//                                                    .withStyle(ChatFormatting.YELLOW)))
//                                    .append(Component.literal("\n  经验: ")
//                                            .withStyle(ChatFormatting.WHITE)
//                                            .append(Component.literal(healthExp + "/" + healthNextLevelExp)
//                                                    .withStyle(ChatFormatting.YELLOW)))
//                                    .append(Component.literal("\n  升级进度: ")
//                                            .withStyle(ChatFormatting.WHITE)
//                                            .append(Component.literal(healthProgress + "%")
//                                                    .withStyle(ChatFormatting.YELLOW)))
//                                    .append(Component.literal("\n\n攻击力经验:")
//                                            .withStyle(ChatFormatting.RED))
//                                    .append(Component.literal("\n  等级: ")
//                                            .withStyle(ChatFormatting.WHITE)
//                                            .append(Component.literal(String.valueOf(attackLevel))
//                                                    .withStyle(ChatFormatting.YELLOW)))
//                                    .append(Component.literal("\n  经验: ")
//                                            .withStyle(ChatFormatting.WHITE)
//                                            .append(Component.literal(attackExp + "/" + attackNextLevelExp)
//                                                    .withStyle(ChatFormatting.YELLOW)))
//                                    .append(Component.literal("\n  升级进度: ")
//                                            .withStyle(ChatFormatting.WHITE)
//                                            .append(Component.literal(attackProgress + "%")
//                                                    .withStyle(ChatFormatting.YELLOW)))
//                                    .append(Component.literal("\n================================")
//                                            .withStyle(ChatFormatting.GOLD));
//
//                            // 发送消息给玩家
//                            player.sendSystemMessage(message);
                            utilsMessage.sendPlayerExpStatusMessage(
                                    player,
                                    playerData.getExperienceLevelLists()
                            );
                        }
                    }
                    return Command.SINGLE_SUCCESS;
                }));
    }
}