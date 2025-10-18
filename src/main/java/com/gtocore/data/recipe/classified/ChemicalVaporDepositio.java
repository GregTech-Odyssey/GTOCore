package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.EV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

final class ChemicalVaporDepositio {

    public static void init() {
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("graphene")
                .notConsumable(TagPrefix.plate, GTMaterials.Nickel)
                .inputItems(TagPrefix.dust, GTOMaterials.GrapheneOxide, 3)
                .outputItems(TagPrefix.foil, GTMaterials.Graphene, 8)
                .inputFluids(GTMaterials.Methane.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(2000))
                .EUt(7680)
                .duration(120)
                .save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("nano_cpu_wafer")
                .inputItems(CENTRAL_PROCESSING_UNIT_WAFER)
                .inputItems(CARBON_FIBERS, 16)
                .inputFluids(Glowstone.getFluid(576))
                .outputItems(NANO_CENTRAL_PROCESSING_UNIT_WAFER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(1200).EUt(VA[EV]).save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("qbit_cpu_wafer_quantum_eye")
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT_WAFER)
                .inputItems(QUANTUM_EYE, 2)
                .inputFluids(GalliumArsenide.getFluid(288))
                .outputItems(QUBIT_CENTRAL_PROCESSING_UNIT_WAFER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(900).EUt(VA[EV]).save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("qbit_cpu_wafer_radon")
                .inputItems(NANO_CENTRAL_PROCESSING_UNIT_WAFER)
                .inputItems(dust, IndiumGalliumPhosphide)
                .inputFluids(Radon.getFluid(50))
                .outputItems(QUBIT_CENTRAL_PROCESSING_UNIT_WAFER)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(1200).EUt(VA[EV]).save();
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.builder("sol_gel_qd_interface_modified_mfpc_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.NanoGoldDepositedCarbonNanotubeModifiedNHSLipoicEsterQDot)
                .inputItems(TagPrefix.dust, HighPressureStaticAdsorptionMFPC)
                .outputItems(TagPrefix.dust, SolGelQDInterfaceModifiedMFPC)
                .inputFluids(GTOMaterials.SilicaGelBase, 1000)
                .cleanroom(CleanroomType.CLEANROOM)
                .EUt(1200)
                .duration(1200)
                .save();
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.builder("extreme_temperature_interface_stabilized_mfpc_dust")
                .inputItems(TagPrefix.dust, InterfaceSupramolecularSelfAssemblyMFPC)
                .outputItems(TagPrefix.dust, ExtremeTemperatureInterfaceStabilizedMFPC)
                .inputFluids(GTOMaterials.Hexamethyldisilazane, 1000)
                .inputFluids(GTMaterials.Silver.getFluid(FluidStorageKeys.PLASMA, 100))
                .inputFluids(GTMaterials.Methane, 4000)
                .EUt(2400)
                .duration(1200)
                .save();
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.builder("ce_ox_poly_dopamine_reinforced_polytetrafluoroethylene_foil")
                .inputItems(TagPrefix.foil, GTOMaterials.PerfluorosulfonicAcidPolytetrafluoroethyleneCopolymer, 16)
                .notConsumable(TagPrefix.plate, GTMaterials.Neutronium)
                .outputItems(TagPrefix.foil, GTOMaterials.CeOxPolyDopamineReinforcedPolytetrafluoroethylene, 16)
                .inputFluids(GTOMaterials.PolyDopamine, 1000)
                .inputFluids(GTOMaterials.Cerium4Sulfate, 1000)
                .inputFluids(GTMaterials.Hydrogen, 10000)
                .outputFluids(GTMaterials.SulfurDioxide, 1000)
                .EUt(320000)
                .duration(1200)
                .save();
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.builder("nanocrack_regulated_self_humidifying_composite_material_foil")
                .inputItems(TagPrefix.dust, GTOMaterials.SulfonatedPolyAryleneEtherSulfoneRandomCopolymer)
                .inputItems(TagPrefix.foil, GTOMaterials.PerfluorosulfonicAcidPolytetrafluoroethyleneCopolymer, 16)
                .outputItems(TagPrefix.foil, GTOMaterials.NanocrackRegulatedSelfHumidifyingCompositeMaterial, 16)
                .inputFluids(GTOMaterials.NMPyrolidone, 3000)
                .inputFluids(GTMaterials.Titanium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.ExtremeTemperatureWater, 1000)
                .outputFluids(GTMaterials.Steam, 1000)
                .EUt(320000)
                .duration(1200)
                .save();
    }
}
