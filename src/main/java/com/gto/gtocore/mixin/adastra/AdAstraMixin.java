package com.gto.gtocore.mixin.adastra;

import net.minecraft.server.MinecraftServer;

import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.api.systems.GravityApi;
import earth.terrarium.adastra.api.systems.OxygenApi;
import earth.terrarium.adastra.api.systems.PlanetData;
import earth.terrarium.adastra.api.systems.TemperatureApi;
import earth.terrarium.adastra.common.network.NetworkHandler;
import earth.terrarium.adastra.common.network.messages.ClientboundSyncLocalPlanetDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AdAstra.class)
public class AdAstraMixin {

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/utils/radio/StationLoader;init()V"), remap = false)
    private static void stationLoader() {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void onServerTick(MinecraftServer server) {
        if (server.getTickCount() % 40 == 0) {
            server.getPlayerList().getPlayers().forEach((player) -> NetworkHandler.CHANNEL.sendToPlayer(new ClientboundSyncLocalPlanetDataPacket(new PlanetData(OxygenApi.API.hasOxygen(player), TemperatureApi.API.getTemperature(player), GravityApi.API.getGravity(player))), player));
        }
    }
}
