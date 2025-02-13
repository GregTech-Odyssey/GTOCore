package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.init.GTOItems;
import com.gto.gtocore.init.GTOMaterials;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface PrecisionLaserEngraver {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("high_precision_crystal_soc"))
                .inputItems(GTItems.CRYSTAL_SYSTEM_ON_CHIP.asStack())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .inputFluids(GTOMaterials.EuvPhotoresist.getFluid(1000))
                .outputItems(GTOItems.HIGH_PRECISION_CRYSTAL_SOC.asStack())
                .EUt(7864320)
                .duration(2400)
                .save(provider);

        GTORecipeTypes.PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("pm_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.EuvPhotoresist.getFluid(1000))
                .outputItems(GTOItems.PM_WAFER.asStack())
                .EUt(1966080)
                .duration(1800)
                .save(provider);

        GTORecipeTypes.PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("fm_wafer"))
                .inputItems(GTOItems.PM_WAFER.asStack())
                .notConsumable(GTOItems.GRATING_LITHOGRAPHY_MASK.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist.getFluid(1000))
                .outputItems(GTOItems.FM_WAFER.asStack())
                .EUt(7864320)
                .duration(2800)
                .save(provider);

        GTORecipeTypes.PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("nm_wafer"))
                .inputItems(GTOItems.RUTHERFORDIUM_NEUTRONIUM_WAFER.asStack())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputFluids(GTOMaterials.Photoresist.getFluid(1000))
                .outputItems(GTOItems.NM_WAFER.asStack())
                .EUt(491520)
                .duration(900)
                .save(provider);

        GTORecipeTypes.PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("raw_photon_carrying_wafer"))
                .inputItems(GTOItems.RUTHERFORDIUM_NEUTRONIUM_WAFER.asStack())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightGray)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Magenta)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .inputFluids(GTOMaterials.Photoresist.getFluid(1000))
                .outputItems(GTOItems.RAW_PHOTON_CARRYING_WAFER.asStack())
                .EUt(1966080)
                .duration(600)
                .save(provider);

        GTORecipeTypes.PRECISION_LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("prepared_cosmic_soc_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(GTOItems.LITHOGRAPHY_MASK.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .inputFluids(GTOMaterials.GammaRaysPhotoresist.getFluid(1000))
                .outputItems(GTOItems.PREPARED_COSMIC_SOC_WAFER.asStack())
                .EUt(31457280)
                .duration(4800)
                .save(provider);
    }
}
