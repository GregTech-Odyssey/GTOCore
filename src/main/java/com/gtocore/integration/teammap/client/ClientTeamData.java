package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.data.OreVeinRecord;
import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.data.SharedKind;
import com.gtocore.integration.teammap.network.ModNetwork;

import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientTeamData {

    private static final UUID NONE = new UUID(0, 0);
    private static final Map<String, SharedEntry> ENTRIES = new ConcurrentHashMap<>();
    private static final Map<String, SharedEntry> GT_ENTRIES = new ConcurrentHashMap<>();
    private static final Map<net.minecraft.resources.ResourceLocation, Map<Long, SharedEntry>> TERRAIN = new ConcurrentHashMap<>();
    private static final Map<String, Long> TERRAIN_REGION_REVISIONS = new ConcurrentHashMap<>();
    private static volatile UUID serverId = NONE;
    private static volatile UUID teamId = NONE;
    private static volatile long serverRevision;
    private static final ThreadLocal<Boolean> REMOTE_APPLY = ThreadLocal.withInitial(() -> false);

    public static void acceptTeam(UUID newServer, UUID newTeam, long revision) {
        if (!serverId.equals(newServer) || !teamId.equals(newTeam)) {
            ClientUpdateScheduler.clear();
            ClientDiskCache.save(serverId, teamId, ENTRIES.values());
            ENTRIES.clear();
            ENTRIES.putAll(ClientDiskCache.load(newServer, newTeam));
            rebuildIndexes();
            TERRAIN_REGION_REVISIONS.clear();
            TeamRegionTextureCache.clear();
            TerrainCapture.reset();
            ClientHistoryImporter.reset();
            if (!newTeam.equals(NONE)) ClientHistoryImporter.scheduleFull(100);
        }
        serverId = newServer;
        teamId = newTeam;
        serverRevision = revision;
        NativeGtTeamOverlay.markDirty();
    }

    public static void accept(Collection<SharedEntry> entries) {
        entries.forEach(entry -> {
            if (entry.kind() == SharedKind.ORE_VEIN) {
                String id = entry.payload().getString("id");
                String spatialKey = SharedKind.ORE_VEIN.name() + '|' + entry.dimension() + '|' + OreVeinRecord.stableKey(id, entry.payload().getLong("center"));
                boolean legacy = entry.stableKey().equals(id);
                if (legacy && ENTRIES.containsKey(spatialKey)) return;
                if (!legacy) {
                    String legacyKey = SharedKind.ORE_VEIN.name() + '|' + entry.dimension() + '|' + id;
                    ENTRIES.remove(legacyKey);
                    GT_ENTRIES.remove(legacyKey);
                }
            }
            boolean[] changed = { false };
            SharedEntry accepted = ENTRIES.compute(entry.mapKey(), (ignored, oldValue) -> {
                if (oldValue != null && (oldValue.revision() > entry.revision() || oldValue.revision() == entry.revision() && oldValue.contentHash() == entry.contentHash()))
                    return oldValue;
                changed[0] = true;
                return entry;
            });
            if (!changed[0]) return;
            if (accepted.kind() == SharedKind.TERRAIN) {
                TERRAIN.computeIfAbsent(accepted.dimension(), ignored -> new ConcurrentHashMap<>())
                        .put(net.minecraft.world.level.ChunkPos.asLong(accepted.chunkX(), accepted.chunkZ()), accepted);
                TERRAIN_REGION_REVISIONS.merge(regionKey(accepted.dimension(),
                        Math.floorDiv(accepted.chunkX(), 32), Math.floorDiv(accepted.chunkZ(), 32)), 1L, Long::sum);
            } else GT_ENTRIES.put(accepted.mapKey(), accepted);
        });
        ClientDiskCache.markDirty();
        NativeGtTeamOverlay.markDirty();
    }

    public static void upload(SharedEntry entry) {
        Minecraft mc = Minecraft.getInstance();
        if (!REMOTE_APPLY.get() && mc.getConnection() != null && !teamId.equals(NONE)) ModNetwork.upload(entry);
    }

    public static Collection<SharedEntry> entries() {
        return List.copyOf(ENTRIES.values());
    }

    public static Collection<SharedEntry> gtEntries() {
        return List.copyOf(GT_ENTRIES.values());
    }

    public static SharedEntry gtEntry(SharedKind kind, net.minecraft.resources.ResourceLocation dimension,
                                      int chunkX, int chunkZ) {
        return GT_ENTRIES.get(kind.name() + '|' + dimension + '|' + chunkX + ',' + chunkZ);
    }

    public static SharedEntry terrain(net.minecraft.resources.ResourceLocation dimension, int chunkX, int chunkZ) {
        Map<Long, SharedEntry> dimensionTerrain = TERRAIN.get(dimension);
        return dimensionTerrain == null ? null : dimensionTerrain.get(net.minecraft.world.level.ChunkPos.asLong(chunkX, chunkZ));
    }

    public static long terrainRegionRevision(net.minecraft.resources.ResourceLocation dimension, int regionX, int regionZ) {
        return TERRAIN_REGION_REVISIONS.getOrDefault(regionKey(dimension, regionX, regionZ), 0L);
    }

    public static UUID serverId() {
        return serverId;
    }

    public static UUID teamId() {
        return teamId;
    }

    public static long serverRevision() {
        return serverRevision;
    }

    public static boolean hasTeam() {
        return !teamId.equals(NONE);
    }

    public static void clear() {
        ClientDiskCache.save(serverId, teamId, ENTRIES.values());
        ENTRIES.clear();
        GT_ENTRIES.clear();
        TERRAIN.clear();
        TERRAIN_REGION_REVISIONS.clear();
        teamId = NONE;
        serverRevision = 0;
        TeamRegionTextureCache.clear();
        ClientUpdateScheduler.clear();
        TerrainCapture.reset();
        ClientHistoryImporter.reset();
        NativeGtTeamOverlay.markDirty();
    }

    private static void rebuildIndexes() {
        TERRAIN.clear();
        GT_ENTRIES.clear();
        ENTRIES.values().stream().filter(entry -> entry.kind() == SharedKind.TERRAIN).forEach(entry -> TERRAIN
                .computeIfAbsent(entry.dimension(), ignored -> new ConcurrentHashMap<>())
                .put(net.minecraft.world.level.ChunkPos.asLong(entry.chunkX(), entry.chunkZ()), entry));
        ENTRIES.values().stream().filter(entry -> entry.kind() != SharedKind.TERRAIN)
                .forEach(entry -> GT_ENTRIES.put(entry.mapKey(), entry));
    }

    private static String regionKey(net.minecraft.resources.ResourceLocation dimension, int regionX, int regionZ) {
        return dimension + ":" + regionX + ":" + regionZ;
    }
}
