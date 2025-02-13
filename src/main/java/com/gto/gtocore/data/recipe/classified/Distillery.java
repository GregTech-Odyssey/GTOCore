package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.init.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Distillery {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.DISTILLERY_RECIPES.recipeBuilder(GTOCore.id("kerosene"))
                .inputItems(TagPrefix.dust, GTMaterials.Coke)
                .inputFluids(GTMaterials.CoalTar.getFluid(200))
                .outputItems(TagPrefix.dust, GTMaterials.DarkAsh)
                .outputFluids(GTOMaterials.Kerosene.getFluid(100))
                .EUt(120)
                .duration(30)
                .save(provider);

        GTRecipeTypes.DISTILLERY_RECIPES.recipeBuilder(GTOCore.id("enriched_dragon_breath"))
                .inputFluids(GTOMaterials.DragonBreath.getFluid(10))
                .outputFluids(GTOMaterials.EnrichedDragonBreath.getFluid(5))
                .EUt(120)
                .duration(100)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);

        GTRecipeTypes.DISTILLERY_RECIPES.recipeBuilder(GTOCore.id("cyclopentadiene"))
                .circuitMeta(12)
                .inputFluids(GTMaterials.SeverelySteamCrackedNaphtha.getFluid(1000))
                .outputFluids(GTOMaterials.Cyclopentadiene.getFluid(150))
                .EUt(30)
                .duration(240)
                .save(provider);

        GTRecipeTypes.DISTILLERY_RECIPES.recipeBuilder(GTOCore.id("rp_1"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Kerosene.getFluid(50))
                .outputFluids(GTOMaterials.Rp1.getFluid(25))
                .EUt(120)
                .duration(16)
                .save(provider);

        GTRecipeTypes.DISTILLERY_RECIPES.recipeBuilder(GTOCore.id("fluoro_benzene"))
                .inputFluids(GTOMaterials.BenzenediazoniumTetrafluoroborate.getFluid(1000))
                .outputFluids(GTOMaterials.FluoroBenzene.getFluid(1000))
                .EUt(122880)
                .duration(100)
                .save(provider);

        GTRecipeTypes.DISTILLERY_RECIPES.recipeBuilder(GTOCore.id("enriched_potassium_iodide_slurry"))
                .inputFluids(GTOMaterials.KelpSlurry.getFluid(1000))
                .outputFluids(GTOMaterials.EnrichedPotassiumIodideSlurry.getFluid(100))
                .EUt(30)
                .duration(200)
                .save(provider);
    }
}
