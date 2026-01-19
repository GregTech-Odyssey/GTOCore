package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
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
                .inputFluids(GTOMaterials.PiranhaSolution, 100)
                .cleanroom(CleanroomType.CLEANROOM)
                .duration(25).EUt(30).save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("naquadria_sulfate_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Sodium, 6)
                .inputFluids(GTOMaterials.AcidicNaquadriaCaesiumfluoride, 3000)
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
                .inputFluids(GTMaterials.HydrogenPeroxide, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.CaesiumHydroxide, 6)
                .EUt(120)
                .duration(180)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("glucose")
                .inputItems(TagPrefix.gem, GTMaterials.Sugar, 2)
                .inputFluids(GTMaterials.Water, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.Glucose, 24)
                .EUt(480)
                .duration(300)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("kevlar_plate")
                .inputItems(GTOItems.WOVEN_KEVLAR)
                .inputFluids(GTOMaterials.PolyurethaneResin, 1000)
                .outputItems(TagPrefix.plate, GTOMaterials.Kevlar)
                .EUt(480)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("photon_carrying_wafer")
                .inputItems(GTOItems.RAW_PHOTON_CARRYING_WAFER)
                .inputFluids(GTMaterials.Blaze, 288)
                .outputItems(GTOItems.PHOTON_CARRYING_WAFER)
                .EUt(1920)
                .duration(800)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("high_strength_concrete")
                .inputItems(TagPrefix.frameGt, GTMaterials.Steel)
                .inputFluids(GTMaterials.Concrete, 1152)
                .outputItems(GTOBlocks.HIGH_STRENGTH_CONCRETE.asItem())
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("damascus_steel_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Steel)
                .inputFluids(GTMaterials.Lubricant, 100)
                .outputItems(TagPrefix.dust, GTMaterials.DamascusSteel)
                .EUt(120)
                .duration(200)
                .dimension(GTODimensions.ANCIENT_WORLD)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("end_stone")
                .inputItems(Blocks.ANDESITE.asItem())
                .inputFluids(GTMaterials.LiquidEnderAir, 1000)
                .outputItems(TagPrefix.rock, GTMaterials.Endstone)
                .EUt(480)
                .duration(800)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("resonating_gem")
                .inputItems(TagPrefix.gemExquisite, GTOMaterials.Resonarium)
                .inputFluids(GTOMaterials.LiquidStarlight, 1000)
                .outputItems(GTOItems.RESONATING_GEM)
                .EUt(31457280)
                .duration(400)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("leached_turpentine")
                .inputItems(Blocks.DARK_OAK_LOG.asItem(), 16)
                .inputFluids(GTMaterials.Naphtha, 1000)
                .outputFluids(GTOMaterials.LeachedTurpentine, 1000)
                .EUt(480)
                .duration(80)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("sculk_vein")
                .inputItems(Blocks.VINE.asItem())
                .inputFluids(GTMaterials.EchoShard, 10)
                .outputItems(Blocks.SCULK_VEIN.asItem())
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("x_ray_waveguide")
                .inputItems(GTOItems.FULLERENE_POLYMER_MATRIX_FINE_TUBING)
                .inputFluids(GTOMaterials.IridiumTrichlorideSolution, 100)
                .outputItems(GTOItems.X_RAY_WAVEGUIDE)
                .EUt(8000000)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("netherrack")
                .inputItems(Blocks.GRANITE.asItem())
                .inputFluids(GTMaterials.LiquidNetherAir, 1000)
                .outputItems(TagPrefix.rock, GTMaterials.Netherrack)
                .EUt(120)
                .duration(800)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("x_ray_mirror_plate")
                .inputItems(TagPrefix.plate, GTMaterials.Graphene)
                .inputFluids(GTOMaterials.IridiumTrichlorideSolution, 100)
                .outputItems(GTOItems.X_RAY_MIRROR_PLATE)
                .EUt(2000000)
                .duration(240)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("black_candle")
                .inputItems(Blocks.TRIPWIRE.asItem())
                .inputFluids(GTMaterials.Oil, 100)
                .outputItems(Blocks.BLACK_CANDLE.asItem())
                .EUt(120)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("ash_leaching_solution")
                .inputItems(TagPrefix.dust, GTMaterials.Ash, 12)
                .inputFluids(GTMaterials.SulfuricAcid, 1000)
                .outputFluids(GTOMaterials.AshLeachingSolution, 1000)
                .EUt(120)
                .duration(400)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("fullerene_polymer_matrix_pulp_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.PalladiumFullereneMatrix)
                .inputFluids(GTOMaterials.PCBs, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.FullerenePolymerMatrixPulp, 2)
                .EUt(8000000)
                .duration(40)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("metal_residue_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.PartiallyOxidizedResidues)
                .inputFluids(GTOMaterials.BedrockGas, 100)
                .outputItems(TagPrefix.dust, GTOMaterials.MetalResidue)
                .EUt(122880)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("ender_obsidian")
                .inputItems(GTOBlocks.SHINING_OBSIDIAN.asItem())
                .inputFluids(GTMaterials.EnderEye, 1152)
                .outputItems(GTOBlocks.ENDER_OBSIDIAN.asItem())
                .EUt(480)
                .duration(200)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("flawless_budding_quartz")
                .inputItems(AEBlocks.FLAWED_BUDDING_QUARTZ.block().asItem())
                .inputFluids(GTMaterials.Water, 1000)
                .outputItems(AEBlocks.FLAWLESS_BUDDING_QUARTZ.block().asItem())
                .EUt(30)
                .duration(400)
                .save();

        CHEMICAL_BATH_RECIPES.recipeBuilder("eternity_catalyst")
                .inputItems(GTOItems.SPACETIME_CATALYST)
                .inputFluids(GTOMaterials.Eternity, 1000)
                .outputItems(GTOItems.ETERNITY_CATALYST)
                .EUt(8053063680L)
                .duration(1600)
                .save();

        CHEMICAL_BATH_RECIPES.builder("yellow_dye_masterbatch")
                .inputItems(GTOTagPrefix.dye, GTMaterials.DyeYellow, 3)
                .outputItems(GTOItems.YELLOW_DYE_MASTERBATCH)
                .inputFluids(GTMaterials.Polyethylene, 2000)
                .EUt(2040)
                .duration(150)
                .save();

        CHEMICAL_BATH_RECIPES.builder("blue_dye_masterbatch")
                .inputItems(GTOTagPrefix.dye, GTMaterials.DyeCyan, 3)
                .outputItems(GTOItems.CYAN_DYE_MASTERBATCH)
                .inputFluids(GTMaterials.Polyethylene, 2000)
                .EUt(2040)
                .duration(150)
                .save();

        CHEMICAL_BATH_RECIPES.builder("red_dye_masterbatch")
                .inputItems(GTOTagPrefix.dye, GTMaterials.DyeMagenta, 3)
                .outputItems(GTOItems.MAGENTA_DYE_MASTERBATCH)
                .inputFluids(GTMaterials.Polyethylene, 2000)
                .EUt(2040)
                .duration(150)
                .save();

        CHEMICAL_BATH_RECIPES.builder("black_dye_masterbatch")
                .inputItems(GTOTagPrefix.dye, GTMaterials.DyeBlack, 3)
                .outputItems(GTOItems.BLACK_DYE_MASTERBATCH)
                .inputFluids(GTMaterials.Polyethylene, 2000)
                .EUt(2040)
                .duration(150)
                .save();

        CHEMICAL_BATH_RECIPES.builder("white_dye_masterbatch")
                .inputItems(GTOTagPrefix.dye, GTMaterials.DyeWhite, 3)
                .outputItems(GTOItems.WHITE_DYE_MASTERBATCH)
                .inputFluids(GTMaterials.Polyethylene, 2000)
                .EUt(2040)
                .duration(150)
                .save();
    }
}
