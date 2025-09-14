package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.EV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.plate;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.CHEMICAL_VAPOR_DEPOSITION_RECIPES;

final class ChemicalVaporDepositio {

    public static void init() {
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("carbon_nanotubes_ingot")
                .circuitMeta(1)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(800))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(200))
                .outputItems(TagPrefix.dust, GTOMaterials.CarbonNanotubes)
                .EUt(320000)
                .duration(290)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("fullerene_doped_nanotubes")
                .inputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(14400))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(3600))
                .outputFluids(GTOMaterials.FullereneDopedNanotubes.getFluid(18000))
                .EUt(320000)
                .duration(290)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("neutronium_doped_nanotubes")
                .inputItems(TagPrefix.dust, GTOMaterials.Neutron)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(800))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(200))
                .outputFluids(GTOMaterials.NeutroniumDopedNanotubes.getFluid(200))
                .EUt(491520)
                .duration(500)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder("seaborgium_doped_nanotubes")
                .inputItems(TagPrefix.dust, GTMaterials.Seaborgium)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(800))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(200))
                .outputFluids(GTOMaterials.SeaborgiumDopedNanotubes.getFluid(144))
                .EUt(320000)
                .duration(390)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

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
    }
}
