package com.gtocore.mixin.teammap;

import com.gtocore.integration.teammap.client.ClientHistoryImporter;
import com.gtocore.integration.teammap.client.ClientTeamData;
import com.gtocore.integration.teammap.client.GTRecordAdapter;
import com.gtocore.integration.teammap.client.NativeGtTeamOverlay;

import com.gregtechceu.gtceu.api.data.worldgen.ores.GeneratedVeinMetadata;
import com.gregtechceu.gtceu.api.gui.misc.ProspectorMode;
import com.gregtechceu.gtceu.integration.map.cache.client.GTClientCache;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GTClientCache.class, remap = false)
public abstract class GTClientCacheMixin {

    @Inject(method = "addVein", at = @At("HEAD"))
    private void gtoTeamMap$personalOreWins(ResourceKey<Level> dimension, int x, int z, GeneratedVeinMetadata vein,
                                            CallbackInfoReturnable<Boolean> cir) {
        NativeGtTeamOverlay.removeTeamOre(dimension, vein);
    }

    @Inject(method = "addVein", at = @At("RETURN"))
    private void gtoTeamMap$ore(ResourceKey<Level> dimension, int x, int z, GeneratedVeinMetadata vein,
                                CallbackInfoReturnable<Boolean> cir) {
        // Existing personal-cache entries return false, but the team may still
        // not have them. Server-side stable-key deduplication makes this safe.
        ClientTeamData.upload(GTRecordAdapter.ore(dimension, vein));
    }

    @Inject(method = "addFluid", at = @At("TAIL"))
    private void gtoTeamMap$fluid(ResourceKey<Level> dimension, int x, int z, ProspectorMode.FluidInfo fluid,
                                  CallbackInfo ci) {
        ClientTeamData.upload(GTRecordAdapter.fluid(dimension, x, z, fluid));
    }

    @Inject(method = "addFluid", at = @At("HEAD"))
    private void gtoTeamMap$personalFluidWins(ResourceKey<Level> dimension, int x, int z,
                                              ProspectorMode.FluidInfo fluid, CallbackInfo ci) {
        NativeGtTeamOverlay.removeTeamFluid(dimension, x, z);
    }

    @Inject(method = "addBedrockOre", at = @At("TAIL"))
    private void gtoTeamMap$bedrockOre(ResourceKey<Level> dimension, int x, int z, ProspectorMode.OreInfo[] ores,
                                       CallbackInfo ci) {
        ClientTeamData.upload(GTRecordAdapter.bedrockOre(dimension, x, z, ores));
    }

    @Inject(method = "addBedrockOre", at = @At("HEAD"))
    private void gtoTeamMap$personalBedrockOreWins(ResourceKey<Level> dimension, int x, int z,
                                                   ProspectorMode.OreInfo[] ores, CallbackInfo ci) {
        NativeGtTeamOverlay.removeTeamBedrockOre(dimension, x, z);
    }

    @Inject(method = "setupCacheFiles", at = @At("TAIL"))
    private void gtoTeamMap$importLoadedGtCache(CallbackInfo ci) {
        ClientHistoryImporter.scheduleGt(20);
        NativeGtTeamOverlay.markDirty();
    }

    @Inject(method = "clear", at = @At("TAIL"))
    private void gtoTeamMap$restoreTeamMarkersAfterClear(CallbackInfo ci) {
        NativeGtTeamOverlay.markDirty();
    }
}
