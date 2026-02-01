package com.gtocore.integration;

import net.minecraftforge.fml.loading.FMLLoader;

import lombok.Getter;

public enum Mods {

    SFM("sfm"),
    FARMERSDELIGHT("farmersdelight"),
    MODULARROUTERS("modularrouters"),
    COMPUTERCRAFT("computercraft"),
    FUNCTIONALSTORAGE("functionalstorage"),
    IMMERSIVE_AIRCRAFT("immersive_aircraft"),
    CHISEL("chisel"),
    SOPHISTICATEDBACKPACKS("sophisticatedbackpacks"),
    BIOMESOPLENTY("biomesoplenty"),
    BIOMESWEVEGONE("biomeswevegone"),
    PIPEZ("pipez"),
    FTBQUESTS("ftbquests"),
    CONSTRUCTION_WAND("constructionwand"),
    JECHARACTERS("jecharacters"),
    LANG("moremorelang");

    @Getter
    private final boolean loaded;

    Mods(String modId) {
        loaded = FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }
}
