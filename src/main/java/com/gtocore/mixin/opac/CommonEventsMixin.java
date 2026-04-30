package com.gtocore.mixin.opac;

import net.minecraft.server.MinecraftServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimPosList;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.event.CommonEvents;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.IOpenPACMinecraftServer;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.IServerDataAPI;
import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.claims.IServerDimensionClaimsManager;
import xaero.pac.common.server.claims.IServerRegionClaims;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.parties.party.IServerParty;

@Mixin(value = CommonEvents.class, remap = false)
public abstract class CommonEventsMixin {

    @Inject(method = "onServerStarting", at = @At("TAIL"))
    private void tcpatch$restoreOfflineForceloads(MinecraftServer server, CallbackInfo ci) {
        IServerDataAPI serverDataApi = ((IOpenPACMinecraftServer) server).getXaero_OPAC_ServerData();
        if (serverDataApi == null) {
            return;
        }
        @SuppressWarnings("unchecked")
        IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>> serverData = (IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>>) serverDataApi;
        var playerConfigManager = ((IServerData<?, ?>) serverDataApi).getPlayerConfigManager();
        serverData.getServerClaimsManager().getTypedPlayerInfoStream().forEach(playerInfo -> {
            serverData.getForceLoadManager().updateTicketsFor(playerConfigManager, playerInfo.getPlayerId(), true);
        });
    }
}
