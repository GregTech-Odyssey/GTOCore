package com.gtocore.client.renderer.item;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.utils.ColorUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.util.Mth;

import com.google.common.collect.ImmutableMap;
import committee.nova.mods.avaritia.client.AvaritiaModClient;
import vazkii.botania.client.core.handler.ClientTickHandler;

import java.util.function.IntSupplier;

public final class MaterialsColorMap {

    public static final ImmutableMap<Material, IntSupplier> MaterialColors;

    static final IntSupplier quantumColor = () -> {
        float spot = (float) ((System.currentTimeMillis() / 500) % 10) / 10;
        if (spot > 0.5) {
            spot = 1 - spot;
        }
        return ColorUtils.getInterpolatedColor(0x00FF84, 0xFF7E00, spot * 2); // * 2 以确保spot在0到1之间平滑过渡
    };

    private static final IntSupplier shimmer = () -> {
        float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
        return Mth.hsvToRgb(time % 200 / 200, 0.4F, 0.9F);
    };

    static {
        ImmutableMap.Builder<Material, IntSupplier> MaterialBuilder = ImmutableMap.builder();
        MaterialBuilder.put(GTOMaterials.Gaia, () -> Mth.hsvToRgb((ClientTickHandler.ticksInGame << 1) % 360 / 360F, 0.25F, 1F));
        MaterialBuilder.put(GTOMaterials.Shimmerwood, shimmer);
        MaterialBuilder.put(GTOMaterials.Shimmerrock, shimmer);
        MaterialBuilder.put(GTOMaterials.BifrostPerm, shimmer);
        MaterialBuilder.put(GTOMaterials.StarStone, () -> ColorUtils.getInterpolatedColor(0xb5d9ce, 0xFFFFFF, Math.abs(1 - (System.currentTimeMillis() % 10000) / 5000.0F)));
        MaterialBuilder.put(GTOMaterials.ChromaticGlass, AvaritiaModClient::getCurrentRainbowColor);
        MaterialBuilder.put(GTOMaterials.Hypogen, () -> ColorUtils.getInterpolatedColor(0xFF3D00, 0xDA9100, Math.abs(1 - (System.currentTimeMillis() % 6000) / 3000.0F)));
        MaterialBuilder.put(GTOMaterials.HexaphaseCopper, () -> {
            float spot = (System.currentTimeMillis() % 4000) / 4000.0F;
            return ColorUtils.getInterpolatedColor(0xEC7916, 0x00FF15, (spot > 0.1 && spot < 0.15 || spot > 0.18 && spot < 0.22) ? 1 : 0);
        });
        MaterialBuilder.put(GTOMaterials.HeavyQuarkDegenerateMatter, quantumColor);
        MaterialBuilder.put(GTOMaterials.QuantumChromoDynamicallyConfinedMatter, quantumColor);
        MaterialColors = MaterialBuilder.build();
    }
}
