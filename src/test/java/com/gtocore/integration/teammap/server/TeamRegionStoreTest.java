package com.gtocore.integration.teammap.server;

import com.gtocore.integration.teammap.data.OreVeinRecord;
import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.data.SharedKind;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TeamRegionStoreTest {
    @TempDir Path directory;

    @Test
    void deduplicatesAndPersistsRegionBuckets() throws Exception {
        UUID team = UUID.randomUUID();
        CompoundTag payload = new CompoundTag();
        payload.putString("definition", "gtceu:magnetite");
        SharedEntry entry = new SharedEntry(SharedKind.ORE_VEIN,
                ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"), -33, 65, "vein-1",
                SharedEntry.hash(payload), 0, payload);

        TeamRegionStore first = new TeamRegionStore(directory);
        SharedEntry accepted = first.put(team, entry);
        SharedEntry duplicate = first.put(team, entry);
        assertEquals(accepted.revision(), duplicate.revision());

        CompoundTag stalePayload = new CompoundTag();
        stalePayload.putString("definition", "gtceu:stale");
        SharedEntry staleImport = new SharedEntry(SharedKind.ORE_VEIN, entry.dimension(),
                entry.chunkX(), entry.chunkZ(), entry.stableKey(), SharedEntry.hash(stalePayload), 0, stalePayload);
        SharedEntry importResult = first.putIfAbsent(team, staleImport);
        assertEquals(accepted, importResult);
        assertEquals(1, first.all(team).size());
        first.flush();

        Path saved;
        try (var files = Files.walk(directory.resolve(team.toString()))) {
            saved = files.filter(path -> path.getFileName().toString().endsWith(".dat"))
                    .findFirst().orElseThrow();
        }
        Path legacy = directory.resolve(team.toString()).resolve("minecraft_overworld").resolve("-2_2.dat");
        Files.createDirectories(legacy.getParent());
        Files.move(saved, legacy);

        TeamRegionStore reloaded = new TeamRegionStore(directory);
        assertEquals(1, reloaded.all(team).size());
        assertEquals(accepted, reloaded.all(team).get(0));
        assertTrue(legacy.toFile().isFile());
    }

    @Test
    void importDoesNotReplaceLegacyOreKey() {
        UUID team = UUID.randomUUID();
        ResourceLocation dimension = ResourceLocation.fromNamespaceAndPath("minecraft", "overworld");
        CompoundTag legacyPayload = new CompoundTag();
        legacyPayload.putString("id", "gtceu:test_vein");
        legacyPayload.putLong("center", 42L);
        SharedEntry legacy = new SharedEntry(SharedKind.ORE_VEIN, dimension, 1, 2,
                "gtceu:test_vein", SharedEntry.hash(legacyPayload), 0, legacyPayload);

        TeamRegionStore store = new TeamRegionStore(directory.resolve("legacy"));
        SharedEntry acceptedLegacy = store.put(team, legacy);

        CompoundTag importedPayload = legacyPayload.copy();
        importedPayload.putBoolean("depleted", true);
        SharedEntry imported = new SharedEntry(SharedKind.ORE_VEIN, dimension, 1, 2,
                OreVeinRecord.stableKey("gtceu:test_vein", 42L), SharedEntry.hash(importedPayload), 0,
                importedPayload);

        assertEquals(acceptedLegacy, store.putIfAbsent(team, imported));
        assertEquals(List.of(acceptedLegacy), store.all(team));

        SharedEntry acceptedLiveUpdate = store.put(team, imported);
        assertEquals(imported.stableKey(), acceptedLiveUpdate.stableKey());
        assertEquals(List.of(acceptedLiveUpdate), store.all(team));
    }

    @Test
    void dimensionFoldersDoNotCollide() throws Exception {
        UUID team = UUID.randomUUID();
        CompoundTag payload = new CompoundTag();
        payload.putString("fluid", "gtceu:oil");
        TeamRegionStore store = new TeamRegionStore(directory.resolve("dimensions"));
        ResourceLocation firstDimension = ResourceLocation.fromNamespaceAndPath("foo", "bar_baz");
        ResourceLocation secondDimension = ResourceLocation.fromNamespaceAndPath("foo_bar", "baz");

        store.put(team, new SharedEntry(SharedKind.BEDROCK_FLUID, firstDimension,
                0, 0, "0,0", SharedEntry.hash(payload), 0, payload));
        store.put(team, new SharedEntry(SharedKind.BEDROCK_FLUID, secondDimension,
                0, 0, "0,0", SharedEntry.hash(payload), 0, payload));
        store.flush();

        Path teamRoot = directory.resolve("dimensions").resolve(team.toString());
        try (var files = Files.walk(teamRoot)) {
            assertEquals(2, files.filter(path -> path.getFileName().toString().endsWith(".dat")).count());
        }
        TeamRegionStore reloaded = new TeamRegionStore(directory.resolve("dimensions"));
        Set<ResourceLocation> dimensions = reloaded.all(team).stream()
                .map(SharedEntry::dimension).collect(Collectors.toSet());
        assertEquals(Set.of(firstDimension, secondDimension), dimensions);
    }
}
