package com.gtocore.integration.teammap.client;

import com.gtocore.integration.teammap.network.ModNetwork;

import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ClientEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    @SubscribeEvent
    public void login(ClientPlayerNetworkEvent.LoggingIn event) {
        ModNetwork.requestSync();
    }

    @SubscribeEvent
    public void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientTeamData.clear();
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientUpdateScheduler.tick();
            TerrainCapture.flush();
            NativeGtTeamOverlay.tick();
            ClientHistoryImporter.tick();
            ClientDiskCache.tick(ClientTeamData.serverId(), ClientTeamData.teamId(), ClientTeamData.entries());
        }
    }
}
