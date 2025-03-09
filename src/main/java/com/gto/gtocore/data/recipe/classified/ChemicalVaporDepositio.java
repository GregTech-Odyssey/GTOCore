package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.*;

interface ChemicalVaporDepositio {

    static void init() {
        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder(GTOCore.id("carbon_nanotubes_ingot"))
                .circuitMeta(1)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(800))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(200))
                .outputItems(TagPrefix.dust, GTOMaterials.CarbonNanotubes)
                .EUt(320000)
                .duration(290)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder(GTOCore.id("fullerene_doped_nanotubes"))
                .inputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(14400))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(3600))
                .outputFluids(GTOMaterials.FullereneDopedNanotubes.getFluid(18000))
                .EUt(320000)
                .duration(290)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder(GTOCore.id("neutronium_doped_nanotubes"))
                .inputItems(TagPrefix.dust, GTOMaterials.Neutron)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(800))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(200))
                .outputFluids(GTOMaterials.NeutroniumDopedNanotubes.getFluid(200))
                .EUt(491520)
                .duration(500)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder(GTOCore.id("seaborgium_doped_nanotubes"))
                .inputItems(TagPrefix.dust, GTMaterials.Seaborgium)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(800))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(200))
                .outputFluids(GTOMaterials.SeaborgiumDopedNanotubes.getFluid(144))
                .EUt(320000)
                .duration(390)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }
}
