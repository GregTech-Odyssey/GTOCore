package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES;

interface FluidSolidfication {

    static void init() {
        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("memory_foam_block"))
                .notConsumable(GTItems.SHAPE_MOLD_BLOCK.asStack())
                .inputFluids(GTOMaterials.ViscoelasticPolyurethaneFoam.getFluid(1000))
                .outputItems(GTOItems.MEMORY_FOAM_BLOCK.asStack())
                .EUt(30)
                .duration(60)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("lumin_essence_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.HighEnergyMixture, 2)
                .inputFluids(GTMaterials.PhosphoricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.LuminEssence)
                .EUt(480)
                .duration(200)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("pellet_antimatter"))
                .notConsumable(GTOItems.BALL_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Antimatter.getFluid(1000))
                .outputItems(GTOItems.PELLET_ANTIMATTER.asStack())
                .EUt(491520)
                .duration(800)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("kevlar_fiber"))
                .notConsumable(GTItems.SHAPE_MOLD_NUGGET.asStack())
                .inputFluids(GTOMaterials.LiquidCrystalKevlar.getFluid(72))
                .outputItems(GTOItems.KEVLAR_FIBER.asStack())
                .EUt(30)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("xenoxene_crystal_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Perlite, 3)
                .inputFluids(GTOMaterials.XenoxeneMixture.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.XenoxeneCrystal, 3)
                .EUt(1920)
                .duration(200)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("degenerate_rhenium_plate"))
                .notConsumable(GTItems.SHAPE_MOLD_PLATE.asStack())
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.LIQUID, 144))
                .outputItems(TagPrefix.plate, GTOMaterials.DegenerateRhenium)
                .EUt(7)
                .duration(400)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("rhodium_plated_palladium"))
                .inputItems(TagPrefix.ingotHot, GTMaterials.Palladium, 3)
                .inputFluids(GTMaterials.Rhodium.getFluid(144))
                .outputItems(TagPrefix.ingotHot, GTMaterials.RhodiumPlatedPalladium, 4)
                .EUt(7680)
                .duration(800)
                .save();

        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("superheavy_mix"))
                .outputItems(TagPrefix.dust, GTOMaterials.SuperheavyMix)
                .inputFluids(GTOMaterials.SuperheavyMix.getFluid(144))
                .EUt(122880)
                .duration(800)
                .save();
    }
}
