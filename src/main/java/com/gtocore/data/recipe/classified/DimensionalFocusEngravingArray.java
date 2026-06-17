package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES;

final class DimensionalFocusEngravingArray {

    public static void init() {
        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder("fullerene_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.UnfoldedFullerene)
                .notConsumable(TagPrefix.lens, GTMaterials.Ruby)
                .outputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .outputFluids(GTMaterials.Ammonia, 10000)
                .EUt(8000000)
                .duration(100)
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder("pm_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Magenta)
                .inputFluids(GTOMaterials.EuvPhotoresist, 100)
                .outputItems(GTOItems.PM_WAFER)
                .EUt(1966080)
                .duration(1800)
                .scanner(b -> b.researchStack(GTOItems.PM_WAFER.asItem())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asItem())
                        .EUt(1966080).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder("raw_photon_carrying_wafer")
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .inputFluids(GTOMaterials.Photoresist, 100)
                .outputItems(GTOItems.RAW_PHOTON_CARRYING_WAFER)
                .EUt(1966080)
                .duration(600)
                .scanner(b -> b.researchStack(GTOItems.RAW_PHOTON_CARRYING_WAFER.asItem())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asItem())
                        .EUt(1966080).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder("high_precision_crystal_soc")
                .inputItems(GTItems.CRYSTAL_SYSTEM_ON_CHIP)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .inputFluids(GTOMaterials.EuvPhotoresist, 100)
                .outputItems(GTOItems.HIGH_PRECISION_CRYSTAL_SOC)
                .EUt(7864320)
                .duration(2400)
                .scanner(b -> b.researchStack(GTOItems.HIGH_PRECISION_CRYSTAL_SOC.asItem())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asItem())
                        .EUt(7864320).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder("prepared_cosmic_soc_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist, 100)
                .outputItems(GTOItems.PREPARED_COSMIC_SOC_WAFER)
                .EUt(31457280)
                .duration(4800)
                .scanner(b -> b.researchStack(GTOItems.PREPARED_COSMIC_SOC_WAFER.asItem())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asItem())
                        .EUt(31457280).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder("nm_wafer")
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .inputFluids(GTOMaterials.Photoresist, 100)
                .outputItems(GTOItems.NM_WAFER)
                .EUt(491520)
                .duration(900)
                .scanner(b -> b.researchStack(GTOItems.NM_WAFER.asItem())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asItem())
                        .EUt(491520).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.recipeBuilder("fm_wafer")
                .inputItems(GTOItems.PM_WAFER)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist, 100)
                .outputItems(GTOItems.FM_WAFER)
                .EUt(7864320)
                .duration(2700)
                .scanner(b -> b.researchStack(GTOItems.FM_WAFER.asItem())
                        .dataStack(GTOItems.OPTICAL_DATA_STICK.asItem())
                        .EUt(7864320).duration(2400))
                .save();

        DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES.builder("lanthanum_embedded_fullerene_dust")
                .notConsumable(TagPrefix.lens, GTMaterials.Sapphire)
                .inputItems(TagPrefix.dust, GTOMaterials.LanthanumFullereneMix, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.LanthanumEmbeddedFullerene, 2)
                .inputFluids(GTMaterials.Nitrogen, 10000)
                .outputFluids(GTMaterials.Ammonia, 10000)
                .EUt(1966080)
                .duration(320)
                .save();
    }
}
