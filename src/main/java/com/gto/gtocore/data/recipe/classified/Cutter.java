package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Cutter {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("exotic_ram_chip"))
                .inputItems(GTOItems.EXOTIC_RAM_WAFER.asStack())
                .inputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid(480))
                .outputItems(GTOItems.EXOTIC_RAM_CHIP.asStack(32))
                .EUt(524288)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("bioware_chip"))
                .inputItems(GTOItems.BIOWARE_BOULE.asStack())
                .inputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid(300))
                .outputItems(GTOItems.BIOWARE_CHIP.asStack(16))
                .outputItems(GTOItems.BIOLOGICAL_CELLS.asStack(8))
                .EUt(491520)
                .duration(600)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("fm_chip"))
                .inputItems(GTOItems.FM_WAFER.asStack())
                .inputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid(1440))
                .outputItems(GTOItems.FM_CHIP.asStack(2))
                .EUt(524288)
                .duration(2700)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("cosmic_soc"))
                .inputItems(GTOItems.COSMIC_SOC_WAFER.asStack())
                .inputFluids(GTOMaterials.DegassedWater.getFluid(450))
                .outputItems(GTOItems.COSMIC_SOC.asStack(8))
                .EUt(7864320)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("pm_chip"))
                .inputItems(GTOItems.PM_WAFER.asStack())
                .inputFluids(GTOMaterials.PHNeutralWater.getFluid(900))
                .outputItems(GTOItems.PM_CHIP.asStack(4))
                .EUt(122880)
                .duration(1800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("nm_chip"))
                .inputItems(GTOItems.NM_WAFER.asStack())
                .inputFluids(GTOMaterials.FlocculentWater.getFluid(900))
                .outputItems(GTOItems.NM_CHIP.asStack(4))
                .EUt(30720)
                .duration(1800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("photocoated_hassium_wafer"))
                .inputItems(GTOItems.PHOTOCOATED_HASSIUM_BOULE.asStack())
                .inputFluids(GTOMaterials.BaryonicPerfectionWater.getFluid(140))
                .outputItems(GTOItems.PHOTOCOATED_HASSIUM_WAFER.asStack(4))
                .EUt(31457280)
                .duration(280)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("cosmic_ram_chip"))
                .inputItems(GTOItems.COSMIC_RAM_WAFER.asStack())
                .inputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid(480))
                .outputItems(GTOItems.COSMIC_RAM_CHIP.asStack(32))
                .EUt(2097152)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("rutherfordium_neutronium_wafer"))
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_BOULE.asStack())
                .inputFluids(GTOMaterials.FlocculentWater.getFluid(1600))
                .outputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asStack(64))
                .outputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asStack(32))
                .EUt(30720)
                .duration(3200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("supracausal_ram_chip"))
                .inputItems(GTOItems.SUPRACAUSAL_RAM_WAFER.asStack())
                .inputFluids(GTOMaterials.DegassedWater.getFluid(480))
                .outputItems(GTOItems.SUPRACAUSAL_RAM_CHIP.asStack(4))
                .EUt(8388608)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("taranium_wafer"))
                .inputItems(GTOItems.TARANIUM_BOULE.asStack())
                .inputFluids(GTOMaterials.PHNeutralWater.getFluid(1600))
                .outputItems(GTOItems.TARANIUM_WAFER.asStack(64))
                .outputItems(GTOItems.TARANIUM_WAFER.asStack(64))
                .EUt(122880)
                .duration(3200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("optical_slice"))
                .inputItems(GTOItems.OPTICAL_WAFER.asStack())
                .inputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid(280))
                .outputItems(GTOItems.OPTICAL_SLICE.asStack(16))
                .EUt(1966080)
                .duration(560)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("exotic_chip"))
                .inputItems(GTOItems.EXOTIC_WAFER.asStack())
                .inputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid(450))
                .outputItems(GTOItems.EXOTIC_CHIP.asStack(4))
                .EUt(1966080)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);

        GTRecipeTypes.CUTTER_RECIPES.recipeBuilder(GTOCore.id("optical_ram_chip"))
                .inputItems(GTOItems.OPTICAL_RAM_WAFER.asStack())
                .inputFluids(GTOMaterials.PHNeutralWater.getFluid(450))
                .outputItems(GTOItems.OPTICAL_RAM_CHIP.asStack(32))
                .EUt(122880)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save(provider);
    }
}
