package com.gto.gtocore.api.playerskill.command;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.playerskill.data.ExperienceSystemManager;
import com.gto.gtocore.api.playerskill.data.PlayerData;
import com.gto.gtocore.api.playerskill.utils.UtilsMessage;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

public class Administration {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("experienceStart")
                .requires(source -> source.hasPermission(2)) // 仅OP可用
                .executes(context -> {
                    ExperienceSystemManager.INSTANCE.enableSystem();
                    GTOCore.LOGGER.info("Experience system enabled via command");

                    context.getSource().sendSuccess(
                            () -> Component.translatable("gtocore.player_exp_status.open").withStyle(ChatFormatting.GREEN), true);
                    return Command.SINGLE_SUCCESS;
                }));

        // ... 其他命令 ...

        dispatcher.register(Commands.literal("experienceStatus")
                .executes(context -> {
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
                }));
    }
}
