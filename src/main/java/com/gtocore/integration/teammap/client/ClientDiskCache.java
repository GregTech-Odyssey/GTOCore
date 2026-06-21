package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.TeamMapShare;
import com.gtocore.integration.teammap.data.SharedEntry;

import com.gtolib.GTOCore;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ClientDiskCache {

    private static final Object WRITE_LOCK = new Object();
    private static final ExecutorService IO = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "GTO Team Map Cache IO");
        thread.setDaemon(true);
        return thread;
    });
    private static volatile boolean dirty;
    private static volatile boolean saving;
    private static int timer;

    public static void markDirty() {
        dirty = true;
    }

    public static void tick(UUID server, UUID team, Collection<SharedEntry> entries) {
        if (!dirty || saving || ++timer < 200) return;
        Path target = file(server, team);
        if (server.getMostSignificantBits() == 0 && server.getLeastSignificantBits() == 0) return;
        ArrayList<SharedEntry> snapshot = new ArrayList<>(entries);
        dirty = false;
        saving = true;
        timer = 0;
        IO.execute(() -> {
            try {
                write(target, snapshot);
            } catch (Exception e) {
                dirty = true;
                GTOCore.LOGGER.warn("Could not write client team map cache {}", target, e);
            } finally {
                saving = false;
            }
        });
    }

    public static Map<String, SharedEntry> load(UUID server, UUID team) {
        Map<String, SharedEntry> result = new HashMap<>();
        if (server.getMostSignificantBits() == 0 && server.getLeastSignificantBits() == 0) return result;
        Path file = file(server, team);
        if (!Files.exists(file)) return result;
        try (InputStream input = Files.newInputStream(file)) {
            ListTag list = NbtIo.readCompressed(input).getList("entries", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                SharedEntry entry = SharedEntry.fromTag(list.getCompound(i));
                result.put(entry.mapKey(), entry);
            }
        } catch (Exception e) {
            GTOCore.LOGGER.warn("Could not read client team map cache {}", file, e);
        }
        return result;
    }

    public static void save(UUID server, UUID team, Collection<SharedEntry> entries) {
        if (server.getMostSignificantBits() == 0 && server.getLeastSignificantBits() == 0) return;
        Path file = file(server, team);
        try {
            write(file, entries);
            dirty = false;
            timer = 0;
        } catch (Exception e) {
            GTOCore.LOGGER.warn("Could not write client team map cache {}", file, e);
        }
    }

    private static void write(Path file, Collection<SharedEntry> entries) throws Exception {
        synchronized (WRITE_LOCK) {
            Files.createDirectories(file.getParent());
            CompoundTag root = new CompoundTag();
            ListTag list = new ListTag();
            entries.forEach(entry -> list.add(entry.toTag()));
            root.put("entries", list);
            Path temp = file.resolveSibling(file.getFileName() + ".tmp");
            try (OutputStream output = Files.newOutputStream(temp)) {
                NbtIo.writeCompressed(root, output);
            }
            try {
                Files.move(temp, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ignored) {
                Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private static Path file(UUID server, UUID team) {
        return Minecraft.getInstance().gameDirectory.toPath().resolve(TeamMapShare.DATA_ID)
                .resolve(server.toString()).resolve(team.toString()).resolve("cache.dat");
    }
}
