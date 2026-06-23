package com.gtocore.integration.teammap;

import com.gtocore.integration.teammap.network.ModNetwork;
import com.gtocore.integration.teammap.server.TeamMapServerEvents;

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

    private TeamMapShare() {}

    public static void initCommon() {
        if (commonInitialized) return;
        commonInitialized = true;
        ModNetwork.init();
        TeamMapServerEvents.register();
    }
}
