package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.data.DimensionMarker;
import com.gregtechceu.gtceu.common.data.GTDimensionMarkers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.AdAstra;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

import static com.gregtechceu.gtceu.common.data.GTDimensionMarkers.END_MARKER;
import static com.gregtechceu.gtceu.common.data.GTDimensionMarkers.NETHER_MARKER;

@Mixin(GTDimensionMarkers.class)
public class GTDimensionMarkersMixin {

    @Shadow(remap = false)
    public static DimensionMarker createAndRegister(ResourceLocation dim, int tier, ResourceLocation itemKey, @Nullable String overrideName) {
        return null;
    }

    @Unique
    private static DimensionMarker gTLCore$createAndRegister(ResourceLocation dim, int tier, Supplier<ItemLike> supplier) {
        DimensionMarker marker = new DimensionMarker(tier, supplier, null);
        marker.register(dim);
        return marker;
    }

    @Inject(method = "init", at = @At("HEAD"), remap = false)
    private static void init(CallbackInfo ci) {
        createAndRegister(new ResourceLocation(AdAstra.MOD_ID, "moon"),
                1, new ResourceLocation(AdAstra.MOD_ID, "moon_stone"), "gui.ad_astra.text.moon");
        createAndRegister(new ResourceLocation(AdAstra.MOD_ID, "mars"),
                2, new ResourceLocation(AdAstra.MOD_ID, "mars_stone"), "gui.ad_astra.text.mars");
        createAndRegister(new ResourceLocation(AdAstra.MOD_ID, "venus"),
                3, new ResourceLocation(AdAstra.MOD_ID, "venus_stone"), "gui.ad_astra.text.venus");
        createAndRegister(new ResourceLocation(AdAstra.MOD_ID, "mercury"),
                3, new ResourceLocation(AdAstra.MOD_ID, "mercury_stone"), "gui.ad_astra.text.mercury");
        createAndRegister(new ResourceLocation(AdAstra.MOD_ID, "glacio"),
                7, new ResourceLocation(AdAstra.MOD_ID, "glacio_stone"), "gui.ad_astra.text.glacio");
        createAndRegister(GTOCore.id("ancient_world"),
                0, GTOCore.id("reactor_core"), "biome.gtocore.ancient_world_biome");
        createAndRegister(GTOCore.id("titan"),
                6, GTOCore.id("titan_stone"), "biome.gtocore.titan_biome");
        createAndRegister(GTOCore.id("pluto"),
                6, GTOCore.id("pluto_stone"), "biome.gtocore.pluto_biome");
        createAndRegister(GTOCore.id("io"),
                5, GTOCore.id("io_stone"), "biome.gtocore.io_biome");
        createAndRegister(GTOCore.id("ganymede"),
                5, GTOCore.id("ganymede_stone"), "biome.gtocore.ganymede_biome");
        createAndRegister(GTOCore.id("enceladus"),
                6, GTOCore.id("enceladus_stone"), "biome.gtocore.enceladus_biome");
        createAndRegister(GTOCore.id("ceres"),
                4, GTOCore.id("ceres_stone"), "biome.gtocore.ceres_biome");
        createAndRegister(GTOCore.id("barnarda_c"),
                8, GTOCore.id("barnarda_c_wood"), "biome.gtocore.barnarda_c_biome");
        createAndRegister(GTOCore.id("create"),
                10, GTOCore.id("dimension_creation_casing"), "biome.gtocore.create");
        createAndRegister(GTOCore.id("void"),
                0, new ResourceLocation("minecraft", "obsidian"), "biome.gtocore.void");
        createAndRegister(GTOCore.id("flat"),
                0, new ResourceLocation("minecraft", "crying_obsidian"), "biome.gtocore.flat");
    }

    @Inject(method = "createAndRegister(Lnet/minecraft/resources/ResourceLocation;ILjava/util/function/Supplier;Ljava/lang/String;)Lcom/gregtechceu/gtceu/api/data/DimensionMarker;", at = @At("HEAD"), remap = false, cancellable = true)
    private static void createAndRegister(ResourceLocation dim, int tier, Supplier<ItemLike> supplier, @Nullable String overrideName, CallbackInfoReturnable<DimensionMarker> cir) {
        if (dim == Level.NETHER.location()) {
            cir.setReturnValue(gTLCore$createAndRegister(Level.NETHER.location(), 3,
                    () -> NETHER_MARKER));
        }
        if (dim == Level.END.location()) {
            cir.setReturnValue(gTLCore$createAndRegister(Level.END.location(), 6,
                    () -> END_MARKER));
        }
    }
}
