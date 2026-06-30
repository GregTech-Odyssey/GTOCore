package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.data.TerrainChunkRecord;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import xaero.map.region.MapBlock;
import xaero.map.region.MapRegion;
import xaero.map.region.MapTile;
import xaero.map.region.MapTileChunk;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/** Captures Xaero's completed 4x4-tile texture buffer, not its intermediate pixels. */
public final class TerrainCapture {

    private static final ArrayDeque<TerrainUpload> UPLOADS = new ArrayDeque<>();
    private static final Map<String, Long> LAST_HASHES = new HashMap<>();
    private static final Map<MapRegion, Deque<ImportMarker>> IMPORTING_REGIONS = new IdentityHashMap<>();
    private static long importGeneration;
    private static long nextImportToken;

    public static synchronized ImportMarker beginImport(MapRegion region) {
        ImportMarker marker = new ImportMarker(++nextImportToken, importGeneration);
        IMPORTING_REGIONS.computeIfAbsent(region, ignored -> new ArrayDeque<>()).addLast(marker);
        return marker;
    }

    public static synchronized void endImport(MapRegion region) {
        Deque<ImportMarker> markers = IMPORTING_REGIONS.get(region);
        if (markers == null) return;
        markers.pollFirst();
        if (markers.isEmpty()) IMPORTING_REGIONS.remove(region);
    }

    public static synchronized void cancelImport(MapRegion region, ImportMarker marker) {
        Deque<ImportMarker> markers = IMPORTING_REGIONS.get(region);
        if (markers == null) return;
        markers.remove(marker);
        if (markers.isEmpty()) IMPORTING_REGIONS.remove(region);
    }

    public static synchronized void queueChunk(MapTileChunk tileChunk) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || !ClientTeamData.hasTeam()) return;
        ByteBuffer colors = tileChunk.getLeafTexture().getDirectColorBuffer();
        if (colors == null || colors.capacity() < 64 * 64 * 4) return;
        ResourceLocation dimension = tileChunk.getInRegion().getDim().getDimId().location();
        Deque<ImportMarker> markers = IMPORTING_REGIONS.get(tileChunk.getInRegion());
        ImportMarker currentImport = markers == null ? null : markers.peekFirst();
        if (currentImport != null && currentImport.generation() != importGeneration) return;
        boolean importOnly = currentImport != null;

        for (int tileZ = 0; tileZ < 4; tileZ++) for (int tileX = 0; tileX < 4; tileX++) {
            MapTile tile = tileChunk.getTile(tileX, tileZ);
            if (tile == null || !tile.wasWrittenOnce() || tile.getWrittenCaveStart() != Integer.MAX_VALUE) continue;
            SharedEntry entry = capture(tile, colors, tileX, tileZ, dimension);
            String key = entry.dimension() + ":" + entry.chunkX() + ":" + entry.chunkZ();
            Long previous = LAST_HASHES.put(key, entry.contentHash());
            if (previous == null || previous.longValue() != entry.contentHash())
                UPLOADS.add(new TerrainUpload(entry, importOnly));
        }
    }

    public static synchronized void flush() {
        for (int count = 0; count < 2 && !UPLOADS.isEmpty(); count++) {
            TerrainUpload upload = UPLOADS.removeFirst();
            if (upload.importOnly()) ClientTeamData.uploadImport(upload.entry());
            else ClientTeamData.upload(upload.entry());
        }
    }

    public static synchronized void reset() {
        UPLOADS.clear();
        LAST_HASHES.clear();
        // In-flight Xaero imports can still call back after team changes; keep
        // their markers so stale callbacks are discarded instead of uploaded.
        importGeneration++;
    }

    private static SharedEntry capture(MapTile tile, ByteBuffer nativeBuffer, int tileX, int tileZ,
                                       ResourceLocation dimension) {
        CompoundTag surface = new CompoundTag();
        int[] xaeroColors = new int[256];
        for (int z = 0; z < 16; z++) for (int x = 0; x < 16; x++) {
            MapBlock block = tile.getBlock(x, z);
            if (block != null && block.getState() != null) {
                // Keep Xaero's exact B,G,R,light packed value. The fourth channel is
                // shader input, not alpha or a colour multiplier.
                int px = tileX * 16 + x;
                int pz = tileZ * 16 + z;
                xaeroColors[z * 16 + x] = nativeBuffer.getInt((pz * 64 + px) * 4);
            }
        }
        surface.putIntArray("xaero_colors", xaeroColors);
        long hash = 0xcbf29ce484222325L;
        for (int color : xaeroColors) {
            hash = (hash ^ color) * 0x100000001b3L;
        }
        return new TerrainChunkRecord(dimension, tile.getChunkX(), tile.getChunkZ(),
                surface, hash, 0).asEntry();
    }

    private record TerrainUpload(SharedEntry entry, boolean importOnly) {}

    public record ImportMarker(long token, long generation) {}
}
