package com.gtocore.integration.teammap.server;

import com.gtocore.integration.teammap.TeamMapShare;

import com.gtolib.GTOCore;
import com.gtolib.utils.ServerUtils;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;

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
        // Standalone bridge worlds used this file. Prefer it when present so
        // upgrading to the integrated implementation keeps the same team data.
        Path file = root.resolve("server-id.txt");
        try {
            if (Files.exists(file)) return UUID.fromString(Files.readString(file).trim());
        } catch (Exception exception) {
            GTOCore.LOGGER.warn("Ignoring invalid legacy team map server identity {}", file, exception);
        }
        return ServerUtils.getServerIdentifier();
    }
}
