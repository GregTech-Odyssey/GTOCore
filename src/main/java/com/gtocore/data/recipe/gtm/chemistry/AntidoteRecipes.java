package com.gtocore.data.recipe.gtm.chemistry;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

final class AntidoteRecipes {

    public static void init() {
        paracetamolProcess();
        potassiumHydroxideProcess();
        prussianBlueProcess();
        dtpaProcess();
    }

    private static void paracetamolProcess() {
        CHEMICAL_RECIPES.recipeBuilder("acetic_anhydride")
                .inputFluids(Ethenone, 1000)
                .inputFluids(AceticAcid, 1000)
                .outputFluids(AceticAnhydride.getFluid(1000))
                .duration(200).EUt(VH[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("aminophenol")
                .inputFluids(Phenol, 1000)
                .inputFluids(NitrationMixture, 1000)
                .notConsumable(dust, Iron)
                .outputFluids(AminoPhenol.getFluid(1000))
                .outputFluids(DilutedSulfuricAcid.getFluid(1000))
                .duration(300).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("paracetamol")
                .inputFluids(AceticAnhydride, 1000)
                .inputFluids(AminoPhenol, 1000)
                .outputItems(dust, Paracetamol, 1)
                .outputFluids(AceticAcid.getFluid(1000))
                .duration(100).EUt(VA[LV]).save();
    }

    private static void potassiumHydroxideProcess() {
        CHEMICAL_RECIPES.recipeBuilder("potassium_iodide")
                .inputItems(dust, PotassiumHydroxide, 3)
                .inputItems(dust, Iodine, 1)
                .outputItems(dust, PotassiumIodide, 1)
                .outputFluids(Oxygen.getFluid(1000))
                .outputFluids(Hydrogen.getFluid(1000))
                .duration(100).EUt(VA[MV]).save();
    }

    private static void prussianBlueProcess() {
        CHEMICAL_RECIPES.recipeBuilder("ammonium_formate")
                .inputFluids(Ammonia, 1000)
                .inputFluids(FormicAcid, 1000)
                .outputFluids(AmmoniumFormate.getFluid(1000))
                .duration(100).EUt(VA[MV]).save();

        FLUID_HEATER_RECIPES.recipeBuilder("formamide")
                .inputFluids(AmmoniumFormate, 100)
                .outputFluids(Formamide.getFluid(100))
                .duration(16).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_cyanide")
                .inputItems(dust, PotassiumHydroxide, 3)
                .inputFluids(Formamide, 1000)
                .outputItems(dust, PotassiumCyanide, 3)
                .outputFluids(Water.getFluid(1000))
                .duration(100).EUt(VHA[MV]).save();

        CHEMICAL_RECIPES.recipeBuilder("hydrogen_cyanide")
                .inputFluids(Methane, 1000)
                .inputFluids(Ammonia, 1000)
                .inputFluids(Oxygen, 3000)
                .notConsumable(dust, Platinum)
                .outputFluids(HydrogenCyanide.getFluid(1000))
                .outputFluids(Water.getFluid(3000))
                .duration(100).EUt(VA[HV]).save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_carbonate")
                .inputItems(dust, PotassiumHydroxide, 6)
                .inputFluids(CarbonDioxide, 1000)
                .outputItems(dust, PotassiumCarbonate, 6)
                .outputFluids(Water.getFluid(1000))
                .duration(100).EUt(VHA[MV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("calcium_ferrocyanide")
                .inputFluids(HydrogenCyanide, 6000)
                .inputFluids(Iron2Chloride, 1000)
                .inputFluids(Water, 7000)
                .inputItems(dust, CalciumHydroxide, 10)
                .outputItems(dust, CalciumFerrocyanide, 15)
                .outputFluids(HydrochloricAcid.getFluid(2000))
                .duration(300).EUt(VA[HV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("potassium_ferrocyanide")
                .inputItems(dust, CalciumFerrocyanide, 15)
                .inputItems(dust, RockSalt, 8)
                .outputItems(dust, PotassiumFerrocyanide, 17)
                .outputItems(dust, CalciumHydroxide, 10)
                .outputFluids(HydrochloricAcid.getFluid(4000))
                .outputFluids(Water.getFluid(4000))
                .duration(300).EUt(VA[HV]).save();

        CHEMICAL_RECIPES.recipeBuilder("prussian_blue")
                .inputItems(dust, PotassiumFerrocyanide, 51)
                .inputFluids(Iron3Chloride, 4000)
                .outputItems(dust, PrussianBlue, 1)
                .outputItems(dust, RockSalt, 24)
                .duration(500).EUt(VA[HV]).save();
    }

    private static void dtpaProcess() {
        CHEMICAL_RECIPES.recipeBuilder("dichloroethane")
                .circuitMeta(2)
                .inputFluids(Ethylene, 1000)
                .inputFluids(Chlorine, 2000)
                .notConsumableFluid(Iron3Chloride.getFluid(100))
                .outputFluids(Dichloroethane.getFluid(1000))
                .duration(100).EUt(VA[MV]).save();

        CHEMICAL_RECIPES.recipeBuilder("diethylenetriamine")
                .inputFluids(Dichloroethane, 2000)
                .inputFluids(Ammonia, 3000)
                .outputFluids(Diethylenetriamine.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(4000))
                .duration(100).EUt(VA[MV]).save();

        CHEMICAL_RECIPES.recipeBuilder("glycolonitrile")
                .inputFluids(Formaldehyde, 1000)
                .inputFluids(HydrogenCyanide, 1000)
                .outputFluids(Glycolonitrile.getFluid(1000))
                .duration(100).EUt(VA[MV]).save();

        CHEMICAL_RECIPES.recipeBuilder("diethylenetriamine_pentaacetonitrile")
                .inputFluids(Diethylenetriamine, 1000)
                .inputFluids(Glycolonitrile, 5000)
                .outputFluids(DiethylenetriaminePentaacetonitrile.getFluid(1000))
                .outputFluids(Water.getFluid(5000))
                .duration(100).EUt(VA[HV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("diethylenetriaminepentaacetic_acid")
                .inputItems(dust, SodiumHydroxide, 15)
                .inputFluids(DiethylenetriaminePentaacetonitrile, 1000)
                .inputFluids(Oxygen, 15000)
                .outputItems(dust, DiethylenetriaminepentaaceticAcid, 1)
                .outputItems(dust, SodiumNitrite, 20)
                .duration(100).EUt(VA[EV]).save();
    }
}
