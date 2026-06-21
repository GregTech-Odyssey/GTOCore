package com.gtocore.integration.teammap;

import com.gtocore.integration.teammap.client.ClientEvents;
import com.gtocore.integration.teammap.command.TeamMapCommands;
import com.gtocore.integration.teammap.network.ModNetwork;
import com.gtocore.integration.teammap.server.ServerEvents;
import com.gtocore.integration.teammap.server.TeamMapServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;

/**
 * Lifecycle entry point for the FTB Teams, Xaero and GTCEu team-map bridge.
 *
 * <p>
 * The on-disk namespace intentionally remains {@code gto_team_map_share}
 * so worlds and client caches created by the standalone bridge keep working.
 * </p>
 */
public final class TeamMapShare {

    public static final String DATA_ID = "gto_team_map_share";
    private static boolean commonInitialized;
    private static boolean clientInitialized;

    private TeamMapShare() {}

    public static void initCommon() {
        if (commonInitialized) return;
        commonInitialized = true;
        ModNetwork.init();
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        MinecraftForge.EVENT_BUS.addListener(TeamMapShare::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(TeamMapShare::serverStopping);
    }

    public static void initClient() {
        if (clientInitialized) return;
        clientInitialized = true;
        ClientEvents.register();
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        TeamMapCommands.register(event.getDispatcher());
    }

    private static void serverStopping(ServerStoppingEvent event) {
        ModNetwork.flushServer(event.getServer());
        ModNetwork.clearServerState();
        TeamMapServer.close(event.getServer());
    }
}
