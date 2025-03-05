package com.gto.gtocore.data.recipe.processing;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.item.MultiStepItemHelper;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTBlocks.OPTICAL_PIPES;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gto.gtocore.common.data.GTOItems.*;
import static com.gto.gtocore.common.data.GTOMaterials.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public interface NewResearchSystem {

    static void init(Consumer<FinishedRecipe> provider) {
        // 重写配方
        {
            CHEMICAL_RECIPES.recipeBuilder("polydimethylsiloxane_from_silicon")
                    .inputItems(dust, Silicon)
                    .inputFluids(HydrochloricAcid.getFluid(2000))
                    .inputFluids(Methanol.getFluid(2000))
                    .outputItems(dust, Polydimethylsiloxane, 3)
                    .outputFluids(DilutedHydrochloricAcid.getFluid(2000))
                    .circuitMeta(2)
                    .duration(480).EUt(96).save();

            BLAST_RECIPES.recipeBuilder("silicon_boule")
                    .inputItems(dust, ElectronicGradeSilicon, 32)
                    .inputItems(dustSmall, GalliumArsenide)
                    .outputItems(SILICON_BOULE)
                    .blastFurnaceTemp(1784)
                    .duration(9000).EUt(VA[MV]).save();

            BLAST_RECIPES.recipeBuilder("phosphorus_boule")
                    .inputItems(dust, ElectronicGradeSilicon, 64)
                    .inputItems(dust, Phosphorus, 8)
                    .inputItems(dustSmall, GalliumArsenide, 2)
                    .inputFluids(Nitrogen.getFluid(8000))
                    .outputItems(PHOSPHORUS_BOULE)
                    .blastFurnaceTemp(2484)
                    .duration(12000).EUt(VA[HV]).save();

            BLAST_RECIPES.recipeBuilder("naquadah_boule")
                    .inputItems(block, ElectronicGradeSilicon, 16)
                    .inputItems(ingot, Naquadah)
                    .inputItems(dust, GalliumArsenide)
                    .inputFluids(Argon.getFluid(8000))
                    .outputItems(NAQUADAH_BOULE)
                    .blastFurnaceTemp(5400)
                    .duration(15000).EUt(VA[EV]).save();

            BLAST_RECIPES.recipeBuilder("neutronium_boule")
                    .inputItems(block, UltraHighPuritySilicon, 32)
                    .inputItems(ingot, Neutronium, 4)
                    .inputItems(dust, GalliumArsenide, 2)
                    .inputFluids(Xenon.getFluid(8000))
                    .outputItems(NEUTRONIUM_BOULE)
                    .blastFurnaceTemp(6484)
                    .duration(18000).EUt(VA[IV]).save();
        }

        // 低相关度配方
        {
            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("make_carbon_tetrafluoride_1"))
                    .inputItems(dust, Carbon, 1)
                    .inputFluids(Fluorine.getFluid(2000))
                    .outputFluids(CarbonTetrafluoride.getFluid(1000))
                    .circuitMeta(1)
                    .duration(200)
                    .EUt(VA[EV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("make_carbon_tetrafluoride_2"))
                    .inputItems(dust, Carbon, 1)
                    .notConsumableFluid(BromineTrifluoride.getFluid(1000))
                    .inputFluids(Fluorine.getFluid(2000))
                    .outputFluids(CarbonTetrafluoride.getFluid(1000))
                    .circuitMeta(2)
                    .duration(20)
                    .EUt(VA[HV])
                    .save();
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
                    .save();

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_trichlorosilane"))
                    .inputItems(dust, Silicon, 1)
                    .inputFluids(HydrochloricAcid.getFluid(3000))
                    .outputFluids(Trichlorosilane.getFluid(1000))
                    .outputFluids(Hydrogen.getFluid(1000))
                    .circuitMeta(1)
                    .duration(300)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_electronic_grade_silicon_1"))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputItems(dust, Zinc, 2)
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputItems(dust, ZincChloride, 6)
                    .duration(200)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_electronic_grade_silicon_2"))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputItems(dust, Sodium, 4)
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputItems(dust, Salt, 8)
                    .duration(100)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_electronic_grade_silicon_3"))
                    .inputFluids(Trichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(1000))
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(600)
                    .EUt(VA[LV])
                    .save();

            REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("chemical_make_silane"))
                    .inputFluids(Trichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(1000))
                    .outputFluids(Silane.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(40)
                    .blastFurnaceTemp(1500)
                    .EUt(VA[MV])
                    .save();

            REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("chemical_make_electronic_grade_silicon_4"))
                    .inputFluids(Silane.getFluid(1000))
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputFluids(Hydrogen.getFluid(2000))
                    .duration(20)
                    .blastFurnaceTemp(1000)
                    .EUt(VA[MV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("chemical_wash_electronic_grade_silicon"))
                    .inputItems(dust, ElectronicGradeSilicon, 1)
                    .inputFluids(HydrofluoricAcid.getFluid(1000))
                    .chancedOutput(ChemicalHelper.get(dust, PickledElectronicGradeSilicon, 1), 9500, 0)
                    .outputFluids(HydrofluoricAcid.getFluid(950))
                    .duration(200)
                    .EUt(VA[EV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_pure_trichlorosilane"))
                    .inputItems(dust, PickledElectronicGradeSilicon, 1)
                    .inputFluids(HydrochloricAcid.getFluid(3000))
                    .outputFluids(PureTrichlorosilane.getFluid(1000))
                    .outputFluids(Hydrogen.getFluid(1000))
                    .circuitMeta(1)
                    .duration(300)
                    .EUt(VA[HV])
                    .save();

            REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("chemical_make_pure_silane"))
                    .inputFluids(PureTrichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(1000))
                    .outputFluids(PureSilane.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(40)
                    .blastFurnaceTemp(1500)
                    .EUt(VA[EV])
                    .save();

            DISTILLATION_RECIPES.recipeBuilder(GTOCore.id("distillation_pure_silane"))
                    .inputFluids(Silane.getFluid(1000))
                    .chancedOutput(ChemicalHelper.get(dustTiny, BariumChloride, 1), 1000, 0)
                    .outputFluids(PureSilane.getFluid(850))
                    .outputFluids(Steam.getFluid(100))
                    .chancedOutput(PhosphorusTrichloride.getFluid(30), 1000, 0)
                    .duration(100)
                    .EUt(VA[HV])
                    .save();

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder(GTOCore.id("make_high_purity_silicon_boule"))
                    .inputFluids(PureSilane.getFluid(32000))
                    .outputItems(HIGH_PURITY_SILICON_BOULE, 1)
                    .outputFluids(Hydrogen.getFluid(64000))
                    .duration(12000)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save();

            BLAST_RECIPES.recipeBuilder(GTOCore.id("make_regional_smelting_silicon_boule"))
                    .inputItems(HIGH_PURITY_SILICON_BOULE, 1)
                    .inputFluids(Helium.getFluid(1000))
                    .outputItems(REGIONAL_SMELTING_SILICON_BOULE, 1)
                    .duration(6000)
                    .blastFurnaceTemp(5000)
                    .EUt(VA[HV])
                    .save();

            LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("make_etched_silicon_boule"))
                    .inputItems(REGIONAL_SMELTING_SILICON_BOULE, 1)
                    .notConsumable(dust, Radium, 1)
                    .outputItems(ETCHED_SILICON_BOULE, 1)
                    .chancedOutput(CarbonMonoxide.getFluid(10), 100, 0)
                    .duration(600)
                    .EUt(VA[ZPM])
                    .addCondition(new VacuumCondition(4))
                    .save();

            AUTOCLAVE_RECIPES.recipeBuilder(GTOCore.id("make_etched_silicon_boule"))
                    .inputItems(ETCHED_SILICON_BOULE, 1)
                    .outputItems(FLOATING_ZONE_PURIFICATION_SILICON_BOULE, 1)
                    .duration(2400)
                    .EUt(VA[EV])
                    .addCondition(new VacuumCondition(4))
                    .save();

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_tetrafluorosilane_solution"))
                    .inputItems(FLOATING_ZONE_PURIFICATION_SILICON_BOULE, 1)
                    .inputFluids(HydrofluoricAcid.getFluid(416000))
                    .outputFluids(TetrafluorosilaneSolution.getFluid(104000))
                    .outputFluids(Hydrogen.getFluid(52000))
                    .duration(6000)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save();

            CHEMICAL_RECIPES.recipeBuilder(GTOCore.id("chemical_make_ultra_high_purity_silicon"))
                    .inputFluids(TetrafluorosilaneSolution.getFluid(4000))
                    .inputFluids(Hydrogen.getFluid(2000))
                    .outputItems(dust, UltraHighPuritySilicon, 1)
                    .outputFluids(HydrofluoricAcid.getFluid(16000))
                    .duration(200)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save();

            BLAST_RECIPES.recipeBuilder("make_high_purity_single_crystal_silicon")
                    .inputItems(block, UltraHighPuritySilicon, 16)
                    .inputItems(dust, GalliumArsenide)
                    .inputFluids(Xenon.getFluid(8000))
                    .outputItems(HIGH_PURITY_SINGLE_CRYSTAL_SILICON)
                    .blastFurnaceTemp(8684)
                    .duration(21000).EUt(VA[ZPM]).save();
        }

        // 高纯度二氧化硅/光纤产线
        {
            REACTION_FURNACE_RECIPES.recipeBuilder(GTOCore.id("make_high_purity_silica"))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputFluids(Oxygen.getFluid(1000))
                    .outputItems(dust, HighPuritySilica, 1)
                    .outputFluids(Chlorine.getFluid(2000))
                    .duration(400)
                    .blastFurnaceTemp(1500)
                    .EUt(VA[EV])
                    .save();

            BLAST_RECIPES.recipeBuilder("make_high_purity_silica_column")
                    .inputItems(dust, HighPuritySilica, 64)
                    .inputItems(dust, HighPuritySilica, 64)
                    .inputFluids(Helium.getFluid(8000))
                    .outputItems(HIGH_PURITY_SILICA_COLUMN, 1)
                    .blastFurnaceTemp(2800)
                    .circuitMeta(1)
                    .duration(12000)
                    .EUt(VA[HV])
                    .save();

            LIQUEFACTION_FURNACE_RECIPES.recipeBuilder("liquefaction_high_purity_silica")
                    .inputItems(dust, HighPuritySilica, 1)
                    .outputFluids(HighPuritySilica.getFluid(144))
                    .duration(60)
                    .blastFurnaceTemp(2000)
                    .EUt(VA[HV])
                    .save();

            ItemStack stack0 = MultiStepItemHelper.toMultiStepItem(HIGH_PURITY_SILICA_TUBE.asStack(), 0, 6);

            FORMING_PRESS_RECIPES.recipeBuilder("extruder_high_purity_silica_tube")
                    .inputItems(HIGH_PURITY_SILICA_COLUMN, 1)
                    .inputItems(toolHeadDrill, VanadiumSteel)
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 1))
                    .duration(300)
                    .EUt(VA[EV])
                    .save();

            FLUID_SOLIDFICATION_RECIPES.recipeBuilder("dolidifier_high_purity_silica_tube")
                    .inputFluids(HighPuritySilica.getFluid(18000))
                    .notConsumable(SHAPE_MOLD_CYLINDER)
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 1))
                    .duration(6000)
                    .EUt(VA[HV])
                    .save();

            ARC_FURNACE_RECIPES.recipeBuilder("make_high_purity_silica_tube1")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 1))
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 2))
                    .duration(30000)
                    .EUt(VA[MV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("make_high_purity_silica_tube2")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 2))
                    .inputFluids(HydrofluoricAcid.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 3))
                    .duration(100)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("make_high_purity_silica_tube3")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 3))
                    .inputFluids(DistilledWater.getFluid(8000))
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 4))
                    .duration(100)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("make_high_purity_silica_tube4")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 4))
                    .inputFluids(SodiumHydroxideSolution.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 5))
                    .duration(100)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("make_high_purity_silica_tube5")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 5))
                    .inputFluids(DistilledWater.getFluid(8000))
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 6))
                    .duration(100)
                    .EUt(VA[LV])
                    .save();

            ItemStack stack1 = MultiStepItemHelper.toMultiStepItem(SIMPLE_OPTICAL_FIBER_PREFORM.asStack(), 9, 9);

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_0")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 6))
                    .inputFluids(Tetrachlorosilane.getFluid(200))
                    .inputFluids(GermaniumTetrachlorideSolution.getFluid(FluidStorageKeys.GAS, 10))
                    .inputFluids(Oxygen.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 1))
                    .duration(100)
                    .EUt(VA[MV])
                    .blastFurnaceTemp(1800)
                    .save();

            for (int n = 1; n <= 3; n++) {
                CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_" + n)
                        .inputItems(MultiStepItemHelper.locateStep(stack1, n))
                        .inputFluids(Tetrachlorosilane.getFluid(600))
                        .inputFluids(GermaniumTetrachlorideSolution.getFluid(FluidStorageKeys.GAS, 30))
                        .inputFluids(Oxygen.getFluid(3000))
                        .outputItems(MultiStepItemHelper.locateStep(stack1, n + 1))
                        .duration(600)
                        .EUt(VA[HV])
                        .blastFurnaceTemp(3200)
                        .save();
            }

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_4")
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 4))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputFluids(CarbonTetrafluoride.getFluid(100))
                    .inputFluids(Oxygen.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 5))
                    .duration(400)
                    .EUt(VA[MV])
                    .blastFurnaceTemp(2500)
                    .save();

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_5")
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 5))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputFluids(CarbonTetrafluoride.getFluid(100))
                    .inputFluids(Oxygen.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 6))
                    .duration(400)
                    .EUt(VA[MV])
                    .blastFurnaceTemp(2500)
                    .save();

            BLAST_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_6")
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 6))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 7))
                    .duration(12000)
                    .EUt(VA[EV])
                    .blastFurnaceTemp(4200)
                    .save();

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_7")
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 7))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputFluids(CarbonTetrafluoride.getFluid(100))
                    .inputFluids(Oxygen.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 8))
                    .duration(400)
                    .EUt(VA[MV])
                    .blastFurnaceTemp(2500)
                    .save();

            BLAST_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_8")
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 8))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 9))
                    .duration(4000)
                    .EUt(VA[EV])
                    .blastFurnaceTemp(4200)
                    .save();

            Material[] forSpools = { Iron, Steel, StainlessSteel, TungstenSteel, Osmiridium };
            ItemStack[] Spools = { SPOOLS_MICRO.asStack(), SPOOLS_SMALL.asStack(), SPOOLS_MEDIUM.asStack(), SPOOLS_LARGE.asStack(), SPOOLS_JUMBO.asStack() };
            for (int n = 0; n < 5; n++) {
                ASSEMBLER_RECIPES.recipeBuilder("make_spool_" + n)
                        .inputItems(plate, forSpools[n], 24)
                        .inputItems(rod, forSpools[n], 6)
                        .inputItems(ring, forSpools[n], 12)
                        .inputItems(screw, forSpools[n], 12)
                        .inputFluids(Polytetrafluoroethylene.getFluid(1152))
                        .outputItems(Spools[n])
                        .circuitMeta(3)
                        .duration(600)
                        .EUt(VA[HV])
                        .save();
            }

            for (int n = 1; n <= 3; n++) {
                int m = (int) Math.pow(4, n - 1);
                DRAWING_RECIPES.recipeBuilder("drawing_simple_fiber_optic_" + n)
                        .inputItems(MultiStepItemHelper.locateStep(stack1, 9))
                        .outputItems(SIMPLE_FIBER_OPTIC_ROUGH, m << 6)
                        .addData("spool", n)
                        .duration((60000 * m) + 12000)
                        .EUt(VA[3 + n])
                        .blastFurnaceTemp(3300 + 1000 * n)
                        .save();
            }
            for (int n = 4; n <= 5; n++) {
                int m = (int) Math.pow(4, n - 1);
                DRAWING_RECIPES.recipeBuilder("drawing_simple_fiber_optic_" + n)
                        .inputItems(MultiStepItemHelper.locateStep(stack1, 9))
                        .outputItems(SIMPLE_FIBER_OPTIC_ROUGH, m << 6)
                        .addData("spool", n)
                        .duration((60000 * m) + 12000)
                        .EUt(VA[3 + n])
                        .blastFurnaceTemp(3300 + 1200 * n)
                        .addCondition(new VacuumCondition(4))
                        .save();
            }

            LAMINATOR_RECIPES.recipeBuilder("make_simple_fiber_optic")
                    .inputItems(SIMPLE_FIBER_OPTIC_ROUGH)
                    .inputFluids(EthylAcrylate.getFluid(72))
                    .outputItems(SIMPLE_FIBER_OPTIC)
                    .duration(100)
                    .EUt(7)
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("optical_pipe")
                    .inputItems(SIMPLE_FIBER_OPTIC, 8)
                    .inputItems(foil, Polyethylene, 32)
                    .inputItems(foil, Graphene, 8)
                    .inputItems(DUCT_TAPE, 1)
                    .inputFluids(Polytetrafluoroethylene.getFluid(8 * L))
                    .outputItems(OPTICAL_PIPES[0])
                    .cleanroom(CleanroomType.CLEANROOM)
                    .duration(100)
                    .EUt(VA[IV])
                    .save();

        }
    }
}
