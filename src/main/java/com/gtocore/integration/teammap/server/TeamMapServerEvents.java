package com.gtocore.integration.teammap.server;

import com.gtocore.integration.teammap.network.ModNetwork;

import net.minecraft.server.MinecraftServer;

import dev.ftb.mods.ftbteams.api.event.PlayerChangedTeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;

public final class TeamMapServerEvents {

    private static int flushTimer;
    private static boolean registered;

    private TeamMapServerEvents() {}

    public static void register() {
        if (registered) return;
        registered = true;
        TeamEvent.PLAYER_CHANGED.register(TeamMapServerEvents::changedTeam);
    }

    private static void changedTeam(PlayerChangedTeamEvent event) {
        if (event.getPlayer() != null) ModNetwork.sync(event.getPlayer());
    }

    public static void tick(MinecraftServer server) {
        ModNetwork.tickServer(server);
        if (++flushTimer >= 20) {
            flushTimer = 0;
            TeamMapServer.get(server).store().flushSome(1);
        }
    }

    public static void stop(MinecraftServer server) {
        ModNetwork.flushServer(server);
        ModNetwork.clearServerState();
        TeamMapServer.close(server);
        flushTimer = 0;
    }
}
