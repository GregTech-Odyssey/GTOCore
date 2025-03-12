package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES;

interface DimensionalFocusEngravingArray {

    static void init() {
        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder(GTOCore.id("pm_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Magenta)
                .inputFluids(GTOMaterials.EuvPhotoresist.getFluid(100))
                .outputItems(GTOItems.PM_WAFER.asItem())
                .EUt(1966080)
                .duration(1800)
                .scannerResearch(b -> b.researchStack(GTOItems.PM_WAFER.asStack())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asStack())
                        .EUt(1966080).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder(GTOCore.id("raw_photon_carrying_wafer"))
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .inputFluids(GTOMaterials.Photoresist.getFluid(100))
                .outputItems(GTOItems.RAW_PHOTON_CARRYING_WAFER.asItem())
                .EUt(1966080)
                .duration(600)
                .scannerResearch(b -> b.researchStack(GTOItems.RAW_PHOTON_CARRYING_WAFER.asStack())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asStack())
                        .EUt(1966080).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder(GTOCore.id("high_precision_crystal_soc"))
                .inputItems(GTItems.CRYSTAL_SYSTEM_ON_CHIP.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .inputFluids(GTOMaterials.EuvPhotoresist.getFluid(100))
                .outputItems(GTOItems.HIGH_PRECISION_CRYSTAL_SOC.asItem())
                .EUt(7864320)
                .duration(2400)
                .scannerResearch(b -> b.researchStack(GTOItems.HIGH_PRECISION_CRYSTAL_SOC.asStack())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asStack())
                        .EUt(7864320).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder(GTOCore.id("prepared_cosmic_soc_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist.getFluid(100))
                .outputItems(GTOItems.PREPARED_COSMIC_SOC_WAFER.asItem())
                .EUt(31457280)
                .duration(4800)
                .scannerResearch(b -> b.researchStack(GTOItems.PREPARED_COSMIC_SOC_WAFER.asStack())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asStack())
                        .EUt(31457280).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder(GTOCore.id("nm_wafer"))
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .inputFluids(GTOMaterials.Photoresist.getFluid(100))
                .outputItems(GTOItems.NM_WAFER.asItem())
                .EUt(491520)
                .duration(900)
                .scannerResearch(b -> b.researchStack(GTOItems.NM_WAFER.asStack())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asStack())
                        .EUt(491520).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder(GTOCore.id("fm_wafer"))
                .inputItems(GTOItems.PM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist.getFluid(100))
                .outputItems(GTOItems.FM_WAFER.asItem())
                .EUt(7864320)
                .duration(2700)
                .scannerResearch(b -> b.researchStack(GTOItems.FM_WAFER.asStack())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asStack())
                        .EUt(7864320).duration(2400))
                .save();
    }
}
