package com.gtocore.integration;

import com.gregtechceu.gtceu.GTCEu;

public final class Mods {

    public static boolean chisel() {
        return GTCEu.isModLoaded("chisel");
    }

    public static boolean sophisticatedbackpacks() {
        return GTCEu.isModLoaded("sophisticatedbackpacks");
    }

    public static boolean biomesoplenty() {
        return GTCEu.isModLoaded("biomesoplenty");
    }

    public static boolean biomeswevegone() {
        return GTCEu.isModLoaded("biomeswevegone");
    }
}
