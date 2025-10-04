package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.StringUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import javax.annotation.Nullable;

import static com.gtocore.common.data.GTORecipeTypes.CUTTER_RECIPES;

final class Cutter {

    public static void init() {
        CUTTER_RECIPES.recipeBuilder("exotic_ram_chip")
                .inputItems(GTOItems.EXOTIC_RAM_WAFER.asItem())
                .inputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid(480))
                .outputItems(GTOItems.EXOTIC_RAM_CHIP.asItem(), 32)
                .EUt(524288)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("bioware_chip")
                .inputItems(GTOItems.BIOWARE_BOULE.asItem())
                .inputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid(300))
                .outputItems(GTOItems.BIOWARE_CHIP.asItem(), 16)
                .outputItems(GTOItems.BIOLOGICAL_CELLS.asItem(), 8)
                .EUt(491520)
                .duration(600)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("fm_chip")
                .inputItems(GTOItems.FM_WAFER.asItem())
                .inputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid(1440))
                .outputItems(GTOItems.FM_CHIP.asItem(), 2)
                .EUt(524288)
                .duration(2700)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("cosmic_soc")
                .inputItems(GTOItems.SIMPLE_COSMIC_SOC_WAFER.asItem())
                .inputFluids(GTOMaterials.DegassedWater.getFluid(450))
                .outputItems(GTOItems.SIMPLE_COSMIC_SOC.asItem(), 8)
                .EUt(7864320)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("pm_chip")
                .inputItems(GTOItems.PM_WAFER.asItem())
                .inputFluids(GTOMaterials.PHNeutralWater.getFluid(900))
                .outputItems(GTOItems.PM_CHIP.asItem(), 4)
                .EUt(122880)
                .duration(1800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("nm_chip")
                .inputItems(GTOItems.NM_WAFER.asItem())
                .inputFluids(GTOMaterials.FlocculentWater.getFluid(900))
                .outputItems(GTOItems.NM_CHIP.asItem(), 4)
                .EUt(30720)
                .duration(1800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("photocoated_hassium_wafer")
                .inputItems(GTOItems.PHOTOCOATED_HASSIUM_BOULE.asItem())
                .inputFluids(GTOMaterials.BaryonicPerfectionWater.getFluid(140))
                .outputItems(GTOItems.PHOTOCOATED_HASSIUM_WAFER.asItem(), 4)
                .EUt(31457280)
                .duration(280)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("cosmic_ram_chip")
                .inputItems(GTOItems.COSMIC_RAM_WAFER.asItem())
                .inputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid(480))
                .outputItems(GTOItems.COSMIC_RAM_CHIP.asItem(), 32)
                .EUt(2097152)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("rutherfordium_neutronium_wafer")
                .inputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_BOULE.asItem())
                .inputFluids(GTOMaterials.FlocculentWater.getFluid(1600))
                .outputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asItem(), 64)
                .outputItems(GTOItems.RUTHERFORDIUM_AMPROSIUM_WAFER.asItem(), 32)
                .EUt(30720)
                .duration(3200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("supracausal_ram_chip")
                .inputItems(GTOItems.SUPRACAUSAL_RAM_WAFER.asItem())
                .inputFluids(GTOMaterials.DegassedWater.getFluid(480))
                .outputItems(GTOItems.SUPRACAUSAL_RAM_CHIP.asItem(), 4)
                .EUt(8388608)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("taranium_wafer")
                .inputItems(GTOItems.TARANIUM_BOULE.asItem())
                .inputFluids(GTOMaterials.PHNeutralWater.getFluid(1600))
                .outputItems(GTOItems.TARANIUM_WAFER.asItem(), 64)
                .outputItems(GTOItems.TARANIUM_WAFER.asItem(), 64)
                .EUt(122880)
                .duration(3200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("optical_slice")
                .inputItems(GTOItems.OPTICAL_WAFER.asItem())
                .inputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid(280))
                .outputItems(GTOItems.OPTICAL_SLICE.asItem(), 16)
                .EUt(1966080)
                .duration(560)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("exotic_chip")
                .inputItems(GTOItems.EXOTIC_WAFER.asItem())
                .inputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid(450))
                .outputItems(GTOItems.EXOTIC_CHIP.asItem(), 4)
                .EUt(1966080)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("optical_ram_chip")
                .inputItems(GTOItems.OPTICAL_RAM_WAFER.asItem())
                .inputFluids(GTOMaterials.PHNeutralWater.getFluid(450))
                .outputItems(GTOItems.OPTICAL_RAM_CHIP.asItem(), 32)
                .EUt(122880)
                .duration(900)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CUTTER_RECIPES.recipeBuilder("silicon_crystal_seed")
                .inputItems(TagPrefix.block, GTMaterials.Silicon)
                .outputItems(GTOTagPrefix.CRYSTAL_SEED, GTMaterials.Silicon, 16)
                .inputFluids(GTMaterials.DistilledWater.getFluid(10000))
                .EUt(120)
                .duration(2600)
                .save();

        CUTTER_RECIPES.recipeBuilder("electronicgradesilicon_crystal_seed")
                .inputItems(TagPrefix.block, GTOMaterials.ElectronicGradeSilicon)
                .outputItems(GTOTagPrefix.CRYSTAL_SEED, GTOMaterials.ElectronicGradeSilicon, 16)
                .inputFluids(GTOMaterials.OzoneWater.getFluid(10000))
                .EUt(480)
                .duration(4800)
                .save();

        CUTTER_RECIPES.recipeBuilder("ultrahighpuritysilicon_crystal_seed")
                .inputItems(TagPrefix.block, GTOMaterials.UltraHighPuritySilicon)
                .outputItems(GTOTagPrefix.CRYSTAL_SEED, GTOMaterials.UltraHighPuritySilicon, 16)
                .inputFluids(GTOMaterials.PHNeutralWater.getFluid(10000))
                .EUt(1920)
                .duration(6000)
                .save();

        add("minecraft:beef", "farmersdelight:minced_beef", 2, null, 1);
        add("farmersdelight:ham", "minecraft:porkchop", 2, "minecraft:bone", 1);
        add("minecraft:cake", "farmersdelight:cake_slice", 7, null, 1);
        add("minecraft:cooked_mutton", "farmersdelight:cooked_mutton_chops", 2, null, 1);
        add("minecraft:salmon", "farmersdelight:salmon_slice", 2, "minecraft:bone_meal", 1);
        add("farmersdelight:smoked_ham", "minecraft:cooked_porkchop", 2, "minecraft:bone", 1);
        add("farmersdelight:sweet_berry_cheesecake", "farmersdelight:sweet_berry_cheesecake_slice", 4, null, 1);
        add("minecraft:chicken", "farmersdelight:chicken_cuts", 2, "minecraft:bone_meal", 1);
        add("minecraft:cooked_cod", "farmersdelight:cooked_cod_slice", 2, "minecraft:bone_meal", 1);
        add("gtceu:dough", "farmersdelight:raw_pasta", 1, null, 1);
        add("farmersdelight:rice_panicle", "farmersdelight:rice", 1, "farmersdelight:straw", 1);
        add("farmersdelight:kelp_roll", "farmersdelight:kelp_roll_slice", 3, null, 1);
        add("minecraft:rose_bush", "farmersrespite:rose_hips", 2, null, 1);
        add("minecraft:pumpkin", "farmersdelight:pumpkin_slice", 4, null, 1);
        add("farmersrespite:coffee_cake", "farmersrespite:coffee_cake_slice", 7, null, 1);
        add("minecraft:mutton", "farmersdelight:mutton_chops", 2, null, 1);
        add("farmersrespite:coffee_berries", "farmersrespite:coffee_beans", 1, null, 1);
        add("farmersdelight:chocolate_pie", "farmersdelight:chocolate_pie_slice", 4, null, 1);
        add("minecraft:cooked_salmon", "farmersdelight:cooked_salmon_slice", 2, "minecraft:bone_meal", 1);
        add("minecraft:porkchop", "farmersdelight:bacon", 2, null, 1);
        add("farmersdelight:cabbage", "farmersdelight:cabbage_leaf", 2, null, 1);
        add("farmersrespite:rose_hip_pie", "farmersrespite:rose_hip_pie_slice", 4, null, 1);
        add("farmersdelight:apple_pie", "farmersdelight:apple_pie_slice", 4, null, 1);
        add("minecraft:cod", "farmersdelight:cod_slice", 2, "minecraft:bone_meal", 1);
        add("minecraft:cooked_chicken", "farmersdelight:cooked_chicken_cuts", 2, "minecraft:bone_meal", 1);
    }

    private static void add(String input, String output1, int c1, @Nullable String output2, int c2) {
        RecipeBuilder builder = CUTTER_RECIPES.recipeBuilder(StringUtils.decompose(output1)[1])
                .inputItems(input)
                .outputItems(output1, c1)
                .EUt(8)
                .duration(20);

        if (output2 != null) {
            builder.outputItems(output2, c2);
        }
        builder.save();
    }
}
