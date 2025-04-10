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
                            () -> Component.translatable("gtocore.player_exp_status.open").withStyle(ChatFormatting.GREEN), true);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(Commands.literal("experienceStop")
                .requires(source -> source.hasPermission(2)) // 仅OP可用
                .executes(context -> {
                    ExperienceSystemManager.INSTANCE.disableSystem();
                    context.getSource().sendSuccess(
                            () -> Component.translatable("gtocore.player_exp_status.close").withStyle(ChatFormatting.RED), true);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(Commands.literal("experienceStatus")
                .executes(context -> {
                    for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
                        PlayerData playerData = ExperienceSystemManager.INSTANCE.getPlayerData(player.getUUID());
                        if (playerData != null) {
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