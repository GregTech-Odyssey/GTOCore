package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.data.SharedEntry;

import com.gregtechceu.gtceu.integration.map.xaeros.XaerosRenderer;

import xaero.map.MapProcessor;
import xaero.map.WorldMapSession;
import xaero.map.region.MapRegion;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ClientHistoryImporter {

    private static int countdown = -1;
    private static int gtCountdown = -1;
    private static int periodicGtTicks;
    private static final ArrayDeque<int[]> XAERO_REGIONS = new ArrayDeque<>();
    private static final ArrayDeque<SharedEntry> GT_ENTRIES = new ArrayDeque<>();
    private static final Map<String, Long> GT_LAST_HASHES = new HashMap<>();
    private static final Pattern REGION_FILE = Pattern.compile("(-?\\d+)_(-?\\d+)\\.zip");

    public static void scheduleFull(int ticks) {
        countdown = Math.max(1, ticks);
    }

    public static void scheduleGt(int ticks) {
        gtCountdown = Math.max(1, ticks);
    }

    public static void reset() {
        countdown = -1;
        gtCountdown = -1;
        periodicGtTicks = 0;
        XAERO_REGIONS.clear();
        GT_ENTRIES.clear();
        GT_LAST_HASHES.clear();
    }

    public static void tick() {
        if (countdown > 0 && --countdown == 0) {
            importGt();
            queueCurrentXaeroDimension();
        }
        if (gtCountdown > 0 && --gtCountdown == 0) importGt();
        if (ClientTeamData.hasTeam() && ++periodicGtTicks >= 600) {
            periodicGtTicks = 0;
            importGt();
        }
        for (int count = 0; count < 8 && !GT_ENTRIES.isEmpty(); count++)
            ClientTeamData.uploadImport(GT_ENTRIES.removeFirst());
        importOneXaeroRegion();
    }

    public static int importGt() {
        try {
            // Import from GTCEu's public Xaero renderer tables. These already
            // contain the loaded personal cache and avoid private cache fields,
            // reflection, accessors and the nested-jar class-loader problem.
            // The same tables also contain our injected team elements, which
            // must never be re-uploaded as authoritative personal history.
            XaerosRenderer.oreElements.getMap().forEach((dimension, values) -> values.forEach((id, element) -> {
                if (!NativeGtTeamOverlay.isTeamOre(dimension, id, element))
                    queueGt(GTRecordAdapter.ore(dimension, element.getVein()));
            }));
            XaerosRenderer.fluidElements.getMap().forEach((dimension, values) -> values.forEach((pos, fluid) -> {
                if (!NativeGtTeamOverlay.isTeamFluid(dimension, pos, fluid))
                    queueGt(GTRecordAdapter.fluid(dimension, pos.x, pos.z, fluid));
            }));
            XaerosRenderer.bedrockOreElements.getMap().forEach((dimension, values) -> values.forEach((pos, ores) -> {
                if (!NativeGtTeamOverlay.isTeamBedrockOre(dimension, pos, ores))
                    queueGt(GTRecordAdapter.bedrockOre(dimension, pos.x, pos.z, ores));
            }));
        } catch (RuntimeException ignored) {
            // A renderer table can be changing while GTCEu reloads. The periodic
            // scan retries without discarding entries already queued.
        }
        return GT_ENTRIES.size();
    }

    private static void queueGt(SharedEntry entry) {
        Long previous = GT_LAST_HASHES.put(entry.mapKey(), entry.contentHash());
        if (previous == null || previous.longValue() != entry.contentHash()) GT_ENTRIES.add(entry);
    }

    private static void queueCurrentXaeroDimension() {
        try {
            WorldMapSession session = WorldMapSession.getCurrentSession();
            if (session == null || !session.isUsable()) return;
            MapProcessor processor = session.getMapProcessor();
            Path folder = processor.getMapSaveLoad().getMWSubFolder(processor.getCurrentWorldId(),
                    processor.getCurrentDimId(), processor.getCurrentMWId());
            if (!Files.isDirectory(folder)) return;
            try (var files = Files.list(folder)) {
                files.map(Path::getFileName).map(Path::toString).forEach(name -> {
                    Matcher matcher = REGION_FILE.matcher(name);
                    if (matcher.matches()) XAERO_REGIONS.add(new int[] { Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) });
                });
            }
        } catch (Exception ignored) {
            // Xaero can still be initializing when the automatic import fires; /teammapshare import retries it.
        }
    }

    private static void importOneXaeroRegion() {
        if (XAERO_REGIONS.isEmpty()) return;
        try {
            WorldMapSession session = WorldMapSession.getCurrentSession();
            if (session == null || !session.isUsable()) return;
            MapProcessor processor = session.getMapProcessor();
            if (processor.getMapSaveLoad().getSizeOfToLoad() > 2) return;
            int[] pos = XAERO_REGIONS.removeFirst();
            MapRegion region = processor.getLeafMapRegion(0, pos[0], pos[1], true);
            if (region != null) processor.getMapSaveLoad().requestLoad(region, "gto_team_map_import", false);
        } catch (Exception ignored) {
            // A bad legacy region must not stop the rest of the import queue.
        }
    }
}
