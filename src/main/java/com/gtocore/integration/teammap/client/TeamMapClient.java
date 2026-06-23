package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.network.ModNetwork;

public final class TeamMapClient {

    private TeamMapClient() {}

    public static void login() {
        ModNetwork.requestSync();
    }

    public static void logout() {
        ClientTeamData.clear();
    }

    public static void tick() {
        ClientUpdateScheduler.tick();
        TerrainCapture.flush();
        NativeGtTeamOverlay.tick();
        ClientHistoryImporter.tick();
        ClientDiskCache.tick(ClientTeamData.serverId(), ClientTeamData.teamId(), ClientTeamData.entries());
    }
}
