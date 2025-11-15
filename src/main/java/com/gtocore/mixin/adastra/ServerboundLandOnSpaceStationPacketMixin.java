package com.gtocore.mixin.adastra;

import com.gregtechceu.gtceu.GTCEu;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import earth.terrarium.adastra.common.handlers.SpaceStationHandler;
import earth.terrarium.adastra.common.handlers.base.SpaceStation;
import earth.terrarium.adastra.common.network.messages.ServerboundLandOnSpaceStationPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(ServerboundLandOnSpaceStationPacket.class)
public class ServerboundLandOnSpaceStationPacketMixin {

    @Redirect(method = "isAllowed", at = @At(value = "INVOKE", target = "Learth/terrarium/adastra/common/handlers/SpaceStationHandler;getOwnedSpaceStations(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/level/ServerLevel;)Ljava/util/Set;", remap = false), remap = false)
    private static Set<SpaceStation> redirectIsAllowed(ServerPlayer player, ServerLevel level) {
        Set<SpaceStation> original = SpaceStationHandler.getOwnedSpaceStations(player, level);
        if (GTCEu.Mods.isFTBTeamsLoaded() && FTBTeamsAPI.api().isManagerLoaded()) {
            var uuid = player.getUUID();
            FTBTeamsAPI.api().getManager().getPlayerTeamForPlayerID(uuid).ifPresent(team -> {
                var teamMembers = team.getMembers();
                for (var memberUUID : teamMembers) {
                    if (memberUUID.equals(uuid)) continue;
                    var memberStations = SpaceStationHandler.getOwnedSpaceStations(memberUUID, level);
                    original.addAll(memberStations);
                }
            });
        }
        return original;
    }
}
