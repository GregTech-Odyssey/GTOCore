package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.SUPERMATERIAL_FORGING_RECIPES;

final class SuperMaterialForge {

    public static void init() {
        SUPERMATERIAL_FORGING_RECIPES.recipeBuilder("carbon_nanotubes_ingot")
                .circuitMeta(1)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(800))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(200))
                .outputItems(TagPrefix.dust, GTOMaterials.CarbonNanotubes)
                .EUt(320000)
                .duration(290)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.recipeBuilder("fullerene_doped_nanotubes")
                .inputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(14400))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(3600))
                .outputFluids(GTOMaterials.FullereneDopedNanotubes.getFluid(18000))
                .EUt(320000)
                .duration(290)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.recipeBuilder("neutronium_doped_nanotubes")
                .inputItems(TagPrefix.dust, GTOMaterials.Neutron)
                .notConsumable(TagPrefix.plate, GTMaterials.Rhenium)
                .inputFluids(GTMaterials.Methane.getFluid(800))
                .inputFluids(GTOMaterials.Cycloparaphenylene.getFluid(200))
                .outputFluids(GTOMaterials.NeutroniumDopedNanotubes.getFluid(200))
                .EUt(491520)
                .duration(500)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        SUPERMATERIAL_FORGING_RECIPES.recipeBuilder("seaborgium_doped_nanotubes")
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
