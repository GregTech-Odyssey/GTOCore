package com.gto.gtocore.api.capability.recipe;

import com.gregtechceu.gtceu.api.registry.GTRegistries;

public final class GTORecipeCapabilities {

    public static void init() {
        GTRegistries.RECIPE_CAPABILITIES.register(ManaRecipeCapability.CAP.name, ManaRecipeCapability.CAP);
    }
}
