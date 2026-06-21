package com.gtocore.integration.teammap.network;

import com.gtocore.integration.teammap.client.ClientTeamData;
import com.gtocore.integration.teammap.client.ClientUpdateScheduler;
import com.gtocore.integration.teammap.config.TeamMapConfig;
import com.gtocore.integration.teammap.data.SharedEntry;
import com.gtocore.integration.teammap.server.TeamMapServer;

import com.gtolib.GTOCore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import dev.ftb.mods.ftbteams.api.Team;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public final class ModNetwork {

    private static final String PROTOCOL = "7";
    private static final int SYNC_BATCH_SIZE = 8;
    private static final int SYNC_BATCHES_PER_TICK = 2;
    private static final Map<UUID, ArrayDeque<SharedEntry>> PENDING_SYNCS = new HashMap<>();
    private static final Map<UUID, LinkedHashMap<String, SharedEntry>> PENDING_UPLOADS = new HashMap<>();
    private static int mergeTimer;
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(GTOCore.MOD_ID, "team_map_share"))
            .networkProtocolVersion(() -> PROTOCOL)
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .simpleChannel();
    private static int id;

    public static void init() {
        CHANNEL.messageBuilder(RequestSync.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(RequestSync::encode).decoder(RequestSync::decode).consumerMainThread(RequestSync::handle).add();
        CHANNEL.messageBuilder(UploadEntry.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(UploadEntry::encode).decoder(UploadEntry::decode).consumerMainThread(UploadEntry::handle).add();
        CHANNEL.messageBuilder(TeamState.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(TeamState::encode).decoder(TeamState::decode).consumerMainThread(TeamState::handle).add();
        CHANNEL.messageBuilder(EntryBatch.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(EntryBatch::encode).decoder(EntryBatch::decode).consumerMainThread(EntryBatch::handle).add();
        CHANNEL.messageBuilder(RunImport.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(RunImport::encode).decoder(RunImport::decode).consumerMainThread(RunImport::handle).add();
    }

    public static void requestSync() {
        CHANNEL.sendToServer(new RequestSync());
    }

    public static void upload(SharedEntry entry) {
        CHANNEL.sendToServer(new UploadEntry(entry));
    }

    public static void runImport(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new RunImport());
    }

    public static void sync(ServerPlayer player) {
        TeamMapServer service = TeamMapServer.get(player.server);
        Team team = service.team(player).orElse(null);
        UUID teamId = team == null ? new UUID(0, 0) : team.getId();
        long revision = team == null ? 0 : service.store().latestRevision(teamId);
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new TeamState(service.serverId(), teamId, revision));
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
                CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new EntryBatch(part));
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
                    EntryBatch update = new EntryBatch(new ArrayList<>(accepted.subList(start,
                            Math.min(accepted.size(), start + SYNC_BATCH_SIZE))));
                    CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), update);
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

    public record RequestSync() {

        static void encode(RequestSync ignored, FriendlyByteBuf buf) {}

        static RequestSync decode(FriendlyByteBuf buf) {
            return new RequestSync();
        }

        static void handle(RequestSync ignored, Supplier<NetworkEvent.Context> supplier) {
            ServerPlayer player = supplier.get().getSender();
            if (player != null) sync(player);
            supplier.get().setPacketHandled(true);
        }
    }

    public record UploadEntry(SharedEntry entry) {

        static void encode(UploadEntry msg, FriendlyByteBuf buf) {
            writeEntry(buf, msg.entry);
        }

        static UploadEntry decode(FriendlyByteBuf buf) {
            return new UploadEntry(readEntry(buf));
        }

        static void handle(UploadEntry msg, Supplier<NetworkEvent.Context> supplier) {
            ServerPlayer sender = supplier.get().getSender();
            if (sender != null && msg.entry.payload().toString().length() <= 1_500_000) {
                TeamMapServer service = TeamMapServer.get(sender.server);
                Team team = service.team(sender).orElse(null);
                if (team != null) {
                    PENDING_UPLOADS.computeIfAbsent(team.getId(), ignored -> new LinkedHashMap<>())
                            .put(msg.entry.mapKey(), msg.entry);
                }
            }
            supplier.get().setPacketHandled(true);
        }
    }

    public record TeamState(UUID serverId, UUID teamId, long revision) {

        static void encode(TeamState msg, FriendlyByteBuf buf) {
            buf.writeUUID(msg.serverId);
            buf.writeUUID(msg.teamId);
            buf.writeVarLong(msg.revision);
        }

        static TeamState decode(FriendlyByteBuf buf) {
            return new TeamState(buf.readUUID(), buf.readUUID(), buf.readVarLong());
        }

        static void handle(TeamState msg, Supplier<NetworkEvent.Context> supplier) {
            ClientTeamData.acceptTeam(msg.serverId, msg.teamId, msg.revision);
            supplier.get().setPacketHandled(true);
        }
    }

    public record EntryBatch(List<SharedEntry> entries) {

        static void encode(EntryBatch msg, FriendlyByteBuf buf) {
            buf.writeVarInt(msg.entries.size());
            msg.entries.forEach(entry -> writeEntry(buf, entry));
        }

        static EntryBatch decode(FriendlyByteBuf buf) {
            int count = buf.readVarInt();
            if (count < 0 || count > 64) throw new IllegalArgumentException("Bad team map batch size " + count);
            List<SharedEntry> entries = new ArrayList<>(count);
            for (int i = 0; i < count; i++) entries.add(readEntry(buf));
            return new EntryBatch(entries);
        }

        static void handle(EntryBatch msg, Supplier<NetworkEvent.Context> supplier) {
            ClientUpdateScheduler.enqueue(msg.entries);
            supplier.get().setPacketHandled(true);
        }
    }

    public record RunImport() {

        static void encode(RunImport ignored, FriendlyByteBuf buf) {}

        static RunImport decode(FriendlyByteBuf buf) {
            return new RunImport();
        }

        static void handle(RunImport ignored, Supplier<NetworkEvent.Context> supplier) {
            com.gtocore.integration.teammap.client.ClientHistoryImporter.scheduleFull(1);
            supplier.get().setPacketHandled(true);
        }
    }
}
