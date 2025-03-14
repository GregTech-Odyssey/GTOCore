package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import static com.gregtechceu.gtceu.api.GTValues.LV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.STEAM_CRACKING_RECIPES;

public interface PetrochemRecipes {

    static void init() {
        crack(SulfuricHeavyFuel, SeverelySteamCrackedHeavyFuel, LightlySteamCrackedHeavyFuel);
        crack(SulfuricLightFuel, SeverelySteamCrackedLightFuel, LightlySteamCrackedLightFuel);
        crack(SulfuricNaphtha, SeverelySteamCrackedNaphtha, LightlySteamCrackedNaphtha);
        crack(SulfuricGas, SeverelySteamCrackedGas, LightlySteamCrackedGas);
    }

    private static void crack(Material... cracked) {
        STEAM_CRACKING_RECIPES.recipeBuilder(GTOCore.id("severely_steam_crack_" + cracked[0].getName()))
                .circuitMeta(1)
                .inputFluids(Hydrogen.getFluid(500))
                .inputFluids(cracked[0].getFluid(1000))
                .outputFluids(cracked[1].getFluid(400))
                .duration(200)
                .EUt(VA[LV])
                .save();

        STEAM_CRACKING_RECIPES.recipeBuilder(GTOCore.id("steam_crack_" + cracked[0].getName()))
                .circuitMeta(2)
                .inputFluids(Hydrogen.getFluid(500))
                .inputFluids(cracked[0].getFluid(1000))
                .outputFluids(cracked[2].getFluid(400))
                .duration(200)
                .EUt(VA[LV])
                .save();
    }
}
