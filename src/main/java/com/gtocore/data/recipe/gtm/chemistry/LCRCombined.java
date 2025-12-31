package com.gtocore.data.recipe.gtm.chemistry;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.LARGE_CHEMICAL_RECIPES;

final class LCRCombined {

    public static void init() {
        LARGE_CHEMICAL_RECIPES.recipeBuilder("ammonia_shortcut")
                .circuitMeta(24)
                .inputFluids(Methane, 3000)
                .inputFluids(Nitrogen, 4000)
                .inputFluids(Oxygen, 3000)
                .outputFluids(Ammonia.getFluid(4000))
                .outputFluids(CarbonMonoxide.getFluid(3000))
                .EUt(VA[HV])
                .duration(320)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("methane_shortcut")
                .circuitMeta(24)
                .inputFluids(Hydrogen, 6000)
                .inputFluids(CarbonMonoxide, 1000)
                .outputFluids(Methane.getFluid(1000))
                .outputFluids(Water.getFluid(1000))
                .EUt(VA[LV])
                .duration(160)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phenol_acetone_shortcut")
                .circuitMeta(24)
                .inputFluids(Propene, 1000)
                .inputFluids(Benzene, 1000)
                .inputFluids(Oxygen, 1000)
                .inputFluids(PhosphoricAcid, 100)
                .outputFluids(Phenol.getFluid(1000))
                .outputFluids(Acetone.getFluid(1000))
                .duration(480).EUt(VA[LV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phenol_hcl_shortcut")
                .circuitMeta(24)
                .inputFluids(Benzene, 1000)
                .inputFluids(Chlorine, 2000)
                .inputFluids(Water, 1000)
                .outputFluids(Phenol.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .outputFluids(DilutedHydrochloricAcid.getFluid(1000))
                .duration(560).EUt(VA[LV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phenol_salt_shortcut")
                .circuitMeta(24)
                .inputFluids(Benzene, 2000)
                .inputFluids(Chlorine, 4000)
                .inputItems(dust, SodiumHydroxide, 6)
                .outputItems(dust, Salt, 4)
                .outputFluids(Phenol.getFluid(2000))
                .outputFluids(HydrochloricAcid.getFluid(2000))
                .duration(1120).EUt(VA[LV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("diesel_shortcut")
                .circuitMeta(24)
                .inputFluids(LightFuel, 20000)
                .inputFluids(HeavyFuel, 4000)
                .outputFluids(Diesel.getFluid(24000))
                .duration(100).EUt(VA[HV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("acetone_shortcut")
                .inputFluids(AceticAcid, 3000)
                .notConsumable(dust, Quicklime)
                .circuitMeta(24)
                .outputFluids(Acetone.getFluid(2000))
                .outputFluids(Oxygen.getFluid(1000))
                .duration(400).EUt(VA[HV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dinitrogen_tetroxide_shortcut")
                .circuitMeta(5)
                .inputFluids(Oxygen, 7000)
                .inputFluids(Nitrogen, 2000)
                .inputFluids(Hydrogen, 6000)
                .outputFluids(DinitrogenTetroxide.getFluid(1000))
                .outputFluids(Water.getFluid(3000))
                .duration(1100).EUt(VA[HV]).save();
    }
}
