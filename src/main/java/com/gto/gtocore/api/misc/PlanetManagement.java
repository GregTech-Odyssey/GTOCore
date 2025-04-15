package com.gto.gtocore.api.misc;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.client.ClientCache;
import com.gto.gtocore.common.network.ServerMessage;
import com.gto.gtocore.common.saved.CommonSavaedData;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import com.hepdd.gtmthings.utils.TeamUtil;
import earth.terrarium.adastra.api.planets.Planet;
import earth.terrarium.adastra.api.planets.PlanetApi;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Optional;
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

    static int calculateTier(Planet targetPlanet, ResourceLocation current) {
        if (targetPlanet.tier() < 10) {
            if (targetPlanet.dimension().location().equals(GTODimensions.BARNARDA_C)) return 8;
            Planet currentPlanet = PlanetApi.API.getPlanet(GTODimensions.getDimensionKey(current));
            if (currentPlanet == null) return 7;
            ResourceLocation target = targetPlanet.dimension().location();
            Optional<ResourceKey<Level>> currentOrbit = currentPlanet.orbit();
            Optional<ResourceKey<Level>> targetOrbit = targetPlanet.orbit();
            if (currentOrbit.isPresent() && currentOrbit.get().location().equals(target)) return 1;
            if (targetOrbit.isPresent() && targetOrbit.get().location().equals(current)) return 1;
            Integer distanceFromEarth1 = GTODimensions.PLANET_DISTANCES.get(target);
            if (distanceFromEarth1 != null) {
                Integer distanceFromEarth2 = GTODimensions.PLANET_DISTANCES.get(current);
                if (distanceFromEarth2 == null) return 7;
                return Math.max(1, Math.min(6, Math.abs(distanceFromEarth1 - distanceFromEarth2)));
            }
        }
        return targetPlanet.tier();
    }
}
