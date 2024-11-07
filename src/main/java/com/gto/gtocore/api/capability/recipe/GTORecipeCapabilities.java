package com.gto.gtocore.api.capability.recipe;

import com.gregtechceu.gtceu.api.registry.GTRegistries;

public class GTORecipeCapabilities {

    public static final ManaRecipeCapability MANA = ManaRecipeCapability.CAP;

    public static void init() {
        GTRegistries.RECIPE_CAPABILITIES.register(MANA.name, MANA);
    }
}
