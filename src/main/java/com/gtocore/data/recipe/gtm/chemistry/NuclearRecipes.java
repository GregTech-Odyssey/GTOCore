package com.gtocore.data.recipe.gtm.chemistry;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

final class NuclearRecipes {

    public static void init() {
        CHEMICAL_RECIPES.recipeBuilder("uranium_hexafluoride").duration(200).EUt(VA[LV])
                .inputItems(dust, Uraninite, 3)
                .inputFluids(HydrofluoricAcid, 4000)
                .inputFluids(Fluorine, 2000)
                .outputFluids(UraniumHexafluoride.getFluid(1000))
                .outputFluids(Water.getFluid(2000))
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("uranium_hexafluoride_separation").duration(160).EUt(VA[HV])
                .inputFluids(UraniumHexafluoride, 1000)
                .outputFluids(EnrichedUraniumHexafluoride.getFluid(100))
                .outputFluids(DepletedUraniumHexafluoride.getFluid(900))
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("enriched_uranium_hexafluoride_separation").duration(160).EUt(VA[MV])
                .inputFluids(EnrichedUraniumHexafluoride, 1000)
                .outputItems(dust, Uranium235)
                .outputFluids(Fluorine.getFluid(6000))
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("depleted_uranium_hexafluoride_separation").duration(160).EUt(VA[MV])
                .inputFluids(DepletedUraniumHexafluoride, 1000)
                .outputItems(dust, Uranium238)
                .outputFluids(Fluorine.getFluid(6000))
                .save();
    }
}
