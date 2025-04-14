package com.gto.gtocore.api.playerskill.command;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.playerskill.SkillData;
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
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import java.util.Arrays;

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
                        .then(Commands.literal("add")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("experienceType", StringArgumentType.word())
                                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                        Arrays.stream(SkillData.SkillType.values())
                                                                .map(Enum::name)
                                                                .map(String::toLowerCase),
                                                        builder))
                                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                        .executes(context -> {
                                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                            String expTypeStr = StringArgumentType.getString(context, "experienceType").toUpperCase();
                                                            int amount = IntegerArgumentType.getInteger(context, "amount");

                                                            try {
                                                                SkillData.SkillType skillType = SkillData.SkillType.valueOf(expTypeStr);
                                                                PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                                                                BasicExperienceLevel expLevel = skillType.getExperienceLevel(playerData);
                                                                UtilsData.addExperienceAndSendMessage(player, expLevel, amount);

                                                                context.getSource().sendSuccess(
                                                                        () -> Component.literal("success")
                                                                                .withStyle(ChatFormatting.GREEN),
                                                                        true);
                                                            } catch (IllegalArgumentException e) {
                                                                // 无效的技能类型
                                                                context.getSource().sendFailure(
                                                                        Component.literal("failure")
                                                                                .withStyle(ChatFormatting.RED));
                                                            }

                                                            return Command.SINGLE_SUCCESS;
                                                        })))))));

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
}
