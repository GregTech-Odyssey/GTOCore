package com.gto.gtocore.data.recipe.processing;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTOMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public interface BrineRecipes {

    static void init() {
        bromineProcess();
        iodineProcess();
    }

    private static void bromineProcess() {
        EVAPORATION_RECIPES.builder("brine_evaporation")
                .inputFluids(SaltWater.getFluid(20000))
                .outputFluids(RawBrine.getFluid(1000))
                .duration(1000).EUt(VA[HV]).save();
        FLUID_HEATER_RECIPES.builder("brine_heating")
                .inputFluids(RawBrine.getFluid(1000))
                .outputFluids(HotBrine.getFluid(1000))
                .duration(12000).EUt(VA[HV]).save();

        // Main chain
        CHEMICAL_RECIPES.builder("brine_chlorination")
                .inputFluids(HotBrine.getFluid(1000))
                .inputFluids(Chlorine.getFluid(1000))
                .outputFluids(HotChlorinatedBrominatedBrine.getFluid(2000))
                .duration(100).EUt(VA[HV]).save();
        CHEMICAL_RECIPES.builder("brine_filtration")
                .inputFluids(HotChlorinatedBrominatedBrine.getFluid(1000))
                .inputFluids(Chlorine.getFluid(1000))
                .inputFluids(Steam.getFluid(1000))
                .outputFluids(HotAlkalineDebrominatedBrine.getFluid(1000))
                .outputFluids(BrominatedChlorineVapor.getFluid(2000))
                .duration(300).EUt(VA[HV]).save();
        CHEMICAL_RECIPES.builder("brominated_chlorine_vapor_condensation")
                .inputFluids(BrominatedChlorineVapor.getFluid(1000))
                .inputFluids(Water.getFluid(1000))
                .outputFluids(AcidicBromineSolution.getFluid(1000))
                .outputFluids(Water.getFluid(1000))
                .duration(200).EUt(VA[HV]).save();
        CHEMICAL_RECIPES.builder("bromine_vapor_concentration")
                .inputFluids(AcidicBromineSolution.getFluid(1000))
                .inputFluids(Steam.getFluid(1000))
                .outputFluids(ConcentratedBromineSolution.getFluid(1000))
                .outputFluids(AcidicBromineExhaust.getFluid(1000))
                .duration(100).EUt(VA[HV]).save();
        DISTILLATION_RECIPES.builder("bromine_distillation")
                .inputFluids(ConcentratedBromineSolution.getFluid(1000))
                .outputFluids(Chlorine.getFluid(500))
                .outputFluids(Bromine.getFluid(1000))
                .duration(500).EUt(VA[HV]).save();

        // byproduct loop
        CHEMICAL_RECIPES.builder("brine_neutralization")
                .inputFluids(HotAlkalineDebrominatedBrine.getFluid(3000))
                .inputItems(dust, Potassium, 1)
                .outputFluids(HotDebrominatedBrine.getFluid(2000))
                .outputItems(dust, RockSalt, 2)
                .duration(100).EUt(VA[HV]).save();
        CHEMICAL_RECIPES.builder("debrominated_brine_raw_brine_mixing")
                .inputFluids(RawBrine.getFluid(1000))
                .inputFluids(HotDebrominatedBrine.getFluid(1000))
                .outputFluids(HotBrine.getFluid(1000))
                .outputFluids(DebrominatedBrine.getFluid(1000))
                .duration(200).EUt(VA[HV]).save();
        CHEMICAL_RECIPES.builder("acidic_bromine_exhaust_heating")
                .inputFluids(AcidicBromineExhaust.getFluid(1000))
                .inputFluids(HotBrine.getFluid(1000))
                .outputFluids(HotChlorinatedBrominatedBrine.getFluid(1000))
                .outputFluids(Steam.getFluid(3000))
                .duration(100).EUt(VA[HV]).save();

        CENTRIFUGE_RECIPES.builder("debrominated_brine_decomposition")
                .inputFluids(DebrominatedBrine.getFluid(2000))
                .outputFluids(SaltWater.getFluid(1000))
                .duration(60).EUt(VA[MV]).save();
    }

    private static void iodineProcess() {
        CHEMICAL_RECIPES.builder("brine_acidification")
                .inputFluids(HotBrine.getFluid(2000))
                .inputFluids(HydrochloricAcid.getFluid(1000))
                .outputFluids(HotAlkalineDebrominatedBrine.getFluid(2000))
                .outputFluids(HydrogenIodide.getFluid(1000))
                .duration(100).EUt(VHA[HV]).save();
        CHEMICAL_RECIPES.builder("iodine")
                .inputFluids(HydrogenIodide.getFluid(2000))
                .inputFluids(Oxygen.getFluid(1000))
                .outputItems(dust, Iodine, 1)
                .outputFluids(Water.getFluid(1000))
                .duration(1000).EUt(VHA[HV]).save();
    }
}
