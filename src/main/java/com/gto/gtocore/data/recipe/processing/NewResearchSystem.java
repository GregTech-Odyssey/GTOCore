package com.gto.gtocore.data.recipe.processing;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gto.gtocore.common.data.GTOItems.*;
import static com.gto.gtocore.common.data.GTOMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public final class NewResearchSystem {

    public static void init(Consumer<FinishedRecipe> provider) {
        //重写配方
        {
            CHEMICAL_RECIPES.recipeBuilder("polydimethylsiloxane_from_silicon")
                    .inputItems(dust, Silicon)
                    .inputFluids(HydrochloricAcid.getFluid(2000))
                    .inputFluids(Methanol.getFluid(2000))
                    .outputItems(dust, Polydimethylsiloxane, 3)
                    .outputFluids(DilutedHydrochloricAcid.getFluid(2000))
                    .circuitMeta(2)
                    .duration(480).EUt(96).save(provider);

            BLAST_RECIPES.recipeBuilder("silicon_boule")
                    .inputItems(dust, ElectronicGradeSilicon, 32)
                    .inputItems(dustSmall, GalliumArsenide)
                    .outputItems(SILICON_BOULE)
                    .blastFurnaceTemp(1784)
                    .duration(9000).EUt(VA[MV]).save(provider);

            BLAST_RECIPES.recipeBuilder("phosphorus_boule")
                    .inputItems(dust, ElectronicGradeSilicon, 64)
                    .inputItems(dust, Phosphorus, 8)
                    .inputItems(dustSmall, GalliumArsenide, 2)
                    .inputFluids(Nitrogen.getFluid(8000))
                    .outputItems(PHOSPHORUS_BOULE)
                    .blastFurnaceTemp(2484)
                    .duration(12000).EUt(VA[HV]).save(provider);

            BLAST_RECIPES.recipeBuilder("naquadah_boule")
                    .inputItems(block, ElectronicGradeSilicon, 16)
                    .inputItems(ingot, Naquadah)
                    .inputItems(dust, GalliumArsenide)
                    .inputFluids(Argon.getFluid(8000))
                    .outputItems(NAQUADAH_BOULE)
                    .blastFurnaceTemp(5400)
                    .duration(15000).EUt(VA[EV]).save(provider);

            BLAST_RECIPES.recipeBuilder("neutronium_boule")
                    .inputItems(block, UltraHighPuritySilicon, 32)
                    .inputItems(ingot, Neutronium, 4)
                    .inputItems(dust, GalliumArsenide, 2)
                    .inputFluids(Xenon.getFluid(8000))
                    .outputItems(NEUTRONIUM_BOULE)
                    .blastFurnaceTemp(6484)
                    .duration(18000).EUt(VA[IV]).save(provider);
        }

        // 前置电子级硅/超高纯硅产线
        {
            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_tetrachlorosilane"))
                    .inputItems(dust, Silicon, 1)
                    .inputFluids(Chlorine.getFluid(4000))
                    .outputFluids(Tetrachlorosilane.getFluid(1000))
                    .circuitMeta(1)
                    .duration(400)
                    .EUt(VA[LV])
                    .save(provider);

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_trichlorosilane"))
                    .inputItems(dust, Silicon, 1)
                    .inputFluids(HydrochloricAcid.getFluid(3000))
                    .outputFluids(Trichlorosilane.getFluid(1000))
                    .outputFluids(Hydrogen.getFluid(1000))
                    .circuitMeta(1)
                    .duration(300)
                    .EUt(VA[LV])
                    .save(provider);

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_electronic_grade_silicon_1"))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputItems(dust, Zinc, 2)
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputItems(dust, ZincChloride, 6)
                    .duration(200)
                    .EUt(VA[LV])
                    .save(provider);

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_electronic_grade_silicon_2"))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputItems(dust, Sodium, 4)
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputItems(dust, Salt, 8)
                    .duration(100)
                    .EUt(VA[LV])
                    .save(provider);

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_electronic_grade_silicon_3"))
                    .inputFluids(Trichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(1000))
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(600)
                    .EUt(VA[LV])
                    .save(provider);

            REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("chemical_make_silane"))
                    .inputFluids(Trichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(1000))
                    .outputFluids(Silane.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(40)
                    .blastFurnaceTemp(1500)
                    .EUt(VA[MV])
                    .save(provider);

            REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("chemical_make_electronic_grade_silicon_4"))
                    .inputFluids(Silane.getFluid(1000))
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputFluids(Hydrogen.getFluid(2000))
                    .duration(20)
                    .blastFurnaceTemp(1000)
                    .EUt(VA[MV])
                    .save(provider);

            CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("chemical_wash_electronic_grade_silicon"))
                    .inputItems(dust, ElectronicGradeSilicon, 1)
                    .inputFluids(HydrofluoricAcid.getFluid(1000))
                    .chancedOutput(ChemicalHelper.get(dust, PickledElectronicGradeSilicon, 1), 9500, 0)
                    .outputFluids(HydrofluoricAcid.getFluid(950))
                    .duration(200)
                    .EUt(VA[EV])
                    .save(provider);

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_pure_trichlorosilane"))
                    .inputItems(dust, PickledElectronicGradeSilicon, 1)
                    .inputFluids(HydrochloricAcid.getFluid(3000))
                    .outputFluids(PureTrichlorosilane.getFluid(1000))
                    .outputFluids(Hydrogen.getFluid(1000))
                    .circuitMeta(1)
                    .duration(300)
                    .EUt(VA[HV])
                    .save(provider);

            REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("chemical_make_pure_silane"))
                    .inputFluids(PureTrichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(1000))
                    .outputFluids(PureSilane.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(40)
                    .blastFurnaceTemp(1500)
                    .EUt(VA[EV])
                    .save(provider);

            DISTILLATION_RECIPES.recipeBuilder(GTOCore.id("distillation_pure_silane"))
                    .inputFluids(Silane.getFluid(1000))
                    .chancedOutput(ChemicalHelper.get(dustTiny, BariumChloride, 1), 1000, 0)
                    .outputFluids(PureSilane.getFluid(850))
                    .outputFluids(Steam.getFluid(100))
                    .chancedOutput(PhosphorusTrichloride.getFluid(30), 1000, 0)
                    .duration(100)
                    .EUt(VA[HV])
                    .save(provider);

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder(GTOCore.id("make_high_purity_silicon_boule"))
                    .inputFluids(PureSilane.getFluid(32000))
                    .outputItems(HIGH_PURITY_SILICON_BOULE, 1)
                    .outputFluids(Hydrogen.getFluid(64000))
                    .duration(12000)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save(provider);

            BLAST_RECIPES.recipeBuilder(GTOCore.id("make_regional_smelting_silicon_boule"))
                    .inputItems(HIGH_PURITY_SILICON_BOULE, 1)
                    .inputFluids(Helium.getFluid(1000))
                    .outputItems(REGIONAL_SMELTING_SILICON_BOULE, 1)
                    .duration(6000)
                    .blastFurnaceTemp(5000)
                    .EUt(VA[HV])
                    .save(provider);

            LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("make_etched_silicon_boule"))
                    .inputItems(REGIONAL_SMELTING_SILICON_BOULE, 1)
                    .notConsumable(dust, Radium, 1)
                    .outputItems(ETCHED_SILICON_BOULE, 1)
                    .chancedOutput(CarbonMonoxide.getFluid(10), 100, 0)
                    .duration(600)
                    .EUt(VA[ZPM])
                    .addCondition(new VacuumCondition(4))
                    .save(provider);

            AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("make_etched_silicon_boule"))
                    .inputItems(ETCHED_SILICON_BOULE, 1)
                    .outputItems(FLOATING_ZONE_PURIFICATION_SILICON_BOULE, 1)
                    .duration(2400)
                    .EUt(VA[EV])
                    .addCondition(new VacuumCondition(4))
                    .save(provider);

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_tetrafluorosilane_solution"))
                    .inputItems(FLOATING_ZONE_PURIFICATION_SILICON_BOULE, 1)
                    .inputFluids(HydrofluoricAcid.getFluid(416000))
                    .outputFluids(TetrafluorosilaneSolution.getFluid(104000))
                    .outputFluids(Hydrogen.getFluid(52000))
                    .duration(6000)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save(provider);

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_ultra_high_purity_silicon"))
                    .inputFluids(TetrafluorosilaneSolution.getFluid(4000))
                    .inputFluids(Hydrogen.getFluid(2000))
                    .outputItems(dust, UltraHighPuritySilicon, 1)
                    .outputFluids(HydrofluoricAcid.getFluid(16000))
                    .duration(200)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save(provider);

            BLAST_RECIPES.recipeBuilder("make_high_purity_single_crystal_silicon")
                    .inputItems(block, UltraHighPuritySilicon, 16)
                    .inputItems(dust, GalliumArsenide)
                    .inputFluids(Xenon.getFluid(8000))
                    .outputItems(HIGH_PURITY_SINGLE_CRYSTAL_SILICON)
                    .blastFurnaceTemp(8684)
                    .duration(21000).EUt(VA[ZPM]).save(provider);
        }



    }
}
