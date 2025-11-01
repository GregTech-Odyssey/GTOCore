package com.gtocore.integration;

import net.minecraftforge.fml.ModList;

import lombok.Getter;

public enum Mods {

    CHISEL("chisel"),
    SOPHISTICATEDBACKPACKS("sophisticatedbackpacks"),
    BIOMESOPLENTY("biomesoplenty"),
    BIOMESWEVEGONE("biomeswevegone");

    @Getter
    private final boolean loaded;

    Mods(String modId) {
        loaded = ModList.get().isLoaded(modId);
    }
}
