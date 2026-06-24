package com.gtocore.integration.teammap.server;

import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.data.SharedKind;

import com.gtolib.GTOCore;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public final class TeamRegionStore {

    private final Path root;
    private final Map<BucketKey, Bucket> buckets = new HashMap<>();
    private final Set<UUID> loadedTeams = new HashSet<>();
    private final AtomicLong revision = new AtomicLong();

    public TeamRegionStore(Path root) {
        this.root = root;
    }

    public synchronized SharedEntry put(UUID teamId, SharedEntry incoming) {
        return put(teamId, incoming, true);
    }

    public synchronized SharedEntry putIfAbsent(UUID teamId, SharedEntry incoming) {
        return put(teamId, incoming, false);
    }

    private SharedEntry put(UUID teamId, SharedEntry incoming, boolean replaceExisting) {
        ensureTeamLoaded(teamId);
        Bucket bucket = buckets.computeIfAbsent(BucketKey.of(teamId, incoming), ignored -> new Bucket());
        SharedEntry previous = bucket.entries.get(incoming.mapKey());
        if (previous != null && (!replaceExisting || previous.contentHash() == incoming.contentHash())) return previous;
        if (incoming.kind() == SharedKind.ORE_VEIN && !incoming.stableKey().equals(incoming.payload().getString("id"))) {
            String legacyKey = SharedKind.ORE_VEIN.name() + '|' + incoming.dimension() + '|' + incoming.payload().getString("id");
            SharedEntry legacy = bucket.entries.get(legacyKey);
            if (legacy != null) {
                if (!replaceExisting) return legacy;
                bucket.entries.remove(legacyKey);
                bucket.dirty = true;
            }
        }
        SharedEntry accepted = incoming.withRevision(revision.incrementAndGet());
        bucket.entries.put(accepted.mapKey(), accepted);
        bucket.dirty = true;
        return accepted;
    }

    public synchronized List<SharedEntry> all(UUID teamId) {
        ensureTeamLoaded(teamId);
        ArrayList<SharedEntry> result = new ArrayList<>();
        buckets.forEach((key, value) -> { if (key.teamId.equals(teamId)) result.addAll(value.entries.values()); });
        result.sort(Comparator.comparingLong(SharedEntry::revision));
        return result;
    }

    public synchronized void flush() {
        buckets.forEach((key, bucket) -> {
            if (!bucket.dirty) return;
            try {
                writeBucket(key, bucket);
                bucket.dirty = false;
            } catch (IOException e) {
                GTOCore.LOGGER.error("Could not save team map bucket {}", key, e);
            }
        });
    }

    public synchronized void flushSome(int limit) {
        int written = 0;
        for (var entry : buckets.entrySet()) {
            if (!entry.getValue().dirty) continue;
            try {
                writeBucket(entry.getKey(), entry.getValue());
                entry.getValue().dirty = false;
            } catch (IOException e) {
                GTOCore.LOGGER.error("Could not save team map bucket {}", entry.getKey(), e);
            }
            if (++written >= limit) return;
        }
    }

    private void ensureTeamLoaded(UUID teamId) {
        if (!loadedTeams.add(teamId)) return;
        Path teamRoot = root.resolve(teamId.toString());
        if (!Files.isDirectory(teamRoot)) return;
        try (var files = Files.walk(teamRoot)) {
            files.filter(path -> path.getFileName().toString().endsWith(".dat")).forEach(path -> readBucket(teamId, path));
        } catch (IOException e) {
            GTOCore.LOGGER.error("Could not load team map data for {}", teamId, e);
        }
    }

    private void readBucket(UUID teamId, Path path) {
        try (InputStream input = Files.newInputStream(path)) {
            CompoundTag tag = NbtIo.readCompressed(input);
            String dimension = tag.getString("dimension");
            int rx = tag.getInt("region_x");
            int rz = tag.getInt("region_z");
            Bucket bucket = buckets.computeIfAbsent(new BucketKey(teamId, dimension, rx, rz), ignored -> new Bucket());
            ListTag list = tag.getList("entries", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                SharedEntry entry = SharedEntry.fromTag(list.getCompound(i));
                bucket.entries.merge(entry.mapKey(), entry,
                        (oldValue, newValue) -> newValue.revision() > oldValue.revision() ? newValue : oldValue);
                revision.accumulateAndGet(entry.revision(), Math::max);
            }
        } catch (Exception e) {
            GTOCore.LOGGER.error("Ignoring corrupt team map bucket {}", path, e);
        }
    }

    private void writeBucket(BucketKey key, Bucket bucket) throws IOException {
        Path folder = dimensionFolder(root.resolve(key.teamId.toString()), key.dimension);
        Files.createDirectories(folder);
        Path target = folder.resolve(key.regionX + "_" + key.regionZ + ".dat");
        Path temp = target.resolveSibling(target.getFileName() + ".tmp");
        CompoundTag tag = new CompoundTag();
        tag.putString("dimension", key.dimension);
        tag.putInt("region_x", key.regionX);
        tag.putInt("region_z", key.regionZ);
        ListTag list = new ListTag();
        bucket.entries.values().forEach(entry -> list.add(entry.toTag()));
        tag.put("entries", list);
        try (OutputStream output = Files.newOutputStream(temp)) {
            NbtIo.writeCompressed(tag, output);
        }
        Util.safeReplaceFile(target, temp, target.resolveSibling(target.getFileName() + ".old"));
        if (Files.exists(temp)) throw new IOException("Could not replace " + target);
    }

    private static Path dimensionFolder(Path teamRoot, String dimension) {
        String encoded = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(dimension.getBytes(StandardCharsets.UTF_8));
        Path folder = teamRoot.resolve("dimensions");
        for (int start = 0; start < encoded.length(); start += 120) {
            folder = folder.resolve(encoded.substring(start, Math.min(start + 120, encoded.length())));
        }
        return folder;
    }

    private record BucketKey(UUID teamId, String dimension, int regionX, int regionZ) {

        static BucketKey of(UUID team, SharedEntry entry) {
            return new BucketKey(team, entry.dimension().toString(), Math.floorDiv(entry.chunkX(), 32), Math.floorDiv(entry.chunkZ(), 32));
        }
    }

    private static final class Bucket {

        final Map<String, SharedEntry> entries = new HashMap<>();
        boolean dirty;
    }
}
