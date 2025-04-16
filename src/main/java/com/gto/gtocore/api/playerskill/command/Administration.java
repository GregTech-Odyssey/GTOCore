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

public class Administration {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skill")
                .then(Commands.literal("admin")
                        .requires(source -> source.hasPermission(2)) // 仅OP可用
                        .then(Commands.literal("start")
                                .executes(context -> {
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
                                    ExperienceSystemManager.INSTANCE.disableSystem();
                                    GTOCore.LOGGER.info("Experience system disabled via command");
                                    context.getSource().sendSuccess(
                                            () -> Component.translatable("gtocore.player_exp_status.close")
                                                    .withStyle(ChatFormatting.RED),
                                            true);
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(createSkillCommand("addExp", (player, skillType, amount, playerData) -> {
                            BasicExperienceLevel expLevel = skillType.getExperienceLevel(playerData);
                            UtilsData.addExperienceAndSendMessage(player, expLevel, amount);
                        }))
                        .then(createSkillCommand("setExp", (player, skillType, amount, playerData) -> {
                            BasicExperienceLevel expLevel = skillType.getExperienceLevel(playerData);
                            expLevel.setExperience(amount);
                        }))
                        .then(createSkillCommand("setLevel", (player, skillType, amount, playerData) -> {
                            BasicExperienceLevel expLevel = skillType.getExperienceLevel(playerData);
                            expLevel.setLevel(amount);
                        }))));

        // 普通权限
        dispatcher.register(Commands.literal("skill")
                .then(Commands.literal("status").executes(context -> {
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

    private static LiteralArgumentBuilder<CommandSourceStack> createSkillCommand(
                                                                                 String commandName, SkillCommandAction executor) {
        return Commands.literal(commandName)
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("experienceType", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        // Arrays.stream(SkillData.SkillType.values())
                                        // .map(Enum::name)
                                        // .map(String::toLowerCase),
                                        // builder)
                                        SkillRegistry.getAll()
                                                .stream()
                                                .map(SkillType::getId)
                                                .map(String::toLowerCase),
                                        builder) // 提示玩家输入的技能类型
                                )
                                .then(Commands.argument("amount", LongArgumentType.longArg())
                                        .executes(context -> {
                                            try {
                                                ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                String expTypeStr = StringArgumentType.getString(context, "experienceType");
                                                long amount = LongArgumentType.getLong(context, "amount");
                                                SkillType skillType = SkillRegistry.getById(expTypeStr)
                                                        .orElseThrow(() -> new IllegalArgumentException("未知的技能类型: " + expTypeStr));
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

    @FunctionalInterface
    private interface SkillCommandAction {

        void execute(ServerPlayer player, SkillType skillType, long amount, PlayerData playerData);
    }
}
