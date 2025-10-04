package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import committee.nova.mods.avaritia.init.registry.ModItems;

import static com.gtocore.common.data.GTORecipeTypes.LASER_ENGRAVER_RECIPES;

final class LaserEngraver {

    public static void init() {
        LASER_ENGRAVER_RECIPES.recipeBuilder("advanced_soc_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .outputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP_WAFER.asItem(), 8)
                .EUt(122880)
                .duration(125)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("soc_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .outputItems(GTItems.SYSTEM_ON_CHIP_WAFER.asItem(), 32)
                .EUt(122880)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("ilc_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .outputItems(GTItems.INTEGRATED_LOGIC_CIRCUIT_WAFER.asItem(), 64)
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("ulpic_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .outputItems(GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT_WAFER.asItem(), 64)
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("lanthanum_embedded_fullerene_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.LanthanumFullereneMix, 2)
                .notConsumable(TagPrefix.lens, GTMaterials.Sapphire)
                .inputFluids(GTMaterials.Nitrogen.getFluid(10000))
                .outputItems(TagPrefix.dust, GTOMaterials.LanthanumEmbeddedFullerene, 2)
                .outputFluids(GTMaterials.Ammonia.getFluid(10000))
                .EUt(1966080)
                .duration(320)
                .addData("special", true)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("lpic_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .outputItems(GTItems.LOW_POWER_INTEGRATED_CIRCUIT_WAFER.asItem(), 64)
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("nor_memory_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .outputItems(GTItems.NOR_MEMORY_CHIP_WAFER.asItem(), 32)
                .EUt(122880)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("cpu_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .outputItems(GTItems.CENTRAL_PROCESSING_UNIT_WAFER.asItem(), 64)
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("fullerene_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.UnfoldedFullerene)
                .notConsumable(TagPrefix.lens, GTMaterials.Ruby)
                .inputFluids(GTMaterials.Nitrogen.getFluid(10000))
                .outputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .outputFluids(GTMaterials.Ammonia.getFluid(10000))
                .EUt(2000000)
                .duration(400)
                .addData("special", true)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("nand_memory_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .outputItems(GTItems.NAND_MEMORY_CHIP_WAFER.asItem(), 32)
                .EUt(122880)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("highly_advanced_soc_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .outputItems(GTItems.HIGHLY_ADVANCED_SOC_WAFER.asItem(), 4)
                .EUt(122880)
                .duration(225)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("ram_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Green)
                .outputItems(GTItems.RANDOM_ACCESS_MEMORY_WAFER.asItem(), 64)
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("mpic_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .outputItems(GTItems.POWER_INTEGRATED_CIRCUIT_WAFER.asItem(), 32)
                .EUt(122880)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("diffractor_grating_mirror")
                .inputItems(GTOItems.PHOTOCOATED_HASSIUM_WAFER.asItem())
                .notConsumable(GTOItems.GRATING_LITHOGRAPHY_MASK.asItem())
                .outputItems(GTOItems.DIFFRACTOR_GRATING_MIRROR.asItem())
                .EUt(31457280)
                .duration(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("singularity")
                .inputItems(TagPrefix.block, GTOMaterials.Magmatter, 16)
                .inputItems(RegistriesUtils.getItemStack("avaritia:singularity", 1, "{Id:\"avaritia:spacetime\"}"))
                .outputItems(ModItems.singularity.get())
                .EUt(527765581332480L)
                .duration(1600)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("exotic_wafer")
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Magenta)
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC_WAFER.asItem())
                .outputItems(GTOItems.EXOTIC_WAFER.asItem())
                .EUt(1966080)
                .duration(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("simple_soc_wafer")
                .inputItems(GTOItems.TARANIUM_WAFER.asItem())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .outputItems(GTItems.SIMPLE_SYSTEM_ON_CHIP_WAFER.asItem(), 64)
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("diamond_crystal_circuit")
                .inputItems(GTOTagPrefix.ARTIFICIAL_GEM, GTMaterials.Diamond)
                .notConsumable(TagPrefix.lens, GTOMaterials.ElfGlass)
                .outputItems(GTOItems.DIAMOND_CRYSTAL_CIRCUIT.asItem(), 8)
                .inputFluids(GTMaterials.DistilledWater.getFluid(800))
                .EUt(1920)
                .duration(800)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("emerald_crystal_circuit")
                .inputItems(GTOTagPrefix.ARTIFICIAL_GEM, GTMaterials.Emerald)
                .notConsumable(TagPrefix.lens, GTOMaterials.ElfGlass)
                .outputItems(GTOItems.EMERALD_CRYSTAL_CIRCUIT.asItem(), 8)
                .inputFluids(GTMaterials.DistilledWater.getFluid(800))
                .EUt(1920)
                .duration(800)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("ruby_crystal_circuit")
                .inputItems(GTOTagPrefix.ARTIFICIAL_GEM, GTMaterials.Ruby)
                .notConsumable(TagPrefix.lens, GTOMaterials.ElfGlass)
                .outputItems(GTOItems.RUBY_CRYSTAL_CIRCUIT.asItem(), 8)
                .inputFluids(GTMaterials.DistilledWater.getFluid(800))
                .EUt(1920)
                .duration(800)
                .save();

        LASER_ENGRAVER_RECIPES.recipeBuilder("sapphire_crystal_circuit")
                .inputItems(GTOTagPrefix.ARTIFICIAL_GEM, GTMaterials.Sapphire)
                .notConsumable(TagPrefix.lens, GTOMaterials.ElfGlass)
                .outputItems(GTOItems.SAPPHIRE_CRYSTAL_CIRCUIT.asItem(), 8)
                .inputFluids(GTMaterials.DistilledWater.getFluid(800))
                .EUt(1920)
                .duration(800)
                .save();
        LASER_ENGRAVER_RECIPES.builder("cascade_mfpc_dustp")
                .inputItems(TagPrefix.dust, GTOMaterials.ExtremeTemperatureInterfaceStabilizedMFPC)
                .notConsumable(GTOItems.ULTRASHORT_PULSE_LASER.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.CascadeMFPC)
                .cleanroom(CleanroomType.CLEANROOM)
                .EUt(129600)
                .duration(20)
                .save();
    }
}
