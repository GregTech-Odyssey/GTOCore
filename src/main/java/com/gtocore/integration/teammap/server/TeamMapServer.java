package com.gtocore.integration.teammap.server;

import com.gtocore.integration.teammap.TeamMapShare;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;

public final class TeamMapServer {

    private static final Map<MinecraftServer, TeamMapServer> INSTANCES = new WeakHashMap<>();
    private final UUID serverId;
    private final TeamRegionStore store;

    private TeamMapServer(MinecraftServer server) {
        Path root = server.getWorldPath(LevelResource.ROOT).resolve("data").resolve(TeamMapShare.DATA_ID);
        this.serverId = loadServerId(root);
        this.store = new TeamRegionStore(root.resolve(serverId.toString()).resolve("teams"));
    }

    public static synchronized TeamMapServer get(MinecraftServer server) {
        return INSTANCES.computeIfAbsent(server, TeamMapServer::new);
    }

    public static synchronized void close(MinecraftServer server) {
        TeamMapServer value = INSTANCES.remove(server);
        if (value != null) value.store.flush();
    }

    public Optional<Team> team(ServerPlayer player) {
        return FTBTeamsAPI.api().getManager().getTeamForPlayer(player);
    }

    public UUID serverId() {
        return serverId;
    }

    public TeamRegionStore store() {
        return store;
    }

    private static UUID loadServerId(Path root) {
        Path file = root.resolve("server-id.txt");
        try {
            Files.createDirectories(root);
            if (Files.exists(file)) return UUID.fromString(Files.readString(file).trim());
            UUID id = UUID.randomUUID();
            Files.writeString(file, id.toString());
            return id;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create GTO Team Map Share server identity", e);
        }
    }
}
