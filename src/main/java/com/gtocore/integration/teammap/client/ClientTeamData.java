package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.data.OreVeinRecord;
import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.data.SharedKind;
import com.gtocore.integration.teammap.network.ModNetwork;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ClientTeamData {

    private static final UUID NONE = Util.NIL_UUID;
    private static final Map<String, SharedEntry> ENTRIES = new ConcurrentHashMap<>();
    private static final Map<String, SharedEntry> GT_ENTRIES = new ConcurrentHashMap<>();
    private static final Map<net.minecraft.resources.ResourceLocation, Map<Long, SharedEntry>> TERRAIN = new ConcurrentHashMap<>();
    private static final Map<String, Long> TERRAIN_REGION_REVISIONS = new ConcurrentHashMap<>();
    private static volatile UUID serverId = NONE;
    private static volatile UUID teamId = NONE;

    public static void acceptTeam(UUID newServer, UUID newTeam) {
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
        NativeGtTeamOverlay.markDirty();
    }

    public static void accept(Collection<SharedEntry> entries) {
        boolean anyChanged = false;
        for (SharedEntry entry : entries) {
            if (entry.kind() == SharedKind.ORE_VEIN) {
                String id = entry.payload().getString("id");
                String spatialKey = SharedKind.ORE_VEIN.name() + '|' + entry.dimension() + '|' + OreVeinRecord.stableKey(id, entry.payload().getLong("center"));
                boolean legacy = entry.stableKey().equals(id);
                if (legacy && ENTRIES.containsKey(spatialKey)) continue;
                if (!legacy) {
                    String legacyKey = SharedKind.ORE_VEIN.name() + '|' + entry.dimension() + '|' + id;
                    if (ENTRIES.remove(legacyKey) != null) anyChanged = true;
                    GT_ENTRIES.remove(legacyKey);
                }
            }
            SharedEntry previous = ENTRIES.get(entry.mapKey());
            if (previous != null && (previous.revision() > entry.revision() ||
                    previous.revision() == entry.revision() && previous.contentHash() == entry.contentHash()))
                continue;
            ENTRIES.put(entry.mapKey(), entry);
            anyChanged = true;
            if (entry.kind() == SharedKind.TERRAIN) {
                TERRAIN.computeIfAbsent(entry.dimension(), ignored -> new ConcurrentHashMap<>())
                        .put(net.minecraft.world.level.ChunkPos.asLong(entry.chunkX(), entry.chunkZ()), entry);
                TERRAIN_REGION_REVISIONS.merge(regionKey(entry.dimension(),
                        Math.floorDiv(entry.chunkX(), 32), Math.floorDiv(entry.chunkZ(), 32)), 1L, Long::sum);
            } else GT_ENTRIES.put(entry.mapKey(), entry);
        }
        if (!anyChanged) return;
        ClientDiskCache.markDirty();
        NativeGtTeamOverlay.markDirty();
    }

    public static void upload(SharedEntry entry) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() != null && !teamId.equals(NONE)) ModNetwork.upload(entry, false);
    }

    public static void uploadImport(SharedEntry entry) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getConnection() != null && !teamId.equals(NONE)) ModNetwork.upload(entry, true);
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

    public static boolean hasBedrockProspection(net.minecraft.resources.ResourceLocation dimension,
                                                int chunkX, int chunkZ) {
        return gtEntry(SharedKind.BEDROCK_FLUID, dimension, chunkX, chunkZ) != null ||
                gtEntry(SharedKind.BEDROCK_ORE, dimension, chunkX, chunkZ) != null;
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
