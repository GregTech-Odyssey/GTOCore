package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.config.TeamMapConfig;
import com.gtocore.integration.teammap.data.SharedEntry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ClientUpdateScheduler {

    private static final Map<String, SharedEntry> PENDING = new LinkedHashMap<>();
    private static int timer;

    public static void enqueue(Collection<SharedEntry> entries) {
        for (SharedEntry entry : entries) PENDING.merge(entry.mapKey(), entry,
                (oldValue, newValue) -> newValue.revision() >= oldValue.revision() ? newValue : oldValue);
    }

    public static void tick() {
        if (PENDING.isEmpty()) {
            timer = 0;
            return;
        }
        if (++timer < TeamMapConfig.clientRefreshIntervalTicks()) return;
        timer = 0;
        var updates = java.util.List.copyOf(PENDING.values());
        PENDING.clear();
        ClientTeamData.accept(updates);
    }

    public static void clear() {
        PENDING.clear();
        timer = 0;
    }
}
