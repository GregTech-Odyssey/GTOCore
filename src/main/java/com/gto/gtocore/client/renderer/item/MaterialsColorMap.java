package com.gto.gtocore.client.renderer.item;

import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.utils.ColorUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import com.google.common.collect.ImmutableMap;
import committee.nova.mods.avaritia.client.AvaritiaModClient;

import java.util.function.Supplier;

public final class MaterialsColorMap {

    public static final ImmutableMap<Material, Supplier<Integer>> MaterialColors;

    static final Supplier<Integer> quantumColor = () -> {
        float spot = (float) ((System.currentTimeMillis() / 500) % 10) / 10;
        if (spot > 0.5) {
            spot = 1 - spot;
        }
        return ColorUtils.getInterpolatedColor(0x00FF84, 0xFF7E00, spot * 2); // * 2 以确保spot在0到1之间平滑过渡
    };

    static {
        ImmutableMap.Builder<Material, Supplier<Integer>> MaterialBuilder = ImmutableMap.builder();
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
