package com.gtocore.data.recipe.processing;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class NaquadahProcess {

    public static void init() {
        REACTION_FURNACE_RECIPES.recipeBuilder("low_purity_naquadah_emulsion").EUt(VA[HV]).duration(100)
                .inputItems(dust, NaquadahOxideMixture, 6)
                .inputFluids(FluoroantimonicAcid, 2000)
                .outputItems(dust, TitaniumTrifluoride, 1)
                .outputFluids(LowPurityNaquadahEmulsion.getFluid(2000))
                .blastFurnaceTemp(4200)
                .save();

        MIXER_RECIPES.recipeBuilder("low_purity_naquadah_solution").EUt(VA[HV]).duration(160)
                .inputItems(dust, SodiumHydroxide, 3)
                .inputFluids(LowPurityNaquadahEmulsion, 5000)
                .outputItems(dust, Naquadah, 4)
                .outputFluids(LowPurityNaquadahSolution.getFluid(3000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("antimony_trioxide").EUt(VA[ULV]).duration(60)
                .inputItems(dust, Antimony, 2)
                .inputFluids(Oxygen, 3000)
                .outputItems(dust, AntimonyTrioxide, 5)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("antimony_trifluoride").EUt(VA[LV]).duration(60)
                .inputItems(dust, AntimonyTrioxide, 5)
                .inputFluids(HydrofluoricAcid, 6000)
                .outputItems(dust, AntimonyTrifluoride, 8)
                .outputFluids(Water.getFluid(3000))
                .save();

        CHEMICAL_RECIPES.recipeBuilder("fluoroantimonic_acid").EUt(VA[HV]).duration(300)
                .inputItems(dust, AntimonyTrifluoride, 4)
                .inputFluids(HydrofluoricAcid, 4000)
                .outputFluids(FluoroantimonicAcid.getFluid(1000))
                .outputFluids(Hydrogen.getFluid(2000))
                .save();

        // STARTING POINT

        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("naquadah_separation")
                .inputFluids(LowPurityNaquadahSolution, 6000)
                .inputItems(dust, Naquadah, 2)
                .outputFluids(ImpureEnrichedNaquadahSolution.getFluid(4000))
                .duration(200)
                .addData("ev_min", 220)
                .addData("ev_max", 230)
                .addData("evt", 56)
                .save();

        // ENRICHED NAQUADAH PROCESS

        CHEMICAL_RECIPES.recipeBuilder("hexafluoride_enriched_naquadah_solution")
                .inputFluids(ImpureEnrichedNaquadahSolution, 1000)
                .inputFluids(Fluorine, 6000)
                .outputFluids(HexafluorideEnrichedNaquadahSolution.getFluid(1000))
                .EUt(VA[IV])
                .duration(150)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("xenon_hexafluoro_enriched_naquadate")
                .inputFluids(HexafluorideEnrichedNaquadahSolution, 1000)
                .inputFluids(Xenon, 1000)
                .outputFluids(XenonHexafluoroEnrichedNaquadate.getFluid(1000))
                .EUt(VA[HV])
                .duration(200)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("xenoauric_fluoroantimonic_acid")
                .inputFluids(XenoauricFluoroantimonicAcid, 1000)
                .outputItems(dust, Gold)
                .outputItems(dust, AntimonyTrifluoride)
                .outputFluids(Xenon.getFluid(1000))
                .outputFluids(Fluorine.getFluid(3000))
                .EUt(VA[MV])
                .duration(800)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("gold_chloride")
                .inputItems(dust, Gold, 2)
                .inputFluids(Chlorine, 6000)
                .outputFluids(GoldChloride.getFluid(1000))
                .EUt(VA[MV])
                .duration(180)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("bromine_trifluoride")
                .inputFluids(Bromine, 1000)
                .inputFluids(Fluorine, 3000)
                .outputFluids(BromineTrifluoride.getFluid(1000))
                .EUt(VA[LV])
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("gold_chloride")
                .inputFluids(GoldChloride, 1000)
                .inputFluids(BromineTrifluoride, 2000)
                .outputItems(dust, GoldTrifluoride, 8)
                .outputFluids(Bromine.getFluid(2000))
                .outputFluids(Chlorine.getFluid(6000))
                .EUt(VA[MV])
                .duration(200)
                .save();

        LARGE_CHEMICAL_RECIPES.recipeBuilder("enriched_naquadah_residue_solution")
                .inputItems(dust, GoldTrifluoride, 4)
                .inputFluids(XenonHexafluoroEnrichedNaquadate, 1000)
                .inputFluids(FluoroantimonicAcid, 1000)
                .inputFluids(Hydrogen, 9000)
                .outputFluids(EnrichedNaquadahSolution.getFluid(1000))
                .outputFluids(XenoauricFluoroantimonicAcid.getFluid(1000))
                .EUt(VA[LuV])
                .duration(160)
                .save();

        MIXER_RECIPES.recipeBuilder("enriched_naquadah_solution_separation").EUt(VA[HV]).duration(100)
                .inputFluids(EnrichedNaquadahSolution, 1000)
                .inputFluids(SulfuricAcid, 2000)
                .outputFluids(AcidicEnrichedNaquadahSolution.getFluid(3000))
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("acidic_enriched_naquadah_separation").EUt(VA[HV]).duration(100)
                .inputFluids(AcidicEnrichedNaquadahSolution, 3000)
                .outputFluids(EnrichedNaquadahWaste.getFluid(2000))
                .outputFluids(Fluorine.getFluid(500))
                .outputItems(dust, EnrichedNaquadahSulfate, 6) // Nq+SO4
                .save();

        REACTION_FURNACE_RECIPES.recipeBuilder("enriched_naquadah_sulfate_separation").EUt(VA[IV]).duration(500)
                .blastFurnaceTemp(7000)
                .inputItems(dust, EnrichedNaquadahSulfate, 6)
                .inputFluids(Hydrogen, 2000)
                .outputItems(ingotHot, NaquadahEnriched)
                .outputFluids(SulfuricAcid.getFluid(1000))
                .save();

        DISTILLATION_RECIPES.recipeBuilder("enriched_naquadah_waste_separation").EUt(VA[HV]).duration(300)
                .inputFluids(EnrichedNaquadahWaste, 2000)
                .chancedOutput(dust, BariumSulfide, 5000, 0)
                .outputFluids(SulfuricAcid.getFluid(500))
                .outputFluids(EnrichedNaquadahSolution.getFluid(250))
                .outputFluids(NaquadriaSolution.getFluid(100))
                .save();

        // NAQUADRIA PROCESS
        NEUTRON_ACTIVATOR_RECIPES.recipeBuilder("enriched_naquadah_separation")
                .inputFluids(FluoroantimonicAcid, 1000)
                .inputItems(dust, NaquadahEnriched, 6)
                .outputFluids(ImpureNaquadriaSolution.getFluid(4000))
                .outputItems(dust, IndiumPhosphide, 1)
                .addData("ev_min", 460)
                .addData("ev_max", 480)
                .addData("evt", 124)
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("hexafluoride_naquadria_solution")
                .inputFluids(ImpureNaquadriaSolution, 1000)
                .inputFluids(Fluorine, 6000)
                .outputFluids(HexafluorideNaquadriaSolution.getFluid(1000))
                .EUt(VA[IV])
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("radon_naquadria")
                .inputFluids(Radon, 1000)
                .inputFluids(Fluorine, 2000)
                .outputFluids(RadonDifluoride.getFluid(1000))
                .EUt(VA[MV])
                .duration(120)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("radon_naquadria_octafluoride")
                .inputFluids(HexafluorideNaquadriaSolution, 1000)
                .inputFluids(RadonDifluoride, 1000)
                .outputFluids(RadonNaquadriaOctafluoride.getFluid(1000))
                .outputFluids(NaquadriaSolution.getFluid(1000))
                .EUt(VA[HV])
                .duration(600)
                .save();

        MIXER_RECIPES.recipeBuilder("naquadria_solution_separation").EUt(VA[HV]).duration(100)
                .inputFluids(NaquadriaSolution, 1000)
                .inputFluids(SulfuricAcid, 2000)
                .outputFluids(AcidicNaquadriaSolution.getFluid(3000))
                .save();

        CENTRIFUGE_RECIPES.recipeBuilder("acidic_naquadria_solution_separation").EUt(VA[HV]).duration(100)
                .inputFluids(AcidicNaquadriaSolution, 3000)
                .outputFluids(NaquadriaWaste.getFluid(2000))
                .outputFluids(Fluorine.getFluid(500))
                .outputItems(dustTiny, NaquadriaSulfate, 2)
                .chancedOutput(dustTiny, NaquadriaSulfate, 1, 6000, 100)
                .chancedOutput(dustTiny, NaquadriaSulfate, 1, 4000, 100)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("xenon_trioxide")
                .inputFluids(Xenon, 1000)
                .inputFluids(Oxygen, 3000)
                .outputFluids(XenonTrioxide.getFluid(1000))
                .EUt(VA[HV])
                .duration(200)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("caesium_fluoride")
                .inputItems(dust, Caesium)
                .inputFluids(Fluorine, 1000)
                .outputFluids(CaesiumFluoride.getFluid(1000))
                .EUt(7)
                .duration(60)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("caesium_xenontrioxide_fluoride")
                .inputFluids(CaesiumFluoride, 1000)
                .inputFluids(XenonTrioxide, 1000)
                .outputFluids(CaesiumXenontrioxideFluoride.getFluid(1000))
                .EUt(VA[MV])
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("naquadria_caesium_xenonnonfluoride")
                .inputFluids(RadonNaquadriaOctafluoride, 1000)
                .inputFluids(CaesiumXenontrioxideFluoride, 1000)
                .outputFluids(NaquadriaCaesiumXenonnonfluoride.getFluid(1000))
                .outputFluids(RadonTrioxide.getFluid(1000))
                .EUt(VA[IV])
                .duration(400)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitryl_fluoride")
                .inputFluids(NitrogenDioxide, 1000)
                .inputFluids(Fluorine, 1000)
                .outputFluids(NitrylFluoride.getFluid(1000))
                .EUt(VA[LV])
                .duration(160)
                .save();

        CHEMICAL_RECIPES.recipeBuilder("nitrosonium_octafluoroxenate")
                .inputFluids(NaquadriaCaesiumXenonnonfluoride, 1000)
                .inputFluids(NitrylFluoride, 2000)
                .outputFluids(NaquadriaCaesiumfluoride.getFluid(1000))
                .outputFluids(NitrosoniumOctafluoroxenate.getFluid(1000))
                .EUt(VA[EV])
                .duration(400)
                .save();

        MIXER_RECIPES.recipeBuilder("acidic_naquadria_caesiumfluoride")
                .inputFluids(SulfuricAcid, 2000)
                .inputFluids(NaquadriaCaesiumfluoride, 1000)
                .outputFluids(AcidicNaquadriaCaesiumfluoride.getFluid(3000))
                .EUt(VA[IV])
                .duration(200)
                .save();

        REACTION_FURNACE_RECIPES.recipeBuilder("naquadria_sulfate_separation").EUt(VA[ZPM]).duration(600).blastFurnaceTemp(9000)
                .inputItems(dust, NaquadriaSulfate, 6)
                .inputFluids(Hydrogen, 2000)
                .outputItems(ingotHot, Naquadria)
                .outputFluids(SulfuricAcid.getFluid(1000))
                .save();

        DISTILLATION_RECIPES.recipeBuilder("naquadria_waste_separation").EUt(VA[EV]).duration(400)
                .inputFluids(NaquadriaWaste, 2000)
                .chancedOutput(dust, TriniumSulfide, 8000, 500)
                .outputFluids(NaquadriaSolution.getFluid(250))
                .outputFluids(EnrichedNaquadahSolution.getFluid(100))
                .save();

        // TRINIUM

        REACTION_FURNACE_RECIPES.recipeBuilder("trinium_sulfide_separation").duration(750).EUt(VA[LuV])
                .blastFurnaceTemp(Trinium.getBlastTemperature())
                .inputItems(dust, TriniumSulfide, 2)
                .inputItems(dust, Zinc)
                .outputItems(ingotHot, Trinium)
                .outputItems(dust, ZincSulfide, 2)
                .save();

        // BYPRODUCT PROCESSING

        // Titanium Trifluoride
        REACTION_FURNACE_RECIPES.recipeBuilder("titanium_trifluoride_separation").EUt(VA[HV]).duration(900).blastFurnaceTemp(1941)
                .inputItems(dust, TitaniumTrifluoride, 4)
                .inputFluids(Hydrogen, 3000)
                .outputItems(ingotHot, Titanium)
                .outputFluids(HydrofluoricAcid.getFluid(3000))
                .save();

        // Indium Phosphide
        CHEMICAL_RECIPES.recipeBuilder("indium_phosphide_separation").duration(30).EUt(VA[ULV])
                .inputItems(dust, IndiumPhosphide, 2)
                .inputItems(dust, Calcium)
                .outputItems(dust, Indium)
                .outputItems(dust, CalciumPhosphide, 2)
                .save();
    }
}
