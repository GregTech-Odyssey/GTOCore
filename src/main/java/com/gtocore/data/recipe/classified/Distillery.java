package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.machine.GTOCleanroomType;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.DISTILLERY_RECIPES;

final class Distillery {

    public static void init() {
        DISTILLERY_RECIPES.recipeBuilder("kerosene")
                .inputItems(TagPrefix.dust, GTMaterials.Coke)
                .inputFluids(GTMaterials.CoalTar.getFluid(200))
                .outputItems(TagPrefix.dust, GTMaterials.DarkAsh)
                .outputFluids(GTOMaterials.Kerosene.getFluid(100))
                .EUt(120)
                .duration(30)
                .save();

        DISTILLERY_RECIPES.recipeBuilder("enriched_dragon_breath")
                .inputFluids(GTOMaterials.DragonBreath.getFluid(10))
                .outputFluids(GTOMaterials.EnrichedDragonBreath.getFluid(5))
                .EUt(120)
                .duration(100)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();

        DISTILLERY_RECIPES.recipeBuilder("cyclopentadiene")
                .circuitMeta(12)
                .inputFluids(GTMaterials.SeverelySteamCrackedNaphtha.getFluid(1000))
                .outputFluids(GTOMaterials.Cyclopentadiene.getFluid(150))
                .EUt(30)
                .duration(240)
                .save();

        DISTILLERY_RECIPES.recipeBuilder("rp_1")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Kerosene.getFluid(50))
                .outputFluids(GTOMaterials.Rp1.getFluid(25))
                .EUt(120)
                .duration(16)
                .save();

        DISTILLERY_RECIPES.recipeBuilder("fluoro_benzene")
                .inputFluids(GTOMaterials.BenzenediazoniumTetrafluoroborate.getFluid(1000))
                .outputFluids(GTOMaterials.FluoroBenzene.getFluid(1000))
                .EUt(122880)
                .duration(100)
                .save();

        DISTILLERY_RECIPES.recipeBuilder("enriched_potassium_iodide_slurry")
                .inputFluids(GTOMaterials.KelpSlurry.getFluid(1000))
                .outputFluids(GTOMaterials.EnrichedPotassiumIodideSlurry.getFluid(100))
                .EUt(30)
                .duration(200)
                .save();

        DISTILLERY_RECIPES.recipeBuilder("poly_aluminium_chloride")
                .outputItems(TagPrefix.dustTiny, GTMaterials.DarkAsh)
                .inputFluids(GTOMaterials.FlocculationWasteSolution.getFluid(1000))
                .outputFluids(GTOMaterials.PolyAluminiumChloride.getFluid(990))
                .EUt(120)
                .duration(400)
                .save();

        DISTILLERY_RECIPES.builder("diethylbenzene_fraction")
                .inputFluids(GTOMaterials.Divinylbenzene, 1000)
                .outputFluids(GTOMaterials.DiethylbenzeneFraction, 800)
                .EUt(400)
                .duration(100)
                .save();
    }
}
