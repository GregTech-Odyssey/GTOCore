package com.gtocore.integration.teammap.network;

import com.gtocore.integration.teammap.client.ClientTeamData;
import com.gtocore.integration.teammap.client.ClientUpdateScheduler;
import com.gtocore.integration.teammap.config.TeamMapConfig;
import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.server.TeamMapServer;

import com.gtolib.api.network.NetworkPack;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import dev.ftb.mods.ftbteams.api.Team;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ModNetwork {

    private static final int SYNC_BATCH_SIZE = 8;
    private static final int SYNC_BATCHES_PER_TICK = 2;
    private static final Map<UUID, ArrayDeque<SharedEntry>> PENDING_SYNCS = new HashMap<>();
    private static final Map<UUID, LinkedHashMap<String, SharedEntry>> PENDING_UPLOADS = new HashMap<>();
    private static final NetworkPack REQUEST_SYNC = NetworkPack.registerC2S(
            "gtocore:team_map_request_sync", (player, buf) -> sync(player));
    private static final NetworkPack UPLOAD_ENTRY = NetworkPack.registerC2S(
            "gtocore:team_map_upload", ModNetwork::handleUpload);
    private static final NetworkPack TEAM_STATE = NetworkPack.registerS2C(
            "gtocore:team_map_state", (player, buf) -> ClientTeamData.acceptTeam(
                    buf.readUUID(), buf.readUUID(), buf.readVarLong()));
    private static final NetworkPack ENTRY_BATCH = NetworkPack.registerS2C(
            "gtocore:team_map_batch", (player, buf) -> ClientUpdateScheduler.enqueue(readBatch(buf)));
    private static final NetworkPack RUN_IMPORT = NetworkPack.registerS2C(
            "gtocore:team_map_import", (player, buf) -> com.gtocore.integration.teammap.client.ClientHistoryImporter.scheduleFull(1));
    private static int mergeTimer;

    /** Forces static packet registration during GTOCore construction. */
    public static void init() {}

    public static void requestSync() {
        REQUEST_SYNC.send(buf -> {});
    }

    public static void upload(SharedEntry entry) {
        UPLOAD_ENTRY.send(buf -> writeEntry(buf, entry));
    }

    public static void runImport(ServerPlayer player) {
        RUN_IMPORT.send(buf -> {}, player);
    }

    public static void sync(ServerPlayer player) {
        TeamMapServer service = TeamMapServer.get(player.server);
        Team team = service.team(player).orElse(null);
        UUID teamId = team == null ? new UUID(0, 0) : team.getId();
        long revision = team == null ? 0 : service.store().latestRevision(teamId);
        TEAM_STATE.send(buf -> {
            buf.writeUUID(service.serverId());
            buf.writeUUID(teamId);
            buf.writeVarLong(revision);
        }, player);
        if (team == null) PENDING_SYNCS.remove(player.getUUID());
        else PENDING_SYNCS.put(player.getUUID(), new ArrayDeque<>(service.store().all(teamId)));
    }

    public static void tickServer(MinecraftServer server) {
        Iterator<Map.Entry<UUID, ArrayDeque<SharedEntry>>> iterator = PENDING_SYNCS.entrySet().iterator();
        while (iterator.hasNext()) {
            var pending = iterator.next();
            ServerPlayer player = server.getPlayerList().getPlayer(pending.getKey());
            if (player == null) {
                iterator.remove();
                continue;
            }
            ArrayDeque<SharedEntry> entries = pending.getValue();
            for (int batch = 0; batch < SYNC_BATCHES_PER_TICK && !entries.isEmpty(); batch++) {
                ArrayList<SharedEntry> part = new ArrayList<>(SYNC_BATCH_SIZE);
                while (part.size() < SYNC_BATCH_SIZE && !entries.isEmpty()) part.add(entries.removeFirst());
                sendBatch(player, part);
            }
            if (entries.isEmpty()) iterator.remove();
        }
        if (++mergeTimer >= TeamMapConfig.serverMergeIntervalTicks()) {
            mergeTimer = 0;
            flushServer(server);
        }
    }

    public static void flushServer(MinecraftServer server) {
        if (PENDING_UPLOADS.isEmpty()) return;
        TeamMapServer service = TeamMapServer.get(server);
        Map<UUID, LinkedHashMap<String, SharedEntry>> batches = new HashMap<>(PENDING_UPLOADS);
        PENDING_UPLOADS.clear();
        batches.forEach((teamId, pending) -> {
            ArrayList<SharedEntry> accepted = new ArrayList<>(pending.size());
            pending.values().forEach(entry -> accepted.add(service.store().put(teamId, entry)));
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                boolean member = service.team(player).map(team -> team.getId().equals(teamId)).orElse(false);
                if (!member) continue;
                for (int from = 0; from < accepted.size(); from += SYNC_BATCH_SIZE) {
                    int start = from;
                    sendBatch(player, accepted.subList(start,
                            Math.min(accepted.size(), start + SYNC_BATCH_SIZE)));
                }
            }
        });
    }

    public static void clearServerState() {
        PENDING_SYNCS.clear();
        PENDING_UPLOADS.clear();
        mergeTimer = 0;
    }

    private static void writeEntry(FriendlyByteBuf buf, SharedEntry entry) {
        buf.writeNbt(entry.toTag());
    }

    private static SharedEntry readEntry(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        if (tag == null) throw new IllegalArgumentException("Missing team map entry");
        return SharedEntry.fromTag(tag);
    }

    private static void handleUpload(ServerPlayer sender, FriendlyByteBuf buf) {
        SharedEntry entry = readEntry(buf);
        if (entry.payload().toString().length() > 1_500_000) return;
        TeamMapServer service = TeamMapServer.get(sender.server);
        Team team = service.team(sender).orElse(null);
        if (team != null) {
            PENDING_UPLOADS.computeIfAbsent(team.getId(), ignored -> new LinkedHashMap<>())
                    .put(entry.mapKey(), entry);
        }
    }

    private static void sendBatch(ServerPlayer player, List<SharedEntry> entries) {
        ENTRY_BATCH.send(buf -> {
            buf.writeVarInt(entries.size());
            entries.forEach(entry -> writeEntry(buf, entry));
        }, player);
    }

    private static List<SharedEntry> readBatch(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        if (count < 0 || count > 64) throw new IllegalArgumentException("Bad team map batch size " + count);
        List<SharedEntry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) entries.add(readEntry(buf));
        return entries;
    }
}
