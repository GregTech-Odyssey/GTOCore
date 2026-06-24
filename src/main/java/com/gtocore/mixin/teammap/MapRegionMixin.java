package com.gtocore.mixin.teammap;

import com.gtocore.integration.teammap.client.TerrainCapture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.region.MapRegion;

@Mixin(value = MapRegion.class, remap = false)
public abstract class MapRegionMixin {

    @Inject(method = "onProcessingEnd", at = @At("TAIL"))
    private void gtoTeamMap$finishHistoryImport(CallbackInfo ci) {
        TerrainCapture.endImport((MapRegion) (Object) this);
    }
}
