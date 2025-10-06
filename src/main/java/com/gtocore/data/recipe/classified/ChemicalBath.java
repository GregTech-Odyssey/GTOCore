package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.data.GTODimensions;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEBlocks;

import static com.gtocore.common.data.GTORecipeTypes.CHEMICAL_BATH_RECIPES;

final class ChemicalBath {

    public static void init() {
        CHEMICAL_BATH_RECIPES.recipeBuilder("petri_dish")
                .inputItems(GTOItems.CONTAMINATED_PETRI_DISH)
                .outputItems(GTItems.PETRI_DISH)
                .inputFluids(GTOMaterials.PiranhaSolution.getFluid(100))
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(25).EUt(30).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("naquadria_sulfate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 6)
                .inputFluids(GTOMaterials.AcidicNaquadriaCaesiumfluoride.getFluid(3000))
                .outputItems(TagPrefix.dust, GTMaterials.NaquadriaSulfate, 6)
                .outputItems(TagPrefix.dust, GTMaterials.TriniumSulfide, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 8)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumSulfate, 7)
                .chancedOutput(TagPrefix.dust, GTMaterials.Caesium, 8000, 500)
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("caesium_hydroxide_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Caesium, 2)
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CaesiumHydroxide, 6)
                .EUt(120)
                .duration(180)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("glucose")
                .inputItems(TagPrefix.gem, GTMaterials.Sugar, 2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Glucose, 24)
                .EUt(480)
                .duration(300)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("kevlar_plate")
                .inputItems(GTOItems.WOVEN_KEVLAR.asItem())
                .inputFluids(GTOMaterials.PolyurethaneResin.getFluid(1000))
                .outputItems(TagPrefix.plate, GTOMaterials.Kevlar)
                .EUt(480)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("photon_carrying_wafer")
                .inputItems(GTOItems.RAW_PHOTON_CARRYING_WAFER.asItem())
                .inputFluids(GTMaterials.Blaze.getFluid(288))
                .outputItems(GTOItems.PHOTON_CARRYING_WAFER.asItem())
                .EUt(1920)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("high_strength_concrete")
                .inputItems(TagPrefix.frameGt, GTMaterials.Steel)
                .inputFluids(GTMaterials.Concrete.getFluid(1152))
                .outputItems(GTOBlocks.HIGH_STRENGTH_CONCRETE.asItem())
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("damascus_steel_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Steel)
                .inputFluids(GTMaterials.Lubricant.getFluid(100))
                .outputItems(TagPrefix.dust, GTMaterials.DamascusSteel)
                .EUt(120)
                .duration(200)
                .dimension(GTODimensions.ANCIENT_WORLD)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("end_stone")
                .inputItems(Blocks.ANDESITE.asItem())
                .inputFluids(GTMaterials.LiquidEnderAir.getFluid(1000))
                .outputItems(TagPrefix.rock, GTMaterials.Endstone)
                .EUt(480)
                .duration(800)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("resonating_gem")
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Sapphire)
                .inputFluids(GTOMaterials.LiquidStarlight.getFluid(1000))
                .outputItems(GTOItems.RESONATING_GEM.asItem())
                .EUt(31457280)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("leached_turpentine")
                .inputItems(Blocks.DARK_OAK_LOG.asItem(), 16)
                .inputFluids(GTMaterials.Naphtha.getFluid(1000))
                .outputFluids(GTOMaterials.LeachedTurpentine.getFluid(1000))
                .EUt(480)
                .duration(80)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("sculk_vein")
                .inputItems(Blocks.VINE.asItem())
                .inputFluids(GTMaterials.EchoShard.getFluid(10))
                .outputItems(Blocks.SCULK_VEIN.asItem())
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("x_ray_waveguide")
                .inputItems(GTOItems.FULLERENE_POLYMER_MATRIX_FINE_TUBING.asItem())
                .inputFluids(GTOMaterials.IridiumTrichlorideSolution.getFluid(100))
                .outputItems(GTOItems.X_RAY_WAVEGUIDE.asItem())
                .EUt(8000000)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("netherrack")
                .inputItems(Blocks.GRANITE.asItem())
                .inputFluids(GTMaterials.LiquidNetherAir.getFluid(1000))
                .outputItems(TagPrefix.rock, GTMaterials.Netherrack)
                .EUt(120)
                .duration(800)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("x_ray_mirror_plate")
                .inputItems(TagPrefix.plate, GTMaterials.Graphene)
                .inputFluids(GTOMaterials.IridiumTrichlorideSolution.getFluid(100))
                .outputItems(GTOItems.X_RAY_MIRROR_PLATE.asItem())
                .EUt(2000000)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("black_candle")
                .inputItems(Blocks.TRIPWIRE.asItem())
                .inputFluids(GTMaterials.Oil.getFluid(100))
                .outputItems(Blocks.BLACK_CANDLE.asItem())
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("ash_leaching_solution")
                .inputItems(TagPrefix.dust, GTMaterials.Ash, 12)
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(1000))
                .outputFluids(GTOMaterials.AshLeachingSolution.getFluid(1000))
                .EUt(120)
                .duration(400)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("fullerene_polymer_matrix_pulp_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.PalladiumFullereneMatrix)
                .inputFluids(GTOMaterials.PCBs.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.FullerenePolymerMatrixPulp, 2)
                .EUt(8000000)
                .duration(40)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("metal_residue_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.PartiallyOxidizedResidues)
                .inputFluids(GTOMaterials.BedrockGas.getFluid(100))
                .outputItems(TagPrefix.dust, GTOMaterials.MetalResidue)
                .EUt(122880)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("ender_obsidian")
                .inputItems(GTOBlocks.SHINING_OBSIDIAN.asItem())
                .inputFluids(GTMaterials.EnderEye.getFluid(1152))
                .outputItems(GTOBlocks.ENDER_OBSIDIAN.asItem())
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("flawless_budding_quartz")
                .inputItems(AEBlocks.FLAWED_BUDDING_QUARTZ.block().asItem())
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(AEBlocks.FLAWLESS_BUDDING_QUARTZ.block().asItem())
                .EUt(30)
                .duration(400)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("eternity_catalyst")
                .inputItems(GTOItems.SPACETIME_CATALYST.asItem())
                .inputFluids(GTOMaterials.Eternity.getFluid(1000))
                .outputItems(GTOItems.ETERNITY_CATALYST.asItem())
                .EUt(8053063680L)
                .duration(1600)
                .save();
    }
}
