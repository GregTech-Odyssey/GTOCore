package com.gtocore.common.network;

import com.gtocore.client.ClientCache;
import com.gtocore.common.machine.monitor.Manager;
import com.gtocore.config.GTOConfig;
import com.gtocore.integration.ae.hooks.IPushResultsHandler;
import com.gtocore.integration.emi.EmiPersist;

import com.gtolib.GTOCore;
import com.gtolib.api.misc.PlanetManagement;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.mixin.BookContentResourceListenerLoaderAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import dev.emi.emi.runtime.EmiPersistentData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.book.BookContentResourceListenerLoader;
import vazkii.patchouli.client.book.ClientBookRegistry;

import java.util.function.Consumer;

public final class ServerMessage {

    public static void sendData(MinecraftServer server, @Nullable ServerPlayer player, String channel, @Nullable CompoundTag data) {
        send(server, player, channel, buf -> buf.writeNbt(data));
    }

    public static void send(MinecraftServer server, @Nullable ServerPlayer player, String channel, @NotNull Consumer<FriendlyByteBuf> consumer) {
        var message = new FromServerMessage(channel, consumer);
        if (player != null) {
            message.sendTo(player);
        } else {
            message.sendToAll(server);
        }
    }

    public static void disableDrift(ServerPlayer serverPlayer, boolean drift) {
        send(serverPlayer.server, serverPlayer, "disableDrift", buf -> buf.writeBoolean(drift));
    }

    public static void planetUnlock(ServerPlayer serverPlayer, ResourceLocation planet) {
        send(serverPlayer.server, serverPlayer, "planetUnlock", buf -> buf.writeResourceLocation(planet));
    }

    static void handle(String channel, @Nullable Player player, FriendlyByteBuf data) {
        if (player == null) return;
        switch (channel) {
            case "planetUnlock" -> PlanetManagement.clientUnlock(data.readResourceLocation());
            case "disableDrift" -> ClientCache.disableDrift = data.readBoolean();
            case "loggedIn" -> {
                ClientCache.UNLOCKED_PLANET.clear();
                if (Minecraft.getInstance().level != null && !ClientCache.initializedBook) {
                    ClientCache.initializedBook = true;
                    Thread.startVirtualThread(() -> {
                        ClientBookRegistry.INSTANCE.reload();
                        ((BookContentResourceListenerLoaderAccessor) BookContentResourceListenerLoader.INSTANCE).getData().clear();
                    });
                }
                ClientCache.SERVER_IDENTIFIER = data.readUUID();
                if (!GTOConfig.INSTANCE.emiGlobalFavorites && EmiPersist.needsRefresh) {
                    // emi has loaded before we receive SERVER_IDENTIFIER, reload it.
                    EmiPersistentData.load();
                    GTOCore.LOGGER.warn("emi reloaded");
                    EmiPersist.needsRefresh = false;
                }
            }
            case "monitorChanged" -> {
                var monitorData = data.readNbt();
                if (monitorData != null && player.level().isClientSide) {
                    Manager.onClientReceived(monitorData);
                }
            }
            case "craftMenuPushResults" -> {
                if (player.containerMenu.containerId == data.readInt() && player.containerMenu instanceof IPushResultsHandler handler) {
                    handler.gtocore$syncCraftingResults(data);
                }
            }
            case "playerData" -> ((IEnhancedPlayer) player).getPlayerData().syncFromBuf(data);
        }
    }
}
