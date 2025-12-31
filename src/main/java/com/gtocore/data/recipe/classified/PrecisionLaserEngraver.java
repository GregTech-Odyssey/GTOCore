package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;

import static com.gtocore.common.data.GTORecipeTypes.PRECISION_LASER_ENGRAVER_RECIPES;

final class PrecisionLaserEngraver {

    public static void init() {
        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder("high_precision_crystal_soc")
                .inputItems(GTItems.CRYSTAL_SYSTEM_ON_CHIP)
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .inputFluids(GTOMaterials.EuvPhotoresist, 1000)
                .outputItems(GTOItems.HIGH_PRECISION_CRYSTAL_SOC)
                .EUt(7864320)
                .duration(2400)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder("pm_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER)
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.EuvPhotoresist, 1000)
                .outputItems(GTOItems.PM_WAFER)
                .EUt(1966080)
                .duration(1800)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder("fm_wafer")
                .inputItems(GTOItems.PM_WAFER)
                .notConsumable(GTOItems.GRATING_LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist, 1000)
                .outputItems(GTOItems.FM_WAFER)
                .EUt(7864320)
                .duration(2800)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder("nm_wafer")
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER)
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.Photoresist, 1000)
                .outputItems(GTOItems.NM_WAFER)
                .EUt(491520)
                .duration(900)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder("raw_photon_carrying_wafer")
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER)
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightGray)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Magenta)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .inputFluids(GTOMaterials.Photoresist, 1000)
                .outputItems(GTOItems.RAW_PHOTON_CARRYING_WAFER)
                .EUt(1966080)
                .duration(600)
                .save();

        PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder("prepared_cosmic_soc_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER)
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist, 1000)
                .outputItems(GTOItems.PREPARED_COSMIC_SOC_WAFER)
                .EUt(31457280)
                .duration(4800)
                .save();
    }
}
