package com.gtocore.data.recipe.processing;

import com.gtocore.common.recipe.condition.VacuumCondition;

import com.gtolib.api.item.MultiStepItemHelper;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.LIQUID;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.PLASMA;
import static com.gregtechceu.gtceu.common.data.GTBlocks.ADVANCED_COMPUTER_CASING;
import static com.gregtechceu.gtceu.common.data.GTBlocks.OPTICAL_PIPES;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.tag.GTOTagPrefix.CATALYST;
import static com.gtocore.common.data.GTOBlocks.BIOCOMPUTER_CASING;
import static com.gtocore.common.data.GTOBlocks.PHASE_CHANGE_BIOCOMPUTER_COOLING_VENTS;
import static com.gtocore.common.data.GTOItems.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtocore.common.data.machines.ExResearchMachines.*;

public final class NewResearchSystem {

    public static void init() {
        // 重写配方
        CHEMICAL_RECIPES.recipeBuilder("polydimethylsiloxane_from_silicon")
                .inputItems(dust, Silicon)
                .inputFluids(HydrochloricAcid.getFluid(2000))
                .inputFluids(Methanol.getFluid(2000))
                .outputItems(dust, Polydimethylsiloxane, 3)
                .outputFluids(DilutedHydrochloricAcid.getFluid(2000))
                .circuitMeta(2)
                .duration(480).EUt(96).save();

        // 低相关度配方
        {
            CHEMICAL_RECIPES.recipeBuilder("make_carbon_tetrafluoride_1")
                    .inputItems(dust, Carbon, 1)
                    .inputFluids(Fluorine.getFluid(2000))
                    .outputFluids(CarbonTetrafluoride.getFluid(1000))
                    .circuitMeta(1)
                    .duration(200)
                    .EUt(VA[EV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("make_carbon_tetrafluoride_2")
                    .inputItems(dust, Carbon, 1)
                    .notConsumableFluid(BromineTrifluoride.getFluid(1000))
                    .inputFluids(Fluorine.getFluid(2000))
                    .outputFluids(CarbonTetrafluoride.getFluid(1000))
                    .circuitMeta(2)
                    .duration(20)
                    .EUt(VA[HV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("make_ethyl_silicate_1")
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputFluids(Ethanol.getFluid(4000))
                    .outputFluids(EthylSilicate.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(4000))
                    .duration(1000)
                    .EUt(VA[IV])
                    .save();

            AUTOCLAVE_RECIPES.recipeBuilder("make_ethyl_silicate_2")
                    .inputItems(CATALYST, Brass)
                    .inputItems(dust, ElectronicGradeSilicon, 1)
                    .inputFluids(AbsoluteEthanol.getFluid(4000))
                    .outputFluids(EthylSilicate.getFluid(1000))
                    .cleanroom(CleanroomType.CLEANROOM)
                    .duration(20)
                    .EUt(VA[ZPM])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("hydrolysis_silicic_acid")
                    .inputFluids(SilicicAcid.getFluid(1000))
                    .inputFluids(HydrofluoricAcid.getFluid(4000))
                    .outputFluids(Tetrafluorosilane.getFluid(1000))
                    .outputFluids(Water.getFluid(3000))
                    .duration(40)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("hydrolysis_tetrafluorosilane_1")
                    .inputFluids(Tetrafluorosilane.getFluid(1000))
                    .inputFluids(Water.getFluid(2000))
                    .outputFluids(DiluteHexafluorosilicicAcid.getFluid(1000))
                    .circuitMeta(1)
                    .duration(40)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("hydrolysis_tetrafluorosilane_2")
                    .notConsumable(DUST_CRYOTHEUM)
                    .inputFluids(Tetrafluorosilane.getFluid(1000))
                    .inputFluids(Water.getFluid(2000))
                    .outputFluids(TetrafluorosilaneSolution.getFluid(1000))
                    .circuitMeta(2)
                    .duration(40)
                    .EUt(VA[LV])
                    .save();

            MIXER_RECIPES.recipeBuilder("mixer_silver_nitrate_solution")
                    .inputItems(dust, SilverNitrate, 5)
                    .inputFluids(Water.getFluid(1000))
                    .outputFluids(SilverNitrateSolution.getFluid(1000))
                    .EUt(60)
                    .duration(80)
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_silver_nitrate_solution")
                    .inputItems(dust, SilverChloride, 2)
                    .inputFluids(NitricAcid.getFluid(1000))
                    .outputFluids(SilverNitrateSolution.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(1000))
                    .EUt(60)
                    .duration(100)
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_trimethylsilanol")
                    .inputFluids(Trimethylchlorosilane.getFluid(1000))
                    .inputFluids(Water.getFluid(1000))
                    .outputFluids(Trimethylsilanol.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(1000))
                    .EUt(1920)
                    .duration(200)
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_hexamethyldisiloxane")
                    .notConsumable(dust, SiliconDioxide)
                    .inputFluids(Trimethylsilanol.getFluid(1000))
                    .outputFluids(Hexamethyldisiloxane.getFluid(1000))
                    .outputFluids(Water.getFluid(1000))
                    .EUt(1920)
                    .duration(600)
                    .save();

            LARGE_CHEMICAL_RECIPES.recipeBuilder("chemical_triethoxysilane")
                    .inputItems(CATALYST, Brass)
                    .inputItems(dust, UltraHighPuritySilicon)
                    .inputFluids(AbsoluteEthanol.getFluid(3000))
                    .outputFluids(Triethoxysilane.getFluid(1000))
                    .outputFluids(Hydrogen.getFluid(1000))
                    .cleanroom(CleanroomType.CLEANROOM)
                    .EUt(30720)
                    .duration(200)
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_chloropropyltriethoxysilane")
                    .inputItems(CATALYST, Platinum)
                    .inputFluids(Triethoxysilane.getFluid(1000))
                    .inputFluids(AllylChloride.getFluid(1000))
                    .outputFluids(Chloropropyltriethoxysilane.getFluid(1000))
                    .EUt(16380)
                    .duration(300)
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_kh550_silane_coupling_agent")
                    .inputFluids(Chloropropyltriethoxysilane.getFluid(1000))
                    .inputFluids(Ammonia.getFluid(4000))
                    .outputFluids(KH550SilaneCouplingAgent.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(2000))
                    .EUt(120)
                    .duration(100)
                    .save();
        }

        // 前置电子级硅/超高纯硅产线
        {
            CHEMICAL_RECIPES.recipeBuilder("chemical_make_tetrachlorosilane")
                    .inputItems(dust, Silicon, 1)
                    .inputFluids(Chlorine.getFluid(4000))
                    .outputFluids(Tetrachlorosilane.getFluid(1000))
                    .circuitMeta(1)
                    .duration(400)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_make_trichlorosilane")
                    .inputItems(dust, Silicon, 1)
                    .inputFluids(HydrochloricAcid.getFluid(3000))
                    .outputFluids(Trichlorosilane.getFluid(1000))
                    .outputFluids(Hydrogen.getFluid(1000))
                    .circuitMeta(1)
                    .duration(300)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_make_electronic_grade_silicon_1")
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputItems(dust, Zinc, 2)
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputItems(dust, ZincChloride, 6)
                    .duration(200)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_make_electronic_grade_silicon_2")
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputItems(dust, Sodium, 4)
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputItems(dust, Salt, 8)
                    .duration(100)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_make_electronic_grade_silicon_3")
                    .inputFluids(Trichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(1000))
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(600)
                    .EUt(VA[LV])
                    .save();

            REACTION_FURNACE_RECIPES.recipeBuilder("chemical_make_silane")
                    .inputFluids(Trichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(3000))
                    .outputFluids(Silane.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(40)
                    .blastFurnaceTemp(1500)
                    .EUt(VA[MV])
                    .save();

            REACTION_FURNACE_RECIPES.recipeBuilder("chemical_make_electronic_grade_silicon_4")
                    .inputFluids(Silane.getFluid(1000))
                    .outputItems(dust, ElectronicGradeSilicon, 1)
                    .outputFluids(Hydrogen.getFluid(2000))
                    .duration(20)
                    .blastFurnaceTemp(1000)
                    .EUt(VA[MV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("chemical_wash_electronic_grade_silicon")
                    .inputItems(dust, ElectronicGradeSilicon, 1)
                    .inputFluids(HydrofluoricAcid.getFluid(1000))
                    .chancedOutput(ChemicalHelper.get(dust, PickledElectronicGradeSilicon, 1), 9500, 0)
                    .outputFluids(HydrofluoricAcid.getFluid(950))
                    .duration(200)
                    .EUt(VA[EV])
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_make_pure_trichlorosilane")
                    .inputItems(dust, PickledElectronicGradeSilicon, 1)
                    .inputFluids(HydrochloricAcid.getFluid(3000))
                    .outputFluids(PureTrichlorosilane.getFluid(1000))
                    .outputFluids(Hydrogen.getFluid(1000))
                    .circuitMeta(1)
                    .duration(300)
                    .EUt(VA[HV])
                    .save();

            REACTION_FURNACE_RECIPES.recipeBuilder("chemical_make_pure_silane")
                    .inputFluids(PureTrichlorosilane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(1000))
                    .outputFluids(PureSilane.getFluid(1000))
                    .outputFluids(HydrochloricAcid.getFluid(3000))
                    .duration(40)
                    .blastFurnaceTemp(1500)
                    .EUt(VA[EV])
                    .save();

            DISTILLATION_RECIPES.recipeBuilder("distillation_pure_silane")
                    .inputFluids(Silane.getFluid(1000))
                    .chancedOutput(ChemicalHelper.get(dustTiny, BariumChloride, 1), 1000, 0)
                    .outputFluids(PureSilane.getFluid(850))
                    .outputFluids(Steam.getFluid(100))
                    .chancedOutput(PhosphorusTrichloride.getFluid(30), 1000, 0)
                    .duration(100)
                    .EUt(VA[HV])
                    .save();

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("make_high_purity_silicon_boule")
                    .inputFluids(PureSilane.getFluid(32000))
                    .outputItems(HIGH_PURITY_SILICON_BOULE, 1)
                    .outputFluids(Hydrogen.getFluid(64000))
                    .duration(12000)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save();

            BLAST_RECIPES.recipeBuilder("make_regional_smelting_silicon_boule")
                    .inputItems(HIGH_PURITY_SILICON_BOULE, 1)
                    .inputFluids(Helium.getFluid(1000))
                    .outputItems(REGIONAL_SMELTING_SILICON_BOULE, 1)
                    .duration(6000)
                    .blastFurnaceTemp(5000)
                    .EUt(VA[HV])
                    .save();

            LASER_ENGRAVER_RECIPES.recipeBuilder("make_etched_silicon_boule")
                    .inputItems(REGIONAL_SMELTING_SILICON_BOULE, 1)
                    .notConsumable(dust, Radium, 1)
                    .outputItems(ETCHED_SILICON_BOULE, 1)
                    .chancedOutput(CarbonMonoxide.getFluid(10), 100, 0)
                    .duration(600)
                    .EUt(VA[ZPM])
                    .addCondition(new VacuumCondition(4))
                    .save();

            AUTOCLAVE_RECIPES.recipeBuilder("make_etched_silicon_boule")
                    .inputItems(ETCHED_SILICON_BOULE, 1)
                    .outputItems(FLOATING_ZONE_PURIFICATION_SILICON_BOULE, 1)
                    .duration(2400)
                    .EUt(VA[EV])
                    .addCondition(new VacuumCondition(4))
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_make_tetrafluorosilane_solution")
                    .inputItems(FLOATING_ZONE_PURIFICATION_SILICON_BOULE, 1)
                    .inputFluids(HydrofluoricAcid.getFluid(416000))
                    .outputFluids(TetrafluorosilaneSolution.getFluid(104000))
                    .outputFluids(Hydrogen.getFluid(52000))
                    .duration(6000)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save();

            CHEMICAL_RECIPES.recipeBuilder("chemical_make_ultra_high_purity_silicon")
                    .inputFluids(TetrafluorosilaneSolution.getFluid(4000))
                    .inputFluids(Hydrogen.getFluid(2000))
                    .outputItems(dust, UltraHighPuritySilicon, 1)
                    .outputFluids(HydrofluoricAcid.getFluid(16000))
                    .duration(200)
                    .EUt(VA[IV])
                    .addCondition(new VacuumCondition(4))
                    .save();
        }

        // 高纯度二氧化硅/光纤产线
        {
            REACTION_FURNACE_RECIPES.recipeBuilder("make_high_purity_silica")
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
                    .circuitMeta(10)
                    .duration(12000)
                    .EUt(VA[HV])
                    .save();

            ItemStack stack0 = MultiStepItemHelper.toMultiStepItem(HIGH_PURITY_SILICA_TUBE.asStack(), 1, 4);

            FORMING_PRESS_RECIPES.recipeBuilder("extruder_high_purity_silica_tube")
                    .inputItems(HIGH_PURITY_SILICA_COLUMN, 1)
                    .inputItems(toolHeadDrill, VanadiumSteel)
                    .outputItems(stack0)
                    .duration(300)
                    .EUt(VA[EV])
                    .save();

            ARC_FURNACE_RECIPES.recipeBuilder("make_high_purity_silica_tube1")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 1))
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 2))
                    .duration(30000)
                    .EUt(VA[MV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("make_high_purity_silica_tube2")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 2))
                    .inputFluids(HydrochloricAcid.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 3))
                    .duration(100)
                    .EUt(VA[LV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("make_high_purity_silica_tube4")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 3))
                    .inputFluids(SodiumHydroxideSolution.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack0, 4))
                    .duration(100)
                    .EUt(VA[LV])
                    .save();

            ItemStack stack1 = MultiStepItemHelper.toMultiStepItem(SIMPLE_OPTICAL_FIBER_PREFORM.asStack(), 1, 4);

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_0")
                    .inputItems(MultiStepItemHelper.locateStep(stack0, 4))
                    .inputFluids(Tetrachlorosilane.getFluid(200))
                    .inputFluids(GermaniumTetrachlorideSolution.getFluid(FluidStorageKeys.GAS, 10))
                    .inputFluids(Oxygen.getFluid(1000))
                    .outputItems(stack1)
                    .duration(100)
                    .EUt(VA[MV])
                    .blastFurnaceTemp(1800)
                    .save();

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_" + 1)
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 1))
                    .inputFluids(Tetrachlorosilane.getFluid(2000))
                    .inputFluids(GermaniumTetrachlorideSolution.getFluid(FluidStorageKeys.GAS, 100))
                    .inputFluids(Oxygen.getFluid(10000))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 2))
                    .duration(6000)
                    .EUt(VA[HV])
                    .blastFurnaceTemp(3200)
                    .save();

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_2")
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 2))
                    .inputFluids(Tetrachlorosilane.getFluid(1000))
                    .inputFluids(CarbonTetrafluoride.getFluid(100))
                    .inputFluids(Oxygen.getFluid(1000))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 3))
                    .duration(400)
                    .EUt(VA[MV])
                    .blastFurnaceTemp(2500)
                    .save();

            BLAST_RECIPES.recipeBuilder("vapro_simple_optical_fiber_preform_4")
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 3))
                    .outputItems(MultiStepItemHelper.locateStep(stack1, 4))
                    .duration(12000)
                    .EUt(VA[EV])
                    .blastFurnaceTemp(4200)
                    .save();

            Material[] forSpools = { Iron, Steel, StainlessSteel, TungstenSteel, Osmiridium };
            Item[] Spools = { SPOOLS_MICRO.asItem(), SPOOLS_SMALL.asItem(), SPOOLS_MEDIUM.asItem(), SPOOLS_LARGE.asItem(), SPOOLS_JUMBO.asItem() };
            for (int n = 0; n < 5; n++) {
                ASSEMBLER_RECIPES.recipeBuilder("make_spool_" + n)
                        .inputItems(plate, forSpools[n], 24)
                        .inputItems(rod, forSpools[n], 6)
                        .inputItems(ring, forSpools[n], 12)
                        .inputItems(screw, forSpools[n], 12)
                        .inputFluids(Polytetrafluoroethylene.getFluid(1152))
                        .outputItems(Spools[n])
                        .circuitMeta(13)
                        .duration(600)
                        .EUt(VA[HV])
                        .save();
            }

            for (int n = 1; n <= 3; n++) {
                int m = (int) Math.pow(4, n - 1);
                DRAWING_RECIPES.recipeBuilder("drawing_simple_fiber_optic_" + n)
                        .circuitMeta(n)
                        .inputItems(MultiStepItemHelper.locateStep(stack1, 4))
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
                        .circuitMeta(10 + n)
                        .inputItems(MultiStepItemHelper.locateStep(stack1, 4))
                        .outputItems(SIMPLE_FIBER_OPTIC_ROUGH, m << 6)
                        .addData("spool", n)
                        .duration((60000 * m) + 12000)
                        .EUt(VA[3 + n])
                        .blastFurnaceTemp(3300 + 1200 * n)
                        .addCondition(new VacuumCondition(4))
                        .save();
            }

            LOOM_RECIPES.recipeBuilder("drawing_simple_fiber_optic")
                    .inputItems(MultiStepItemHelper.locateStep(stack1, 4))
                    .inputItems(SPOOLS_JUMBO)
                    .outputItems(SIMPLE_FIBER_OPTIC_ROUGH, 16384)
                    .duration(1200)
                    .EUt(VA[UIV])
                    .save();

            LAMINATOR_RECIPES.recipeBuilder("make_simple_fiber_optic")
                    .inputItems(SIMPLE_FIBER_OPTIC_ROUGH)
                    .inputFluids(EthylAcrylate.getFluid(18))
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

        // MFPC相变微粒产线
        {
            LARGE_CHEMICAL_RECIPES.recipeBuilder("make_silicic_acid")
                    .inputFluids(EthylSilicate.getFluid(1000))
                    .inputFluids(ClearAmmoniaSolution.getFluid(4000))
                    .outputFluids(SilicicAcid.getFluid(1000))
                    .outputFluids(Ethanol.getFluid(4000))
                    .outputFluids(Ammonia.getFluid(4000))
                    .duration(120)
                    .EUt(VA[EV])
                    .save();

            LARGE_CHEMICAL_RECIPES.recipeBuilder("make_mfpc_1")
                    .inputItems(dust, Polystyrene, 1)
                    .inputFluids(SilicicAcid.getFluid(500))
                    .outputItems(dust, HollowCeramicMicrosphereRoughEmbryo, 16)
                    .outputFluids(Water.getFluid(500))
                    .duration(100)
                    .EUt(VA[IV])
                    .save();

            SINTERING_FURNACE_RECIPES.recipeBuilder("make_mfpc_2")
                    .inputItems(dust, HollowCeramicMicrosphereRoughEmbryo)
                    .outputItems(dust, HollowCeramicMicrospheres)
                    .duration(20)
                    .EUt(VA[HV])
                    .blastFurnaceTemp(800)
                    .save();

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("make_mfpc_3")
                    .inputItems(dust, HollowCeramicMicrospheres, 8)
                    .inputFluids(SilverNitrateSolution.getFluid(500))
                    .inputFluids(Hydrazine.getFluid(500))
                    .outputItems(dust, SilverCoatedHollowCeramicMicrospheres, 8)
                    .outputFluids(Ammonia.getFluid(1000))
                    .duration(12000)
                    .EUt(VA[UV])
                    .blastFurnaceTemp(600)
                    .save();

            CANNER_RECIPES.recipeBuilder("make_mfpc_4")
                    .inputItems(dust, SilverCoatedHollowCeramicMicrospheres, 10)
                    .inputFluids(Octane.getFluid(800))
                    .outputItems(dust, SilverCoatedOctaneCeramicBeads, 10)
                    .duration(2000)
                    .EUt(VA[MV])
                    .save();

            LASER_ENGRAVER_RECIPES.recipeBuilder("make_mfpc_5")
                    .inputItems(dust, SilverCoatedOctaneCeramicBeads, 4)
                    .notConsumable(lens, NetherStar)
                    .inputFluids(Hexamethyldisiloxane.getFluid(50))
                    .inputFluids(Argon.getFluid(PLASMA, 1))
                    .outputItems(dust, SealedPhaseChangeBeads, 4)
                    .duration(300)
                    .EUt(VA[ZPM])
                    .save();

            CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("make_mfpc_6")
                    .notConsumable(dust, Cobalt)
                    .inputItems(dust, SealedPhaseChangeBeads, 16)
                    .inputFluids(Methane.getFluid(1000))
                    .inputFluids(Hydrogen.getFluid(4000))
                    .inputFluids(Argon.getFluid(PLASMA, 100))
                    .outputItems(dust, CarbonNanotubeCoatedPhaseChangeMicrobeads, 16)
                    .duration(800)
                    .EUt(VA[IV])
                    .save();

            PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("make_mfpc_7")
                    .inputItems(dust, CarbonNanotubeCoatedPhaseChangeMicrobeads, 32)
                    .inputItems(plate, SiliconCarbide, 1)
                    .inputItems(dust, FerriteMixture, 1)
                    .inputFluids(Argon.getFluid(PLASMA, 100))
                    .outputItems(dust, MicrowaveAttenuatingCoatedPhaseChangeMicrobeads, 32)
                    .duration(4000)
                    .EUt(VA[UV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("make_mfpc_8")
                    .inputItems(dust, MicrowaveAttenuatingCoatedPhaseChangeMicrobeads)
                    .inputFluids(KH550SilaneCouplingAgent.getFluid(5))
                    .outputItems(dust, SurfaceFunctionalizedPhaseChangeMicrobeads)
                    .duration(100)
                    .EUt(VA[MV])
                    .save();

            ISOSTATIC_PRESSING_RECIPES.recipeBuilder("make_mfpc_9")
                    .inputItems(dust, SurfaceFunctionalizedPhaseChangeMicrobeads)
                    .inputFluids(MutatedLivingSolder.getFluid(10))
                    .outputItems(dust, BasicMFPC)
                    .duration(400)
                    .EUt(VA[UV])
                    .save();

            CENTRIFUGE_RECIPES.recipeBuilder("recycle_mfpc_1")
                    .inputItems(dust, InvalidationBasicMFPC)
                    .outputItems(dust, RecycledPhaseChangeMicrobeads)
                    .duration(20)
                    .EUt(VA[LV])
                    .save();

            CANNER_RECIPES.recipeBuilder("recycle_mfpc_2")
                    .inputItems(dust, RecycledPhaseChangeMicrobeads, 10)
                    .inputFluids(Octane.getFluid(800))
                    .outputItems(dust, OctaneLoadedPhaseChangeMicrobeads, 10)
                    .duration(2000)
                    .EUt(VA[MV])
                    .save();

            CHEMICAL_BATH_RECIPES.recipeBuilder("recycle_mfpc_3")
                    .inputItems(dust, OctaneLoadedPhaseChangeMicrobeads)
                    .inputFluids(KH550SilaneCouplingAgent.getFluid(5))
                    .outputItems(dust, ReactivatedPhaseChangeMicrobeads)
                    .duration(100)
                    .EUt(VA[MV])
                    .save();

            ISOSTATIC_PRESSING_RECIPES.recipeBuilder("recycle_mfpc_4")
                    .inputItems(dust, ReactivatedPhaseChangeMicrobeads)
                    .inputFluids(MutatedLivingSolder.getFluid(10))
                    .outputItems(dust, BasicMFPC)
                    .duration(400)
                    .EUt(VA[UV])
                    .save();

        }

        {
            CIRCUIT_ASSEMBLER_RECIPES.recipeBuilder("make_neural_matrix")
                    .inputItems(BIOWARE_PRINTED_CIRCUIT_BOARD)
                    .inputItems(CustomTags.UHV_CIRCUITS, 2)
                    .inputItems(SUPER_CEREBRUM, 2)
                    .inputItems(BIOWARE_CHIP, 32)
                    .inputItems(NM_CHIP, 64)
                    .inputItems(wireFine, RutheniumTriniumAmericiumNeutronate, 64)
                    .outputItems(NEURAL_MATRIX)
                    .duration(600)
                    .EUt(VA[UV])
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("make_biocomputer_casing")
                    .inputItems(ADVANCED_COMPUTER_CASING)
                    .inputItems(CustomTags.UHV_CIRCUITS, 2)
                    .inputItems(wireFine, Titanium, 64)
                    .inputItems(wireFine, AbyssalAlloy, 64)
                    .inputItems(wireGtSingle, RutheniumTriniumAmericiumNeutronate, 16)
                    .outputItems(BIOCOMPUTER_CASING)
                    .duration(400)
                    .EUt(VA[UV])
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("make_phase_change_biocomputer_cooling_vents")
                    .inputItems(BIOCOMPUTER_CASING)
                    .inputItems(ELECTRIC_MOTOR_UHV, 2)
                    .inputItems(ingot, BasicMFPC, 64)
                    .inputItems(pipeTinyFluid, Neutronium, 16)
                    .inputItems(plate, Orichalcum, 16)
                    .inputItems(wireGtSingle, RutheniumTriniumAmericiumNeutronate, 16)
                    .outputItems(PHASE_CHANGE_BIOCOMPUTER_COOLING_VENTS)
                    .duration(400)
                    .EUt(VA[UV])
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("make_nich_empty_component")
                    .inputItems(BIOCOMPUTER_CASING)
                    .inputItems(CustomTags.UV_CIRCUITS, 2)
                    .inputItems(NEURAL_MATRIX)
                    .inputFluids(Helium.getFluid(LIQUID, 8000))
                    .outputItems(NICH_EMPTY_COMPONENT)
                    .cleanroom(CleanroomType.CLEANROOM)
                    .duration(200)
                    .EUt(VA[UHV])
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("make_nich_computing_components")
                    .inputItems(NICH_EMPTY_COMPONENT)
                    .inputItems(CustomTags.UEV_CIRCUITS, 8)
                    .inputItems(FIELD_GENERATOR_UHV)
                    .inputFluids(Helium.getFluid(LIQUID, 8000))
                    .outputItems(NICH_COMPUTING_COMPONENTS)
                    .cleanroom(CleanroomType.CLEANROOM)
                    .duration(200)
                    .EUt(VA[UHV])
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("make_nich_cooling_components")
                    .inputItems(NICH_EMPTY_COMPONENT)
                    .inputItems(block, BasicMFPC, 64)
                    .inputItems(pipeTinyFluid, Neutronium, 8)
                    .inputItems(plate, Orichalcum, 32)
                    .outputItems(NICH_COOLING_COMPONENTS)
                    .cleanroom(CleanroomType.CLEANROOM)
                    .duration(200)
                    .EUt(VA[UHV])
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("make_nich_bridge_component")
                    .inputItems(NICH_EMPTY_COMPONENT)
                    .inputItems(CustomTags.UEV_CIRCUITS, 2)
                    .inputItems(EMITTER_UHV)
                    .inputItems(OPTICAL_PIPES[0].asItem(), 32)
                    .inputFluids(Helium.getFluid(LIQUID, 8000))
                    .outputItems(NICH_BRIDGE_COMPONENT)
                    .cleanroom(CleanroomType.CLEANROOM)
                    .duration(200)
                    .EUt(VA[UHV])
                    .save();
        }
    }
}
