package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.PRECISION_LASER_ENGRAVER_RECIPES;

interface PrecisionLaserEngraver {

    static void init() {
        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("high_precision_crystal_soc"))
                .inputItems(GTItems.CRYSTAL_SYSTEM_ON_CHIP.asItem())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .inputFluids(GTOMaterials.EuvPhotoresist.getFluid(1000))
                .outputItems(GTOItems.HIGH_PRECISION_CRYSTAL_SOC.asItem())
                .EUt(7864320)
                .duration(2400)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("pm_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.EuvPhotoresist.getFluid(1000))
                .outputItems(GTOItems.PM_WAFER.asItem())
                .EUt(1966080)
                .duration(1800)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("fm_wafer"))
                .inputItems(GTOItems.PM_WAFER.asItem())
                .notConsumable(GTOItems.GRATING_LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist.getFluid(1000))
                .outputItems(GTOItems.FM_WAFER.asItem())
                .EUt(7864320)
                .duration(2800)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("nm_wafer"))
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asItem())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.Photoresist.getFluid(1000))
                .outputItems(GTOItems.NM_WAFER.asItem())
                .EUt(491520)
                .duration(900)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("raw_photon_carrying_wafer"))
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asItem())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightGray)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Magenta)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .inputFluids(GTOMaterials.Photoresist.getFluid(1000))
                .outputItems(GTOItems.RAW_PHOTON_CARRYING_WAFER.asItem())
                .EUt(1966080)
                .duration(600)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("prepared_cosmic_soc_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist.getFluid(1000))
                .outputItems(GTOItems.PREPARED_COSMIC_SOC_WAFER.asItem())
                .EUt(31457280)
                .duration(4800)
                .save();
    }
}
