package com.gtocore.integration.teammap.command;

import com.gtocore.integration.teammap.network.ModNetwork;
import com.gtocore.integration.teammap.server.TeamMapServer;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import com.mojang.brigadier.CommandDispatcher;

public final class TeamMapCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("teammapshare")
                .then(Commands.literal("status").executes(ctx -> status(ctx.getSource())))
                .then(Commands.literal("resync").executes(ctx -> resync(ctx.getSource())))
                .then(Commands.literal("import").executes(ctx -> startImport(ctx.getSource()))));
    }

    private static int status(CommandSourceStack source) {
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            return 0;
        }
        TeamMapServer service = TeamMapServer.get(player.server);
        var team = service.team(player);
        int count = team.map(value -> service.store().all(value.getId()).size()).orElse(0);
        source.sendSuccess(() -> Component.literal("GTO Team Map Share: server=" + service.serverId() + ", team=" + team.map(value -> value.getId().toString()).orElse("none") + ", entries=" + count), false);
        return count;
    }

    private static int resync(CommandSourceStack source) {
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            return 0;
        }
        ModNetwork.sync(player);
        source.sendSuccess(() -> Component.literal("Team map resync queued."), false);
        return 1;
    }

    private static int startImport(CommandSourceStack source) {
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            return 0;
        }
        ModNetwork.runImport(player);
        source.sendSuccess(() -> Component.literal("Local GT cache import queued; loaded Xaero surface regions import as they are read."), false);
        return 1;
    }
}
