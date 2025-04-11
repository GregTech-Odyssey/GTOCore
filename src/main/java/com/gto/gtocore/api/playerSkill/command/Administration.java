package com.gto.gtocore.api.playerSkill.command;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.playerSkill.logic.ExperienceSystemManager;
import com.gto.gtocore.api.playerSkill.logic.PlayerData;
import com.gto.gtocore.api.playerSkill.utils.utilsMessage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class Administration {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("experienceStart")
                .requires(source -> source.hasPermission(2)) // 仅OP可用
                .executes(context -> {
                    ServerLevel level = context.getSource().getLevel() instanceof ServerLevel ?
                            (ServerLevel) context.getSource().getLevel() :
                            context.getSource().getServer().getLevel(Level.OVERWORLD);

                    // 确保系统已初始化
                    if (ExperienceSystemManager.INSTANCE == null) {
                        ExperienceSystemManager.ensureInitialized(level);
                        GTOCore.LOGGER.info("ExperienceSystemManager initialized during command execution");
                    }

                    ExperienceSystemManager.INSTANCE.enableSystem();
                    GTOCore.LOGGER.info("Experience system enabled via command");

                    context.getSource().sendSuccess(
                            () -> Component.translatable("gtocore.player_exp_status.open").withStyle(ChatFormatting.GREEN), true);
                    return Command.SINGLE_SUCCESS;
                }));

        // ... 其他命令 ...

        dispatcher.register(Commands.literal("experienceStatus")
                .executes(context -> {
                    ServerLevel level = context.getSource().getLevel() instanceof ServerLevel ?
                            (ServerLevel) context.getSource().getLevel() :
                            context.getSource().getServer().getLevel(Level.OVERWORLD);

                    // 确保系统已初始化
                    if (ExperienceSystemManager.INSTANCE == null) {
                        ExperienceSystemManager.ensureInitialized(level);
                        GTOCore.LOGGER.info("ExperienceSystemManager initialized during status command");
                    }

                    if (ExperienceSystemManager.INSTANCE != null) {
                        GTOCore.LOGGER.info("Experience system status: {}", ExperienceSystemManager.INSTANCE.isEnabled());
                        for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
                            PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                            if (playerData != null) {
                                GTOCore.LOGGER.info("Sending status to player: {}", player.getName().getString());
                                utilsMessage.sendPlayerExpStatusMessage(
                                        player,
                                        playerData.getExperienceLevelLists()
                                );
                            } else {
                                GTOCore.LOGGER.warn("No player data found for: {}", player.getName().getString());
                                // 尝试添加玩家数据
                                ExperienceSystemManager.INSTANCE.addPlayer(player.getUUID());
                            }
                        }
                    } else {
                        GTOCore.LOGGER.error("ExperienceSystemManager is still null after initialization attempt!");
                    }
                    return Command.SINGLE_SUCCESS;
                }));
    }
}