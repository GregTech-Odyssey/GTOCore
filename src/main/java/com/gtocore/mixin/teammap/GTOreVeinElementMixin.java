package com.gtocore.mixin.teammap;

import com.gtocore.integration.teammap.client.ClientTeamData;
import com.gtocore.integration.teammap.client.GTRecordAdapter;

import com.gregtechceu.gtceu.api.data.worldgen.ores.GeneratedVeinMetadata;
import com.gregtechceu.gtceu.integration.map.xaeros.worldmap.ore.OreVeinElement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.WorldMapSession;

@Mixin(value = OreVeinElement.class, remap = false)
public abstract class GTOreVeinElementMixin {

    @Inject(method = "toggleDepleted", at = @At("TAIL"))
    private void gtoTeamMap$shareDepletedState(CallbackInfo ci) {
        try {
            WorldMapSession session = WorldMapSession.getCurrentSession();
            if (session == null || !session.isUsable()) return;
            var dimension = session.getMapProcessor().getMapWorld().getCurrentDimension().getDimId();
            GeneratedVeinMetadata vein = ((OreVeinElement) (Object) this).getVein();
            ClientTeamData.upload(GTRecordAdapter.ore(dimension, vein));
        } catch (RuntimeException ignored) {}
    }
}
