package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

interface FluidSolidfication {

    static void init() {
        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("memory_foam_block"))
                .notConsumable(GTItems.SHAPE_MOLD_BLOCK.asStack())
                .inputFluids(GTOMaterials.ViscoelasticPolyurethaneFoam.getFluid(1000))
                .outputItems(GTOItems.MEMORY_FOAM_BLOCK.asStack())
                .EUt(30)
                .duration(60)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("omni_purpose_infinity_fused_glass"))
                .inputItems(GTOBlocks.TARANIUM_BOROSILICATE_GLASS.asStack())
                .inputFluids(GTOMaterials.QuarkGluon.getFluid(FluidStorageKeys.PLASMA, 500))
                .outputItems(GTOBlocks.OMNI_PURPOSE_INFINITY_FUSED_GLASS.asStack())
                .EUt(491520)
                .duration(200)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("non_photonic_matter_exclusion_glass"))
                .inputItems(GTOBlocks.QUARKS_BOROSILICATE_GLASS.asStack())
                .inputFluids(GTOMaterials.Legendarium.getFluid(FluidStorageKeys.PLASMA, 576))
                .outputItems(GTOBlocks.NON_PHOTONIC_MATTER_EXCLUSION_GLASS.asStack())
                .EUt(1966080)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("lumin_essence_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.HighEnergyMixture, 2)
                .inputFluids(GTMaterials.PhosphoricAcid.getFluid(2000))
                .outputItems(TagPrefix.dust, GTOMaterials.LuminEssence)
                .EUt(480)
                .duration(200)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("pellet_antimatter"))
                .notConsumable(GTOItems.BALL_FIELD_SHAPE.asStack())
                .inputFluids(GTOMaterials.Antimatter.getFluid(1000))
                .outputItems(GTOItems.PELLET_ANTIMATTER.asStack())
                .EUt(491520)
                .duration(800)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("kevlar_fiber"))
                .notConsumable(GTItems.SHAPE_MOLD_NUGGET.asStack())
                .inputFluids(GTOMaterials.LiquidCrystalKevlar.getFluid(72))
                .outputItems(GTOItems.KEVLAR_FIBER.asStack())
                .EUt(30)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("xenoxene_crystal_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Perlite, 3)
                .inputFluids(GTOMaterials.XenoxeneMixture.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.XenoxeneCrystal, 3)
                .EUt(1920)
                .duration(200)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("electron_permeable_neutronium_coated_glass"))
                .inputItems(GTOBlocks.AMPROSIUM_BOROSILICATE_GLASS.asStack())
                .inputFluids(GTMaterials.Sulfur.getFluid(FluidStorageKeys.PLASMA, 288))
                .outputItems(GTOBlocks.ELECTRON_PERMEABLE_AMPROSIUM_COATED_GLASS.asStack())
                .EUt(122880)
                .duration(100)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("degenerate_rhenium_plate"))
                .notConsumable(GTItems.SHAPE_MOLD_PLATE.asStack())
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.LIQUID, 144))
                .outputItems(TagPrefix.plate, GTOMaterials.DegenerateRhenium)
                .EUt(7)
                .duration(400)
                .save();

        GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GTOCore.id("rhodium_plated_palladium"))
                .inputItems(TagPrefix.ingotHot, GTMaterials.Palladium, 3)
                .inputFluids(GTMaterials.Rhodium.getFluid(144))
                .outputItems(TagPrefix.ingotHot, GTMaterials.RhodiumPlatedPalladium, 4)
                .EUt(7680)
                .duration(800)
                .save();
    }
}
