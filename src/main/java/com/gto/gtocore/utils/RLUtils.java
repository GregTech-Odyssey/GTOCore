package com.gto.gtocore.utils;

import net.minecraft.resources.ResourceLocation;

import earth.terrarium.adastra.AdAstra;
import vazkii.botania.api.BotaniaAPI;

public final class RLUtils {

    public static ResourceLocation mc(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public static ResourceLocation avaritia(String path) {
        return new ResourceLocation("avaritia", path);
    }

    public static ResourceLocation eio(String path) {
        return new ResourceLocation("enderio", path);
    }

    public static ResourceLocation sp(String path) {
        return new ResourceLocation("sophisticatedbackpacks", path);
    }

    public static ResourceLocation fd(String path) {
        return new ResourceLocation("farmersdelight", path);
    }

    public static ResourceLocation ad(String path) {
        return new ResourceLocation(AdAstra.MOD_ID, path);
    }

    public static ResourceLocation bot(String path) {
        return new ResourceLocation(BotaniaAPI.MODID, path);
    }
}
