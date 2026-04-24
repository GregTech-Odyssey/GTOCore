package com.gtocore.integration.ftbquests;

import com.gtolib.GTOCore;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public final class MEQuestDetectorRuntime {

    private static final Map<UUID, Set<IMEQuestDetectorRuntimeTarget>> TEAM_TARGETS = new HashMap<>();
    private static final ThreadLocal<Integer> DIRTY_NOTIFICATION_SUPPRESSION = ThreadLocal.withInitial(() -> 0);

    private MEQuestDetectorRuntime() {}

    public static synchronized void bind(UUID teamId, IMEQuestDetectorRuntimeTarget target) {
        TEAM_TARGETS.computeIfAbsent(teamId, ignored -> java.util.Collections.newSetFromMap(new IdentityHashMap<>()))
                .add(target);
        GTOCore.LOGGER.info("[ME Quest Detector] Bound runtime target {} to team {}", target, teamId);
    }

    public static synchronized void unbind(@Nullable UUID teamId, IMEQuestDetectorRuntimeTarget target) {
        if (teamId == null) {
            return;
        }

        var targets = TEAM_TARGETS.get(teamId);
        if (targets == null) {
            return;
        }

        if (targets.remove(target)) {
            GTOCore.LOGGER.info("[ME Quest Detector] Unbound runtime target {} from team {}", target, teamId);
        }

        if (targets.isEmpty()) {
            TEAM_TARGETS.remove(teamId);
        }
    }

    public static synchronized void clearAll(String reason) {
        if (!TEAM_TARGETS.isEmpty()) {
            GTOCore.LOGGER.info("[ME Quest Detector] Clearing {} bound detector targets: {}", TEAM_TARGETS.size(), reason);
        }
        TEAM_TARGETS.clear();
    }

    public static void notifyTeamDirty(UUID teamId, String reason) {
        List<IMEQuestDetectorRuntimeTarget> snapshot;
        synchronized (MEQuestDetectorRuntime.class) {
            var targets = TEAM_TARGETS.get(teamId);
            if (targets == null || targets.isEmpty()) {
                return;
            }
            snapshot = new ArrayList<>(targets);
        }

        GTOCore.LOGGER.info("[ME Quest Detector] Team {} marked dirty, notifying {} detectors: {}", teamId, snapshot.size(), reason);
        for (var target : snapshot) {
            if (target.isDetectorRuntimeActive()) {
                target.onDetectorContextChanged(reason);
            }
        }
    }

    public static void notifyQuestIndexInvalidated(String reason) {
        List<IMEQuestDetectorRuntimeTarget> snapshot = new ArrayList<>();
        synchronized (MEQuestDetectorRuntime.class) {
            TEAM_TARGETS.values().forEach(snapshot::addAll);
        }

        if (snapshot.isEmpty()) {
            return;
        }

        GTOCore.LOGGER.info("[ME Quest Detector] Quest index invalidated, notifying {} detectors: {}", snapshot.size(), reason);
        for (var target : snapshot) {
            if (target.isDetectorRuntimeActive()) {
                target.onDetectorContextChanged("quest-index-invalidated:" + reason);
            }
        }
    }

    public static boolean isDirtyNotificationSuppressed() {
        return DIRTY_NOTIFICATION_SUPPRESSION.get() > 0;
    }

    public static void runWithoutDirtyNotification(Runnable runnable) {
        callWithoutDirtyNotification(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T> T callWithoutDirtyNotification(Supplier<T> supplier) {
        DIRTY_NOTIFICATION_SUPPRESSION.set(DIRTY_NOTIFICATION_SUPPRESSION.get() + 1);
        try {
            return supplier.get();
        } finally {
            DIRTY_NOTIFICATION_SUPPRESSION.set(Math.max(0, DIRTY_NOTIFICATION_SUPPRESSION.get() - 1));
        }
    }
}
