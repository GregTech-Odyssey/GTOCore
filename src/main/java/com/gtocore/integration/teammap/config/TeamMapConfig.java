package com.gtocore.integration.teammap.config;

import com.gtocore.config.GTOConfig;

public final class TeamMapConfig {

    private TeamMapConfig() {}

    public static int serverMergeIntervalTicks() {
        return Math.max(1, GTOConfig.INSTANCE.teamMapShare.serverMergeIntervalSeconds * 20);
    }

    public static int clientRefreshIntervalTicks() {
        return Math.max(1, GTOConfig.INSTANCE.teamMapShare.clientRefreshIntervalSeconds * 20);
    }
}
