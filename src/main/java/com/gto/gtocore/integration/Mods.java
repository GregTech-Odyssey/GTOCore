package com.gto.gtocore.integration;

import com.gregtechceu.gtceu.GTCEu;

public interface Mods {

    static boolean biomesoplenty() {
        return GTCEu.isModLoaded("biomesoplenty");
    }

    static boolean biomeswevegone() {
        return GTCEu.isModLoaded("biomeswevegone");
    }
}
