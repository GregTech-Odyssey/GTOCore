package com.gtocore.data.recipe.gtm.chemistry;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dustTiny;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.CHEMICAL_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.LARGE_CHEMICAL_RECIPES;

final class PolymerRecipes {

    public static void init() {
        polyethyleneProcess();
        polyvinylChlorideProcess();
        ptfeProcess();
        epoxyProcess();
        styreneButadieneProcess();
        polybenzimidazoleProcess();
        polycaprolactamProcess();
    }

    private static void polyethyleneProcess() {
        CHEMICAL_RECIPES.recipeBuilder("ethylene_from_glycerol")
                .inputFluids(Glycerol, 1000)
                .inputFluids(CarbonDioxide, 1000)
                .outputFluids(Ethylene.getFluid(2000))
                .outputFluids(Oxygen.getFluid(5000))
                .duration(400).EUt(200).save();
    }

    private static void polyvinylChlorideProcess() {
        CHEMICAL_RECIPES.recipeBuilder("vinyl_chloride_from_hydrochloric")
                .circuitMeta(3)
                .inputFluids(Oxygen, 1000)
                .inputFluids(HydrochloricAcid, 1000)
                .inputFluids(Ethylene, 1000)
                .outputFluids(Water.getFluid(1000))
                .outputFluids(VinylChloride.getFluid(1000))
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("vinyl_chloride_from_chlorine")
                .circuitMeta(1)
                .inputFluids(Chlorine, 2000)
                .inputFluids(Ethylene, 1000)
                .outputFluids(VinylChloride.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .duration(160).EUt(VA[LV]).save();
    }

    private static void ptfeProcess() {
        CHEMICAL_RECIPES.recipeBuilder("chloroform")
                .circuitMeta(1)
                .inputFluids(Chlorine, 6000)
                .inputFluids(Methane, 1000)
                .outputFluids(HydrochloricAcid.getFluid(3000))
                .outputFluids(Chloroform.getFluid(1000))
                .duration(80).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("tetrafluoroethylene_from_chloroform")
                .inputFluids(Chloroform, 2000)
                .inputFluids(HydrofluoricAcid, 4000)
                .outputFluids(HydrochloricAcid.getFluid(6000))
                .outputFluids(Tetrafluoroethylene.getFluid(1000))
                .duration(480).EUt(240).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("tetrafluoroethylene_from_methane")
                .circuitMeta(24)
                .inputFluids(HydrofluoricAcid, 4000)
                .inputFluids(Methane, 2000)
                .inputFluids(Chlorine, 12000)
                .outputFluids(Tetrafluoroethylene.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(12000))
                .duration(540).EUt(VA[IV]).save();
    }

    private static void epoxyProcess() {
        CHEMICAL_RECIPES.recipeBuilder("glycerol_from_seed_oil_ethanol")
                .inputItems(dustTiny, SodiumHydroxide)
                .inputFluids(SeedOil, 6000)
                .inputFluids(Ethanol, 1000)
                .outputFluids(Glycerol.getFluid(1000))
                .outputFluids(BioDiesel.getFluid(6000))
                .duration(600).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("glycerol_from_fish_oil_ethanol")
                .inputItems(dustTiny, SodiumHydroxide)
                .inputFluids(FishOil, 6000)
                .inputFluids(Ethanol, 1000)
                .outputFluids(Glycerol.getFluid(1000))
                .outputFluids(BioDiesel.getFluid(6000))
                .duration(600).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("allyl_chloride")
                .inputFluids(Propene, 1000)
                .inputFluids(Chlorine, 2000)
                .circuitMeta(1)
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .outputFluids(AllylChloride.getFluid(1000))
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("epichlorohydrin_from_glycerol")
                .circuitMeta(5)
                .inputFluids(Glycerol, 1000)
                .inputFluids(HydrochloricAcid, 1000)
                .outputFluids(Water.getFluid(2000))
                .outputFluids(Epichlorohydrin.getFluid(1000))
                .duration(480).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("epichlorohydrin_from_allyl_chloride")
                .inputItems(dust, SodiumHydroxide, 3)
                .inputFluids(AllylChloride, 1000)
                .inputFluids(HypochlorousAcid, 1000)
                .outputFluids(SaltWater.getFluid(1000))
                .outputFluids(Epichlorohydrin.getFluid(1000))
                .duration(480).EUt(VA[LV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("epichlorohydrin_shortcut_water")
                .circuitMeta(23)
                .inputFluids(Chlorine, 4000)
                .inputFluids(Propene, 1000)
                .inputFluids(Water, 1000)
                .inputItems(dust, SodiumHydroxide, 3)
                .outputFluids(Epichlorohydrin.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(2000))
                .outputFluids(SaltWater.getFluid(1000))
                .duration(640).EUt(VA[LV]).save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("epichlorohydrin_shortcut_hypochlorous")
                .circuitMeta(24)
                .inputFluids(Chlorine, 2000)
                .inputFluids(Propene, 1000)
                .inputFluids(HypochlorousAcid, 1000)
                .inputItems(dust, SodiumHydroxide, 3)
                .outputFluids(Epichlorohydrin.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .outputFluids(SaltWater.getFluid(1000))
                .duration(640).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("phenol_from_cumene")
                .inputFluids(Oxygen, 2000)
                .inputFluids(Cumene, 1000)
                .outputFluids(Phenol.getFluid(1000))
                .outputFluids(Acetone.getFluid(1000))
                .duration(160).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("bisphenol_a")
                .circuitMeta(1)
                .inputFluids(HydrochloricAcid, 1000)
                .inputFluids(Acetone, 1000)
                .inputFluids(Phenol, 2000)
                .outputFluids(BisphenolA.getFluid(1000))
                .outputFluids(DilutedHydrochloricAcid.getFluid(1000))
                .duration(160).EUt(VA[LV]).save();
    }

    private static void styreneButadieneProcess() {
        CHEMICAL_RECIPES.recipeBuilder("styrene_from_benzene")
                .inputFluids(Ethylene, 1000)
                .inputFluids(Benzene, 1000)
                .outputFluids(Hydrogen.getFluid(2000))
                .outputFluids(Styrene.getFluid(1000))
                .duration(120).EUt(VA[LV]).save();

        CHEMICAL_RECIPES.recipeBuilder("styrene_butadiene_rubber")
                .inputItems(dust, RawStyreneButadieneRubber, 9)
                .inputItems(dust, Sulfur)
                .outputFluids(StyreneButadieneRubber.getFluid(1296))
                .duration(600).EUt(VA[LV]).save();
    }

    private static void polybenzimidazoleProcess() {
        // 3,3-Diaminobenzidine
        LARGE_CHEMICAL_RECIPES.recipeBuilder("diaminobenzidine").EUt(VA[IV]).duration(100)
                .inputFluids(Dichlorobenzidine, 1000)
                .inputFluids(Ammonia, 2000)
                .notConsumable(dust, Zinc)
                .outputFluids(Diaminobenzidine.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(2000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("dichlorobenzidine").EUt(VA[EV]).duration(200)
                .inputItems(dustTiny, Copper)
                .inputFluids(Nitrochlorobenzene, 2000)
                .inputFluids(Hydrogen, 2000)
                .outputFluids(Dichlorobenzidine.getFluid(1000))
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dichlorobenzidine_9").EUt(VA[EV]).duration(1800)
                .inputItems(dust, Copper)
                .inputFluids(Nitrochlorobenzene, 18000)
                .inputFluids(Hydrogen, 18000)
                .outputFluids(Dichlorobenzidine.getFluid(9000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitrochlorobenzene").EUt(VA[HV]).duration(100)
                .inputFluids(NitrationMixture, 2000)
                .inputFluids(Chlorobenzene, 1000)
                .outputFluids(Nitrochlorobenzene.getFluid(1000))
                .outputFluids(DilutedSulfuricAcid.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("chlorobenzene").EUt(VA[LV]).duration(240)
                .inputFluids(Chlorine, 2000)
                .inputFluids(Benzene, 1000)
                .circuitMeta(1)
                .outputFluids(Chlorobenzene.getFluid(1000))
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .save();

        // Diphenyl Isophthalate
        LARGE_CHEMICAL_RECIPES.recipeBuilder("diphenyl_isophtalate").EUt(VA[IV]).duration(100)
                .inputFluids(Phenol, 2000)
                .inputFluids(SulfuricAcid, 1000)
                .inputFluids(PhthalicAcid, 1000)
                .outputFluids(DiphenylIsophtalate.getFluid(1000))
                .outputFluids(DilutedSulfuricAcid.getFluid(1000))
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phthalic_acid_from_dimethylbenzene").EUt(VA[EV]).duration(100)
                .inputItems(dustTiny, PotassiumDichromate)
                .inputFluids(Dimethylbenzene, 1000)
                .inputFluids(Oxygen, 2000)
                .outputFluids(PhthalicAcid.getFluid(1000))
                .outputFluids(Water.getFluid(2000))
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phthalic_acid_from_dimethylbenzene_9").EUt(VA[EV]).duration(900)
                .inputItems(dust, PotassiumDichromate)
                .inputFluids(Dimethylbenzene, 9000)
                .inputFluids(Oxygen, 18000)
                .outputFluids(PhthalicAcid.getFluid(9000))
                .outputFluids(Water.getFluid(18000))
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phthalic_acid_from_naphthalene").EUt(VA[LV]).duration(125)
                .inputFluids(Naphthalene, 2000)
                .inputFluids(SulfuricAcid, 1000)
                .inputItems(dustTiny, Potassium)
                .outputFluids(PhthalicAcid.getFluid(2500))
                .outputFluids(HydrogenSulfide.getFluid(1000))
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("phthalic_acid_from_naphthalene_9").EUt(VA[LV]).duration(1125)
                .inputFluids(Naphthalene, 18000)
                .inputFluids(SulfuricAcid, 9000)
                .inputItems(dust, Potassium)
                .outputFluids(PhthalicAcid.getFluid(22500))
                .outputFluids(HydrogenSulfide.getFluid(9000))
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("dimethylbenzene").EUt(VA[MV]).duration(4000)
                .inputFluids(Methane, 2000)
                .inputFluids(Benzene, 1000)
                .circuitMeta(1)
                .outputFluids(Dimethylbenzene.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("potassium_dichromate").EUt(VA[HV]).duration(100)
                .inputItems(dust, Saltpeter, 10)
                .inputItems(dust, ChromiumTrioxide, 8)
                .outputItems(dust, PotassiumDichromate, 11)
                .outputFluids(NitrogenDioxide.getFluid(2000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("chromium_trioxide").EUt(60).duration(100)
                .inputItems(dust, Chromium)
                .inputFluids(Oxygen, 3000)
                .outputItems(dust, ChromiumTrioxide, 4)
                .save();
    }

    private static void polycaprolactamProcess() {
        CHEMICAL_RECIPES.recipeBuilder("cyclohexane").EUt(VA[HV]).duration(400)
                .notConsumable(dust, Nickel)
                .inputFluids(Benzene, 1000)
                .inputFluids(Hydrogen, 6000)
                .outputFluids(Cyclohexane.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitrosyl_chloride").EUt(VA[LV]).duration(100)
                .inputFluids(Chlorine, 1000)
                .inputFluids(NitricOxide, 1000)
                .outputFluids(NitrosylChloride.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("cyclohexanone_oxime").EUt(VA[MV]).duration(100)
                .inputFluids(Cyclohexane, 1000)
                .inputFluids(NitrosylChloride, 1000)
                .outputItems(dust, CyclohexanoneOxime, 19)
                .outputFluids(HydrochloricAcid.getFluid(1000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("caprolactam").EUt(VA[HV]).duration(200)
                .inputItems(dust, CyclohexanoneOxime, 19)
                .inputFluids(SulfuricAcid, 1000)
                .outputItems(dust, Caprolactam, 19)
                .outputFluids(DilutedSulfuricAcid.getFluid(1000))
                .save();
    }
}
