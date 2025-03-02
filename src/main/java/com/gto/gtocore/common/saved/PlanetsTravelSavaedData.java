package com.gto.gtocore.common.saved;

import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.client.ClientCache;
import com.gto.gtocore.common.network.ServerMessage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;

import com.hepdd.gtmthings.utils.TeamUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.gto.gtocore.api.GTOValues.*;

public final class PlanetsTravelSavaedData extends SavedData {

    public static PlanetsTravelSavaedData INSTANCE = new PlanetsTravelSavaedData();

    public static boolean isClientUnlocked(ResourceLocation planet) {
        if (planet == null) return false;
        if (planet.equals(GTODimensions.OVERWORLD)) return true;
        return ClientCache.UNLOCKED_PLANET.contains(planet);
    }

    public static void clientUnlock(ResourceLocation planet) {
        if (planet == null) return;
        ClientCache.UNLOCKED_PLANET.add(planet);
    }

    public static void checkIsUnlocked(ServerPlayer serverPlayer, ResourceLocation planet) {
        if (planet.equals(GTODimensions.OVERWORLD)) return;
        boolean value = isUnlocked(serverPlayer, planet);
        if (value) {
            ServerMessage.planetUnlock(serverPlayer, planet);
        }
    }

    public static boolean isUnlocked(ServerPlayer serverPlayer, ResourceLocation planet) {
        if (planet == null) return false;
        if (planet.equals(GTODimensions.OVERWORLD)) return true;
        return INSTANCE.unlocked.getOrDefault(TeamUtil.getTeamUUID(serverPlayer.getUUID()), Set.of()).contains(planet);
    }

    public static void unlock(UUID uuid, ResourceLocation planet) {
        if (planet == null) return;
        INSTANCE.unlocked.computeIfAbsent(TeamUtil.getTeamUUID(uuid), k -> new ObjectOpenHashSet<>()).add(planet);
        INSTANCE.setDirty();
    }

    private final Map<UUID, Set<ResourceLocation>> unlocked;

    public PlanetsTravelSavaedData() {
        unlocked = new Object2ObjectOpenHashMap<>();
        setDirty();
    }

    public PlanetsTravelSavaedData(CompoundTag compoundTag) {
        this();
        ListTag listTag = compoundTag.getList(PLAYER_LIST, 10);
        for (Tag tag : listTag) {
            CompoundTag playerTag = (CompoundTag) tag;
            Set<ResourceLocation> set = new ObjectOpenHashSet<>();
            ListTag planetsList = playerTag.getList(PLANET_LIST, 10);
            for (Tag planetTag : planetsList) {
                set.add(new ResourceLocation(((CompoundTag) planetTag).getString(PLANET_NAME)));
            }
            unlocked.put(playerTag.getUUID(PLAYER_UUID), set);
        }
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag) {
        ListTag listTag = new ListTag();
        unlocked.forEach((k, v) -> {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID(PLAYER_UUID, k);
            ListTag planetsList = new ListTag();
            v.forEach(planet -> {
                CompoundTag planetTag = new CompoundTag();
                planetTag.putString(PLANET_NAME, planet.toString());
                planetsList.add(planetTag);
            });
            playerTag.put(PLANET_LIST, planetsList);
            listTag.add(playerTag);
        });
        compoundTag.put(PLAYER_LIST, listTag);
        return compoundTag;
    }
}
