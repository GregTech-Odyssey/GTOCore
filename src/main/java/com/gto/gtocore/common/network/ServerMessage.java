package com.gto.gtocore.common.network;

import com.gto.gtocore.api.misc.PlanetManagement;
import com.gto.gtocore.client.ClientCache;
import com.gto.gtocore.mixin.patchouli.BookContentResourceListenerLoaderAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.book.BookContentResourceListenerLoader;
import vazkii.patchouli.client.book.ClientBookRegistry;

public interface ServerMessage {

    static void sendData(MinecraftServer server, @Nullable ServerPlayer player, String channel, @Nullable CompoundTag data) {
        if (player != null) {
            new FromServerMessage(channel, data).sendTo(player);
        } else {
            new FromServerMessage(channel, data).sendToAll(server);
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
                PlanetManagement.clientUnlock(planet);
                break;
            }
            case "disableDrift": {
                ClientCache.disableDrift = data.getBoolean("disableDrift");
                break;
            }
            case "loggedIn": {
                ClientCache.UNLOCKED_PLANET.clear();
                if (Minecraft.getInstance().level != null && !ClientCache.initializedBook) {
                    ClientCache.initializedBook = true;
                    Thread thread = new Thread(() -> {
                        ClientBookRegistry.INSTANCE.reload();
                        ((BookContentResourceListenerLoaderAccessor) BookContentResourceListenerLoader.INSTANCE).getData().clear();
                    });
                    thread.setDaemon(true);
                    thread.start();
                }
                break;
            }
        }
    }
}
