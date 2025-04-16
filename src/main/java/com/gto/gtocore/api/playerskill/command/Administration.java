package com.gto.gtocore.api.playerskill.command;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.playerskill.SkillRegistry;
import com.gto.gtocore.api.playerskill.SkillType;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.experiencelevel.BasicExperienceLevel;
import com.gto.gtocore.api.playerskill.utils.UtilsData;
import com.gto.gtocore.api.playerskill.utils.UtilsMessage;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

/**
 * 技能系统管理命令类
 * 提供了一系列管理技能系统的命令，包括启用/禁用系统、
 * 修改玩家经验值和等级等功能
 */
public class Administration {

    /**
     * 注册所有技能系统相关命令
     * 
     * @param dispatcher 命令调度器
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skill")
                .then(Commands.literal("admin")
                        .requires(source -> source.hasPermission(2)) // 仅OP可用
                        .then(Commands.literal("start")
                                .executes(context -> {
                                    // 启用经验系统
                                    ExperienceSystemManager.INSTANCE.enableSystem();
                                    GTOCore.LOGGER.info("Experience system enabled via command");
                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("gtocore.player_exp_status.open")
                                                    .withStyle(ChatFormatting.GREEN),
                                            true);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("stop")
                                .executes(context -> {
                                    // 禁用经验系统
                                    ExperienceSystemManager.INSTANCE.disableSystem();
                                    GTOCore.LOGGER.info("Experience system disabled via command");
                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("gtocore.player_exp_status.close")
                                                    .withStyle(ChatFormatting.RED),
                                            true);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(createSkillCommand("addExp", (player, skillType, amount, playerData) -> {
                            // 为玩家添加经验值
                            BasicExperienceLevel expLevel = skillType.getExperienceLevel(playerData);
                            UtilsData.addExperienceAndSendMessage(player, expLevel, amount);
                        }))
                        .then(createSkillCommand("setExp", (player, skillType, amount, playerData) -> {
                            // 设置玩家经验值
                            BasicExperienceLevel expLevel = skillType.getExperienceLevel(playerData);
                            expLevel.setExperience(amount);
                        }))
                        .then(createSkillCommand("setLevel", (player, skillType, amount, playerData) -> {
                            // 设置玩家等级
                            BasicExperienceLevel expLevel = skillType.getExperienceLevel(playerData);
                            expLevel.setLevel(amount);
                        }))));

        // 普通权限命令
        dispatcher.register(Commands.literal("skill")
                .then(Commands.literal("status").executes(context -> {
                    // 查询技能系统状态并向所有玩家发送信息
                    if (ExperienceSystemManager.INSTANCE != null) {
                        GTOCore.LOGGER.info("Experience system status: {}", ExperienceSystemManager.INSTANCE.isEnabled());
                        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
                            PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                            GTOCore.LOGGER.info("Sending status to player: {}", player.getName().getString());
                            UtilsMessage.sendPlayerExpStatusMessage(
                                    player,
                                    playerData.getExperienceLevelLists());
                        }
                    } else {
                        GTOCore.LOGGER.error("ExperienceSystemManager is still null after initialization attempt!");
                    }
                    return Command.SINGLE_SUCCESS;
                })));
    }

    /**
     * 创建技能相关命令的通用方法
     * 
     * @param commandName 命令名称
     * @param executor    命令执行器
     * @return 构建好的命令对象
     */
    private static LiteralArgumentBuilder<CommandSourceStack> createSkillCommand(
                                                                                 String commandName, SkillCommandAction executor) {
        return Commands.literal(commandName)
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("experienceType", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        // 获取所有注册的技能类型供玩家选择
                                        SkillRegistry.getAll()
                                                .stream()
                                                .map(SkillType::getId)
                                                .map(String::toLowerCase),
                                        builder) // 提示玩家输入的技能类型
                                )
                                .then(Commands.argument("amount", LongArgumentType.longArg())
                                        .executes(context -> {
                                            try {
                                                // 获取命令参数
                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                String expTypeStr = StringArgumentType.getString(context, "experienceType");
                                                long amount = LongArgumentType.getLong(context, "amount");

                                                // 获取对应的技能类型
                                                SkillType skillType = SkillRegistry.getById(expTypeStr)
                                                        .orElseThrow(() -> new IllegalArgumentException("未知的技能类型: " + expTypeStr));

                                                // 获取玩家数据并执行命令
                                                PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                                                executor.execute(player, skillType, amount, playerData);
                                                context.getSource().sendSuccess(
                                                        () -> Component.literal("success").withStyle(ChatFormatting.GREEN),
                                                        true);
                                            } catch (IllegalArgumentException e) {
                                                context.getSource().sendFailure(
                                                        Component.literal("failure").withStyle(ChatFormatting.RED));
                                                GTOCore.LOGGER.error("Skill | Failed to execute command: ", e);
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))));
    }

    /**
     * 技能命令执行器函数式接口
     * 定义了技能命令的执行逻辑
     */
    @FunctionalInterface
    private interface SkillCommandAction {

        /**
         * 执行技能命令
         * 
         * @param player     目标玩家
         * @param skillType  技能类型
         * @param amount     数值参数（经验值或等级）
         * @param playerData 玩家数据
         */
        void execute(ServerPlayer player, SkillType skillType, long amount, PlayerData playerData);
    }
}
