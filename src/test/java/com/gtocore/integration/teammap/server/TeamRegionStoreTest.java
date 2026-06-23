package com.gtocore.integration.teammap.server;

import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.data.SharedKind;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TeamRegionStoreTest {
    @TempDir Path directory;

    @Test
    void deduplicatesAndPersistsRegionBuckets() {
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

        TeamRegionStore reloaded = new TeamRegionStore(directory);
        assertEquals(1, reloaded.all(team).size());
        assertEquals(accepted, reloaded.all(team).get(0));
        assertTrue(directory.resolve(team.toString()).resolve("minecraft_overworld").resolve("-2_2.dat").toFile().isFile());
    }
}
