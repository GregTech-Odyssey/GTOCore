package com.gtocore.mixin.teammap;

import com.gtocore.integration.teammap.client.TerrainCapture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.MapProcessor;
import xaero.map.biome.BlockTintProvider;
import xaero.map.cache.BlockStateShortShapeCache;
import xaero.map.region.MapTileChunk;
import xaero.map.region.MapUpdateFastConfig;
import xaero.map.region.OverlayManager;

@Mixin(value = MapTileChunk.class, remap = false)
public abstract class MapTileChunkMixin {

    @Inject(method = "updateBuffers", at = @At("TAIL"))
    private void gtoTeamMap$captureNativeColors(MapProcessor processor, BlockTintProvider tintProvider,
                                                OverlayManager overlays, boolean debug,
                                                BlockStateShortShapeCache shapeCache,
                                                MapUpdateFastConfig config, CallbackInfo ci) {
        TerrainCapture.queueChunk((MapTileChunk) (Object) this);
    }
}
