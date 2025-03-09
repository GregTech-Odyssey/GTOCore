package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.PHYSICAL_VAPOR_DEPOSITION_RECIPES;

interface PhysicalVaporDeposition {

    static void init() {
        PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder(GTOCore.id("highly_insulating_foil"))
                .inputItems(TagPrefix.foil, GTOMaterials.Polyetheretherketone)
                .inputFluids(GTOMaterials.Azafullerene.getFluid(10))
                .outputItems(GTOItems.HIGHLY_INSULATING_FOIL.asStack())
                .EUt(7680)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        PHYSICAL_VAPOR_DEPOSITION_RECIPES.recipeBuilder(GTOCore.id("cosmic_soc_wafer"))
                .inputItems(GTOItems.PREPARED_COSMIC_SOC_WAFER.asStack())
                .inputFluids(GTMaterials.Argon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.COSMIC_SOC_WAFER.asStack())
                .EUt(7864320)
                .duration(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }
}
