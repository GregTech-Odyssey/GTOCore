package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.plateDouble;
import static com.gregtechceu.gtceu.common.data.GTItems.NEUTRON_REFLECTOR;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.TinAlloy;
import static com.gtocore.common.data.GTORecipeTypes.ELECTROPLATING_RECIPES;

public class ElectroPlating {

    public static void init() {
        ELECTROPLATING_RECIPES.builder("titanium_nanotube_precursor")
                .inputItems(foil, Titanium, 16)
                .notConsumable(foil, Platinum, 16)
                .inputFluids(GTOMaterials.DiethyleneGlycol.getFluid(96 * 16))
                .inputFluids(GTOMaterials.AmmoniumBifluorideSolution.getFluid(1600))
                .inputFluids(GTMaterials.DistilledWater.getFluid(1600))
                .outputItems(dust, GTOMaterials.TitaniumNanotubePrecursor)
                .outputFluids(GTOMaterials.DiethyleneGlycol.getFluid(72 * 16))
                .outputFluids(GTMaterials.HydrofluoricAcid.getFluid(72 * 16))
                .EUt(60)
                .duration(20 * 1800)
                .save();

        ELECTROPLATING_RECIPES.recipeBuilder("neutron_reflector").duration(4000).EUt(VA[MV])
                .inputItems(plate, Ruridit)
                .inputItems(plateDouble, Beryllium, 4)
                .inputItems(plateDouble, TungstenCarbide, 2)
                .inputFluids(TinAlloy.getFluid(2 * L << 5))
                .outputItems(NEUTRON_REFLECTOR)
                .outputFluids(TinAlloy.getFluid(L << 5))
                .save();
    }
}
