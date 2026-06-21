package com.gtocore.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.ModList;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class PlayerNameUtils {

    public static final String UNKNOWN = "Unknown";

    private PlayerNameUtils() {}

    public static String getLastKnownName(@Nullable Level level, @Nullable UUID uuid) {
        String name = findLastKnownName(level, uuid);
        return name == null ? UNKNOWN : name;
    }

    @Nullable
    public static String findLastKnownName(@Nullable Level level, @Nullable UUID uuid) {
        if (uuid == null) {
            return null;
        }

        if (level != null) {
            Player player = level.getPlayerByUUID(uuid);
            if (player != null) {
                return normalize(player.getName().getString());
            }
        }

        String ftbName = findFromFTBTeams(uuid);
        if (ftbName != null) {
            return ftbName;
        }

        String cachedName = normalize(UsernameCache.getLastKnownUsername(uuid));
        if (cachedName != null) {
            return cachedName;
        }

        return null;
    }

    @Nullable
    private static String findFromFTBTeams(UUID uuid) {
        if (!ModList.get().isLoaded("ftbteams")) {
            return null;
        }

        var api = FTBTeamsAPI.api();
        if (api.isClientManagerLoaded()) {
            var knownPlayer = api.getClientManager().getKnownPlayer(uuid);
            if (knownPlayer.isPresent()) {
                return normalize(knownPlayer.get().name());
            }
        }

        if (api.isManagerLoaded()) {
            var team = api.getManager().getKnownPlayerTeams().get(uuid);
            if (team != null && team.isPlayerTeam()) {
                return normalize(team.getName().getString());
            }
        }

        return null;
    }

    @Nullable
    private static String normalize(@Nullable String name) {
        return name == null || name.isBlank() || name.equalsIgnoreCase(UNKNOWN) ? null : name;
    }
}
