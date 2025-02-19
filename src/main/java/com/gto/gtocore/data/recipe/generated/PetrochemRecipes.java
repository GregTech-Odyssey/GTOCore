package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.LV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.STEAM_CRACKING_RECIPES;

public final class PetrochemRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        crack(provider, SulfuricHeavyFuel, SeverelySteamCrackedHeavyFuel, LightlySteamCrackedHeavyFuel);
        crack(provider, SulfuricLightFuel, SeverelySteamCrackedLightFuel, LightlySteamCrackedLightFuel);
        crack(provider, SulfuricNaphtha, SeverelySteamCrackedNaphtha, LightlySteamCrackedNaphtha);
        crack(provider, SulfuricGas, SeverelySteamCrackedGas, LightlySteamCrackedGas);
    }

    private static void crack(Consumer<FinishedRecipe> provider, Material... cracked) {
        STEAM_CRACKING_RECIPES.recipeBuilder(GTOCore.id("severely_steam_crack_" + cracked[0].getName()))
                .circuitMeta(1)
                .inputFluids(cracked[0].getFluid(1000))
                .outputFluids(cracked[1].getFluid(400))
                .duration(200)
                .EUt(VA[LV])
                .save(provider);

        STEAM_CRACKING_RECIPES.recipeBuilder(GTOCore.id("steam_crack_" + cracked[0].getName()))
                .circuitMeta(2)
                .inputFluids(cracked[0].getFluid(1000))
                .outputFluids(cracked[2].getFluid(400))
                .duration(200)
                .EUt(VA[LV])
                .save(provider);
    }
}
