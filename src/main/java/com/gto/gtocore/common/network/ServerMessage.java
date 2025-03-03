package com.gto.gtocore.common.network;

import com.gto.gtocore.client.ClientCache;
import com.gto.gtocore.common.saved.PlanetsTravelSavaedData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import dev.latvian.mods.kubejs.net.SendDataFromServerMessage;
import org.jetbrains.annotations.Nullable;

public interface ServerMessage {

    static void sendData(MinecraftServer server, @Nullable ServerPlayer player, String channel, @Nullable CompoundTag data) {
        if (player != null) {
            new SendDataFromServerMessage(channel, data).sendTo(player);
        } else {
            new SendDataFromServerMessage(channel, data).sendToAll(server);
        }
    }

    static void disableDrift(ServerPlayer serverPlayer, boolean drift) {
        CompoundTag data = new CompoundTag();
        data.putBoolean("disableDrift", drift);
        sendData(serverPlayer.server, serverPlayer, "disableDrift", data);
    }

    static void planetUnlock(ServerPlayer serverPlayer, ResourceLocation planet) {
        CompoundTag data = new CompoundTag();
        data.putString("planet", planet.toString());
        sendData(serverPlayer.server, serverPlayer, "planetUnlock", data);
    }

    static void handle(String channel, @Nullable Player player, CompoundTag data) {
        if (player == null) return;
        switch (channel) {
            case "planetUnlock": {
                ResourceLocation planet = new ResourceLocation(data.getString("planet"));
                PlanetsTravelSavaedData.clientUnlock(planet);
                break;
            }
            case "disableDrift": {
                ClientCache.disableDrift = data.getBoolean("disableDrift");
                break;
            }
            case "loggedIn": {
                ClientCache.UNLOCKED_PLANET.clear();
                break;
            }
        }
    }
}
