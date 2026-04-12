package com.gtocore.integration.ftbquests;

import com.gtolib.GTOCore;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.ftb.mods.ftbquests.events.ClearFileCacheEvent;

public final class MEQuestDetectorBootstrap {

    private static boolean initialized;

    private MEQuestDetectorBootstrap() {}

    public static void init() {
        if (initialized) {
            return;
        }

        initialized = true;

        ClearFileCacheEvent.EVENT.register(file -> {
            if (!file.isServerSide()) {
                return;
            }

            GTOCore.LOGGER.info("[ME Quest Detector] Received FTB Quests cache clear event");
            MEQuestDetectorTaskIndex.invalidate("ftb-quests-clear-file-cache");
            MEQuestDetectorRuntime.notifyQuestIndexInvalidated("ftb-quests-clear-file-cache");
        });

        LifecycleEvent.SERVER_STOPPING.register(server -> {
            GTOCore.LOGGER.info("[ME Quest Detector] Server stopping, clearing detector runtime state");
            MEQuestDetectorTaskIndex.invalidate("server-stopping");
            MEQuestDetectorRuntime.clearAll("server-stopping");
        });
    }
}
