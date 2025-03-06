package com.gto.gtocore.api.misc;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.client.ClientCache;
import com.gto.gtocore.common.network.ServerMessage;
import com.gto.gtocore.common.saved.CommonSavaedData;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import com.hepdd.gtmthings.utils.TeamUtil;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;
import java.util.UUID;

public interface PlanetManagement {

    static boolean isClientUnlocked(ResourceLocation planet) {
        if (planet == null) return false;
        if (planet.equals(GTODimensions.OVERWORLD)) return true;
        return ClientCache.UNLOCKED_PLANET.contains(planet);
    }

    static void clientUnlock(ResourceLocation planet) {
        if (planet == null) return;
        ClientCache.UNLOCKED_PLANET.add(planet);
    }

    static void checkIsUnlocked(ServerPlayer serverPlayer, ResourceLocation planet) {
        if (planet.equals(GTODimensions.OVERWORLD)) return;
        boolean value = isUnlocked(serverPlayer, planet);
        if (value) {
            ServerMessage.planetUnlock(serverPlayer, planet);
        }
    }

    static boolean isUnlocked(ServerPlayer serverPlayer, ResourceLocation planet) {
        if (planet == null) return false;
        if (planet.equals(GTODimensions.OVERWORLD)) return true;
        return CommonSavaedData.INSTANCE.getPlanetUnlocked().getOrDefault(TeamUtil.getTeamUUID(serverPlayer.getUUID()), Set.of()).contains(planet);
    }

    static void unlock(UUID uuid, ResourceLocation planet) {
        if (planet == null) return;
        CommonSavaedData.INSTANCE.getPlanetUnlocked().computeIfAbsent(TeamUtil.getTeamUUID(uuid), k -> new ObjectOpenHashSet<>()).add(planet);
        CommonSavaedData.INSTANCE.setDirty();
    }
}
