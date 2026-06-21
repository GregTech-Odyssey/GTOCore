package com.gtocore.integration.teammap.server;

import com.gtocore.integration.teammap.network.ModNetwork;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import dev.ftb.mods.ftbteams.api.event.PlayerChangedTeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;

public final class ServerEvents {

    private int flushTimer;

    public ServerEvents() {
        TeamEvent.PLAYER_LOGGED_IN.register(event -> ModNetwork.sync(event.getPlayer()));
        TeamEvent.PLAYER_CHANGED.register(this::changedTeam);
    }

    public void changedTeam(PlayerChangedTeamEvent event) {
        if (event.getPlayer() != null) ModNetwork.sync(event.getPlayer());
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        ModNetwork.tickServer(event.getServer());
        if (++flushTimer >= 20) {
            flushTimer = 0;
            TeamMapServer.get(event.getServer()).store().flushSome(1);
        }
    }
}
