package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import committee.nova.mods.avaritia.init.registry.ModItems;

interface LaserEngraver {

    static void init() {
        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("advanced_soc_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .outputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP_WAFER.asStack(8))
                .EUt(122880)
                .duration(125)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("soc_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .outputItems(GTItems.SYSTEM_ON_CHIP_WAFER.asStack(32))
                .EUt(122880)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("ilc_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .outputItems(GTItems.INTEGRATED_LOGIC_CIRCUIT_WAFER.asStack(64))
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("ulpic_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .outputItems(GTItems.ULTRA_LOW_POWER_INTEGRATED_CIRCUIT_WAFER.asStack(64))
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("lanthanum_embedded_fullerene_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.LanthanumFullereneMix, 2)
                .notConsumable(TagPrefix.lens, GTMaterials.Sapphire)
                .inputFluids(GTMaterials.Nitrogen.getFluid(10000))
                .outputItems(TagPrefix.dust, GTOMaterials.LanthanumEmbeddedFullerene, 2)
                .outputFluids(GTMaterials.Ammonia.getFluid(10000))
                .EUt(1966080)
                .duration(320)
                .addData("special", true)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("lpic_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .outputItems(GTItems.LOW_POWER_INTEGRATED_CIRCUIT_WAFER.asStack(64))
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("nor_memory_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .outputItems(GTItems.NOR_MEMORY_CHIP_WAFER.asStack(32))
                .EUt(122880)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("cpu_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .outputItems(GTItems.CENTRAL_PROCESSING_UNIT_WAFER.asStack(64))
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("fullerene_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.UnfoldedFullerene)
                .notConsumable(TagPrefix.lens, GTMaterials.Ruby)
                .inputFluids(GTMaterials.Nitrogen.getFluid(10000))
                .outputItems(TagPrefix.dust, GTOMaterials.Fullerene)
                .outputFluids(GTMaterials.Ammonia.getFluid(10000))
                .EUt(2000000)
                .duration(400)
                .addData("special", true)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("nand_memory_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .outputItems(GTItems.NAND_MEMORY_CHIP_WAFER.asStack(32))
                .EUt(122880)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("highly_advanced_soc_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .outputItems(GTItems.HIGHLY_ADVANCED_SOC_WAFER.asStack(4))
                .EUt(122880)
                .duration(225)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("ram_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Green)
                .outputItems(GTItems.RANDOM_ACCESS_MEMORY_WAFER.asStack(64))
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("mpic_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .outputItems(GTItems.POWER_INTEGRATED_CIRCUIT_WAFER.asStack(32))
                .EUt(122880)
                .duration(50)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("diffractor_grating_mirror"))
                .inputItems(GTOItems.PHOTOCOATED_HASSIUM_WAFER.asStack())
                .notConsumable(GTOItems.GRATING_LITHOGRAPHY_MASK.asStack())
                .outputItems(GTOItems.DIFFRACTOR_GRATING_MIRROR.asStack())
                .EUt(31457280)
                .duration(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("singularity"))
                .inputItems(TagPrefix.block, GTOMaterials.Magmatter, 16)
                .inputItems(RegistriesUtils.getItemStack("avaritia:singularity", 1, "{Id:\"avaritia:spacetime\"}"))
                .outputItems(ModItems.singularity.get())
                .EUt(527765581332480L)
                .duration(1600)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("exotic_wafer"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Magenta)
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC_WAFER.asStack())
                .outputItems(GTOItems.EXOTIC_WAFER.asStack())
                .EUt(1966080)
                .duration(600)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.LASER_ENGRAVER_RECIPES.recipeBuilder(GTOCore.id("simple_soc_wafer"))
                .inputItems(GTOItems.TARANIUM_WAFER.asStack())
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .outputItems(GTItems.SIMPLE_SYSTEM_ON_CHIP_WAFER.asStack(64))
                .EUt(122880)
                .duration(13)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();
    }
}
